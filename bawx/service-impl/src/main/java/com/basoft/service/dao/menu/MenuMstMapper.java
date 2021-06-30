package com.basoft.service.dao.menu;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.basoft.service.dto.menu.MenuDto;
import com.basoft.service.entity.menu.MenuMst;
import com.basoft.service.entity.menu.MenuMstExample;
import com.basoft.service.param.menu.MenuAuthQueryParam;
import com.basoft.service.param.menu.MenuQueryParam;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:19 2018/4/12
 **/
@Repository
public interface MenuMstMapper {
	int countByExample(MenuMstExample example);

	int deleteByExample(MenuMstExample example);

	int deleteByPrimaryKey(Long id);

	int insert(MenuMst record);

	int insertSelective(MenuMst record);

	List<MenuMst> selectByExample(MenuMstExample example);

	MenuMst selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") MenuMst record, @Param("example") MenuMstExample example);

	int updateByExample(@Param("record") MenuMst record, @Param("example") MenuMstExample example);

	int updateByPrimaryKeySelective(MenuMst record);

	int updateByPrimaryKey(MenuMst record);

	List<MenuDto> menuFindAll(MenuQueryParam menuQueryParam);

	List<MenuMst> menuFindByPid(@Param("pid") Long pid);

	int updateChildMenu(@Param("record") MenuMst record, @Param("menuMstList") List<MenuMst> menuMstList);

	int selectMaxOrder();

	List<MenuDto> findMenuByPid(MenuAuthQueryParam param);
}