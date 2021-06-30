package com.basoft.api.controller.weixin.shopWxmenu;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.api.vo.wechat.shopMenu.ShopWxMenuVo;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.base.IdService;
// import com.basoft.service.definition.wechat.shopMenu.OracleShopMenuWxService;// 20200427
import com.basoft.service.definition.wechat.shopMenu.ShopMenuWxService;
import com.basoft.service.dto.wechat.shopMenu.ShopWxMenuDto;
import com.basoft.service.entity.wechat.shopMenu.ShopWxMenu;
import com.basoft.service.param.wechat.shopMenu.ShopWxMenuForm;
import com.basoft.service.param.wechat.shopMenu.ShopWxMenuQueryParam;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;// 20200427
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:34 2018/4/12
 **/

@RestController
public class ShopWxMenuController extends BaseController{
    @Autowired
    private IdService idService;
    
    @Autowired
    private ShopMenuWxService shopMenuWxService;

    // 20200427
    //@Autowired
    //@Qualifier("oracleShopWxMenuServiceImpl")
    //private OracleShopMenuWxService oracleShopMenuWxService;

    /**
     *@author Dong Xifu
     *@Date 2018/4/12 上午10:44
     *@describe 查询所有菜单
     *@param
     *@return
     **/
    @RequestMapping(value = "/shopWxMenuFindAll",method = RequestMethod.GET)
    public ApiJson<List> shopWxMenuFindAll(){
        ApiJson<List> result = new ApiJson<>();

        ShopWxMenuQueryParam param = new ShopWxMenuQueryParam();
        param.setMenuParentId(0L);
        param.setShopId(getShopId());
        List<ShopWxMenuDto> list = shopMenuWxService.findAllShopWxMenu(param);
        for (ShopWxMenuDto dto:list) {
            /*if(dto.getMenuOpId()!=null){
                ShopWxNewDto shopWxNewDto = shopWxNewService.getShopWxNewsItem(dto.getMenuOpId());
                dto.setDto(shopWxNewDto.getDto());
                dto.setShopWxNewsItemChild(shopWxNewDto.getShopWxNewsItemChild());
            }*/

            param.setMenuParentId(dto.getMenuId());
            List<ShopWxMenuDto> twoShopMenuList = shopMenuWxService.findAllShopWxMenu(param);

            dto.setSecondShopMenu(twoShopMenuList);
        }
        result.setRows(list.stream().map(data-> new ShopWxMenuVo(data)).collect(Collectors.toList()));
        return result;
    }

    /**
     *@author Dong Xifu
     *@Date 2018/4/12 下午1:17
     *@describe 操作菜单
     *@param
     *@return
     **/
    @PostMapping(value = "/saveShopWxMenu",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Echo<?> saveShopWxMenu(@RequestBody ShopWxMenuForm form) {
        Long forNum = 1L;
        for (ShopWxMenuDto dto:form.getWxMenuList()) {
            forNum+= RandomUtils.nextLong(1, 999);
            dto.setShopId(getShopId());
            dto.setMenuId(idService.generateWxMenu()-forNum);
            dto.setMenuParentId(0L);
            dto.setCreateDt(new Date());
            dto.setCreateId(getUserId());
            if(dto.getSortNo()==null){
                dto.setSortNo((byte) 1);
            }

            /*dto.setMenuOpType(BizConstants.MENUOPTYPE_VIEW);
            dto.setMenuSts(BizConstants.MENU_STATE_ENABLE);*/
            List<ShopWxMenuDto> secondMenuList = dto.getSecondShopMenu();

            if(secondMenuList!=null&&secondMenuList.size()>0)

            for (ShopWxMenu entity:secondMenuList) {
                forNum+= RandomUtils.nextLong(1, 999);
                entity.setShopId(getShopId());
                entity.setMenuParentId(dto.getMenuId());
                Long num = System.currentTimeMillis()-forNum;
                Long random =  RandomUtils.nextLong(1, num);
                entity.setMenuId(idService.generateWxMenu()-random);
                entity.setCreateId(getUserId());
                entity.setCreateDt(new Date());
                if(entity.getSortNo()==null){
                    entity.setSortNo((byte)1);
                }
                /*entity.setMenuSts(BizConstants.MENU_STATE_ENABLE);*/
            }
        }
        form.setShopId(getShopId());
        int result = shopMenuWxService.saveShopWxMenu(form);
        return new Echo<Integer>(result) ;
    }

    /**
     *@author Dong Xifu
     *@Date 2018/4/12 下午1:18
     *@describe 菜单修改前回显
     *@param
     *@return
     **/
    @RequestMapping(value = "/getShopWxMenu",method = RequestMethod.GET)
    public ShopWxMenuVo getShopWxMenu(@RequestParam(value = "menuId")String menuId){
        if("".equals(menuId)){
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        Long shopId = getShopId();
        ShopWxMenu shopWxMenu = shopMenuWxService.getShopWxMenu(shopId,Long.valueOf(menuId));
        return  new ShopWxMenuVo(shopWxMenu);
    }

    /**
     *@author Dong Xifu
     *@Date 2018/4/12 下午1:24
     *@describe 修改菜单
     *@param
     *@return
     **/
    @PostMapping(value = "/upShopWxMenu",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Echo<?> upShopWxMenu(@RequestBody ShopWxMenu menu){
        menu.setShopId(getShopId());
        menu.setModifyId(getUserId());
        int result = shopMenuWxService.upShopWxMenu(menu);
        return new Echo<Integer>(result);
    }

    @RequestMapping(value = "/delShopWxMenu",method = RequestMethod.GET)
    public Echo<?> delShopWxMenu(@RequestParam(value = "menuId")String menuId){
        if("".equals(menuId)){
            throw new BizException(ErrorCode.PARAM_MISSING);
        }
        int result = shopMenuWxService.deletShopWxMenu(Long.valueOf(menuId));
        return new Echo<Integer>(result);
    }

	/**
	 * 发布菜单
	 **/
	@RequestMapping(value = "/publishMenu", method = RequestMethod.POST)
	public Echo<?> publishMenu() {
		Long shopId = getShopId();
		
		// 检查菜单-start
		String dbType = getGlobalCommonConfig("dbType");
		boolean checkResult = false;
		if("mysql".equals(dbType)) {
			checkResult = shopMenuWxService.checkMenu(shopId);
		} else if("oracle".equals(dbType)) {
		    // 20200427
			//checkResult = oracleShopMenuWxService.checkMenu(shopId);
		} else {
			throw new BizException(ErrorCode.BIZ_EXCEPTION, "没有指定数据库类型！");
		}
		if (!checkResult) {
			// TODO 需要进行国际化
			throw new BizException(ErrorCode.BIZ_EXCEPTION, "菜单必须指定操作。如果存在二级菜单，二级菜单必须指定操作！");
		}
		// 检查菜单-end
		
		// 发布菜单留档并进行发布-start
		Long i = 0L;
		if("mysql".equals(dbType)) {
			i = shopMenuWxService.publishMenu(shopId);
		} else if("oracle".equals(dbType)) {
            // 20200427
		    //i = oracleShopMenuWxService.publishMenu(shopId);
		} else {
			throw new BizException(ErrorCode.BIZ_EXCEPTION, "没有指定数据库类型！");
		}
		if (i > 0) {
			return new Echo<String>("OK");
		}
		// 发布菜单留档并进行发布-end
		
		return new Echo<String>("FAILURE");
	}
}
