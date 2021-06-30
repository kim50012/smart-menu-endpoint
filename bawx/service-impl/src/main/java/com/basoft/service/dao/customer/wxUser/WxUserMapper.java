package com.basoft.service.dao.customer.wxUser;

import com.basoft.service.dto.customer.WxUserSituationDto;
import com.basoft.service.dto.customer.WxUserTotalCityDto;
import com.basoft.service.dto.customer.WxUserTotalLangDto;
import com.basoft.service.dto.customer.WxUserTotalProvinDto;
import com.basoft.service.entity.customer.wxUser.WxUser;
import com.basoft.service.entity.customer.wxUser.WxUserExample;
import com.basoft.service.param.customer.WxUserQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WxUserMapper {
    int countByExample(WxUserExample example);

    int deleteByExample(WxUserExample example);

    int deleteByPrimaryKey(String openid);

    int insert(WxUser record);

    int insertSelective(WxUser record);

    List<WxUser> selectByExample(WxUserExample example);

    WxUser selectByPrimaryKey(String openid);

    int updateByExampleSelective(@Param("record") WxUser record, @Param("example") WxUserExample example);

    int updateByExample(@Param("record") WxUser record, @Param("example") WxUserExample example);

    int updateByPrimaryKeySelective(WxUser record);

    int updateByPrimaryKey(WxUser record);

    public List<Integer> selectSituation(WxUserQueryParam param);

    //查询日期
    public List<String> userSituatDate(WxUserQueryParam param);

    //新增粉丝总数
    public int sumNewFans(WxUserQueryParam param);

    //取消关注总数
    public int sumOutFans(WxUserQueryParam param);

    //总粉丝数
    public int totalFans(WxUserQueryParam param);

    //查询粉丝关注现况
    public List<WxUserSituationDto> selectSituationDto(WxUserQueryParam param);

    //统计语种
    public WxUserTotalLangDto totalLanguigeType(@Param("shopId") Long shopId);

    //统计语种List
    public List<WxUserTotalProvinDto> selectLanguigeTypeList(@Param("shopId") Long shopId);

    //统计城市
    public List<WxUserTotalCityDto> totalCity(@Param("shopId") Long shopId);

    //统计省份
    public List<WxUserTotalProvinDto> totalProvince(@Param("shopId") Long shopId);
}