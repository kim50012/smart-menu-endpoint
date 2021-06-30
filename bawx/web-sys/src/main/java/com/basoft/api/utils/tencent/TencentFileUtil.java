package com.basoft.api.utils.tencent;

import com.basoft.api.utils.aws.core.domain.FileReference;
import com.basoft.api.utils.aws.core.domain.UploadFile;
import com.basoft.api.utils.tencent.service.TencentFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TencentFileUtil {
    @Autowired
    private TencentFileService tencentFileService;

    TencentFileUtil() {
    }

    public TencentFileUtil(TencentFileService tencentFileService) {
        this.tencentFileService = tencentFileService;
    }

    public Map<String, String> uploadFile(HttpServletRequest request, MultipartFile mf) {
        try {
            String type = mf.getContentType();
            String originalName = mf.getOriginalFilename();
            String name = request.getParameter("file");

            if (StringUtils.isEmpty(name)) {
                name = mf.getOriginalFilename();
                log.info("BAWE MultipartFile Name::" + name);
            }

            byte[] contents = mf.getBytes();
            UploadFile uploadFile = UploadFile.newBuild().name(name).originalName(originalName).type(type).payload(contents).build();
            log.debug("BAWE MultipartFile Detail Info::" + uploadFile);
            FileReference df = tencentFileService.uploadFile(uploadFile);

            Map<String, String> sMap = new HashMap<>();
            sMap.put("id", df.getId());
            sMap.put("fullPath", df.getVisitFullPath());
            sMap.put("fileUri", df.fullName());
            return sMap;
        } catch (IOException e) {
            // e.printStackTrace();
            log.info("FileController [UploadFile] IOException Info::" + e.getMessage());
            log.info("FileController [UploadFile] IOException::", e);
            throw new RuntimeException(e);
        }
    }
}