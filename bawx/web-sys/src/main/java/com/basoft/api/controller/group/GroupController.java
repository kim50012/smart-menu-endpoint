package com.basoft.api.controller.group;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basoft.api.controller.BaseController;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.group.GroupService;
import com.basoft.service.entity.group.Group;

@RestController
@RequestMapping(value = "/group")
public class GroupController extends BaseController {

    @Autowired
    private GroupService groupService;

    /**
     * 根据公司id获取公司信息
     * 
     * @param groupId
     * @return
     */
    // @RequestMapping(value = "/{groupId}/info", method = RequestMethod.POST)
    @PostMapping(value = "/{groupId}/info")
    public Echo<?> getGroupInfo(@PathVariable String groupId){
    	// 验证参数公司ID
    	if(StringUtils.isEmpty(groupId)) {
    		throw new BizException(ErrorCode.PARAM_MISSING);
    	}
    	Group group = groupService.getGroupById(groupId);
    	if(null == group) {
    		throw new BizException(ErrorCode.SYS_EMPTY);
    	}
    	return new Echo<Group>(group);
    }
    
    /*{
    	"gCorpId": 1,
        "gCorpNm": "博珥佐特",
        "contactNm": "ba",
        "contactMobileNo": "18812345678",
        "contactEmail": "18812345678@basofttech.com",
        "contactQq": "123456",
        "adminUserId": "root",
        "createdUserId": "root",
        "sortNo": 1,
        "modifiedDt": 1523326630000,
        "createdDt": 1523326627000,
        "isDelete": 0,
    }*/
    /*
    {
        "gCorpNm": "博珥佐特",
        "contactNm": "ba",
        "contactMobileNo": "18812345678",
        "contactEmail": "18812345678@basofttech.com",
        "contactQq": "123456",
        "modifiedDt": 1523326630000
    }
    */
    /**
     * 根据公司id修改公司信息
     * 
     * @param groupId
     * @return
     */
    // @RequestMapping(value = "/{groupId}/update", method = RequestMethod.POST)
	@PostMapping(value = "/{groupId}/update")
	public Echo<?> updateGroupInfo(@PathVariable String groupId, @RequestBody Group group) {
		// 验证参数公司ID
		if (StringUtils.isEmpty(groupId)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}

		// 设置公司ID
		group.setgCorpId(Integer.parseInt(groupId));
		
		// 更新公司信息
		int i = groupService.updateGroupById(group);

		if (i == 0) {
			throw new BizException(ErrorCode.SYS_ERROR);
		}

		return new Echo<Integer>(i);
	}
}