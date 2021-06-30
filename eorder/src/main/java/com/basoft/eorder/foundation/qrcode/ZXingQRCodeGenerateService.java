package com.basoft.eorder.foundation.qrcode;

import com.basoft.eorder.application.QRCodeGenerateRequest;
import com.basoft.eorder.application.QRCodeGenerateService;
import com.basoft.eorder.application.file.FileService;
import com.basoft.eorder.application.file.UploadFile;
import com.basoft.eorder.domain.model.StoreTable;
import com.basoft.eorder.domain.model.agent.QRCodeAgent;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service("zxingQRService")
public class ZXingQRCodeGenerateService implements QRCodeGenerateService {

    private FileService fs;

    ZXingQRCodeGenerateService(){}


    public ZXingQRCodeGenerateService(FileService fus){
        this.fs = fus;
    }

    private byte[] generateByte(String contents){

        int width=300;
        int height=300;
        String format="png";
        HashMap map=new HashMap();
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        map.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.M);
        map.put(EncodeHintType.MARGIN, 0);
        try {
            BitMatrix bm = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            MatrixToImageWriter.writeToStream(bm,format,bos);
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }




    @Override
    public byte[] buildQRCode(String contents) {

        int width=300;
        int height=300;
        String format="png";
        HashMap map=new HashMap();
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        map.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.M);
        map.put(EncodeHintType.MARGIN, 0);
        try {
            BitMatrix bm = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            MatrixToImageWriter.writeToStream(bm, format, baos);


//            String uuid = rootPath+"/"+UUID.randomUUID().toString()+".png";
//            final Path path = Paths.get(uuid).toAbsolutePath();
//            MatrixToImageWriter.writeToPath(bm, format, path);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    @Override
    public List<GenerateQRCodeResponse> batchGenerateWechatQRCode(List<QRCodeGenerateRequest> requestList) {

        List<UploadFile> ufList =
                requestList.stream().map((req) -> {
                    byte[] qrCodeByte = buildQRCode(req.getContent());
                    return UploadFile.newBuild()
                            .type("image/png")
                            .name("qrcode-image")
                            .originalName(req.getSid())
                            .payload(qrCodeByte)
                            .prop("sid",req.getSid())
                            .build();
                })
                .collect(Collectors.toList());

        return
            this.fs.batchUpload(ufList)
                    .stream()
                    .map((fr) -> {
                            String sid = (String)fr.prop("sid");
                           return new GenerateQRCodeResponse(sid,fr.getFullPath());
                    })
                    .collect(Collectors.toList());
    }

    @Override
    public List<StoreTable> batchGenerateWechatQRCodes(List<StoreTable> collect) {
        return null;
    }

    @Override
    public List<StoreTable> matchGenerateWechatQRCodes(List<StoreTable> collect) {
        return null;
    }

    /**
     * 根据代理商Id，生成代理商微信二维码
     *
     * @param agentId
     * @return
     */
    @Override
    public QRCodeAgent matchGenerateCagentWechatQRCode(String agentId) {
        return null;
    }
}
