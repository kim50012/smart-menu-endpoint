package com.basoft.eorder;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

public class QRCodeTestCase {


    public static void main(String...args){

        int width=300;
        int height=300;
        String format="png";
        String contents="https://www.baidu.com";
        HashMap map=new HashMap();
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        map.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.M);
        map.put(EncodeHintType.MARGIN, 0);
        try {
            BitMatrix bm = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height);


            String uuid = UUID.randomUUID().toString()+".png";
            final Path path = Paths.get(uuid).toAbsolutePath();
            MatrixToImageWriter.writeToPath(bm, format, path);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
