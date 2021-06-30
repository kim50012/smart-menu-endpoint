package com.basoft.service.definition.wechat;

/**
 * @Author:DongXifu
 * @Description:常量类
 * @Date Created in 上午11:55 2018/4/3
 **/
public class BizConstants {
    public static final  Integer IS_DELETE_N = 0;//未删除

    public static final  Integer IS_DELETE_Y = 1;//已删除

    public static final  String IS_DELETE_START = "启用";//未删除

    public static final  String IS_DELETE_FORBID = "禁用";//未删除

    public  static final Integer SEND_TYPE_ID_ALL = 1;//所有人

    /********微信自动回复*****start*****/
    public  static final String MSG_GROUP_AUTOMATIC = "AUTO";//自动回复

    public  static final String KEY_WORD_FOLLOW = "FOCUS";//关注回复

    public  static final String KEYWORD_REPLY = "KEYWORD";//关键字回复

    public  static final String MSG_GROUP_SPAM = "SPAM";//群发
    
    public  static final int MSG_TYPE_NEWS = 1;//NEWS
    
    public  static final int MSG_TYPE_IMAGE = 2;//IMAGE
    
    public  static final int MSG_TYPE_TEXT = 3;//TEXT
    
    public  static final int MSG_TYPE_VIDEO = 4;//VIDEO
    
    public  static final int MSG_TYPE_VOICE = 5;//VOICE
    /********微信自动回复****end******/


    /**系统菜单***start****/

    public static final String MENU_LEVEL_ONE = "1";//菜单等级一级

    public static final String MENU_LEVEL_TWO = "2";//菜单等级二级

    public static final Byte MENU_STATE_ENABLE = 1;//菜单状态启用

    public static final Byte MENU_STATE_FORBID = 0;//菜单状态禁用
    /**系统菜单***end****/


    /****微信菜单******start******/
    public static final String MENUOPTYPE_VIEW = "view";//view

    public static final String MENUOPTYPE_CLICK = "click";//click
    /****微信菜单******end******/


    /*******shop_wx_news******start*******/
    public static final String SHOP_WX_NEWS_MSGNM = "head";
    /*******shop_wx_news******end*******/
}
