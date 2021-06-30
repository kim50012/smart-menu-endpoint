package com.basoft.service.impl.customer.grade;

import com.basoft.service.dao.customer.grade.GradeMstMapper;
import com.basoft.service.definition.customer.grade.GradeMstService;
import com.basoft.service.dto.customer.GradeMstDto;
import com.basoft.service.entity.customer.grade.GradeMst;
import com.basoft.service.entity.customer.grade.GradeMstKey;
import com.basoft.service.param.customer.GradeMstQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:53 2018/4/23
 **/
@Service
public class GradeMstServiceImpl implements GradeMstService {
	@Autowired
	private GradeMstMapper gradeMstMapper;

	@Override
	public PageInfo<GradeMstDto> gradeMstFindAll(GradeMstQueryParam param) {
		if (param == null) {
			return null;
		}
		PageHelper.startPage(param.getPage(), param.getRows());
		List<GradeMstDto> findAll = gradeMstMapper.gradeMstFindAll(param);
		return new PageInfo<>(findAll);
	}

	@Override
	public int insertGradeMst(GradeMst gradeMst) {
		return gradeMstMapper.insertSelective(gradeMst);
	}

	@Override
	public GradeMst getGradeMst(Long shopId, Long gradeId) {
		GradeMstKey key = new GradeMstKey();
		key.setShopId(shopId);
		key.setGradeId(gradeId);
		return gradeMstMapper.selectByPrimaryKey(key);
	}

	@Override
	public int updateGradeMst(GradeMst gradeMst) {
		return gradeMstMapper.updateByPrimaryKeySelective(gradeMst);
	}

	@Override
	public int deleteGradeMst(Long shopId, Long gradeId) {
		GradeMstKey key = new GradeMstKey();
		key.setShopId(shopId);
		key.setGradeId(gradeId);
		return gradeMstMapper.deleteByPrimaryKey(key);
	}
	
	/**
	 * 检查等级是否存在
	 * 
	 * @param baseQty
	 * @return
	 */
	public int checkGradeisExistByBaseQty(String baseQty) {
		return gradeMstMapper.checkGradeisExistByBaseQty(baseQty);
	}

	/**
	 * 检查等级名称是否存在
	 * 
	 * @param gradeNm
	 * @return
	 */
	public int checkGradeisExistByGradeNm(String gradeNm) {
		return gradeMstMapper.checkGradeisExistByGradeNm(gradeNm);
	}

	/**
	 * 检查该等级是否在用
	 * 
	 * @param valueOf
	 * @return
	 */
	public int checkGradeIsUsed(Long gradeId) {
		return gradeMstMapper.checkGradeIsUsed(gradeId);
	}
}
