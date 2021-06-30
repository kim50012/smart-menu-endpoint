package com.basoft.eorder.interfaces.command.agent;

import com.basoft.eorder.application.*;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.application.wx.api.WechatAPI;
import com.basoft.eorder.domain.AgentRepository;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.UserRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.User;
import com.basoft.eorder.domain.model.agent.Agent;
import com.basoft.eorder.domain.model.agent.AgentAimMap;
import com.basoft.eorder.domain.model.agent.QRCodeAgent;
import com.basoft.eorder.interfaces.query.agent.AgentQuery;
import com.basoft.eorder.util.UidGenerator;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date
 **/
@CommandHandler.AutoCommandHandler("AgentCommandHandler")
@Transactional
public class AgentCommandHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AgentQuery agentQuery;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    @Qualifier("wechatQRService")
    private QRCodeGenerateService wechatService;

    @Autowired
    private UserRepository userRepository;

    /**
     * 新增或修改代理商
     *
     * @return int
     * @Param saveAgent
     * @author Dong Xifu
     * @date
     */
    @CommandHandler.AutoCommandHandler(SaveAgent.SaveAgent)
    @Transactional
    public Long saveAgent(SaveAgent saveAgent) {
        logger.debug("Start saveAgent -------");
        // 修改
        Long agtId = saveAgent.getAgtId();
        logger.info("Start saveAgent：：：：agtId = " + agtId);
        if (agtId != null && agtId > 0) {
            // 修改时必须传代理商编码，因为更新代理商用户信息时依据代理商编码
            if (StringUtils.isEmpty(saveAgent.getAgtCode())) {
                throw new BizException(ErrorCode.PARAM_INVALID);
            }
            Agent agent = saveAgent.build(agtId, createAndCheckAgtCode(saveAgent.getAgtType(), saveAgent.getAgtCode(), false));
            logger.info("Start saveAgent：：：：agent = " + agent);
            agentRepository.updateAgent(agent);
            updateUser(agent);//修改登录账号信息
            return agtId;
        }
        // 新增
        else {
            Long agentId = uidGenerator.generate(BusinessTypeEnum.AGENT);
            // 构建代理商信息
            Agent agent = saveAgent.build(agentId, createAndCheckAgtCode(saveAgent.getAgtType(), saveAgent.getAgtCode(), true));
            // 保存代理商
            agentRepository.insertAgent(agent);
            createUser(agent);//新建登录账号信息
            return agentId;
        }
    }

    /**
     * 新建登录用户信息
     *
     * @Param agent
     * @return int
     * @author Dong Xifu
     * @date 2019/9/29 3:46 下午
     */
    private int createUser(Agent agent) {
        // 根据代理商编码检查是否已存在用户
        User isExitUser = userRepository.checkAccountIsExist(agent.getAgtCode());
        if (isExitUser != null)
            throw new BizException(ErrorCode.AGENT_LOGIN_USER_EXIT);

        // 构建代理商的Manager APP Account
        User user = new User().newUserFromAgentForInsert(uidGenerator.generate(BusinessTypeEnum.USER), agent);

        // 保存代理商用户信息
        return  userRepository.insertUserForAgent(user);
    }

    /**
     * 修改登录用户信息
     *
     * @Param agent
     * @return int
     * @author Dong Xifu
     * @date 2019/9/29 3:46 下午
     */
    private int updateUser(Agent agent) {
        // 同步修改信息到Manager用户表:代理商新名称，代理商新密码，代理商新手机号码，代理商新邮箱
        User user = new User().newUserFromAgentForUpdate(agent);

        logger.info("Start saveAgent：：：：user = " + user);

        // 同步修改保存代理商用户信息
       return userRepository.updateUserForAgent(user);
    }

    /**
     * 生成代理商编码
     *
     * @return java.lang.String
     * @Param agtType 代理商类型  code 代理商编码
     * @author Dong Xifu
     * @date 2019/9/17 9:51 上午
     */
    private String createAndCheckAgtCode(Integer agtType, String code,Boolean isChecked) {
        if (agtType == null || StringUtils.isBlank(code))
            throw new BizException(ErrorCode.PARAM_MISSING);

        if (agtType == Agent.SA_TP) {
            code = Agent.SA + "-" + code;
        } else if (agtType == Agent.CA_TP) {
            code = Agent.CA + "-" + code;
        } else {
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }

        if(isChecked) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("agtCode", code);
            int count = agentQuery.getAgentCount(param);
            if (count > 0) {
                throw new BizException(ErrorCode.SYS_DUPLICATE);
            }
        }
        return code;
    }

    /**
     * 续约代理商
     *
     * @Param saveAgent
     * @return java.lang.Long
     * @author Dong Xifu
     * @date 2019/9/24 2:41 下午
     */
    @CommandHandler.AutoCommandHandler(SaveAgent.RenewAgt)
    public Long renewAgt(SaveAgent saveAgent) {
        logger.debug("Start renewAgent -------");
        Agent agent = saveAgent.build(saveAgent.getAgtId(), "");
        return agentRepository.renewAgent(agent);
    }

    /**
     * 修改代理商状态
     * 状态：1-正常   2-禁用   3-删除
     *
     * @return java.lang.Long
     * @Param
     * @author Dong Xifu
     * @date 2019/9/16 4:02 下午
     */
    @CommandHandler.AutoCommandHandler(SaveAgent.UpAgentStatus)
    @Transactional
    public Long upAgentStatus(SaveAgent saveAgent) {
        Agent agent = saveAgent.build(saveAgent.getAgtId(), saveAgent.getAgtCode());

        userRepository.delUserByAccount(agent.getAgtCode());

        return agentRepository.updateAgentStatus(agent);
    }

    /**
     * 新增或修改代理商关系表
     *
     * @return java.lang.Long
     * @Param saveMap
     * @author Dong Xifu
     * @date 2019/9/17 1:28 下午
     */
    @CommandHandler.AutoCommandHandler(SaveAgentAimMap.SaveAgentAimMap)
    @Transactional
    public int saveAgentAimMap(SaveAgentAimMap saveMap) {
        Store store = storeRepository.getStore(Long.valueOf(saveMap.getStoreId()));
        if (saveMap.getId() != null && saveMap.getId() > 0) {

            AgentAimMap aimMap = saveMap.build(store);
            return agentRepository.updateAgentStoreMap(aimMap);//修改
        } else {
            AgentAimMap aimMap = saveMap.build(store);
            return insertAgtSAMap(aimMap);
        }
    }

    /**
     * 判断只有没有被代理的店铺才可以插入(SA)
     * 实现插入代理商关系表(先删除后新增)
     *
     * @param aimMap
     * @return Long
     */
    private int insertAgtSAMap(AgentAimMap aimMap) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", aimMap.getStoreId());
        //筛选出前端传入SA类型的代理商
        Integer atpType = aimMap.getAimMapList().stream()
                .filter(a -> a.getAgtType() == Agent.SA_TP)
                .map(AgentAimMap::getAgtType)
                .findFirst()
                .orElseGet(() -> null);

        if (atpType != null && atpType > 0) {
            param.put("agtType", Agent.SA_TP);
        }
        return agentRepository.insertAgentStoreMap(aimMap);

		/*int count = agentQuery.getIsBindCount(param);
		if (count <= 1) {

		}else {
			throw new BizException(ErrorCode.AGENT_STORE_ISBIND);
		}*/
    }

    /**
     * 生成CA的QRCode
     * 1、cms调用传递agentId
     * 2、app调用不传递agentId
     *
     * @param generateCagentQRCode [agentId,CA Agent ID]
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler(GenerateCagentQRCode.NAME)
    public Map<Long, String> generateCagentQRCode(GenerateCagentQRCode generateCagentQRCode, CommandHandlerContext context) {
        // agentId
        String agentId = generateCagentQRCode.getAgentId();
        if (StringUtils.isEmpty(agentId)) {
            // 会话信息
            UserSession userSession = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
            logger.info("=========▶ UserSession : " + userSession);
            Long aId = userSession.getAgentId();
            if (aId != null && aId != 0L) {
                agentId = String.valueOf(userSession.getAgentId());
            } else {
                throw new BizException(ErrorCode.PARAM_INVALID);
            }
        }

        // 根据caID查询Ca信息，包含二维码信息。如果存在二维码信息则直接返回。
        Map agentMap = Maps.newHashMap();
        agentMap.put("agtId", agentId);
        Agent caAgent = agentQuery.getAgent(agentMap);
        logger.info("查询到的代理商信息：："+ caAgent);
        if (caAgent != null) {
            String agtQrcodeId = caAgent.getAgtQrcodeId();
            String agtTicket = caAgent.getAgtTicket();
            // 存在二维码，直接返回
            if (StringUtils.isNotEmpty(agtQrcodeId) && StringUtils.isNotEmpty(agtTicket)) {
                Map returnMap = Maps.newHashMap();
                returnMap.put(agentId, WechatAPI.getInstance().getShowqrcodeUrl() + agtTicket);
                return returnMap;
            }
            // 不存在二维码，则进行生成
            else {
                // 生成新的二维码
                QRCodeAgent agentQRCode = wechatService.matchGenerateCagentWechatQRCode(agentId);
                logger.info("新生成的二维码信息QRCodeAgent>>>>>>" + agentQRCode.toString());
                Map returnMap = Maps.newHashMap();
                returnMap.put(agentId, WechatAPI.getInstance().getShowqrcodeUrl() + agentQRCode.getQrcodeTicket());
                return returnMap;
            }
        } else {
            throw new BizException(ErrorCode.SYS_EMPTY);
        }
    }
}

