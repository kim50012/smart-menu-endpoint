package com.basoft.api.controller.customer;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.service.definition.wechat.reply.ReplyService;
import com.basoft.service.dto.customer.CustMsgDto;
import com.basoft.service.param.customer.CustMsgQueryParam;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:29 2018/5/7
 **/

@RestController
public class CustMsgController extends BaseController{

    @Autowired
    private ReplyService replyService;

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/msgListFragment",method = RequestMethod.GET)
    public ApiJson<List> msgListFragment(@RequestParam(value = "page",defaultValue = "1" )String page,
                                         @RequestParam(value = "rows",defaultValue = "20" )String rows,
                                         @RequestParam(value = "param",defaultValue = "" )String param){
        CustMsgQueryParam queryParam = new CustMsgQueryParam();
        queryParam.setPage(Integer.parseInt(page));
        queryParam.setRows(Integer.parseInt(rows));
        queryParam.setShopId(getShopId());
        queryParam.setUserId(getUserId());
        ApiJson<List> result = new ApiJson<>();

        try {
            PageInfo<CustMsgDto> pageInfo = replyService.msgListFragment(queryParam);

            if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
                result.setPage(pageInfo.getPageNum());
                result.setRecords((int) pageInfo.getTotal());
                result.setTotal(pageInfo.getPages());
                result.setRows(pageInfo.getList());
            } else {
                result.setPage(1);
                result.setRecords(0);
                result.setTotal(0);
                result.setRows(new ArrayList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setErrorCode(0);
        result.setErrorMsg("Success");

        return  result;
    }
}
