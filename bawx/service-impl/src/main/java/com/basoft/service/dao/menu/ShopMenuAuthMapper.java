package com.basoft.service.dao.menu;

import com.basoft.service.dto.menu.MenuAuthDto;
import com.basoft.service.dto.menu.MenuDto;
import com.basoft.service.entity.menu.MenuMst;
import com.basoft.service.entity.menu.ShopMenuAuth;
import com.basoft.service.entity.menu.ShopMenuAuthExample;
import com.basoft.service.param.menu.MenuAuthQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopMenuAuthMapper {
    int countByExample(ShopMenuAuthExample example);

    int deleteByExample(ShopMenuAuthExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShopMenuAuth record);

    int insertSelective(ShopMenuAuth record);

    List<ShopMenuAuth> selectByExample(ShopMenuAuthExample example);

    ShopMenuAuth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ShopMenuAuth record, @Param("example") ShopMenuAuthExample example);

    int updateByExample(@Param("record") ShopMenuAuth record, @Param("example") ShopMenuAuthExample example);

    int updateByPrimaryKeySelective(ShopMenuAuth record);

    int updateByPrimaryKey(ShopMenuAuth record);

    int saveMenuAuthList(List<ShopMenuAuth> shopMenuAuthList);

    List<MenuAuthDto> findMenuByParam(MenuAuthQueryParam param);

    //查询以选择的菜单
    List<String> findSelectMenu(@Param("shopId") Long shopId);

    //查询当前登录的公众号能看见哪些菜单
    List<MenuDto> findMenuAuthByShop(MenuAuthQueryParam param);

    List<MenuMst> findLevelOne(Long shopId);
}