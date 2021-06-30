package com.basoft.api.utils.aws.core.service.impl.transfer;

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
import com.basoft.api.utils.aws.core.domain.FileReference;
import com.basoft.api.utils.aws.core.domain.UploadFile;
import com.basoft.api.utils.aws.core.service.impl.transfer.adapter.FileTransferAdapter;
import com.basoft.api.utils.aws.core.service.interfaze.FileTransfer;
import com.basoft.api.utils.aws.core.service.interfaze.base.FileFetcher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 为 AwsClient s3 上传处理，本设计为不能获取数据，只是传送的时候调用API
 * 如果获取的时候也调用将会产生费用，所以传送之后获取最终的aws s3的访问路径，保存
 */
@Component
public final class AwsS3FileSerivce extends FileTransferAdapter implements FileTransfer, FileFetcher {
    //region
    @Value("${aws.props.client_region}")
    private String clientRegion;
    // appkey
    @Value("${aws.props.appKey}")
    private String appKey;
    // 密码
    @Value("${aws.props.secretAccessKey}")
    private String secretAccessKey;
    // 文件桶名称
    @Value("${aws.props.bucketName}")
    private String bucketName;
    // root key
    @Value("${aws.props.rootKey}")
    private String rootKey;
    // 访问全路径
    @Value("${aws.props.visitAwsFullPath}")
    private String visitAwsFullPath;

    private AmazonS3 s3Client;

    private String httpS3RootPath;

    private String visitFullPath;

    private AwsS3FileSerivce() {
        // 初始化时无法读取到配置的信息，因为springboot value还没有工作。
        /*
        // https://basoft-test-images.s3.ap-northeast-2.amazonaws.com
        this.httpS3RootPath = new StringBuilder("https://s3.")
                .append(this.clientRegion)
                .append(".")
                .append("amazonaws.com/")
                .append(this.bucketName).append("/")
                .toString();
        System.out.println("S3 Full Path:" + httpS3RootPath);

        this.visitFullPath = StringUtils.isEmpty(this.visitAwsFullPath) ? this.httpS3RootPath : this.visitAwsFullPath;
        System.out.println("Visit Full Path:" + visitFullPath);

        this.s3Client = this.initS3Client();
        */
    }

    /**
     * 对象初始化交由容器管理后执行该方法进行初始化
     */
    @PostConstruct
    public void initService(){
        // https://basoft-test-images.s3.ap-northeast-2.amazonaws.com
        this.httpS3RootPath = new StringBuilder("https://s3.")
                .append(this.clientRegion)
                .append(".")
                .append("amazonaws.com/")
                .append(this.bucketName).append("/")
                .toString();
        System.out.println("S3 Full Path:" + httpS3RootPath);

        this.visitFullPath = StringUtils.isEmpty(this.visitAwsFullPath) ? this.httpS3RootPath : this.visitAwsFullPath;
        System.out.println("Visit Full Path:" + visitFullPath);

        this.s3Client = this.initS3Client();
    }

    private AmazonS3 initS3Client() {
        //https://jingyan.baidu.com/article/7082dc1c66a268e40a89bd04.html
        try {
            ClientConfiguration clientConfig = new ClientConfiguration();
            clientConfig.setConnectionTimeout(5000);
            // clientConfig.setRequestTimeout(1000);
            clientConfig.setSocketTimeout(5000);

            return AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(appKey, secretAccessKey)))
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

    public static final String buildFilePath(String rootPath, String name) {
        if ("/".startsWith(rootPath)) {
            return name;
        }
        return rootPath + "/" + name;
    }

    @Override
    public FileReference uploadFile(UploadFile file) {
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
            return new FileReference(fileId, writeToUrl, fullName, visitFullPath, file.props(), file);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            throw new IllegalStateException(e);
        }
    }

    @Override
    public byte[] fetch(String fileFullPath) {
        // this.s3Client.listObjects();
        String fileKey = buildFilePath(this.rootKey, fileFullPath);
        S3Object object = this.s3Client.getObject(new GetObjectRequest(bucketName, fileKey));

        if (object == null) {
            throw new IllegalArgumentException("not found file on:" + fileFullPath);
        }

        InputStream input = object.getObjectContent();
        try {
            return org.apache.commons.io.IOUtils.toByteArray(input);
        } catch (IOException e) {
            throw new IllegalStateException("io error");
        }
    }

    @Override
    public List<FileReference> batchUpload(List<UploadFile> collect) {
        /*this.s3Clien
        ObjectMetadataProvider metadataProvider = new ObjectMetadataProvider() {
            public void provideObjectMetadata(File file, ObjectMetadata metadata) {
                if (isJPEG(file)) {
                    metadata.addUserMetadata("original-image-date",parseExifImageDate(file));
                }
            }
        };
        MultipleFileUpload upload = tm.uploadFileList(this.bucketName, myKeyPrefix, rootDirectory, fileList, metadataProvider);*/
        //return null;
        //throw new UnsupportedOperationException("not support batchUpload of s3");

        List<FileReference> refList = new ArrayList<>(collect.size());
        for (UploadFile file : collect) {
            try {
                final FileReference fileRef = this.uploadFile(file);
                refList.add(fileRef);
            } catch (Throwable te) {
                te.printStackTrace();
            }
        }
        return refList;
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
