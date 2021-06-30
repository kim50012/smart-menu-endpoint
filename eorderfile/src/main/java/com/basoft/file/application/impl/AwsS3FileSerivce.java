package com.basoft.file.application.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.basoft.file.application.FileFetcher;
import com.basoft.file.application.FileService;
import com.basoft.file.application.FileTransfer;
import com.basoft.file.application.UploadFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author woonill
 * @since 2019.1.06
 * <p>
 * 为 AwsClient s3 上传处理，本设计为不能获取数据，只是传送的时候调用API
 * 如果获取的时候也调用将会产生费用，所以传送之后获取最终的aws s3的访问路径，保存
 */
@Slf4j
public final class AwsS3FileSerivce extends FileTransferAdapter implements FileTransfer, FileFetcher {
    public static final String CONFIG_PROP = "aws_s3_config";
    private String bucketName;
    private String rootKey;
    private AmazonS3 s3Client;
    private String httpS3RootPath;
    private String visitFullPath;

    private AwsS3FileSerivce() {
    }

    public AwsS3FileSerivce(String bucketName, AmazonS3 client, String rootKey, String s3RootPath) {
        this(bucketName, client, rootKey, s3RootPath, s3RootPath);
    }

    private AwsS3FileSerivce(String bucketName, AmazonS3 s3Client, String rootKey, String s3RootPath, String lastVisitUrl) {
        this.bucketName = bucketName;
        this.s3Client = s3Client;
        this.rootKey = rootKey;
        this.httpS3RootPath = s3RootPath;
        this.visitFullPath = lastVisitUrl;
    }

    public static final String buildFilePath(String rootPath, String name) {
        if ("/".startsWith(rootPath)) {
            return name;
        }
        return rootPath + "/" + name;
    }

    public static FileTransfer of(Map<String, Object> aws_s3_config) {
        return new AwsS3FileServiceFactory().of(aws_s3_config);
    }

