package com.basoft.api.utils.aws;

import com.basoft.api.utils.aws.core.domain.FileReference;
import com.basoft.api.utils.aws.core.service.interfaze.FileService;
import com.basoft.api.utils.aws.core.domain.UploadFile;
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
public class AwsFileUtil {
    @Autowired
    private FileService fileService;

    AwsFileUtil() {
    }

    public AwsFileUtil(FileService fileService) {
        this.fileService = fileService;
    }

    public Map<String, String> uploadFile(HttpServletRequest request, MultipartFile mf) {
        try {
            //String rType = request.getContentType();
            String type = mf.getContentType();
            String originalName = mf.getOriginalFilename();
            String name = request.getParameter("file");

            if (StringUtils.isEmpty(name)) {
                name = mf.getOriginalFilename();
                log.info("MultipartFile Name::" + name);
            }

            byte[] contents = mf.getBytes();
            UploadFile uf = UploadFile.newBuild().name(name).originalName(originalName).type(type).payload(contents).build();
            FileReference df = fileService.uploadFile(uf);

            Map<String, String> sMap = new HashMap<>();
            sMap.put("id", df.getId());
            sMap.put("fullPath", df.getVisitFullPath());
            sMap.put("fileUri", df.fullName());
            return sMap;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("FileController [UploadFile] IOException Info::" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}