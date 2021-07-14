package com.basoft.file;

import com.basoft.file.application.FileService;
import com.basoft.file.application.UploadFile;
import com.basoft.file.application.impl.AwsS3FileSerivce;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AwsS3FileServiceTest {


//    @Test
    public void testUpload(){


        try {
            final byte[] bytes = Files.readAllBytes(Paths.get("D:\\003.jpg"));
            AwsS3FileSerivce s = new AwsS3FileSerivce.Builder()
                    .bucketName("s3orderbucket")
                    .accessKey("AKIAYZXO2WEU7RIBSFML")
                    .clientRegion("ap-southeast-1")
                    .secretAccessKey("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")
                    .rootKey("orderking-images")
                    .visitLastPath("https://s3orderbucket.s3.ap-southeast-1.amazonaws.com")
                    .build();            
            
/*            final ObjectListing objectListing = s.getS3Client().listObjects("basoft-test-images");
            for(S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
               System.out.println(objectSummary.getKey());
            }*/

/*
            final byte[] fetch = s.fetch("jeju-images/6616659d-91e5-4364-96aa-62b43cf4900a.png");
            System.out.println(fetch.length);
*/

            
//            final byte[] fetch = s.fetch("a.jpg");
//            System.out.println(fetch.length);





            UploadFile uf = new UploadFile.Builder()
                    .name("ff")
                    .originalName("a.jpg")
                    .payload(bytes)
                    .type("image/jpg").build();
            final FileService.FileRef fileRef = s.uploadFile(uf);
            System.out.println(fileRef.getFullPath());
            System.out.println(fileRef.getVisitFullPath());


        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
