package com.basoft.service.impl.menu;

import com.basoft.service.dao.menu.ShopMenuAuthMapper;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.menu.MenuAuthService;
import com.basoft.service.dto.menu.MenuAuthDto;
import com.basoft.service.dto.menu.MenuDto;
import com.basoft.service.entity.menu.MenuMst;
import com.basoft.service.entity.menu.ShopMenuAuth;
import com.basoft.service.entity.menu.ShopMenuAuthExample;
import com.basoft.service.param.menu.MenuAuthForm;
import com.basoft.service.param.menu.MenuAuthQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:28 2018/4/20
 **/
@Service
@Slf4j
public class MenuAuthServiceImpl implements MenuAuthService{

    @Autowired
    private IdService idService;
    @Autowired
    private ShopMenuAuthMapper shopMenuAuthMapper;

    @Override
    public List<MenuAuthDto> findMenuByParam(MenuAuthQueryParam param) {
        return shopMenuAuthMapper.findMenuByParam(param);
    }

    @Override
    //@Transactional(transactionManager = "primaryTransactionManager", rollbackFor = Exception.class)
    @Transactional
    public int saveMenuAuth(MenuAuthForm form) {
        ShopMenuAuthExample ex = new ShopMenuAuthExample();
        ex.createCriteria().andShopIdEqualTo(form.getShopId());
        shopMenuAuthMapper.deleteByExample(ex);
        /*for (ShopMenuAuth shopMenuAuth:form.getShopMenuAuthList()) {
            Long menuAuthId = idService.generateMenuAuth()- org.apache.commons.lang3.RandomUtils.nextLong(1, 9999);
            shopMenuAuth.setId(menuAuthId);
            shopMenuAuth.setShopId(form.getShopId());
            shopMenuAuth.setCreateDt(new Date());
        }*/
        List<ShopMenuAuth> shopMenuAuthList = new ArrayList<>();
        Long num = 1L;
        for (int i=0; i<form.getCheckList().size();i++) {
            Long random = (long)RandomUtils.nextInt(500);
            log.info("menuRandom="+random);
            num+=random;
            Long menuAuthId = idService.generateMenuAuth()-num;
            ShopMenuAuth shopMenuAuth = new ShopMenuAuth();
            shopMenuAuth.setId(menuAuthId);
            shopMenuAuth.setShopId(form.getShopId());
            shopMenuAuth.setMenuId(form.getCheckList().get(i));
            shopMenuAuth.setCreateDt(new Date());
            shopMenuAuthList.add(shopMenuAuth);
        }

        // 如果没有选中菜单则不再进行保存
        if(shopMenuAuthList.size() == 0) {
        	// return 0;// 由于前段处理返回0为连接失败，调整此处返回值
        	return 9999;
        }
        return shopMenuAuthMapper.saveMenuAuthList(shopMenuAuthList);
    }

    @Override
    public List<String> findSelectMenu(Long shopId) {
        return shopMenuAuthMapper.findSelectMenu(shopId);
    }

    @Override
    public List<MenuDto> findMenuAuthByShop(MenuAuthQueryParam param) {
        return shopMenuAuthMapper.findMenuAuthByShop(param);
    }

    @Override
    public List<MenuMst> findLevelOne(Long shopId) {
        return shopMenuAuthMapper.findLevelOne(shopId);
    }
}
