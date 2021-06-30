package com.basoft.service.enumerate;

/**
 * @author BASOFT
 */
public enum BatchEnum {
    articleSummaryGroupHair(1, 20),
    articleSummaryDetailStatics(2, 20),
    articleSummaryHourDetailStatics(3, 20),
    batchInsertStreamMsgYesTodayHour(4, 20),
    batchInsertStreamMsgYesToday(5, 20),
    batchInsertStreamMsgLastWeek(6, 20),
    batchUser(7, 20),
    batchColonyTest(8, 20),
    batchGetWxToken(9, 20),
    batchAlliexTransPayment(10, 20),
    batchAlliexTransExchange(11, 20),
    batchAlliexTransPaymentClosing(12, 20),
    batchAlliexTransPaymentQuery(13, 20),
    batchAlliexTransRefund(14, 20),
    batchTestServerOrderCheck(15, 20),
    batchBookingOrderSendMsg(16, 20);

    // 定时任务类型
    private int type;

    // 定时任务加锁状态有效期，单位分钟
    private int expires;

    BatchEnum(int type, int expires) {
        this.type = type;
        this.expires = expires;
    }

    public int getType() {
        return this.type;
    }

    public int getExpires() {
        return this.expires;
    }

    public static BatchEnum valueOf(int type) {
        for (BatchEnum be : BatchEnum.values()) {
            if (be.getType() == type) {
                return be;
            }
        }
        return null;
    }
}