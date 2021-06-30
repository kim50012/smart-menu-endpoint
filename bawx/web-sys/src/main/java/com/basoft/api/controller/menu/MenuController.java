package com.basoft.api.controller.menu;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.api.vo.menu.MenuVO;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.menu.MenuService;
import com.basoft.service.definition.wechat.BizConstants;
import com.basoft.service.dto.menu.MenuDto;
import com.basoft.service.entity.menu.MenuMst;
import com.basoft.service.param.menu.MenuQueryParam;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:菜单
 * @Date Created in 下午1:31 2018/4/8
 **/
@RestController
public class MenuController extends BaseController {
	@Autowired
	private IdService idService;

	@Autowired
	private MenuService menuService;

	/**
	 * 菜单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/menuFindAll", method = RequestMethod.GET)
    public ApiJson<List> menuFindAll(@RequestParam(value = "page",defaultValue = "1") String page,
                                     @RequestParam(value = "rows",defaultValue = "20") String rows,
                                     @RequestParam(value = "menuName",defaultValue = "" )String menuName,
                                     @RequestParam(value="menuLevel",defaultValue = "")String menuLevel){
		ApiJson<List> result = new ApiJson<>();
		try {
			MenuQueryParam menuQueryParam = new MenuQueryParam();
			menuQueryParam.setPage(Integer.valueOf(page));
			menuQueryParam.setRows(Integer.valueOf(rows));
			menuQueryParam.setMenuNm(menuName);
			menuQueryParam.setMenuLevel(menuLevel);
			PageInfo<MenuDto> pageInfo = menuService.menuFindAll(menuQueryParam);

			for (MenuDto dto : pageInfo.getList()) {
				if (1 == dto.getMenuLevel()) {
					dto.setMenuNm2("");
				} else if (2 == dto.getMenuLevel()) {
					List<MenuMst> menuMsts = menuService.menuFindByPid(dto.getMenuPid());
					dto.setMenuNm2(dto.getMenuNm());
					if (menuMsts != null&&menuMsts.size()>0)
						dto.setMenuNm(menuMsts.get(0).getMenuNm());
				}
			}


			if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
				result.setPage(pageInfo.getPageNum());
				result.setRecords((int) pageInfo.getTotal());
				result.setTotal(pageInfo.getPages());
				result.setRows(pageInfo.getList().stream().map(data -> new MenuVO(data)).collect(Collectors.toList()));
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
	 * 查询一级菜单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getMenuLevelOne", method = RequestMethod.GET)
	public List<MenuVO> getMenuLevelOne() {
		MenuQueryParam menuQueryParam = new MenuQueryParam();
		menuQueryParam.setMenuLevel(BizConstants.MENU_LEVEL_ONE);
		PageInfo<MenuDto> menuDtoPageInfo = menuService.menuFindAll(menuQueryParam);
		return menuDtoPageInfo.getList().stream().map(data -> new MenuVO(data)).collect(Collectors.toList());
	}

	/**
	 * 新增菜单
	 * @param menuMst
	 * @authour DongXifu
	 * @return
	 */
	@PostMapping(value = "/inserMenuMst", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> inserMenuMst(@RequestBody MenuMst menuMst) {
		menuMst.setId(idService.generateMenuId());
		menuMst.setMenuId(idService.generateMenuMst());
		int result = menuService.saveMenuMst(menuMst);
		return new Echo<Integer>(result);
	}

	/**
	 * 修改前回显
	 * 
	 * @param id
	 * @return MenuVO
	 */
	@RequestMapping(value = "/getMenuById", method = RequestMethod.GET)
	public MenuVO getMenu(@RequestParam(value = "id") String id) {
		if ("".equals(id)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
		MenuMst menu = menuService.getMenuById(Long.valueOf(id));
		return new MenuVO(menu);
	}

	/**
	 * @author Dong Xifu
	 * @Date 2018/4/9_下午12:00
	 * @describe 修改菜单
	 * @param menuMst
	 * @return int
	 **/
	@PostMapping(value = "/updateMenu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> upMenu(@Validated @RequestBody MenuMst menuMst) {
		int result = menuService.upMenuMst(menuMst);
		return new Echo<Integer>(result);
	}

	/**
	 * @author Dong Xifu
	 * @Date 2018/4/9_下午12:02
	 * @describe 禁用启用菜单
	 * @param id
	 * @return int
	 **/
	@RequestMapping(value = "/forbidMenuById", method = RequestMethod.GET)
	public Echo<?> forbidMenuById(@RequestParam(value = "id") String id, @RequestParam(value = "activeSts") String activeSts) {
		int result = menuService.forbidMenu(Long.valueOf(id), (byte) Integer.parseInt(activeSts));
		return new Echo<Integer>(result);
	}

	/**
	 * @author Dong Xifu
	 * @Date 2018/4/9_下午4:07
	 * @describe 删除菜单
	 * @param id
	 * @return
	 **/
	@RequestMapping(value = "/delMenuById", method = RequestMethod.GET)
	public int delMenuById(@RequestParam(value = "id") String id) {
		return menuService.delMenuMst(Long.valueOf(id));
	}
}