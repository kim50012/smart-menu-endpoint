package com.basoft.api.utils.ueditor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.basoft.api.utils.aws.AwsFileUtil;
import com.basoft.api.utils.tencent.TencentFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.ConfigManager;
import com.baidu.ueditor.define.ActionMap;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.hunter.FileManager;
import com.baidu.ueditor.hunter.ImageHunter;
import com.basoft.api.utils.upload.UploadMultipartFileUtil;

/**
 * @author basoft
 */
@Slf4j
public class CustomActionEnter extends ActionEnter {
    private MultipartFile file;

    private UploadMultipartFileUtil uploadMultipartFileUtil;

    private AwsFileUtil awsFileUtil;

    private TencentFileUtil tencentFileUtil;

    private Map<String, String> uploadParam;

    private String serverDomain;

    private String serverContextPath;

    private String target;

    // 20190725AWS云存储停用
    /*public CustomActionEnter(HttpServletRequest request, String rootPath, MultipartFile file,
                             UploadMultipartFileUtil uploadMultipartFileUtil,
                             AwsFileUtil awsFileUtil, Map<String, String> uploadParam,
                             String serverContextPath, String serverDomain, String target) {*/

    public CustomActionEnter(HttpServletRequest request, String rootPath, MultipartFile file,
                UploadMultipartFileUtil uploadMultipartFileUtil,
                TencentFileUtil tencentFileUtil, Map<String, String> uploadParam,
                String serverContextPath, String serverDomain, String target) {
        super(request, rootPath);
        actionType = request.getParameter("action");
        contextPath = request.getContextPath();
        configManager = ConfigManager.getInstance(rootPath, contextPath, request.getRequestURI());
        this.file = file;
        this.uploadMultipartFileUtil = uploadMultipartFileUtil;
        this.tencentFileUtil = tencentFileUtil;
        this.uploadParam = uploadParam;
        this.serverDomain = serverDomain;
        this.serverContextPath = serverContextPath;
        this.target = target;
    }

    public String exec() {
        String callbackName = request.getParameter("callback");
        if (callbackName != null) {
            if (!validCallbackName(callbackName)) {
                return new BaseState(false, AppInfo.ILLEGAL).toJSONString();
            }

            String invokeResult = "";
            try {
                invokeResult = this.invoke();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return callbackName + "(" + invokeResult + ");";
        } else {
            String invokeResult = "";
            try {
                invokeResult = this.invoke();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return invokeResult;
        }
    }

    public String invoke() throws JSONException {
        // 核查操作类型
        if (actionType == null || !ActionMap.mapping.containsKey(actionType)) {
            return new BaseState(false, AppInfo.INVALID_ACTION).toJSONString();
        }
        // 核查CONFIG配置
        if (this.configManager == null || !this.configManager.valid()) {
            return new BaseState(false, AppInfo.CONFIG_ERROR).toJSONString();
        }

        // 获取操作码
        int actionCode = ActionMap.getType(this.actionType);
        log.info("当前操作码为actionCode：：" + actionCode);

        Map<String, Object> conf = null;
        State state = null;
        switch (actionCode) {
            case ActionMap.CONFIG:
                return this.configManager.getAllConfig().toString();
            case ActionMap.UPLOAD_IMAGE:
            case ActionMap.UPLOAD_SCRAWL:
            case ActionMap.UPLOAD_VIDEO:
            case ActionMap.UPLOAD_FILE:
                conf = this.configManager.getConfig(actionCode);
                log.info("上传目的地为target：：：" + target);
                // UEditor上传至云存储
                if ("oud".equals(target)) {
                    // 20190725AWS云存储停用
                    // state = new CustomAwsUploader(request, conf, file, awsFileUtil).doExec();

                    // 20190725上传至腾讯云
                    state = new CustomTencentUploader(request, conf, file, tencentFileUtil).doExec();
                }
                // 存储在服务器硬盘
                else {
                    state = new CustomUploader(request, conf, file, uploadMultipartFileUtil,
                            uploadParam, serverContextPath, serverDomain).doExec();
                }
                break;
            case ActionMap.CATCH_IMAGE:
                conf = configManager.getConfig(actionCode);
                String[] list = this.request.getParameterValues((String) conf.get("fieldName"));
                state = new ImageHunter(conf).capture(list);
                break;
            case ActionMap.LIST_IMAGE:
            case ActionMap.LIST_FILE:
                conf = configManager.getConfig(actionCode);
                int start = this.getStartIndex();
                state = new FileManager(conf).listFile(start);
                break;
        }
        return state.toJSONString();
    }

    public int getStartIndex() {
        String start = this.request.getParameter("start");
        try {
            return Integer.parseInt(start);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * callback参数验证
     */
    public boolean validCallbackName(String name) {
        if (name.matches("^[a-zA-Z_]+[\\w0-9_]*$")) {
            return true;
        }
        return false;
    }
}