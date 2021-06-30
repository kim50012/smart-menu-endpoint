package com.basoft.service.impl.custGrade;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.basoft.service.enumerate.BusinessTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basoft.service.dao.customer.cust.CustMapper;
import com.basoft.service.dao.customer.custShop.CustShopMapper;
import com.basoft.service.definition.customer.grade.CustGradeService;
import com.basoft.service.dto.customer.CustGradeDto;
import com.basoft.service.dto.customer.CustSexRadioDto;
import com.basoft.service.dto.customer.WxUserTotalProvinDto;
import com.basoft.service.entity.customer.custShop.CustShop;
import com.basoft.service.entity.customer.custShop.CustShopExample;
import com.basoft.service.param.customer.CustGradeQueryParam;
import com.basoft.service.param.customer.CustShopParam;
import com.basoft.service.param.customer.WxUserQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:28 2018/4/25
 **/
@Service
public class CustGradeServiceImpl implements CustGradeService{
	@Autowired
	private CustMapper custMapper;

	@Autowired
	private CustShopMapper custShopMapper;

	/**
	 * 根据查询条件分页查询关注客户列表
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<CustGradeDto> findAllCustGrade(CustGradeQueryParam param) {
		PageHelper.startPage(param.getPage(), param.getRows());
		List<CustGradeDto> allCustGrade = custMapper.findAllCustGrade(param);

		allCustGrade.stream().map(cust->{
			String nickName = baseConvertStr(cust.getCustNickNm());
			cust.setCustNickNm(nickName);
			return  cust;

			}).collect(Collectors.toList());

		return new PageInfo<>(allCustGrade);
	}


	private static String baseConvertStr(String str) {
		if (null != str) {
			Base64.Decoder decoder = Base64.getDecoder();
			try {
				return new String(decoder.decode(str.getBytes()), "UTF-8");
			} catch (Exception e) {
				return str;
			}
		}
		return null;

	}

    @Override
    //@Transactional(transactionManager = "primaryTransactionManager",rollbackFor = Exception.class)
    @Transactional
	public int setGradeBycust(CustShopParam custShopParam) {
		int setCount = 0;
		for (Long custSysId : custShopParam.getCustList()) {
			// 删除存在的客户和等级关系
			CustShopExample ex = new CustShopExample();
			ex.createCriteria().andCustSysIdEqualTo(custSysId);
			custShopMapper.deleteByExample(ex);
			// 新增客户和等级关系
			CustShop custShop = new CustShop();
			custShop.setShopId(custShopParam.getShopId());
			custShop.setCreatedDt(new Date());
			custShop.setCustSysId(custSysId);
			custShop.setGradeId(custShopParam.getGradeId());
			custShop.setCustPoint(0);
			custShopMapper.insertSelective(custShop);
			setCount++;
		}
		return setCount;
	}

	@Override
	public CustSexRadioDto selectSexRadio(WxUserQueryParam param) {
		return custMapper.selectSexRadio(param);
	}

	@Override
	public List<WxUserTotalProvinDto> selectSexRadioList(WxUserQueryParam param) {
		return custMapper.selectSexRadioList(param);
	}
}
