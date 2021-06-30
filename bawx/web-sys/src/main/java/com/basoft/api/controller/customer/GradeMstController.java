package com.basoft.api.controller.customer;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.api.vo.customer.GradeMstVo;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.customer.grade.GradeMstService;
import com.basoft.service.definition.wechat.BizConstants;
import com.basoft.service.dto.customer.GradeMstDto;
import com.basoft.service.entity.customer.grade.GradeMst;
import com.basoft.service.param.customer.GradeMstQueryParam;
import com.basoft.service.param.wechat.gradeMst.GradeMstForm;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:47 2018/4/23
 **/
@RestController
public class GradeMstController extends BaseController{

    @Autowired
    private IdService idService;
    @Autowired
    private GradeMstService gradeMstService;

    /**
     * 查询等级列表
     * 
     * @param page
     * @param rows
     * @param param
     * @return
     * @author Dong Xifu
     * @Date 2018/4/23 下午4:28
     */
    @RequestMapping(value = "/gradeMstFindAll",method = RequestMethod.GET)
    public ApiJson<List<GradeMstVo>> gradeMstFindAll(@RequestParam(value = "page",defaultValue = "1" )String page,
                                         @RequestParam(value = "rows",defaultValue = "20" )String rows,
                                         @RequestParam(value = "param",defaultValue = "" )String param) {
        ApiJson<List<GradeMstVo>> result = new ApiJson<>();
        GradeMstQueryParam queryParam = new GradeMstQueryParam();
        queryParam.setShopId(getShopId());
        queryParam.setPage(Integer.parseInt(page));
        queryParam.setRows(Integer.parseInt(rows));
        queryParam.setParam(param);

        try {
            PageInfo<GradeMstDto> pageInfo = gradeMstService.gradeMstFindAll(queryParam);
            if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
                result.setPage(pageInfo.getPageNum());
                result.setRecords((int) pageInfo.getTotal());
                result.setTotal(pageInfo.getPages());
                result.setRows(pageInfo.getList().stream().map(data -> new GradeMstVo(data)).collect(Collectors.toList()));
            } else {
                result.setPage(1);
                result.setRecords(0);
                result.setTotal(0);
                result.setRows(new ArrayList<GradeMstVo>());
            }

            result.setErrorCode(0);
            result.setErrorMsg("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
	/**
	 *  检验是否存在等级和等级名称
	 *  
	 * @param baseQty
	 * @param gradeNm
	 * @return
	 */
	@RequestMapping(value = "/checkGradeIsExist", method = RequestMethod.POST)
	// public Echo<?> checkGradeIsExist(@RequestParam(value = "baseQty") String baseQty, @RequestParam(value = "gradeNm") String gradeNm) {
	public Echo<?> checkGradeIsExist(@RequestBody Map<String,String> params) {
		// 检查等级
		int checkBaseQty = gradeMstService.checkGradeisExistByBaseQty(params.get("baseQty"));
		// 检查等级名称
		int checkGradeNm = gradeMstService.checkGradeisExistByGradeNm(params.get("gradeNm"));
		if (checkBaseQty > 0) {
			// 等级存在，等级名称也存在
			if(checkGradeNm > 0) {
				return new Echo<Integer>(3);
			} else {
				// 等级存在，等级名称不存在
				return new Echo<Integer>(1);
			}
		} else {
			if(checkGradeNm > 0) {
				// 等级不存在，等级名称存在
				return new Echo<Integer>(2);
			} else {
				// 等级不存在，等级名称也不存在
				return new Echo<Integer>(0);
			}
		}
	}

	/**
	 * @author Dong Xifu
	 * @Date 2018/4/24 下午1:45
	 * @describe 插入等级
	 * @param
	 * @return
	 **/
	@PostMapping(value = "/insertGradeMst", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> insertGradeMst(@Valid @RequestBody GradeMstForm form) {
		GradeMst gradeMst = new GradeMst();
		BeanUtils.copyProperties(form, gradeMst);
		gradeMst.setShopId(getShopId());
		gradeMst.setGradeId(idService.generateGradeMst());
		gradeMst.setIsUse(BizConstants.MENU_STATE_ENABLE);
		gradeMst.setCreatedDt(new Date());
		gradeMst.setCreatedId(getUserId());
		int result = gradeMstService.insertGradeMst(gradeMst);
		return new Echo<Integer>(result);
	}

    /**
     *@author Dong Xifu
     *@Date 2018/4/24 下午1:45
     *@describe 获取等级一条
     *@param
     *@return
     **/
    @RequestMapping(value = "/getGradeMst",method = RequestMethod.GET)
    public GradeMstVo getGradeMst(@RequestParam(value = "gradeId")String gradeId){
        if(StringUtils.isBlank(gradeId)){
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        GradeMst gradeMst = gradeMstService.getGradeMst(getShopId(), Long.valueOf(gradeId));
        return new GradeMstVo(gradeMst);
    }

    /**
     *@author Dong Xifu
     *@Date 2018/4/24 下午1:45
     *@describe 修改等级/停用等级
     *@param
     *@return
     **/
    @PostMapping(value = "/updateGradeMst",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Echo<?> updateGradeMst(@RequestBody GradeMst gradeMst){
        gradeMst.setShopId(getShopId());
        gradeMst.setCreatedId(getUserId());
        int result = gradeMstService.updateGradeMst(gradeMst);
        return new Echo<Integer>(result);
    }

    /**
     *@author Dong Xifu
     *@Date 2018/4/24 下午1:47
     *@describe 删除等级
     *@param
     *@return
     **/
	@RequestMapping(value = "/deleteGradeMst", method = RequestMethod.GET)
	public Echo<?> deleteGradeMst(@RequestParam(value = "gradeId") String gradeId) {
		// 检查该等级是否在用
		int check = gradeMstService.checkGradeIsUsed(Long.valueOf(gradeId));
		if (check > 0) {
			return new Echo<Integer>(9999);
		} else {
			int result = gradeMstService.deleteGradeMst(getShopId(), Long.valueOf(gradeId));
			return new Echo<Integer>(result);
		}
	}
}
