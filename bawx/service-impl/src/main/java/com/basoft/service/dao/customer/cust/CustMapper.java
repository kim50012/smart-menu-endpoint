package com.basoft.service.dao.customer.cust;

import com.basoft.service.dto.customer.CustDto;
import com.basoft.service.dto.customer.CustGradeDto;
import com.basoft.service.dto.customer.CustSexRadioDto;
import com.basoft.service.dto.customer.WxUserTotalProvinDto;
import com.basoft.service.entity.customer.cust.Cust;
import com.basoft.service.entity.customer.cust.CustExample;
import com.basoft.service.entity.wechat.wxMessage.*;
import com.basoft.service.param.customer.CustGradeQueryParam;
import com.basoft.service.param.customer.CustQueryParam;
import com.basoft.service.param.customer.WxUserQueryParam;
import com.basoft.service.param.wechat.wxMessage.WxMessageQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustMapper {
    int countByExample(CustExample example);

    int deleteByExample(CustExample example);

    int deleteByPrimaryKey(Long custSysId);

    int insert(Cust record);

    int insertSelective(Cust record);

    List<Cust> selectByExample(CustExample example);

    Cust selectByPrimaryKey(Long custSysId);

    int updateByExampleSelective(@Param("record") Cust record, @Param("example") CustExample example);

    int updateByExample(@Param("record") Cust record, @Param("example") CustExample example);

    int updateByPrimaryKeySelective(Cust record);

    int updateByPrimaryKey(Cust record);

    List<CustGradeDto> findAllCustGrade(CustGradeQueryParam param);

    //查询性别比列扇形图
    CustSexRadioDto selectSexRadio(WxUserQueryParam param);

    //查询性别比列列表
    List<WxUserTotalProvinDto> selectSexRadioList(WxUserQueryParam param);

    List<CustDto> findAllCust(CustQueryParam param);

    //获得微信用户与公众号的聊天记录/天 小时
    List<WxIfStreamMsgStatsData> selectCustMsgStatic(WxMessageQueryParam queryParam);

    //查询发送次数所占百分比
    List<WxIfMsgCountPercent> selectCountIntervalPercent(@Param("shopId")Long shopId,@Param("timeType")int timeType,
                                                         @Param("startTime")String startTime,@Param("endTime")String endTime);

    WxIfMsgSumYestoday selectMsgSumYestoday(@Param("shopId")Long shopId, @Param("timeType")int timeType,
                                            @Param("startTime")String startTime, @Param("endTime")String endTime);

    List<WxIfStreamMsgStatsDataWeek> selectCustMsgStaticWeek(@Param("shopId")Long shopId, @Param("timeType")int timeType,
                                                             @Param("startTime")String startTime, @Param("endTime")String endTime);

    List<WxIfMsgCountPercent> selectCountIntervalPercentWeek(@Param("shopId")Long shopId, @Param("timeType")int timeType,
                                                             @Param("startTime")String startTime, @Param("endTime")String endTime);

    List<WxKeyMsgStats> findCustMsgKeyStatic(WxMessageQueryParam queryParam);


    List<WxKeyMsgStats> findKeyStaticByCustom(WxMessageQueryParam queryParam);

}