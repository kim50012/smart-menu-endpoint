package com.basoft.service.dao.wechat.wxMessage;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.basoft.service.dto.wechat.WxMessageDto;
import com.basoft.service.entity.wechat.wxMessage.WxMessage;
import com.basoft.service.entity.wechat.wxMessage.WxMessageExample;
import com.basoft.service.entity.wechat.wxMessage.WxMessageWithBLOBs;
import com.basoft.service.param.wechat.wxMessage.WxMessageQueryParam;

@Repository
public interface WxMessageMapper {
    int countByExample(WxMessageExample example);

    int deleteByExample(WxMessageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxMessageWithBLOBs record);

    int insertSelective(WxMessageWithBLOBs record);

    List<WxMessageWithBLOBs> selectByExampleWithBLOBs(WxMessageExample example);

    List<WxMessage> selectByExample(WxMessageExample example);

    WxMessageWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxMessageWithBLOBs record, @Param("example") WxMessageExample example);

    int updateByExampleWithBLOBs(@Param("record") WxMessageWithBLOBs record, @Param("example") WxMessageExample example);

    int updateByExample(@Param("record") WxMessage record, @Param("example") WxMessageExample example);

    int updateByPrimaryKeySelective(WxMessageWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(WxMessageWithBLOBs record);

    int updateByPrimaryKey(WxMessage record);

    List<WxMessageDto> finCustChatContent(WxMessageQueryParam queryParam);
}