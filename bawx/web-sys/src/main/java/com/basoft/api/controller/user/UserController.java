package com.basoft.api.controller.user;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basoft.api.controller.BaseController;
import com.basoft.api.security.TokenSession;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.web.vo.ApiJson;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.shop.ShopService;
import com.basoft.service.definition.user.UserService;
import com.basoft.service.entity.shop.ShopWithBLOBs;
import com.basoft.service.entity.user.User;
import com.basoft.service.entity.user.UserDTO;
import com.basoft.service.entity.user.UserListDTO;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    /**
     * 根据查询条件获取用户信息
     *
     * @param userListDTO
     * @return
     */
    @PostMapping(value = "/list")
    public Echo<?> getUsers(@RequestBody UserListDTO userListDTO) {
        ApiJson<List<User>> result = new ApiJson<>();
        // PageInfo<User> pageInfo = userService.getUsers(userListDTO);
        PageInfo<User> pageInfo = userService.getUsersWithShops(userListDTO);
        if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
            result.setPage(pageInfo.getPageNum());
            result.setRecords((int) pageInfo.getTotal());
            result.setTotal(pageInfo.getPages());
            result.setRows(pageInfo.getList());
        } else {
            result.setPage(1);
            result.setRecords(0);
            result.setTotal(0);
            result.setRows(null);
        }
        result.setErrorCode(0);
        result.setErrorMsg("Success");
        return new Echo<ApiJson<List<User>>>(result);
    }

    /**
     * 根据userId获取用户信息
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/{userId}/info")
    public Echo<?> getUserInfo(@PathVariable String userId) {
        // 验证userID
        if (StringUtils.isEmpty(userId)) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 验证userID
        if ("root".equals(userId)) {
            throw new BizException(ErrorCode.SYS_ERROR);
        }

        User user = userService.getUserByUserId(userId);

        if (null == user) {
            throw new BizException(ErrorCode.SYS_EMPTY);
        }
        return new Echo<User>(user);
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/add")
    public Echo<?> addUser(@RequestBody User user) {
        // 验证userID
        if (StringUtils.isEmpty(user.getUserId())) {
            throw new BizException(ErrorCode.PARAM_MISSING);
        }

        // 检验用户id是否可用
        if (isExistedUser(user.getUserId())) {
            throw new BizException(ErrorCode.SYS_DUPLICATE);
        }

        // 如果是userId是否可用性校验此时进行返回
        if ("basoft100001$ZE_check_679".equals(user.getUserNickNm())) {
            return new Echo<String>("OK");
        }

        // 检验用户信息
        if (!checkUser(user)) {
            // 缺少必要的用户信息
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 初始化用户信息
        initUserInfo(user, getTokenSession());

        // 新增公众号
        int i = userService.addUser(user);

        if (i == 0) {
            throw new BizException(ErrorCode.SYS_ERROR);
        }

        return new Echo<Integer>(i);
    }

    /**
     * 检验用户id是否可用
     *
     * @param userId
     */
    private boolean isExistedUser(String userId) {
        User user = userService.getUserByUserId(userId);
        if (null == user) {
            return false;
        }
        return true;
    }

    /**
     * 检验用户信息
     *
     * @param user
     */
    private boolean checkUser(User user) {
        // 用户id 姓名 邮箱 手机号 密码 不能为空
        if (StringUtils.isEmpty(user.getUserId()) || StringUtils.isEmpty(user.getUserNickNm())
                || StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getMobileNo())
                || StringUtils.isEmpty(user.getPwd())) {
            return false;
        }

        // userId不能为root
        if ("root".equals(user.getUserId())) {
            return false;
        }

        return true;
    }

    /**
     * 初始化用户信息
     *
     * @param user
     * @param tokenSession
     */
    private void initUserInfo(User user, TokenSession tokenSession) {
        user.setPwd(BCrypt.hashpw(user.getPwd(), BCrypt.gensalt()));
        // 用户状态默认设为停用状态,用户类型默认为N
        user.setActiveSts("2");
        user.setUserAuth("N");
        // 公司id
        user.setCompId(tokenSession.getGroupId());
        user.setCreatedDt(new Date());
    }

    /**
     * 根据userId修改用户信息
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/{userId}/update")
    public Echo<?> updateUser(@PathVariable String userId, @RequestBody User user) {
        // 验证userID
        if (StringUtils.isEmpty(userId)) {
            throw new BizException(ErrorCode.PARAM_MISSING);
        }

        // 非超级管理员不能修改超级管理员账号
        if ("root".equals(userId) && !"root".equals(getTokenSession().getUserId())) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 设置userId
        user.setUserId(userId);
        // 进行必要的设置-密码处理和修改时间
        if (StringUtils.isNotEmpty(user.getPwd())) {
            user.setPwd(BCrypt.hashpw(user.getPwd(), BCrypt.gensalt()));
        }
        user.setModifiedDt(new Date());

        // 更新用户信息
        int i = userService.updateUserById(user);

        if (i == 0) {
            throw new BizException(ErrorCode.SYS_EMPTY);
        }

        return new Echo<Integer>(i);
    }

    /**
     * 用户列表-密码修改
     *
     * @param userId
     * @param user   旧密码oldPwd user.pwd  新密码newPwd user.dept 复用dept传递新密码
     * @return
     */
    @PostMapping(value = "/{userId}/modpwd")
    public Echo<?> modifyPassword(@PathVariable String userId, @RequestBody User user) {
        // 验证userID,oldPwd,newPwd
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(user.getPwd()) || StringUtils.isEmpty(user.getDept())) {
            throw new BizException(ErrorCode.PARAM_MISSING);
        }

        // 查询用户
        User userInfo = userService.getUserByUserId(userId);
        if (null == userInfo) {
            throw new BizException(ErrorCode.SYS_EMPTY);
        } else {
            // 使用BCrypt对密码进行校验是否相同
            if (!BCrypt.checkpw(user.getPwd(), userInfo.getPwd())) {
                return new Echo<Integer>(4000);
            } else {
                // 设置userId
                user.setUserId(userId);
                // 密码加密
                user.setPwd(BCrypt.hashpw(user.getDept(), BCrypt.gensalt()));
                // 修改时间
                user.setModifiedDt(new Date());
            }
        }

        // 更新用户信息
        int i = userService.updateUserById(user);
        if (i == 0) {
            throw new BizException(ErrorCode.SYS_EMPTY);
        }
        // 更新成功
        return new Echo<Integer>(8000);
    }

    /**
     * 根据userId删除用户信息
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/{userId}/delete")
    public Echo<?> deleteUser(@PathVariable String userId) {
        // 验证userID
        if (StringUtils.isEmpty(userId)) {
            throw new BizException(ErrorCode.PARAM_MISSING);
        }

        // 不允许删除root超级管理员
        if ("root".equals(userId)) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 更新用户信息
        int i = userService.deleteUserById(userId);

        if (i == 0) {
            throw new BizException(ErrorCode.SYS_EMPTY);
        }

        return new Echo<Integer>(i);
    }

    /**
     * 根据userId修改用户权限。进行事务控制。
     *
     * @param userDTO
     * @return
     */
    @PostMapping(value = "/auth")
    //@Transactional(value = "primaryTransactionManager")
    @Transactional
    public Echo<?> authUser(@RequestBody UserDTO userDTO) {
        // 验证userID
        if (StringUtils.isEmpty(userDTO.getUserId())) {
            throw new BizException(ErrorCode.PARAM_MISSING);
        }

        // 不允许为root和S类型
        if ("root".equals(userDTO.getUserId()) || "S".equals(userDTO.getUserType())) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 修改用户权限
        int i = userService.authUser(userDTO);

        if (i == 0) {
            throw new BizException(ErrorCode.SYS_EMPTY);
        }

        // 修改用户的店铺权限-start
        // 1-删除用户所负责的店铺
        int j = userService.deleteUserShop(userDTO.getUserId());
        // 2-重新增加用户负责的店铺
        List<Long> shopList = userDTO.getShopList();
        int k = 0;
        if (CollectionUtils.isNotEmpty(shopList)) {
            k = userService.insertUserShop(userDTO.getUserId(), shopList);
        }
        // 修改用户的店铺权限-end

        return new Echo<String>(i + "," + j + "," + k);
    }

    /**
     * 查询微信公众号列表
     *
     * @return
     */
    @PostMapping(value = "/shoplist")
    public Echo<?> shopList() {
        Integer groupId = getTokenSession().getGroupId();
        if (groupId == null || groupId == 0) {
            throw new BizException(ErrorCode.SYS_TIMEOUT);
        }

        List<ShopWithBLOBs> shopList = shopService.findAllShopList(groupId);

        return new Echo<List<ShopWithBLOBs>>(shopList);
    }

    /**
     * 登录用户自修改密码
     *
     * @param jsonObject
     * @param request
     * @param res
     * @return
     */
    @PostMapping("/modp")
    public Echo<Map<String, String>> modifyPassword(@RequestBody JSONObject jsonObject,
                                                    HttpServletRequest request, HttpServletResponse res) {
        String oldPassword = jsonObject.get("oop").toString();
        String newPassword = jsonObject.get("nep").toString();
        // 验证参数信息
        if (oldPassword == null || "".equals(oldPassword.trim()) || newPassword == null || "".equals(newPassword.trim())) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 获取登录用户的userId
        String userId = getUserId();
        if (userId == null) {
            throw new BizException(ErrorCode.SYS_TIMEOUT);
        }

        // 查询用户
        User userInfo = userService.getUserByUserId(userId);
        if (null == userInfo) {
            throw new BizException(ErrorCode.SYS_EMPTY);
        } else {
            Map<String, String> respMap = new HashMap<>();
            // 使用BCrypt对密码进行校验原密码是否正确
            if (!BCrypt.checkpw(oldPassword, userInfo.getPwd())) {
                // 密码不正确
                respMap.put("isSucess", "0");
                respMap.put("operCode", "1001");
                respMap.put("operCodeDesc", "您输入的原密码不正确！");
                return new Echo<Map<String, String>>(respMap);
            } else {
                User user = new User();
                user.setPwd(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                user.setUserId(userId);
                user.setModifiedDt(new Date());
                int i = userService.updateUserPasswordById(user);
                if (i >= 1) {
                    respMap.put("isSucess", "1");
                    respMap.put("operCode", "1002");
                    respMap.put("operCodeDesc", "密码修改成功！");
                } else {
                    respMap.put("isSucess", "0");
                    respMap.put("operCode", "1003");
                    respMap.put("operCodeDesc", "密码修改失败！");
                }
                return new Echo<Map<String, String>>(respMap);
            }
        }
    }
}