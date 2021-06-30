package com.basoft.api.controller.cmCode;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.api.vo.cmCode.CmCodeVo;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.service.definition.cmCode.CmCodeService;
import com.basoft.service.entity.code.CmCodeWithBLOBs;
import com.basoft.service.param.cmCode.CmCodeQueryParam;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:19 2018/4/11
 **/

@RestController
public class CmCodeController extends BaseController{

    @Autowired
    private CmCodeService cmCodeService;

    /**
     * 字典列表
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/cmCodeFindAll",method = RequestMethod.GET)
    public ApiJson<List> cmCodeFindAll(@RequestParam(value = "page",defaultValue = "" )String page,
                                       @RequestParam(value = "rows",defaultValue = "" )String rows,
                                       @RequestParam(value = "param",defaultValue = "" )String param){

        CmCodeQueryParam queryParam = new CmCodeQueryParam();
        queryParam.setPage(Integer.valueOf(page));
        queryParam.setRows(Integer.valueOf(rows));
        queryParam.setParam(param);
        ApiJson<List> result = new ApiJson<>();

        try {
            PageInfo<CmCodeWithBLOBs> pageInfo = cmCodeService.findAllCmCode(queryParam);
            if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
                result.setPage(pageInfo.getPageNum());
                result.setRecords((int) pageInfo.getTotal());
                result.setTotal(pageInfo.getPages());
                result.setRows(pageInfo.getList().stream().map(data -> new CmCodeVo(data)).collect(Collectors.toList()));
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

    /**
     *@author Dong Xifu
     *@Date 2018/4/11_下午3:54
     *@describe 新增字典
     *@param bloBs
     *@return
     **/
    @PostMapping(value = "/inserCmCode",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int inserCmCode(@RequestBody CmCodeWithBLOBs bloBs) {
        if(StringUtils.isBlank(bloBs.getTbNm())||StringUtils.isBlank(bloBs.getFdNm())||bloBs.getCdId()==null)
            throw new BizException(ErrorCode.PARAM_MISSING);
        bloBs.setCreatedDt(new Date());
        return cmCodeService.insertCmCode(bloBs);
    }

    /**
     *@author Dong Xifu
     *@Date 2018/4/11 下午4:00
     *@describe 修改前回显
     *@param
     *@return
     **/
    public CmCodeVo getCmCode(@RequestParam(value = "tbNm")String tbNm,
                              @RequestParam(value = "fdNm")String fdNm,
                              @RequestParam(value = "cdId")String cdId){
        if("".equals(cdId)){
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        CmCodeWithBLOBs cmCode = cmCodeService.getCmCode(tbNm,fdNm,Integer.valueOf(cdId));
        return  new CmCodeVo(cmCode);
    }

    /**
     *@author Dong Xifu
     *@Date 2018/4/11 下午5:24
     *@describe 修改字典
     *@param
     *@return
     **/
    @PostMapping(value = "/upCmCode",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int upCmCode(@RequestBody CmCodeWithBLOBs bloBs){
        if(StringUtils.isBlank(bloBs.getTbNm())||StringUtils.isBlank(bloBs.getFdNm())||bloBs.getCdId()==null)
            throw new BizException(ErrorCode.PARAM_MISSING);

        return cmCodeService.updateCmCode(bloBs);
    }


    /**
     *@author Dong Xifu
     *@Date 2018/4/17 下午2:32
     *@describe 查询字典list 根据 表名和表字段
     *@param
     *@return
     **/
    @RequestMapping(value = "/findCodeBykey",method = RequestMethod.GET)
    public List<CmCodeVo> findCodeBykey(@RequestParam(value = "tbNm")String tbNm,
                                        @RequestParam(value = "fdNm")String fdNm){
        if(StringUtils.isBlank(fdNm)||StringUtils.isBlank(tbNm)){
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        List<CmCodeWithBLOBs> codeListByKey = cmCodeService.findCodeListByKey(tbNm, fdNm);
        return  codeListByKey.stream().map(data->new CmCodeVo(data)).collect(Collectors.toList());
    }

    /**
     * 根据表名和字段头部查询
     * @param tbNm
     * @param fdNm
     * @return
     */
    @RequestMapping(value = "/findCodeLikeBykey",method = RequestMethod.GET)
    public Map<String,List<CmCodeWithBLOBs>> findCodeLikeBykey(@RequestParam(value = "tbNm")String tbNm,
                                        @RequestParam(value = "fdNm")String fdNm){
        if(StringUtils.isBlank(fdNm)||StringUtils.isBlank(tbNm)){
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        List<CmCodeWithBLOBs> codeListByKey = cmCodeService.findCodeLikeBykey(tbNm, fdNm);
        Map<String,List<CmCodeWithBLOBs>> listMap= codeListByKey.stream().collect(Collectors.groupingBy(CmCodeWithBLOBs::getFdNm));
        return listMap;
        //return  codeListByKey.stream().map(data->new CmCodeVo(data)).collect(Collectors.toList());
    }

    /**
     *@author Dong Xifu
     *@Date 2018/4/11 下午5:47
     *@describe 禁用启用字典
     *@param
     *@return
     **/
    @RequestMapping(value = "/forbidCmCode",method = RequestMethod.GET)
    public int forbidCmCode(@RequestParam(value = "tbNm")String tbNm,
                              @RequestParam(value = "fdNm")String fdNm,
                              @RequestParam(value = "cdId")String cdId){
        if("".equals(cdId)){
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        return  cmCodeService.forbidCmCode(tbNm,fdNm,Integer.valueOf(cdId));
    }



    /**
     *@author Dong Xifu
     *@Date 2018/4/17 下午4:07
     *@describe删除代码
     *@param
     *@return
     **/
    @RequestMapping(value = "/deleteCmCode",method = RequestMethod.GET)
    public int deleteCmCode(@RequestParam(value = "tbNm")String tbNm,
                            @RequestParam(value = "fdNm")String fdNm,
                            @RequestParam(value = "cdId")String cdId){
        return  cmCodeService.deleteCmCode(tbNm,fdNm,Integer.valueOf(cdId));
    }

}


