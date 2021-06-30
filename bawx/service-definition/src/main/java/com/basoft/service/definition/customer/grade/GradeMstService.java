package com.basoft.service.definition.customer.grade;

import com.basoft.service.dto.customer.GradeMstDto;
import com.basoft.service.entity.customer.grade.GradeMst;
import com.basoft.service.param.customer.GradeMstQueryParam;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:50 2018/4/23
 **/
public interface GradeMstService {
    public PageInfo<GradeMstDto> gradeMstFindAll(GradeMstQueryParam param);

    public int insertGradeMst(GradeMst gradeMst);

    public GradeMst getGradeMst(Long shopId,Long gradeId);

    public int updateGradeMst(GradeMst gradeMst);

    public int deleteGradeMst(Long shopId,Long gradeId);
    
	/**
	 * 检查等级名称是否存在
	 * 
	 * @param gradeNm
	 * @return
	 */
	public int checkGradeisExistByGradeNm(String gradeNm);

	/**
	 * 检查该等级是否在用
	 * 
	 * @param valueOf
	 * @return
	 */
	public int checkGradeIsUsed(Long gradeId);
    
	/**
	 * 检查等级是否存在
	 * 
	 * @param baseQty
	 * @return
	 */
	public int checkGradeisExistByBaseQty(String baseQty);
}
