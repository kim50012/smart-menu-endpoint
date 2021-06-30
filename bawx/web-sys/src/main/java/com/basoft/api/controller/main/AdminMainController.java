package com.basoft.api.controller.main;


import com.basoft.api.controller.BaseController;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.group.GroupService;
import com.basoft.service.definition.shop.ShopService;
import com.basoft.service.entity.group.Group;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin")
public class AdminMainController extends BaseController {
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private ShopService shopService;

    /**
     * 获取当前登录用户可展现的公众号列表
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "/main", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Echo<?> main(@RequestBody Map<String,String> params){
    	// 缓存中获取登陆用户的ID
    	String userId = getTokenSession().getUserId();
    	
    	// 定义用户权限类型：默认为N即普通员工。用来限制查询公众号列表
    	String userAuth = getTokenSession().getUserAuth() == null ? "N" : getTokenSession().getUserAuth();
    	if(StringUtils.isEmpty(userId)) {
    		throw new BizException(ErrorCode.SYS_ERROR);
    	} 
		
		//根据id查找公司-目前只有一个公司，即basoft公司的独立的微信项目产品-该项目立项所定
		List<Group> grouplist = groupService.getGroupListByUserId(userId);
		
		if("S".equals(userAuth)) {// 超级管理员，显示全部（包括状态不正常的）
			for (int i = 0; i < grouplist.size(); i++) {
				Integer companyId = Integer.valueOf(grouplist.get(i).getgCorpId());
				grouplist.get(i).setShopList(shopService.getShopList(companyId));
			}
		} else if("N".equals(userAuth) || "A".equals(userAuth)){// 普通员工或管理员，显示自己所负责的
			for (int i = 0; i < grouplist.size(); i++) {
				Integer companyId = Integer.valueOf(grouplist.get(i).getgCorpId());
				grouplist.get(i).setShopList(shopService.getResponsibleShopList(userId, companyId));
			}
		}
		// 20190806放开该角色以适应WechatPlace项目的Admin权限管理
		else {
			for (int i = 0; i < grouplist.size(); i++) {
				Integer companyId = Integer.valueOf(grouplist.get(i).getgCorpId());
				grouplist.get(i).setShopList(shopService.getResponsibleShopList(userId, companyId));
			}
		}
		
		return new Echo<List<Group>>(grouplist);
    }
}