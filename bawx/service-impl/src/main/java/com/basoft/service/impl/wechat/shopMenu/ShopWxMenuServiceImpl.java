package com.basoft.service.impl.wechat.shopMenu;

import com.basoft.core.ware.wechat.domain.menu.Button;
import com.basoft.core.ware.wechat.domain.menu.ClickButton;
import com.basoft.core.ware.wechat.domain.menu.ComplexButton;
import com.basoft.core.ware.wechat.domain.menu.Menu;
import com.basoft.core.ware.wechat.domain.menu.ViewButton;
import com.basoft.core.ware.wechat.exception.WeixinErrorException;
import com.basoft.core.ware.wechat.util.Oauth2Utils;
import com.basoft.core.ware.wechat.util.WeixinMenuUtils;
import com.basoft.service.dao.wechat.appinfo.AppInfoMapper;
import com.basoft.service.dao.wechat.shopMenu.ShopWxMenuMapper;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechat.shopMenu.ShopMenuWxService;
import com.basoft.service.dto.wechat.shopMenu.ShopWxMenuDto;
import com.basoft.service.entity.menu.MenuMst;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import com.basoft.service.entity.wechat.shopMenu.ShopWxMenu;
import com.basoft.service.entity.wechat.shopMenu.ShopWxMenuExample;
import com.basoft.service.entity.wechat.shopMenu.ShopWxMenuKey;
import com.basoft.service.param.wechat.shopMenu.ShopWxMenuForm;
import com.basoft.service.param.wechat.shopMenu.ShopWxMenuQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:09 2018/4/12
 **/
@Slf4j
@Service
public class ShopWxMenuServiceImpl implements ShopMenuWxService {
    @Autowired
    private ShopWxMenuMapper shopWxMenuMapper;

    @Autowired
    private AppInfoMapper appInfoMapper;

    @Autowired
    private WechatService wechatService;

    @Override
    public List<ShopWxMenuDto> findAllShopWxMenu(ShopWxMenuQueryParam param) {

        List<ShopWxMenuDto> dto = shopWxMenuMapper.findAllShopWxMenu(param);
        return dto;
    }

    @Override
    public List<ShopWxMenu> secondMenuList(Long parentId) {
        ShopWxMenuExample ex = new ShopWxMenuExample();
        ex.createCriteria().andMenuParentIdEqualTo(parentId);
        return shopWxMenuMapper.selectByExample(ex);
    }

    @Override
    //@Transactional(transactionManager = "primaryTransactionManager", rollbackFor = Exception.class)
    @Transactional
    public int saveShopWxMenu(ShopWxMenuForm form) {

        shopWxMenuMapper.deleteAllShopWxMenu(form.getShopId());
        List<ShopWxMenuDto> wxMenuList = form.getWxMenuList();
        for (ShopWxMenuDto dto : wxMenuList) {
            List<ShopWxMenuDto> twoShopMenuList = dto.getSecondShopMenu();
            if (twoShopMenuList != null && twoShopMenuList.size() > 0) {
                shopWxMenuMapper.inserTwoShopMenu(twoShopMenuList);
            }

        }
        return shopWxMenuMapper.insertShopWxMenu(form.getWxMenuList());
    }

    @Override
    public ShopWxMenu getShopWxMenu(Long shopId, Long menuId) {
        ShopWxMenuKey key = new ShopWxMenuKey();
        key.setShopId(shopId);
        key.setMenuId(menuId);
        return shopWxMenuMapper.selectByPrimaryKey(key);
    }

    @Override
    public int upShopWxMenu(ShopWxMenu shopWxMenu) {
        shopWxMenu.setModifyDt(new Date());
        return shopWxMenuMapper.updateByPrimaryKeySelective(shopWxMenu);
    }

    @Override
    public int deletShopWxMenu(Long menuId) {
        ShopWxMenuKey key = new ShopWxMenuKey();
        key.setMenuId(menuId);
        return shopWxMenuMapper.deleteByPrimaryKey(key);
    }

