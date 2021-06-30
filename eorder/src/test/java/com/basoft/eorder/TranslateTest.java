package com.basoft.eorder;

import net.sf.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.basoft.eorder.util.TransApi;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:40 2019/5/28
 **/
public class TranslateTest {

    private static final String APP_ID = "20190528000302547";
    private static final String SECURITY_KEY = "TOs10NCC8DLoshay3qnG";

    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private static final String LAN_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/language";

//    @Test
    public  void translateTest() {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好";

        String lan = getEnv(query);
        String jsons = api.getTransResult(TRANS_API_HOST,query, "zh", lan);

        JSONObject jsonObject = new JSONObject(jsons);

        JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("trans_result"));

        for(int i=0;i<jsonArray.size(); i++){
            net.sf.json.JSONObject jo = jsonArray.getJSONObject(i);
            System.out.println(jo.getString("dst"));
        }

    }


    public static String getEnv(String query) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String jsons = api.getTransResult(LAN_API_HOST,query, "zh", "kor");

        JSONObject jsonObject = new JSONObject(jsons);
        String src =  jsonObject.get("data").toString();
        return new JSONObject(src).get("src").toString().equals("zh") ? "kor" : "zh";
    }


}
