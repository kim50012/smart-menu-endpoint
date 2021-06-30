package com.basoft.eorder.application;

import com.basoft.eorder.domain.model.StoreTable;
import com.basoft.eorder.domain.model.agent.QRCodeAgent;

import java.util.List;

public interface QRCodeGenerateService {
    byte[] buildQRCode(String contents);

    default List<GenerateQRCodeResponse> batchGenerateWechatQRCode(List<QRCodeGenerateRequest> collect) {
        return null;
    }

    default List<StoreTable> batchGenerateWechatQRCodes(List<StoreTable> collect) {
        return null;
    }

    List<StoreTable> matchGenerateWechatQRCodes(List<StoreTable> collect);

    /**
     * 根据代理商Id，生成代理商微信二维码
     *
     * @param agentId
     * @return
     */
    QRCodeAgent matchGenerateCagentWechatQRCode(String agentId);

    public static final class GenerateQRCodeResponse {
        // context id
        private String sid;
        private String imageUrl;

        public GenerateQRCodeResponse(String sid, String fullPath) {
            this.sid = sid;
            this.imageUrl = fullPath;
        }

        public String getSid() {
            return sid;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
