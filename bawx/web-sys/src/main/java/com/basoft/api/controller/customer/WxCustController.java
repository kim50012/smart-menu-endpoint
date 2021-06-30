package com.basoft.api.controller.customer;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.api.vo.customer.CustVo;
import com.basoft.service.batch.wechat.thread.SyncUserThread;
import com.basoft.service.definition.customer.cust.custShop.CustService;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechat.user.WeixinUserService;
import com.basoft.service.dto.customer.CustDto;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;
import com.basoft.service.enumerate.BatchEnum;
import com.basoft.service.param.customer.CustQueryParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:04 2018/5/9
 **/
@RestController
public class WxCustController extends BaseController {

    @Autowired
    private CustService custService;

    /**
     * @param
     * @return
     * @author Dong Xifu
     * @Date 2018/5/9 上午9:40
     * @describe 查询客户
     **/
    @RequestMapping(value = "/findCustAll", method = RequestMethod.GET)
    public ApiJson<List> findCustAll(@RequestParam(value = "page", defaultValue = "1") String page,
                                     @RequestParam(value = "rows", defaultValue = "20") String rows,
                                     @RequestParam(value = "param", defaultValue = "") String custName) {
        CustQueryParam queryParam = new CustQueryParam();
        queryParam.setPage(Integer.parseInt(page));
        queryParam.setRows(Integer.parseInt(rows));
        queryParam.setCustName(custName);
        queryParam.setShopId(getShopId());
        ApiJson<List> result = new ApiJson<>();
        try {
            List<CustDto> list = custService.findCustAll(queryParam);
            if (list != null && CollectionUtils.isNotEmpty(list)) {
                /* result.setPage(pageInfo.getPageNum());*/
                result.setRecords(list.size());
                result.setTotal(1);
                result.setRows(list.stream().map(data -> new CustVo(data)).collect(Collectors.toList()));
            } else {
                result.setPage(1);
                result.setRecords(0);
                result.setTotal(0);
                result.setRows(new ArrayList());
            }
            result.setErrorCode(0);
            result.setErrorMsg("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Autowired
    private WechatService wechatService;

    @Autowired
    WeixinUserService weixinUserService;

    @PostMapping(value = "/ManualSaveSubScrible")
    public void ManualSaveSubScrible() {
        List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
        for (AppInfoWithBLOBs bloBs : appInfoList) {
            // 锁检查
            boolean isRun = wechatService.queryWxBatchLock(bloBs.getSysId(), BatchEnum.batchUser.getType());
            // 非加锁状态
            if (!isRun) {
                Thread thread = new Thread(new SyncUserThread(wechatService, weixinUserService, bloBs.getSysId(), bloBs.getShopId()));
                thread.start();
            } else {
                logger.info("|||||||||||||||||||||||||||||||||||||||||||||||||||batchInsertWxUse Cust|||||||||||||||||||||||||||||||||||||||||||||||||||");
            }
        }


    }

}
