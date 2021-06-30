package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.domain.UserRepository;
import com.basoft.eorder.domain.model.User;
import com.basoft.eorder.util.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@CommandHandler.AutoCommandHandler("UserCommandHandler")
public class UserCommandHandler {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UidGenerator uidGenerator;

    @CommandHandler.AutoCommandHandler(CreateUser.NAME)
    public Object saveUser(CreateUser sp) {
        Long newId = sp.getId();
        if (sp.getId() == null) {
            newId = uidGenerator.generate(BusinessTypeEnum.USER);
            User user = sp.buildForNew(newId);
            // User isExitUser = userRepository.getUserByAccount(user.getAccount());//错误的方法调用，不能调用该方法检查account是否存在
            User isExitUser = userRepository.checkAccountIsExist(user.getAccount());
            if (isExitUser != null)
                throw new BizException(ErrorCode.LOGIN_USER_EXIT);

            userRepository.insertUser(user);
        } else {
            //更新的时候要从
            User oUser = userRepository.getUser(sp.getId());
            sp.setPassword(oUser.getPassword());
            User user = sp.buildForUpdate(newId);
            userRepository.updateUser(user);
        }
        return newId;
    }

    @CommandHandler.AutoCommandHandler(UpdateUserStatus.NAME)
    @Transactional
    public int updateUserStatus(UpdateUserStatus userStatus) {
        return userRepository.updateUserStatus(userStatus);
    }


    /*@CommandHandler.AutoCommandHandler(UpdateUser.NAME)
    @Transactional
    public Object updateUser(UpdateUser sp){
        System.out.println("\n\n\n");
        System.out.println("Transactional --------");

        userRepository.insertUser(null);
        userRepository.insertUser(null);
        return "ok";
    }*/

    @CommandHandler.AutoCommandHandler(CreateUserInManager.NAME)
    public Object saveUserInManager(CreateUserInManager userInManager, CommandHandlerContext context) {
        Long userId = userInManager.getId();
        // 新增
        if (userId == null || userId == 0) {
            // 验证登录账号是否存在
            // User isExitUser = userRepository.getUserByAccount(userInManager.getAccount());
            User isExitUser = userRepository.checkAccountIsExist(userInManager.getAccount());
            if (isExitUser != null){
                throw new BizException(ErrorCode.LOGIN_USER_EXIT);
            }

            UserSession userSession = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
            // 生成新的用户ID
            userId = uidGenerator.generate(BusinessTypeEnum.USER);
            User user = userInManager.buildForNew(userId, userSession.getStoreId());
            userRepository.insertUserInManager(user);
        }
        // 修改
        else {
            String newPassword = userInManager.getNewPassword();
            if(newPassword == null || "".equals(newPassword.trim())){
                //更新的时候要从数据库中获取密码
                User oldUser = userRepository.getUser(userInManager.getId());
                userInManager.setPassword(oldUser.getPassword());
            }

            UserSession userSession = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);

            User user = userInManager.buildForUpdate(userId, userSession.getStoreId());
            userRepository.updateUserInManager(user);
        }
        return userId;
    }
}
