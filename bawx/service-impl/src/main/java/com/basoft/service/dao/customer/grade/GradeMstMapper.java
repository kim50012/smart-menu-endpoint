package com.basoft.service.dao.customer.grade;

import com.basoft.service.dto.customer.GradeMstDto;
import com.basoft.service.entity.customer.grade.GradeMst;
import com.basoft.service.entity.customer.grade.GradeMstExample;
import com.basoft.service.entity.customer.grade.GradeMstKey;
import com.basoft.service.param.customer.GradeMstQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeMstMapper {
    int countByExample(GradeMstExample example);

    int deleteByExample(GradeMstExample example);

    int deleteByPrimaryKey(GradeMstKey key);

    int insert(GradeMst record);

    int insertSelective(GradeMst record);

    List<GradeMst> selectByExample(GradeMstExample example);

    GradeMst selectByPrimaryKey(GradeMstKey key);

    int updateByExampleSelective(@Param("record") GradeMst record, @Param("example") GradeMstExample example);

    int updateByExample(@Param("record") GradeMst record, @Param("example") GradeMstExample example);

    int updateByPrimaryKeySelective(GradeMst record);

    int updateByPrimaryKey(GradeMst record);

    List<GradeMstDto> gradeMstFindAll(GradeMstQueryParam param);
    
	/**
	 * 检查等级是否存在
	 * 
	 * @param baseQty
	 * @return
	 */
	int checkGradeisExistByBaseQty(String baseQty);

	/**
	 * 检查等级名称是否存在
	 * 
	 * @param gradeNm
	 * @return
	 */
	int checkGradeisExistByGradeNm(String gradeNm);

	/**
	 * 检查该等级是否在用
	 * 
	 * @param valueOf
	 * @return
	 */
	int checkGradeIsUsed(Long gradeId);
}