package com.basoft.api.controller.menu;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.api.vo.menu.MenuAuthSelectVo;
import com.basoft.api.vo.menu.MenuAuthVo;
import com.basoft.api.vo.menu.MenuVO;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.menu.MenuAuthService;
import com.basoft.service.definition.menu.MenuService;
import com.basoft.service.dto.menu.MenuAuthDto;
import com.basoft.service.dto.menu.MenuDto;
import com.basoft.service.entity.menu.MenuMst;
import com.basoft.service.param.menu.MenuAuthForm;
import com.basoft.service.param.menu.MenuAuthQueryParam;
import com.basoft.service.param.menu.MenuQueryParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:57 2018/4/20
 **/
@RestController
public class MenuAuthController extends BaseController{

    @Autowired
    private IdService idService;
    @Autowired
    private MenuAuthService menuAuthService;
    @Autowired
    private MenuService menuService;


    /**
     *@author Dong Xifu
     *@Date 2018/4/20 下午5:55
     *@describe  查询菜单及子集
     *@param
     *@return
     **/
    @RequestMapping(value = "/findMenuAuthAll",method = RequestMethod.GET)
    public MenuAuthSelectVo findMenuAuthAll(@RequestParam(value = "shopId")String shopId){
        MenuAuthQueryParam param = new MenuAuthQueryParam();
        param.setMenuLevel(1);
        param.setShopId(shopId);
        List<MenuAuthDto> menuLevelOne = menuAuthService.findMenuByParam(param);
        param.setMenuLevel(2);
        for (MenuAuthDto dto:menuLevelOne) {
            param.setMenuId(dto.getId());
            List<MenuAuthDto> menuLevelTwo =  menuAuthService.findMenuByParam(param);//查询二级菜单
            dto.setChildren(menuLevelTwo);//将二级菜单放入dto中
        }
        List<MenuAuthVo>  menuAuthVoList = menuLevelOne.stream().map(dto -> new MenuAuthVo(dto)).collect(Collectors.toList());
        List<String> selectMenuList = menuAuthService.findSelectMenu(Long.valueOf(shopId));
        return new MenuAuthSelectVo(menuAuthVoList,selectMenuList);
    }

    /**
     *@author Dong Xifu
     *@Date 2018/4/20 下午5:02
     *@describe 新增或修改权限
     *@param
     *@return
     **/
    @PostMapping(value = "/saveMenuAuth",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int saveMenuAuth(@RequestBody  MenuAuthForm form) {
       return menuAuthService.saveMenuAuth(form);
    }

    /**
     * @param shopId
     * @return int
     * @describe 新建公众号时需要插入预设菜单
     * @author Dong Xifu
     * @date 2018/5/24 下午3:20
     */
    public int insertMenuAuthAll(Long shopId) {
        MenuQueryParam param = new MenuQueryParam();
        param.setDisabled(true);
        List<MenuDto> dtoList = menuService.menuFindAllByDisaBled(param);
        MenuAuthForm form = new MenuAuthForm();
        form.setShopId(shopId);
        try {
            List<Long> checkList = new ArrayList<>();
            for (MenuDto dto : dtoList) {
                checkList.add(dto.getMenuId());
            }
            form.setCheckList(checkList);
            int result = menuAuthService.saveMenuAuth(form);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     *@author Dong Xifu
     *@Date 2018/4/20 下午5:02
     *@describe 新增或修改权限
     *@param
     *@return
     **/
    @PostMapping(value = "/findMenuAuthByShop",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int findMenuAuthByShop(@RequestBody  MenuAuthForm form) {
        return menuAuthService.saveMenuAuth(form);
    }


    /**
     *@author Dong Xifu
     *@Date 2018/5/11 下午2:08
     *@describe 查询当前公众号能看见哪些菜单
     *@param
     *@return
     **/
    @RequestMapping(value = "/findMenuAuthByShop",method = RequestMethod.GET)
    public ApiJson<List> findMenuAuthByShop(){

        ApiJson<List> result = new ApiJson<>();
        try {
            MenuAuthQueryParam param = new MenuAuthQueryParam();
            param.setShopId(String.valueOf(getShopId()));
            param.setMenuLevel(1);
            param.setPid(0L);

            List<MenuMst> menuMstIdOneList = menuAuthService.findLevelOne(getShopId());//查询一级菜单Id
            if(menuMstIdOneList!=null&&menuMstIdOneList.size()>0){
                param.setMenuList(menuMstIdOneList);
            }else {
                result.setRecords(0);
                result.setTotal(0);
                result.setRows(new ArrayList());
                return  result;
            }
            //List<MenuDto> dtoLevelOneList = menuAuthService.findMenuAuthByShop(param);
            List<MenuDto> dtoLevelOneList = menuService.findMenuByPid(param);
                param.setMenuLevel(2);
            for (MenuDto dto:dtoLevelOneList) {
                param.setPid(dto.getMenuId());
                List<MenuDto> dtoLevelTwo =  menuAuthService.findMenuAuthByShop(param);
                dto.setChildren(dtoLevelTwo);
            }
            if (dtoLevelOneList != null && CollectionUtils.isNotEmpty(dtoLevelOneList)) {
                result.setRecords(dtoLevelOneList.size());
                result.setTotal(1);
                result.setRows(dtoLevelOneList.stream().map(data -> new MenuVO(data)).collect(Collectors.toList()));
            } else {
                result.setPage(1);
                result.setRecords(0);
                result.setTotal(0);
                result.setRows(new ArrayList());
            }
            result.setErrorCode(0);
            result.setErrorMsg("Success");
        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;
    }

}
