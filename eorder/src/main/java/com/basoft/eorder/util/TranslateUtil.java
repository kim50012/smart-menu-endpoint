package com.basoft.eorder.util;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午6:34 2019/5/28
 **/
@Slf4j
public class TranslateUtil {
    private static final String APP_ID = "20190528000302547";

    private static final String SECURITY_KEY = "TOs10NCC8DLoshay3qnG";

    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private static final String LAN_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/language";

    /**
     * 翻译
     * @param query
     * @return
     */
    public static String translateTest(String query) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String lan = getEnv(query);
        String jsons = api.getTransResult(TRANS_API_HOST, query, "auto", lan);
        JSONObject jsonObject = new JSONObject(jsons);
        JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("trans_result"));
        net.sf.json.JSONObject jo = jsonArray.getJSONObject(0);
        return jo.getString("dst");
    }

    /**
     * 获取语种
     * @param query
     * @return
     */
    public static String getEnv(String query) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String jsons = api.getTransResult(LAN_API_HOST, query, "zh", "kor");
        log.info("TranslateUtil[getEnv]>>>" + jsons);
        JSONObject jsonObject = new JSONObject(jsons);
        String src = jsonObject.get("data").toString();
        return new JSONObject(src).get("src").toString().equals("zh") ? "kor" : "zh";
    }
}
