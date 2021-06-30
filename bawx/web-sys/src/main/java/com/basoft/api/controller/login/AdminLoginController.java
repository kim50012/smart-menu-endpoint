package com.basoft.api.controller.login;


import com.basoft.api.config.TokenConfig;
import com.basoft.api.controller.BaseController;
import com.basoft.api.security.TokenGeneratorUtil;
import com.basoft.api.security.TokenSession;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.util.CacheMap;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.user.UserService;
import com.basoft.service.dto.login.LoginParam;
import com.basoft.service.dto.login.UserDTO;
import com.basoft.service.entity.user.User;
import com.basoft.service.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

/**
 * 登录和缓存
 * 
 * @author basoft
 */
@RestController
@RequestMapping(value = "/admin")
public class AdminLoginController extends BaseController {

    @Autowired
    private UserService userService;
    
    @Autowired
    TokenConfig tokenConfig;

	@Autowired
	private RedisUtil redisUtil;

	private ObjectMapper om = new ObjectMapper();

    /**
     * 登录
     * 
     * @param
     * @return
     */
    @RequestMapping(value = "/loginCheck", method = RequestMethod.POST)
    public Echo<?> loginCheck(@RequestBody LoginParam loginParam){
    	String account = loginParam.getAccount();
    	String password = loginParam.getPassword();
    	// 验证登陆信息
    	if(account == null || "".equals(account.trim()) || password == null || "".equals(password.trim())) {
    		throw new BizException(ErrorCode.LOGIN_INVALID);
    	}
    	
    	// 查询用户信息
    	User user=new User();
    	user = userService.getUserByAccount(account.trim());
    	if (user != null) {
    		// 使用BCrypt对密码进行校验是否相同
            if (!BCrypt.checkpw(loginParam.getPassword(), user.getPwd())) {
                throw new BizException(ErrorCode.LOGIN_ERROR);
            }
            
            // 为了安全过滤向前台传递的用户属性
            UserDTO userDTO = new UserDTO();
            try {
				BeanUtils.copyProperties(userDTO, user);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
            
            // 缓存登录信息
            try {
            	String token = cacheLoginInfo(user,userDTO);
            	userDTO.setLoginToken(token);


			} catch (IllegalArgumentException | UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new BizException(ErrorCode.SYS_ERROR);
			}
            
    		return new Echo<UserDTO>(userDTO);
    	} else {
    		throw new BizException(ErrorCode.LOGIN_ERROR);
    	}
    }

	/**
	 * 缓存登录信息
	 * 
	 * @param user
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 * @return token
	 */
	private String cacheLoginInfo(User user,UserDTO userDTO) throws IllegalArgumentException, UnsupportedEncodingException {
		String token;
		String tokenKey;
		// 生成token
		token = TokenGeneratorUtil.createToken(user.getUserId());
		logger.info("Create token for basoft user" + user.getUserId() + "::" + token);
		// 组装token
		tokenKey = tokenConfig.tokenKeyPrefix + token;
		logger.info("auth:" + tokenKey+"|| 开始缓存用户" + user.getUserId() + "登录信息！");
		
		// 判断该用户是否存在缓存信息，如果存在则清理
        if (TokenSession.getTokenCache().containsKey((tokenKey))) {
            // 清空存在的缓存信息，重新缓存用户信息
        	TokenSession.getTokenCache().remove(tokenKey);
        }
        
        // 缓存登录信息
        TokenSession tokenSession = new TokenSession();
        tokenSession.setUserId(user.getUserId());
        String userName = user.getUserRealNm() == null ?(user.getUserNickNm()==null?"":user.getUserNickNm()):user.getUserRealNm();
        tokenSession.setUserName(userName);
        tokenSession.setGroupId(user.getCompId());
        tokenSession.setUserAuth(user.getUserAuth());
        TokenSession.getTokenCache().put(tokenKey, tokenSession);
        logger.info("缓存用户到cachMap---" + user.getUserId() + "登录信息完毕！");

		userDTO.setLoginToken(token);
		setUserJsonToRedis(userDTO,tokenSession,tokenKey);//将当前用户信息缓存到redis
        return token;
	}

	/*
	 * @param  [user]
	 * @return net.sf.json.JSONObject
	 * @describe 将当前用户信息缓存到redis
	 * @author Dong Xifu
	 * @date 2018/12/16 下午1:35
	 */
	private void setUserJsonToRedis(UserDTO dto,TokenSession session,String tokenKey){
//		JSONObject userJson =  JSONObject.fromObject(dto);
		try {
			String  userJsonString = om.writeValueAsString(dto);
			boolean res = redisUtil.hset(RedisUtil.REDIS_CMS_KEY,tokenKey,userJsonString,RedisUtil.TOKEN_EXPIRE_TIME);
			redisUtil.hset(RedisUtil.TOKEN_SESSION,tokenKey,session,RedisUtil.TOKEN_EXPIRE_TIME);
			if(!res){
				throw new IllegalArgumentException("can not set to Redis");
			}


			//redisUtil.hset(REDIS_LOGIN_KEY,user.getToken(),user,CURRENT_USER_KEY_TIMOUT);
			logger.info("缓存用户到redis---" + dto.getUserId() + "登录信息完毕！");
//			return userJsonString;

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}
}