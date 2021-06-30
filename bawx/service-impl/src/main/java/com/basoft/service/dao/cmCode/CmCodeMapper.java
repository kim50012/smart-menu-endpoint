package com.basoft.service.dao.cmCode;

import com.basoft.service.entity.code.CmCode;
import com.basoft.service.entity.code.CmCodeExample;
import com.basoft.service.entity.code.CmCodeKey;
import com.basoft.service.entity.code.CmCodeWithBLOBs;
import com.basoft.service.param.cmCode.CmCodeQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmCodeMapper {
    int countByExample(CmCodeExample example);

    int deleteByExample(CmCodeExample example);

    int deleteByPrimaryKey(CmCodeKey key);

    int insert(CmCodeWithBLOBs record);

    int insertSelective(CmCodeWithBLOBs record);

    List<CmCodeWithBLOBs> selectByExampleWithBLOBs(CmCodeExample example);

    List<CmCode> selectByExample(CmCodeExample example);

    CmCodeWithBLOBs selectByPrimaryKey(CmCodeKey key);

    int updateByExampleSelective(@Param("record") CmCodeWithBLOBs record, @Param("example") CmCodeExample example);

    int updateByExampleWithBLOBs(@Param("record") CmCodeWithBLOBs record, @Param("example") CmCodeExample example);

    int updateByExample(@Param("record") CmCode record, @Param("example") CmCodeExample example);

    int updateByPrimaryKeySelective(CmCodeWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(CmCodeWithBLOBs record);

    int updateByPrimaryKey(CmCode record);

    List<CmCodeWithBLOBs> findAllByParam(CmCodeQueryParam param);
}