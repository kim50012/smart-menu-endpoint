package com.baidu.ueditor;

import com.baidu.ueditor.define.ActionMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理器
 *
 * @author hancong03@baidu.com
 */
public final class ConfigManager {

    private final String rootPath;
    private final String originalPath;
    private final String contextPath;
    private static final String configFileName = "config.json";
    private String parentPath = null;
    private JSONObject jsonConfig = null;
    // 涂鸦上传filename定义
    private final static String SCRAWL_FILE_NAME = "scrawl";
    // 远程图片抓取filename定义
    private final static String REMOTE_FILE_NAME = "remote";

    /*
     * 通过一个给定的路径构建一个配置管理器， 该管理器要求地址路径所在目录下必须存在config.properties文件
     */
    private ConfigManager(String rootPath, String contextPath, String uri) throws FileNotFoundException, IOException {
        //20190603注释掉：重构config.json的读取方式
        //rootPath = rootPath.replace( "\\", "/" );

        this.rootPath = rootPath;
        this.contextPath = contextPath;
		
		/*if ( contextPath.length() > 0 ) {
			this.originalPath = this.rootPath + uri.substring( contextPath.length() );
		} else {
			this.originalPath = this.rootPath + uri;
		}*/

        //20190603注释掉：重构config.json的读取方式
        //this.originalPath = "src/main/resources/ueditor/config.json";
        this.originalPath = rootPath;

        //20190603注释掉：重构config.json的读取方式
        //this.initEnv();//baidu ueditor原生的initEnv方法
        this.initEnvSpringboot();
    }

     /**
     * 配置管理器构造工厂
     *
     * @param rootPath    服务器根路径
     * @param contextPath 服务器所在项目路径
     * @param uri         当前访问的uri
     * @return 配置管理器实例或者null
     */
    public static ConfigManager getInstance(String rootPath, String contextPath, String uri) {
        try {
            return new ConfigManager(rootPath, contextPath, uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 验证配置文件加载是否正确
    public boolean valid() {
        return this.jsonConfig != null;
    }

    public JSONObject getAllConfig() {
        return this.jsonConfig;
    }

    public Map<String, Object> getConfig(int type) throws JSONException {
        Map<String, Object> conf = new HashMap<String, Object>();
        String savePath = null;

        switch (type) {
            case ActionMap.UPLOAD_FILE:
                conf.put("isBase64", "false");
                conf.put("maxSize", this.jsonConfig.getLong("fileMaxSize"));
                conf.put("allowFiles", this.getArray("fileAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getString("fileFieldName"));
                savePath = this.jsonConfig.getString("filePathFormat");
                break;

            case ActionMap.UPLOAD_IMAGE:
                conf.put("isBase64", "false");
                conf.put("maxSize", this.jsonConfig.getLong("imageMaxSize"));
                conf.put("allowFiles", this.getArray("imageAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getString("imageFieldName"));
                savePath = this.jsonConfig.getString("imagePathFormat");
                break;

            case ActionMap.UPLOAD_VIDEO:
                conf.put("maxSize", this.jsonConfig.getLong("videoMaxSize"));
                conf.put("allowFiles", this.getArray("videoAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getString("videoFieldName"));
                savePath = this.jsonConfig.getString("videoPathFormat");
                break;

            case ActionMap.UPLOAD_SCRAWL:
                conf.put("filename", ConfigManager.SCRAWL_FILE_NAME);
                conf.put("maxSize", this.jsonConfig.getLong("scrawlMaxSize"));
                conf.put("fieldName", this.jsonConfig.getString("scrawlFieldName"));
                conf.put("isBase64", "true");
                savePath = this.jsonConfig.getString("scrawlPathFormat");
                break;

            case ActionMap.CATCH_IMAGE:
                conf.put("filename", ConfigManager.REMOTE_FILE_NAME);
                conf.put("filter", this.getArray("catcherLocalDomain"));
                conf.put("maxSize", this.jsonConfig.getLong("catcherMaxSize"));
                conf.put("allowFiles", this.getArray("catcherAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getString("catcherFieldName") + "[]");
                savePath = this.jsonConfig.getString("catcherPathFormat");
                break;

            case ActionMap.LIST_IMAGE:
                conf.put("allowFiles", this.getArray("imageManagerAllowFiles"));
                conf.put("dir", this.jsonConfig.getString("imageManagerListPath"));
                conf.put("count", this.jsonConfig.getInt("imageManagerListSize"));
                break;

            case ActionMap.LIST_FILE:
                conf.put("allowFiles", this.getArray("fileManagerAllowFiles"));
                conf.put("dir", this.jsonConfig.getString("fileManagerListPath"));
                conf.put("count", this.jsonConfig.getInt("fileManagerListSize"));
                break;

        }

        conf.put("savePath", savePath);
        conf.put("rootPath", this.rootPath);

        return conf;
    }

    private void initEnvSpringboot() throws IOException {
        // 读取config.json
        String configContent = this.readFileSpringboot(this.originalPath);
        System.out.println("BaiDu UEditor初始化配置文件字符数为：：" + configContent.length());
        try {
            JSONObject jsonConfig = new JSONObject(configContent);
            this.jsonConfig = jsonConfig;
        } catch (Exception e) {
            e.printStackTrace();
            this.jsonConfig = null;
        }
    }

    private String readFileSpringboot(String springBootResourcesPath) throws IOException {
        System.out.println("开始读取初始化配置::STARTING READ INIT CONFIG......");
        StringBuilder builder = new StringBuilder();

        // 获取config.json的文件输入流
        //InputStream inputStream = ConfigManager.class.getResourceAsStream(springBootResourcesPath);
        InputStream inputStream = this.getClass().getResourceAsStream(springBootResourcesPath);
        System.out.println("获取config.json的文件输入流::" + inputStream);
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            // 创建config.json文件输入流的reader
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

            // 包装为BufferedReader
            bufferedReader = new BufferedReader(inputStreamReader);
            String tmpContent = null;
            while ((tmpContent = bufferedReader.readLine()) != null) {
                builder.append(tmpContent);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            //关闭BufferedReader
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            //关闭InputStreamReader
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            //关闭文件输入流
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return this.filter(builder.toString());
    }

    private void initEnv() throws FileNotFoundException, IOException {
        File file = new File(this.originalPath);

        if (!file.isAbsolute()) {
            file = new File(file.getAbsolutePath());
        }

        this.parentPath = file.getParent();

        String configContent = this.readFile(this.getConfigPath());

        try {
            JSONObject jsonConfig = new JSONObject(configContent);
            this.jsonConfig = jsonConfig;
        } catch (Exception e) {
            this.jsonConfig = null;
        }
    }

    private String getConfigPath() {
        return this.parentPath + File.separator + ConfigManager.configFileName;
    }

    private String readFile(String path) throws IOException {
        StringBuilder builder = new StringBuilder();
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
            BufferedReader bfReader = new BufferedReader(reader);

            String tmpContent = null;

            while ((tmpContent = bfReader.readLine()) != null) {
                builder.append(tmpContent);
            }

            bfReader.close();
        } catch (UnsupportedEncodingException e) {
            // 忽略
            e.printStackTrace();
        }

        return this.filter(builder.toString());
    }

    private String[] getArray(String key) throws JSONException {
        JSONArray jsonArray = this.jsonConfig.getJSONArray(key);
        String[] result = new String[jsonArray.length()];

        for (int i = 0, len = jsonArray.length(); i < len; i++) {
            result[i] = jsonArray.getString(i);
        }

        return result;
    }

    // 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
    private String filter(String input) {
        return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
    }
}