    /**
     * 检验菜单设置是否符合上传发布规则
     *
     * @param shopId
     * @return
     */
    public boolean checkMenu(Long shopId) {
        List<MenuMst> reList = shopWxMenuMapper.checkMenu(shopId);
        if (reList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 发布菜单留档并进行发布
     *
     * @param shopId
     * @return
     */
    public Long publishMenu(Long shopId) {
        // 对发布的菜单留档
        Map<String, Long> iMap = new HashMap<String, Long>();
        iMap.put("shopId", shopId);
        shopWxMenuMapper.insertPublisMenuWithCount(iMap);
        Long insertCount = (Long) iMap.get("insertCount");

        // 发布菜单到微信平台
        if (insertCount > 0) {
            Long uploadCount = uploadMenus(shopId);
            return uploadCount;
        }
        return insertCount;
    }

    /**
     * 发布微信菜单到微信服务器
     *
     * @param shopId
     */
    private Long uploadMenus(Long shopId) {
        AppInfo appInfo = appInfoMapper.selectAppInfoByShopId(shopId);
        if (appInfo == null) {
            throw new WeixinErrorException("该商店没有权限发布菜单!");
        }

        //List<Map<String, Object>> list = shopWxMenuMapper.selectMenuLevel1(shopId);

        List<Map<String, Object>> menuList = shopWxMenuMapper.selectMenuLevel1ByShop(shopId);
        for (Map<String, Object> map : menuList) {
            log.info("menuName=" + (String) map.get("IS_LEAF"));
        }

        List<Button> buttonList = new ArrayList<Button>();
        for (Map<String, Object> map : menuList) {
            Long menuId = (Long) map.get("MENU_ID");
            String menuName = (String) map.get("MENU_NM");
            // Long eventKey = (Long) map.get("MENU_OP_ID");
            String eventKey = (String) map.get("MENU_OP_WX_ID");
            String url = (String) map.get("MENU_OP_COM");
            String isLeaf = (String) map.get("IS_LEAF");
            String menuType = (String) map.get("MENU_OP_TYPE");

            if ("Y".equals(isLeaf)) {
                if ("click".equals(menuType)) {
                    //20191118-改造。一级菜单支持发送文本消息、图文消息和图片消息等-start
					/*ClickButton btn = new ClickButton();
					btn.setName(menuName);
					btn.setType(menuType);
					btn.setKey(eventKey + "");
					buttonList.add(btn);*/

                    ClickButton level1btn = new ClickButton();
                    level1btn.setType(menuType);
                    level1btn.setName(menuName);
                    level1btn.setKey(eventKey + "");

                    // 消息类型 1-图文 2-图片 3-文本
                    Integer menuMsgType = (Integer) map.get("MENU_MSG_TYPE");
                    // 微信端media id:图文或图频的id(1##mediaId或2##mediaID)
                    String meadiaId = (String) map.get("MENU_OP_WX_ID");
                    if (menuMsgType == 1) {
                        level1btn.setKey(meadiaId);
                    } else if (menuMsgType == 2) {
                        level1btn.setKey(meadiaId);
                    } else if (menuMsgType == 3) {
                        level1btn.setKey("3##" + url);
                    }
                    buttonList.add(level1btn);
                    //20191118-改造。一级菜单支持发送文本消息、图文消息和图片消息等-end
                } else if ("scancode_push".equals(menuType)) {
                    ClickButton btn = new ClickButton();
                    btn.setName(menuName);
                    btn.setType(menuType);
                    btn.setKey(eventKey + "");

                    buttonList.add(btn);
                } else if ("scancode_waitmsg".equals(menuType)) {
                    ClickButton btn = new ClickButton();
                    btn.setName(menuName);
                    btn.setType(menuType);
                    btn.setKey(eventKey + "");

                    buttonList.add(btn);
                } else if ("view".equals(menuType)) {
                    ViewButton btn = new ViewButton();
                    btn.setName(menuName);
                    btn.setType(menuType);
                    if (whetherMakeOauth2Url(appInfo, url)) {
                        com.basoft.core.ware.wechat.domain.AppInfo ai = new com.basoft.core.ware.wechat.domain.AppInfo();
                        try {
                            BeanUtils.copyProperties(ai, appInfo);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        btn.setUrl(Oauth2Utils.getMenuIinkUrlNew(ai, menuId));
                    } else {
                        btn.setUrl(url);
                    }
                    buttonList.add(btn);
                }
            } else {
                ComplexButton cbtn = new ComplexButton();
                cbtn.setName(menuName);
                // cbtn.setSub_button(new Button[] { btn11, btn12, btn13, btn14, btn15});

                List<Button> subBtns = new ArrayList<Button>();
                List<Map<String, Object>> sublist = shopWxMenuMapper.selectMenuLevel2(shopId, menuId);
                log.info("sublist.size=" + sublist.size());
                for (Map<String, Object> item : sublist) {
                    Long menuId2 = (Long) item.get("MENU_ID");
                    String menuName2 = (String) item.get("MENU_NM");
                    // Long eventKey2 = (Long) map.get("MENU_OP_ID");
                    String eventKey2 = (String) map.get("MENU_OP_WX_ID");
                    String url2 = (String) item.get("MENU_OP_COM");
                    String menuType2 = (String) item.get("MENU_OP_TYPE");

                    if ("click".equals(menuType2)) {
                        ClickButton btn = new ClickButton();
                        btn.setType(menuType2);
                        btn.setName(menuName2);
                        btn.setKey(eventKey2 + "");

                        // 消息类型 1-图文 2-图片 3-文本
                        Integer menuType3 = (Integer) item.get("MENU_MSG_TYPE");
                        // 微信端media id:图文或图频的id
                        String eventKey3 = (String) item.get("MENU_OP_WX_ID");
                        if (menuType3 == 1) {
                            btn.setKey(eventKey3);
                        } else if (menuType3 == 2) {
                            btn.setKey(eventKey3);
                        } else if (menuType3 == 3) {
                            // 20191118-支持文本内容发送
                            btn.setKey("3##" + url2);
                        }

                        subBtns.add(btn);
                    } else if ("scancode_push".equals(menuType2)) {
                        ClickButton btn = new ClickButton();
                        btn.setType(menuType2);
                        btn.setName(menuName2);
                        btn.setKey(eventKey2 + "");

                        subBtns.add(btn);
                    } else if ("scancode_waitmsg".equals(menuType2)) {
                        ClickButton btn = new ClickButton();
                        btn.setType(menuType2);
                        btn.setName(menuName2);
                        btn.setKey(eventKey2 + "");

                        subBtns.add(btn);
                    } else if ("view".equals(menuType2)) {
                        ViewButton btn = new ViewButton();
                        btn.setType(menuType2);
                        btn.setName(menuName2);
                        if (whetherMakeOauth2Url(appInfo, url2)) {
                            com.basoft.core.ware.wechat.domain.AppInfo ai = new com.basoft.core.ware.wechat.domain.AppInfo();
                            try {
                                BeanUtils.copyProperties(ai, appInfo);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            btn.setUrl(Oauth2Utils.getMenuIinkUrlNew(ai, menuId2));
                        } else {
                            btn.setUrl(url2);
                        }
                        subBtns.add(btn);
                    }
                }
                cbtn.setSub_button((Button[]) subBtns.toArray(new Button[subBtns.size()]));
                buttonList.add(cbtn);
            }
        }

        Menu menu = new Menu();
        menu.setButton((Button[]) buttonList.toArray(new Button[buttonList.size()]));

        String token = wechatService.getAccessToken(appInfo);
        WeixinMenuUtils.createMenu(token, menu);
        return new Long(buttonList.size());
    }

    private boolean whetherMakeOauth2Url(AppInfo appInfo, String url) {
        if (StringUtils.isNotBlank(url) && (url.startsWith("/") || url.startsWith(appInfo.getUrl()) && !url.endsWith(".html"))) {
            return true;
        }
        return false;
    }
}
