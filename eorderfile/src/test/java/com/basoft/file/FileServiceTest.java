package com.basoft.file;

import com.basoft.file.application.FileService;
import com.basoft.file.application.UploadFile;
import com.basoft.file.application.impl.DefaultFileTransfer;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import junit.framework.TestCase;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FileServiceTest {



    public DataSource getDataSource() {
        MysqlDataSource mds = new MysqlDataSource();
        mds.setUrl("jdbc:mysql://192.168.0.241:3307/eorder?useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true&amp;useSSL=true&amp;failOverReadOnly=false");
        mds.setUser("bawechat");
        mds.setPassword("1q2w3e4r!");
        mds.setUseSSL(true);
//        mds.setDatabaseName("");
//        mds.setPortNumber(3307);
//        mds.setUser("bawechat");
//        mds.setPassword("1q2w3e4r!");
        return mds;
    }


//    @Test
    public void testRootParser(){


        String logicalRootPath = DefaultFileTransfer.getRootPath("./upload-files");
        TestCase.assertEquals("/upload-files",logicalRootPath);

        logicalRootPath = DefaultFileTransfer.getSubPath("upload-files","./upload-files/assf/2013423/");
        TestCase.assertEquals("/assf/2013423",logicalRootPath);



    }

//    @Test
    public void testFileService(){

/*        final FileService mock = Mockito.mock(FileService.class);
        Mockito.when(mock.uploadFile(Mockito.isA(UploadFile.class))).then(new Answer<FileService.FileRef>() {
            @Override
            public FileService.FileRef answer(InvocationOnMock invocationOnMock) throws Throwable {
                UploadFile cc =  (UploadFile) invocationOnMock.getArguments()[0];
                TestCase.assertEquals(cc.name(),"test-case");
                return new FileService.FileRef(UUID.randomUUID().toString(),"/fullpath");
            }
        });*/


        ByteArrayOutputStream bis = new ByteArrayOutputStream(1024);
        bis.write(123123);
        final byte[] bytes = bis.toByteArray();

        try{
            final DefaultFileTransfer fileTransfer = new DefaultFileTransfer("./images","http://192.168.0.81/file/api/v1/list/") {
                @Override
                protected void writeFile(String fullFilePath, byte[] payload) {
                    System.out.println(fullFilePath);
                    //不写任何操作
                }
            };
//            DefaultFileService dfs = new DefaultFileService(getDataSource(),fileTransfer);
            FileService.FileRef fileRef = fileTransfer.uploadFile(UploadFile.newBuild().type("image/png").name("a.file").originalName("a.png").payload(bytes).build());
            TestCase.assertEquals(fileRef.getFullPath(),"http://192.168.0.81/file/api/v1/list/"+fileRef.fullName());
        }
        catch (Throwable te){
            te.printStackTrace();
        }
        finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

