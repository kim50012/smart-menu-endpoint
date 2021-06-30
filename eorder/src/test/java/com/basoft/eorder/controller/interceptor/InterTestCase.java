package com.basoft.eorder.controller.interceptor;

import com.basoft.eorder.interfaces.controller.interceptor.WechatInterceptor;
import junit.framework.TestCase;
import org.junit.Test;

public class InterTestCase {


    @Test
    public void testWechatMached(){


        TestCase.assertTrue(WechatInterceptor.matched("/eorder/wechat/api/v1/main"));
        TestCase.assertTrue(WechatInterceptor.matched("/eorder/wechat/api/v1/query?query(sroder=sdf){id,name}"));
        TestCase.assertTrue(WechatInterceptor.matched("/eorder/wechat/api/v1/command/saveOrder"));
        TestCase.assertFalse(WechatInterceptor.matched("/eorder/wechat/api/v1/initBasoft/123123"));


        TestCase.assertTrue(WechatInterceptor.matched("/eorder/wechat/api/v1/main?storeId=535049498704811014"));
    }
}
