package com.basoft.file;

import com.basoft.file.application.impl.AwsS3FileSerivce;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AwsS3FileServiceTest {


//    @Test
    public void testUpload(){


        try {
            final byte[] bytes = Files.readAllBytes(Paths.get("/Users/woonill/woonill/workspace/ba-soft-pro/basoft_fs/src/test/java/com/basoft/file/a.jpg"));
            AwsS3FileSerivce s = new AwsS3FileSerivce.Builder()
                    .bucketName("basoft-test-images")
                    .accessKey("AKIAIC3O6DK3OOMTAVOQ")
                    .clientRegion("ap-northeast-2")
                    .secretAccessKey("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")
                    .rootKey("jeju-images")
                    .visitLastPath("https://d2or24sz8bxkt1.cloudfront.net")
                    .build();

/*            final ObjectListing objectListing = s.getS3Client().listObjects("basoft-test-images");
            for(S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
               System.out.println(objectSummary.getKey());
            }*/

/*
            final byte[] fetch = s.fetch("jeju-images/6616659d-91e5-4364-96aa-62b43cf4900a.png");
            System.out.println(fetch.length);
*/

            final byte[] fetch = s.fetch("6616659d-91e5-4364-96aa-62b43cf4900a.png");
            System.out.println(fetch.length);




/*
            UploadFile uf = new UploadFile.Builder()
                    .name("ff")
                    .originalName("a.png")
                    .payload(bytes)
                    .type("image/png").build();
            final FileService.FileRef fileRef = s.uploadFile(uf);
            System.out.println(fileRef.getFullPath());
            System.out.println(fileRef.getVisitFullPath());
*/

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
