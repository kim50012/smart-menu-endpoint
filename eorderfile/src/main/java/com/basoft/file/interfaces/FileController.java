package com.basoft.file.interfaces;

import com.basoft.file.application.FileData;
import com.basoft.file.application.FileService;
import com.basoft.file.application.UploadFile;
import com.basoft.file.tencent.base.service.TencentFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/file/api/v1")
@ResponseBody
@Slf4j
public class FileController {
    private FileService fileService;

    private TencentFileService tencentFileService;

    FileController() {
    }

    @Autowired
    public FileController(FileService fileService, TencentFileService tencentFileService) {
        this.fileService = fileService;
        this.tencentFileService = tencentFileService;
    }

    /********************************************Tencent Cloud File Upload Start********************************************/
    @PostMapping("/upload")
    public Map<String, String> uploadFile2Tencent(HttpServletRequest request, @RequestParam("file") MultipartFile mf) {
        try {
            String type = mf.getContentType();
            String originalName = mf.getOriginalFilename();
            String name = request.getParameter("file");

            if (StringUtils.isEmpty(name)) {
                name = mf.getOriginalFilename();
                log.info("MultipartFile Name::" + name);
            }

            byte[] contents = mf.getBytes();
            UploadFile uf = UploadFile.newBuild().name(name).originalName(originalName).type(type).payload(contents).build();
            FileService.FileRef df = tencentFileService.uploadFile(uf);

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

    /**
     * ?????????????????????
     *
     * @param fid
     * @param req
     * @param res
     */
    @RequestMapping(value = "/coslist/{name}", method = RequestMethod.GET)
    public void fileFromTencentCOS(@PathVariable("name") String fid, HttpServletRequest req, HttpServletResponse res) {
        try {
            FileData fd = tencentFileService.getFile(fid);
            if (fd == null) {
                res.setStatus(404);
                res.getWriter().write("NOT FOUND FILE");
                return;
            }
            byte[] payload = fd.payload();
            res.setContentType(fd.type());
            res.getOutputStream().write(payload);
        } catch (IOException e) {
            log.error("FileController[ListFile] IOException Info::" + e.getMessage());
            log.error("FileController[ListFile] IOException Info::\n", e);
            try {
                res.sendError(500);
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }
    }
    /********************************************Tencent Cloud File Upload End********************************************/


    /********************************************AWS Cloud File Upload Start********************************************/
    // 20190730??????AWS
    // @PostMapping("/upload")
    public Map<String, String> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile mf) {
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
            FileService.FileRef df = fileService.uploadFile(uf);

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

    // 20190730??????AWS
    // @RequestMapping(value = "/list/{name}", method = RequestMethod.GET)
    public void files(@PathVariable("name") String fid, HttpServletRequest req, HttpServletResponse res) {
        try {
            //String fileId = req.getParameter("fid");
            FileData fd = fileService.getFile(fid);
            if (fd == null) {
                res.setStatus(404);
                res.getWriter().write("not found image");
                return;
            }
            byte[] payload = fd.payload();
            res.setContentType(fd.type());
            res.getOutputStream().write(payload);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("FileController [ListFile] IOException Info::" + e.getMessage());
            try {
                res.sendError(500);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    /********************************************AWS Cloud File Upload End********************************************/


    /***********************************************Wechat QRCode Start**********************************************/
    /**
     * Wechat QRCode Main API Method
     *
     * @param req
     * @param res
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/wechatQrCodeImage", method = RequestMethod.POST)
    @ResponseBody
    public List<QRImageUrl> wechatQrCodeImage(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String host = req.getHeader("host");
        log.info("FileController[wechatQrCodeImage]host value is {}", host);

        ObjectMapper om = new ObjectMapper();
        WechatFileTransFile wFile = om.readValue(req.getInputStream(), WechatFileTransFile.class);
        log.info("FileController[wechatQrCodeImage]wFile value is::::" + wFile);

        List<QRImageUrl> collect = wFile.imageUrlList;

        // 20190730??????aws fileService
        // final List<FileData> fileDataList = fileService.fileListOfGroup(wFile.groupId);
        final List<FileData> fileDataList = tencentFileService.fileListOfGroup(wFile.groupId);
        if(fileDataList != null){
            log.info("FileController[wechatQrCodeImage]fileDataList size is::::" + fileDataList.size());
        }
        if (!fileDataList.isEmpty()) {
            Map<String, FileData> dataMap = new HashMap<>();
            fileDataList.stream().forEach(new Consumer<FileData>() {
                @Override
                public void accept(FileData fileData) {
                    if (StringUtils.isNotBlank(fileData.getKeyUrl())) {
                        dataMap.put(fileData.getKeyUrl().trim(), fileData);
                    }
                }
            });

            /*collect = collect.stream()
                    .filter((f) -> {
                        return  dataMap.get(f.wechatImageUrl) == null;
                    }).collect(Collectors.toList());*/
            collect.forEach(new Consumer<QRImageUrl>() {
                @Override
                public void accept(QRImageUrl qrImageUrl) {
                    final FileData fileData = dataMap.get(qrImageUrl.wechatImageUrl.trim());
                    if (fileData != null) {
                        qrImageUrl.fileId = fileData.getId();
                        qrImageUrl.wechatImageUrl = buildLocalVisitPath(host, fileData.getId());
                    }
                }
            });
        }

        log.info("Last wechatQRCode Image :" + collect.size());
        //????????????????????????????????????????????????????????????wechat?????????????????????????????????????????? db???
        // System.out.println("start fetch image from wechat server");
        List<UploadFile> uploadFiles = new ArrayList<>(collect.size());

        List<QRImageUrl> wechatQRCollect = collect.stream().filter((cc) -> cc.fileId == null).collect(Collectors.toList());
        if (!wechatQRCollect.isEmpty()) {
            for (QRImageUrl url : wechatQRCollect) {
                System.out.println("fetch image:" + url.wechatImageUrl);
                byte[] contents = imagePayload(url.wechatImageUrl);
                if (contents.length > 0) {
                    UploadFile uf = UploadFile.newBuild().name("wechat-image")
                            .originalName("wechat-image.png")
                            .type("image/png")
                            .fullUrl(url.wechatImageUrl)
                            .payload(contents)
                            .groupId(wFile.groupId)
                            .prop("client_id", url.id).build();
                    uploadFiles.add(uf);
                }
            }

            if (!uploadFiles.isEmpty()) {
                // 20190730??????aws fileService
                // final List<FileService.FileRef> fileRefs = fileService.batchUpload(uploadFiles);
                final List<FileService.FileRef> fileRefs = tencentFileService.batchUpload(uploadFiles);
                collect.forEach(new Consumer<QRImageUrl>() {
                    @Override
                    public void accept(QRImageUrl qrImageUrl) {
                        for (FileService.FileRef ref : fileRefs) {
                            String clientId = (String) ref.prop("client_id");
                            if (clientId.equalsIgnoreCase(qrImageUrl.id)) {
                                qrImageUrl.fileId = ref.getId();
                                qrImageUrl.wechatImageUrl = buildLocalVisitPath(host, ref.getId());
                                break;
                            }
                        }
                    }
                });
            }
        }
        return collect;
    }

    /**
     * Wechat QRCode Related Static Class
     */
    public static class WechatFileTransFile {
        private String groupId;

        private List<QRImageUrl> imageUrlList;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public List<QRImageUrl> getImageUrlList() {
            return imageUrlList;
        }

        public void setImageUrlList(List<QRImageUrl> imageUrlList) {
            this.imageUrlList = imageUrlList;
        }

        @Override
        public String toString() {
            return "WechatFileTransFile{" +
                    "groupId='" + groupId + '\'' +
                    ", imageUrlList=" + imageUrlList +
                    '}';
        }
    }

    // CloseableHttpClient
    CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * Wechat QRCode Related Method
     */
    private byte[] imagePayload(String fullPath) {
        HttpGet httpPost = new HttpGet(fullPath);
        try {
            final CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            final HttpEntity entity = httpResponse.getEntity();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            entity.writeTo(baos);
            System.out.println(baos.size());
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String buildLocalVisitPath(String host, String id) {
        // 20190730??????aws
        // return new StringBuilder("http://").append(host).append("/file/api/v1/list/").append(id).toString();
        return new StringBuilder("http://").append(host).append("/file/api/v1/coslist/").append(id).toString();
    }

    static final class QRImageUrl {
        private String fileId;
        private String id;
        private String wechatImageUrl;

        QRImageUrl() {
        }

        public QRImageUrl(String clientId, String visitPath) {
            this.id = clientId;
            this.wechatImageUrl = visitPath;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWechatImageUrl() {
            return wechatImageUrl;
        }

        public void setWechatImageUrl(String wechatImageUrl) {
            this.wechatImageUrl = wechatImageUrl;
        }
    }

    /****************************************??????????????????-20190925-START*********************************************/
    /**
     * Agent Wechat QRCode Main API Method
     * ?????????????????????????????????????????????API?????????????????????????????????????????????????????????????????????wFile.imageUrlList???????????????????????????????????????
     * ?????????????????????
     * 1??????????????????ID?????????????????????????????????????????????????????????file_mst??????????????????
     * ?????????????????????????????????????????????
     * 2????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????file_mst???
     *
     * @param req
     * @param res
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/agentWechatQrCodeImage", method = RequestMethod.POST)
    @ResponseBody
    public List<QRImageUrl> agentWechatQrCodeImage(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String host = req.getHeader("host");
        log.info("FileController[AgentWechatQrCodeImage]host value is {}", host);

        ObjectMapper om = new ObjectMapper();
        WechatFileTransFile wFile = om.readValue(req.getInputStream(), WechatFileTransFile.class);
        log.info("FileController[AgentWechatQrCodeImage]wFile value is::::" + wFile);

        // collect?????????1
        List<QRImageUrl> collect = wFile.imageUrlList;

        // ???????????????????????????file??????
        FileData fileData = tencentFileService.fileOfAgent(wFile.groupId);
        log.info("FileController[AgentWechatQrCodeImage]fileData is::::" + fileData);

        // ???????????????????????????????????????????????????????????????????????????URL????????????
        if (fileData != null) {
            collect.forEach(new Consumer<QRImageUrl>() {
                @Override
                public void accept(QRImageUrl qrImageUrl) {
                    qrImageUrl.fileId = fileData.getId();
                    qrImageUrl.wechatImageUrl = buildLocalVisitPath(host, fileData.getId());
                }
            });
            return collect;
        }
        // ????????????????????????????????????????????????????????????????????????????????????????????????
        else {
            List<UploadFile> uploadFiles = new ArrayList<>(collect.size());
            // collect?????????1
            for (QRImageUrl url : collect) {
                // ???????????????????????????
                byte[] contents = imagePayload(url.wechatImageUrl);
                if (contents.length > 0) {
                    UploadFile uf = UploadFile.newBuild().name("agent-wechatQRCode-image")
                            .originalName("agent-wechatQRCode-image.png")
                            .type("image/png")
                            .fullUrl(url.wechatImageUrl)
                            .payload(contents)
                            // ?????????ID
                            .groupId(wFile.groupId)
                            .prop("client_id", url.id).build();
                    log.info("FileController[AgentWechatQrCodeImage]UploadFile is::::" + uf);
                    uploadFiles.add(uf);
                }
            }

            if (!uploadFiles.isEmpty()) {
                final List<FileService.FileRef> fileRefs = tencentFileService.batchUpload(uploadFiles);
                collect.forEach(new Consumer<QRImageUrl>() {
                    @Override
                    public void accept(QRImageUrl qrImageUrl) {
                        for (FileService.FileRef ref : fileRefs) {
                            // ?????????ID??????????????????prop("client_id", url.id).build();
                            String clientId = (String) ref.prop("client_id");
                            if (clientId.equalsIgnoreCase(qrImageUrl.id)) {
                                // ?????????????????????ID,???file uuid(??????TencentTransfer??????uploadFile?????????String fileUUID = UUID.randomUUID().toString();)
                                qrImageUrl.fileId = ref.getId();
                                qrImageUrl.wechatImageUrl = buildLocalVisitPath(host, ref.getId());
                                break;
                            }
                        }
                    }
                });
            }
            return collect;
        }
    }
    /***********************************************Wechat QRCode End**********************************************/
}
