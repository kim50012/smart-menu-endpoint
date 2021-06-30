package com.basoft.service.definition.cmCode;

import com.basoft.service.entity.code.CmCodeWithBLOBs;
import com.basoft.service.param.cmCode.CmCodeQueryParam;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:字典接口
 * @Date Created in 下午2:45 2018/4/11
 **/
public interface CmCodeService {

    //查询字典
    public PageInfo<CmCodeWithBLOBs> findAllCmCode(CmCodeQueryParam param);

    //新增字典
    public int insertCmCode(CmCodeWithBLOBs bloBs);

    //回显字典
    public CmCodeWithBLOBs getCmCode(String tbNm,String fdNm,int cdId);

    //查询下拉框字典list
    public List<CmCodeWithBLOBs> findCodeListByKey(String tbNm,String fdNm);

    public List<CmCodeWithBLOBs> findCodeLikeBykey(String tbNm,String fdNm);

    //修改字典
    public int updateCmCode(CmCodeWithBLOBs bloBs);

    //禁用启用字典
    public int forbidCmCode(String tbNm,String fdNm,int cdId);

    //删除字典
    public int deleteCmCode(String tbNm,String fdNm,int cdId);

}