    @Override
    public FileService.FileRef uploadFile(UploadFile file) {
        try {
            String fileId = UUID.randomUUID().toString();
            String fullName = fullName(fileId, file.originalName());
            String key = buildFilePath(this.rootKey, fullName);

            // System.out.println("Result key:"+key);
            String writeToUrl = new StringBuilder(this.httpS3RootPath).append(key).toString();
            String visitFullPath = new StringBuilder(this.visitFullPath).append("/").append(key).toString();

            final ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.type());
            objectMetadata.setContentLength(file.contents().length);
            objectMetadata.setContentEncoding("utf-8");

            final PutObjectResult result = s3Client.putObject(bucketName, key, new ByteArrayInputStream(file.contents()), objectMetadata);
            // ObjectMapper om = new ObjectMapper();
            // System.out.println(om.writeValueAsString(result));
            return new FileService.FileRef(fileId, writeToUrl, fullName, visitFullPath, file.props(), file);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            throw new IllegalStateException(e);
        }
    }

    @Override
    public byte[] fetch(String fileFullPath) {
        log.info("AwsS3FileSerivce[fetch]根据AWS File Key Get Object from AWS Storage!!!");
        // this.s3Client.listObjects();
        String fileKey = buildFilePath(this.rootKey, fileFullPath);
        S3Object object = this.s3Client.getObject(new GetObjectRequest(bucketName, fileKey));

        if (object == null) {
            throw new IllegalArgumentException("not found file on:" + fileFullPath);
        }

        InputStream input = object.getObjectContent();
        try {
            log.info("AwsS3FileSerivce[fetch]根据AWS File Key Get Object from AWS Storage FINISH!!!");
            return org.apache.commons.io.IOUtils.toByteArray(input);
        } catch (IOException e) {
            throw new IllegalStateException("io error");
        }
    }

    @Override
    public List<FileService.FileRef> batchUpload(List<UploadFile> collect) {
        /* this.s3Clien
        ObjectMetadataProvider metadataProvider = new ObjectMetadataProvider() {
            public void provideObjectMetadata(File file, ObjectMetadata metadata) {
                if (isJPEG(file)) {
                    metadata.addUserMetadata("original-image-date",parseExifImageDate(file));
                }
            }
        };
        MultipleFileUpload upload = tm.uploadFileList(this.bucketName, myKeyPrefix, rootDirectory, fileList, metadataProvider);*/
        // return null;
        // throw new UnsupportedOperationException("not support batchUpload of s3");

        List<FileService.FileRef> refList = new ArrayList<>(collect.size());
        for (UploadFile file : collect) {
            try {
                final FileService.FileRef fileRef = this.uploadFile(file);
                refList.add(fileRef);
            } catch (Throwable te) {
                te.printStackTrace();
            }
        }
        return refList;
    }


    public AmazonS3 getS3Client() {
        return s3Client;
    }

    public static final class Builder {
        private String bucketName;
        private String clientRegion;
        private String accessKey;
        private String secretAccessKey;
        private String rootKey;
        private String visitPath;

//      String clientRegion = "ap-northeast-2";
//      String bucketName = "basoft-test-images";
//      final String accessKey = "AKIAIC3O6DK3OOMTAVOQ";
//      final String secretAccessKey = "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";

        public Builder bucketName(String name) {
            this.bucketName = name;
            return this;
        }

        public Builder clientRegion(String reg) {
            this.clientRegion = reg;
            return this;
        }

        public Builder accessKey(String akey) {
            this.accessKey = akey;
            return this;
        }

        public Builder secretAccessKey(String secKey) {
            this.secretAccessKey = secKey;
            return this;
        }

        public Builder rootKey(String rootKey) {
            this.rootKey = rootKey;
            return this;
        }

        public Builder visitLastPath(String visitUrl) {
            this.visitPath = visitUrl;
            return this;
        }

        public AwsS3FileSerivce build() {
            String s3RootPath = new StringBuilder("https://s3.")
                    .append(this.clientRegion)
                    .append(".")
                    .append("amazonaws.com/")
                    .append(this.bucketName).append("/")
                    .toString();
            System.out.println("S3 Full Path:" + s3RootPath);
            String lastVisitUrl = StringUtils.isEmpty(this.visitPath) ? s3RootPath : this.visitPath;
            AmazonS3 cilent = this.initS3Client();
            return new AwsS3FileSerivce(this.bucketName, cilent, this.rootKey, s3RootPath, lastVisitUrl);
        }

        private AmazonS3 initS3Client() {
            try {
                ClientConfiguration clientConfig = new ClientConfiguration();
                clientConfig.setConnectionTimeout(5000);
                // clientConfig.setRequestTimeout(1000);
                clientConfig.setSocketTimeout(5000);

                return AmazonS3ClientBuilder.standard()
                        .withRegion(clientRegion)
                        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretAccessKey)))
                        .withClientConfiguration(clientConfig)
                        .build();
            } catch (AmazonServiceException e) {
                // The call was transmitted successfully, but Amazon S3 couldn't process
                // it, so it returned an error response.
                e.printStackTrace();
                throw new IllegalStateException(e);
            } catch (SdkClientException e) {
                // Amazon S3 couldn't be contacted for a response, or the client
                // couldn't parse the response from Amazon S3.
                e.printStackTrace();
                throw new IllegalStateException(e);
            }
        }
    }

    public static final class AwsS3FileServiceFactory implements FileTransfer.FileTransferFactory {
        public static final String BUCKET_NAME_PROP = "bucket";
        public static final String CLIENT_REGION_PROP = "client_region";
        public static final String S3_ACCESS_KEY_PROP = "appKey";
        public static final String S3_SECRET_ACCESS_KEY_PROP = "secretAccessKey";
        public static final String S3_ROOT_FOLDER_PATH_PROP = "rootKey";

        @Override
        public FileTransfer of(Map<String, Object> envMap) {
            String bucketName = (String) envMap.get(BUCKET_NAME_PROP);
            String clientRegion = (String) envMap.get(CLIENT_REGION_PROP);
            String accKey = (String) envMap.get(S3_ACCESS_KEY_PROP);
            String secKey = (String) envMap.get(S3_SECRET_ACCESS_KEY_PROP);
            String rootFolderPath = (String) envMap.get(S3_ROOT_FOLDER_PATH_PROP);
            String visitUrl = (String) envMap.get("visitAwsFullPath");

            try {
                System.out.println(new ObjectMapper().writeValueAsString(envMap));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return new Builder()
                    .bucketName(bucketName)
                    .clientRegion(clientRegion)
                    .accessKey(accKey)
                    .secretAccessKey(secKey)
                    .rootKey(rootFolderPath)
                    .visitLastPath(visitUrl)
                    .build();
        }
    }

    void print() {
        final List<Bucket> buckets = s3Client.listBuckets();
        buckets.stream().forEach((bucket) -> System.out.println("Bucket name:" + bucket.getName()));
        ObjectListing objects = s3Client.listObjects(bucketName);
        do {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                System.out.println(objectSummary.getETag() + "  " + objectSummary.getStorageClass() + " " + objectSummary.getOwner());
                System.out.println("Object: " + objectSummary.getKey());
            }
            objects = s3Client.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());
    }
}
