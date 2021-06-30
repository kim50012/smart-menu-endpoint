package com.basoft.service.impl.cmCode;

import com.basoft.service.dao.cmCode.CmCodeMapper;
import com.basoft.service.definition.cmCode.CmCodeService;
import com.basoft.service.entity.code.CmCodeExample;
import com.basoft.service.entity.code.CmCodeKey;
import com.basoft.service.entity.code.CmCodeWithBLOBs;
import com.basoft.service.param.cmCode.CmCodeQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:54 2018/4/11
 **/
@Service
public class CmCodeServiceImpl implements CmCodeService{

    @Autowired
    private CmCodeMapper cmCodeMapper;
    @Override
    public PageInfo<CmCodeWithBLOBs> findAllCmCode(CmCodeQueryParam param) {
        if(param==null){
            return null;
        }
        PageHelper.startPage(param.getPage(),param.getRows());

        List<CmCodeWithBLOBs> list = cmCodeMapper.findAllByParam(param);
        return new PageInfo<>(list);
    }

    @Override
    public int insertCmCode(CmCodeWithBLOBs bloBs) {
        bloBs.setIsDelete((byte)0);
        return cmCodeMapper.insert(bloBs);
    }

    @Override
    public CmCodeWithBLOBs getCmCode(String tbNm,String fdNm,int cdId) {
        CmCodeKey key = new CmCodeKey();
        key.setTbNm(tbNm);
        key.setFdNm(fdNm);
        key.setCdId(cdId);
        return cmCodeMapper.selectByPrimaryKey(key); 
    }

    @Override
    public List<CmCodeWithBLOBs> findCodeListByKey(String tbNm, String fdNm) {
        CmCodeExample ex = new CmCodeExample();
        ex.createCriteria().andTbNmEqualTo(tbNm).andFdNmEqualTo(fdNm).andIsDeleteEqualTo((byte)0);
        return cmCodeMapper.selectByExampleWithBLOBs(ex);
    }

    @Override
    public List<CmCodeWithBLOBs> findCodeLikeBykey(String tbNm, String fdNm) {
        CmCodeExample ex = new CmCodeExample();
        ex.createCriteria().andTbNmEqualTo(tbNm).andFdNmLike('%'+fdNm+'%').andIsDeleteEqualTo((byte)0);
        return cmCodeMapper.selectByExampleWithBLOBs(ex);
    }

    @Override
    public int updateCmCode(CmCodeWithBLOBs bloBs) {
        bloBs.setModifyDt(new Date());
        return cmCodeMapper.updateByPrimaryKeySelective(bloBs);
    }

    @Override
    public int forbidCmCode(String tbNm,String fdNm,int cdId) {
        CmCodeExample ex = new CmCodeExample();
        ex.createCriteria().andTbNmEqualTo(tbNm).andFdNmEqualTo(fdNm)
        .andCdIdEqualTo(cdId);
        List<CmCodeWithBLOBs> bloBs = cmCodeMapper.selectByExampleWithBLOBs(ex);

        if (bloBs!=null) {
            if (bloBs.get(0).getIsDelete() == 0) {
                bloBs.get(0).setIsDelete((byte) 1);
            } else {
                bloBs.get(0).setIsDelete((byte) 0);
            }
        }
        return cmCodeMapper.updateByExampleWithBLOBs(bloBs.get(0),ex);
    }

    @Override
    public int deleteCmCode(String tbNm, String fdNm, int cdId) {
        CmCodeKey key = new CmCodeKey();
        key.setTbNm(tbNm);
        key.setFdNm(fdNm);
        key.setCdId(cdId);
        return cmCodeMapper.deleteByPrimaryKey(key);
    }
}
