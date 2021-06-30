package com.basoft.eorder.foundation.jdbc.service;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.LoginService;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.UserRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.User;
import com.basoft.eorder.domain.model.agent.Agent;
import com.basoft.eorder.interfaces.query.agent.AgentQuery;
import com.basoft.eorder.util.RedisUtil;
import com.basoft.eorder.util.TokenGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class CacheLoginService implements LoginService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String REDIS_LOGIN_KEY = "CMS_USERS";

    public static final String REDIS_CMS_KEY = "BAWX_CMS_TOKEN";

    public static final Long CURRENT_USER_KEY_TIMOUT = 20 * 60 * 1000L;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AgentQuery agentQuery;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public UserSession login(String account) {
        // 根据账号获取用户信息
        User user = userRepository.getUserByAccount(account);
        if (user != null) {
            String cms_token = generalToekn(user);
            UserSession userSession = new UserSession.Builder().id(user.getId()).name(user.getName())
                    .account(user.getAccount()).password(user.getPassword()).email(user.getEmail())
                    .mobile(user.getMobile()).token(cms_token).storeId(user.getStoreId()).storeType(user.getStoreType())
                    .accountType(user.getAccountType()).bizType(user.getBizType()).accountRole(user.getAccountRole()).build();

            // 如果是代理商用户，查询设置agentId
            if ("3" .equals(user.getAccountType())
                    || "5" .equals(user.getBizType())
                    // SA代理商
                    || (user.getAccountRole() != 0 && String.valueOf(user.getAccountRole()).startsWith("3"))
                    // CA代理商
                    || (user.getAccountRole() != 0 && String.valueOf(user.getAccountRole()).startsWith("4"))) {
                Agent agent = agentQuery.getAgentByCode(user.getAccount());
                if (agent == null) {
                    throw new BizException(ErrorCode.LOGIN_AGENT_USER_NULL);
                } else {
                    // 查询该用户的代理商ID
                    userSession.setAgentId(agent.getAgtId());
                }
            }

            this.cacheLoginInfo(userSession);
            return userSession;
        }
        return null;
    }

    private String generalToekn(User user) {
        try {
            return TokenGeneratorUtil.createToken(user.getAccount());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserSession getCurrentUser(String cmsToken) {
        return (UserSession) redisUtil.hget(REDIS_LOGIN_KEY, cmsToken);
    }

    /**
     * 缓存用户信息
     *
     * @param user 用户信息
     * @return
     * @author Dong Xifu
     * @date 2018/12/17 下午4:09
     */
    private UserSession cacheLoginInfo(UserSession user) {
        // 生成token
        // 组装token
		/*try {
			// byte[] ss = or.writeValueAsBytes(user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}*/

        redisUtil.hset(REDIS_LOGIN_KEY, user.getToken(), user, CURRENT_USER_KEY_TIMOUT);
        logger.info("token=" + user.getToken());
        logger.info("缓存用户" + user.getAccount() + "登录信息完毕！");
        return user;
    }

    /**
     * Admin CMS中单点登录Manager CMS时对Admin Access Token的合法性验证
     *
     * @param adminAccessToken
     * @return
     */
    public boolean checkAdminAccessToken(String adminAccessToken) {
        String token = "token-" + adminAccessToken;
        log.info("Admin access token info form single login manager CMS::::" + token);

        Object bawx_cms_token = redisUtil.hget("BAWX_CMS_TOKEN", token);

        // 登录过期
        if (bawx_cms_token == null) {
            log.error("Not Found Value of Token Info::::" + token);
            return false;
        }

        return true;
    }

    /**
     * Admin CMS中单点登录Manager CMS时对storeId和managerId匹配性进行检查
     *
     * @param storeId
     * @param managerId
     * @return
     */
    public boolean checkStoreManager(String storeId, String managerId) {
        Store store = storeRepository.checkStoreManager(storeId, managerId);
        if (store == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Admin CMS中单点登录Manager CMS
     *
     * @param managerId
     * @return UserSession
     */
    @Override
    public UserSession loginManagerCMSByManagerId(String managerId) {
        // 根据ID获取用户信息
        User user = userRepository.getUserByManagerId(managerId);
        if (user != null) {
            String cms_token = generalToekn(user);
            UserSession userSession = new UserSession.Builder().id(user.getId()).name(user.getName())
                    .account(user.getAccount()).password(user.getPassword()).email(user.getEmail())
                    .mobile(user.getMobile()).token(cms_token).storeId(user.getStoreId()).storeType(user.getStoreType())
                    .accountType(user.getAccountType()).bizType(user.getBizType()).accountRole(user.getAccountRole()).build();
            this.cacheLoginInfo(userSession);
            return userSession;
        }
        return null;
    }
}