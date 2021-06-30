package com.basoft.eorder.util;

import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.interfaces.query.StoreDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;

@Slf4j
public class StoreUtil {
    /**
     * 计算门店当前是否正在营业
     * <p>
     * 暂不支持跨天营业时间段设置
     *
     * @param store
     * @return 0-打烊 1-营业中
     */
    public static int ifOpening(Store store) {
        int isOpening = store.isOpening();
        // 手工打烊
        if (isOpening == 0) {
            return 0;
        }

        // 营业中
        else if (isOpening == 1) {
            // 获取当前时间：时分
            Calendar now = Calendar.getInstance();
            int nowHour = now.get(Calendar.HOUR_OF_DAY);
            int nowMinute = now.get(Calendar.MINUTE);
            log.info("当前时: " + nowHour);
            log.info("当前分: " + nowMinute);

            // 根据营业时间段设置判断当前是否营业
            int isSegmented = store.isSegmented();
            log.info("是否分阶段营业: " + isSegmented);

            // 不分阶段营业:营业起始和结束时间存于morningSt和morningEt
            if (isSegmented == 0) {
                String morningSt = store.morningSt();
                String morningEt = store.morningEt();

                // 起始和结束时间有一个为空则直接返回为营业中状态-即全天营业
                if (morningSt == null || morningEt == null || "".equals(morningSt.trim()) || "".equals(morningEt.trim())) {
                    return 1;
                }

                // 获取开始的小时和分钟
                String[] moningStArray = morningSt.split(":");
                int mStHour = Integer.valueOf(moningStArray[0]);
                int mStMinute = Integer.valueOf(moningStArray[1]);

                // 获取结束的小时和分钟
                String[] moningEtArray = morningEt.split(":");
                int mEtHour = Integer.valueOf(moningEtArray[0]);
                int mEtMinute = Integer.valueOf(moningEtArray[1]);

                if (nowHour > mStHour && nowHour < mEtHour) {
                    return 1;
                } else if (nowHour == mStHour && nowMinute >= mStMinute) {
                    return 1;
                } else if (nowHour == mEtHour && nowMinute <= mEtMinute) {
                    return 1;
                } else {
                    return 0;
                }
            }

            // 分段营业
            else if (isSegmented == 1) {
                // 早段
                String morningSt = store.morningSt();
                String morningEt = store.morningEt();
                // 中段
                String noonSt = store.noonSt();
                String noonEt = store.noonEt();
                // 晚段
                String eveningSt = store.eveningSt();
                String eveningEt = store.eveningEt();
                // 预留
                /*String afternoonSt = store.afternoonSt();
                String afternoonEt = store.afternoonEt();
                String midnightSt = store.midnightSt();
                String midnightEt = store.midnightEt();*/

                boolean morningUse = true;
                if (morningSt == null || morningEt == null || "".equals(morningSt.trim()) || "".equals(morningEt.trim())) {
                    morningUse = false;
                }

                boolean noonUse = true;
                if (noonSt == null || noonEt == null || "".equals(noonSt.trim()) || "".equals(noonEt.trim())) {
                    noonUse = false;
                }

                boolean eveningUse = true;
                if (eveningSt == null || eveningEt == null || "".equals(eveningSt.trim()) || "".equals(eveningEt.trim())) {
                    eveningUse = false;
                }

                // 早、中、晚阶段
                if (morningUse && noonUse && eveningUse) {
                    // 获取早段开始的小时和分钟
                    String[] moningStArray = morningSt.split(":");
                    int mStHour = Integer.valueOf(moningStArray[0]);
                    int mStMinute = Integer.valueOf(moningStArray[1]);

                    // 获取早段结束的小时和分钟
                    String[] moningEtArray = morningEt.split(":");
                    int mEtHour = Integer.valueOf(moningEtArray[0]);
                    int mEtMinute = Integer.valueOf(moningEtArray[1]);

                    // 获取中段开始的小时和分钟
                    String[] noonStArray = noonSt.split(":");
                    int nStHour = Integer.valueOf(noonStArray[0]);
                    int nStMinute = Integer.valueOf(noonStArray[1]);

                    // 获取中段结束的小时和分钟
                    String[] noonEtArray = noonEt.split(":");
                    int nEtHour = Integer.valueOf(noonEtArray[0]);
                    int nEtMinute = Integer.valueOf(noonEtArray[1]);

                    // 获取晚段开始的小时和分钟
                    String[] eveningStArray = eveningSt.split(":");
                    int eStHour = Integer.valueOf(eveningStArray[0]);
                    int eStMinute = Integer.valueOf(eveningStArray[1]);

                    // 获取晚段结束的小时和分钟
                    String[] eveningEtArray = eveningEt.split(":");
                    int eEtHour = Integer.valueOf(eveningEtArray[0]);
                    int eEtMinute = Integer.valueOf(eveningEtArray[1]);

                    // 早段
                    if (nowHour > mStHour && nowHour < mEtHour) {
                        return 1;
                    } else if (nowHour == mStHour && nowMinute >= mStMinute) {
                        return 1;
                    } else if (nowHour == mEtHour && nowMinute <= mEtMinute) {
                        return 1;
                    }

                    // 中段
                    else if (nowHour > nStHour && nowHour < nEtHour) {
                        return 1;
                    } else if (nowHour == nStHour && nowMinute >= nStMinute) {
                        return 1;
                    } else if (nowHour == nEtHour && nowMinute <= nEtMinute) {
                        return 1;
                    }

                    // 晚段
                    else if (nowHour > eStHour && nowHour < eEtHour) {
                        return 1;
                    } else if (nowHour == eStHour && nowMinute >= eStMinute) {
                        return 1;
                    } else if (nowHour == eEtHour && nowMinute <= eEtMinute) {
                        return 1;
                    }

                    // 非营业时间
                    else {
                        return 0;
                    }
                }

                // 早、中阶段
                else if (morningUse && noonUse) {
                    // 获取早段开始的小时和分钟
                    String[] moningStArray = morningSt.split(":");
                    int mStHour = Integer.valueOf(moningStArray[0]);
                    int mStMinute = Integer.valueOf(moningStArray[1]);

                    // 获取早段结束的小时和分钟
                    String[] moningEtArray = morningEt.split(":");
                    int mEtHour = Integer.valueOf(moningEtArray[0]);
                    int mEtMinute = Integer.valueOf(moningEtArray[1]);

                    // 获取中段开始的小时和分钟
                    String[] noonStArray = noonSt.split(":");
                    int nStHour = Integer.valueOf(noonStArray[0]);
                    int nStMinute = Integer.valueOf(noonStArray[1]);

                    // 获取中段结束的小时和分钟
                    String[] noonEtArray = noonEt.split(":");
                    int nEtHour = Integer.valueOf(noonEtArray[0]);
                    int nEtMinute = Integer.valueOf(noonEtArray[1]);

                    // 早段
                    if (nowHour > mStHour && nowHour < mEtHour) {
                        return 1;
                    } else if (nowHour == mStHour && nowMinute >= mStMinute) {
                        return 1;
                    } else if (nowHour == mEtHour && nowMinute <= mEtMinute) {
                        return 1;
                    }

                    // 中段
                    else if (nowHour > nStHour && nowHour < nEtHour) {
                        return 1;
                    } else if (nowHour == nStHour && nowMinute >= nStMinute) {
                        return 1;
                    } else if (nowHour == nEtHour && nowMinute <= nEtMinute) {
                        return 1;
                    }

                    // 非营业时间
                    else {
                        return 0;
                    }
                }

                // 早、晚阶段
                else if (morningUse && eveningUse) {
                    // 获取早段开始的小时和分钟
                    String[] moningStArray = morningSt.split(":");
                    int mStHour = Integer.valueOf(moningStArray[0]);
                    int mStMinute = Integer.valueOf(moningStArray[1]);

                    // 获取早段结束的小时和分钟
                    String[] moningEtArray = morningEt.split(":");
                    int mEtHour = Integer.valueOf(moningEtArray[0]);
                    int mEtMinute = Integer.valueOf(moningEtArray[1]);

                    // 获取晚段开始的小时和分钟
                    String[] eveningStArray = eveningSt.split(":");
                    int eStHour = Integer.valueOf(eveningStArray[0]);
                    int eStMinute = Integer.valueOf(eveningStArray[1]);

                    // 获取晚段结束的小时和分钟
                    String[] eveningEtArray = eveningEt.split(":");
                    int eEtHour = Integer.valueOf(eveningEtArray[0]);
                    int eEtMinute = Integer.valueOf(eveningEtArray[1]);

                    // 早段
                    if (nowHour > mStHour && nowHour < mEtHour) {
                        return 1;
                    } else if (nowHour == mStHour && nowMinute >= mStMinute) {
                        return 1;
                    } else if (nowHour == mEtHour && nowMinute <= mEtMinute) {
                        return 1;
                    }

                    // 晚段
                    else if (nowHour > eStHour && nowHour < eEtHour) {
                        return 1;
                    } else if (nowHour == eStHour && nowMinute >= eStMinute) {
                        return 1;
                    } else if (nowHour == eEtHour && nowMinute <= eEtMinute) {
                        return 1;
                    }

                    // 非营业时间
                    else {
                        return 0;
                    }
                }

                // 中、晚阶段
                else if (noonUse && eveningUse) {
                    // 获取中段开始的小时和分钟
                    String[] noonStArray = noonSt.split(":");
                    int nStHour = Integer.valueOf(noonStArray[0]);
                    int nStMinute = Integer.valueOf(noonStArray[1]);

                    // 获取中段结束的小时和分钟
                    String[] noonEtArray = noonEt.split(":");
                    int nEtHour = Integer.valueOf(noonEtArray[0]);
                    int nEtMinute = Integer.valueOf(noonEtArray[1]);

                    // 获取晚段开始的小时和分钟
                    String[] eveningStArray = eveningSt.split(":");
                    int eStHour = Integer.valueOf(eveningStArray[0]);
                    int eStMinute = Integer.valueOf(eveningStArray[1]);

                    // 获取晚段结束的小时和分钟
                    String[] eveningEtArray = eveningEt.split(":");
                    int eEtHour = Integer.valueOf(eveningEtArray[0]);
                    int eEtMinute = Integer.valueOf(eveningEtArray[1]);

                    // 中段
                    if (nowHour > nStHour && nowHour < nEtHour) {
                        return 1;
                    } else if (nowHour == nStHour && nowMinute >= nStMinute) {
                        return 1;
                    } else if (nowHour == nEtHour && nowMinute <= nEtMinute) {
                        return 1;
                    }

                    // 晚段
                    else if (nowHour > eStHour && nowHour < eEtHour) {
                        return 1;
                    } else if (nowHour == eStHour && nowMinute >= eStMinute) {
                        return 1;
                    } else if (nowHour == eEtHour && nowMinute <= eEtMinute) {
                        return 1;
                    }

                    // 非营业时间
                    else {
                        return 0;
                    }
                }

                // 早阶段
                else if (morningUse) {
                    // 获取早段开始的小时和分钟
                    String[] moningStArray = morningSt.split(":");
                    int mStHour = Integer.valueOf(moningStArray[0]);
                    int mStMinute = Integer.valueOf(moningStArray[1]);

                    // 获取早段结束的小时和分钟
                    String[] moningEtArray = morningEt.split(":");
                    int mEtHour = Integer.valueOf(moningEtArray[0]);
                    int mEtMinute = Integer.valueOf(moningEtArray[1]);

                    // 早段
                    if (nowHour > mStHour && nowHour < mEtHour) {
                        return 1;
                    } else if (nowHour == mStHour && nowMinute >= mStMinute) {
                        return 1;
                    } else if (nowHour == mEtHour && nowMinute <= mEtMinute) {
                        return 1;
                    }
                    // 非营业时间
                    else {
                        return 0;
                    }
                }

                // 中阶段
                else if (noonUse) {
                    // 获取中段开始的小时和分钟
                    String[] noonStArray = noonSt.split(":");
                    int nStHour = Integer.valueOf(noonStArray[0]);
                    int nStMinute = Integer.valueOf(noonStArray[1]);

                    // 获取中段结束的小时和分钟
                    String[] noonEtArray = noonEt.split(":");
                    int nEtHour = Integer.valueOf(noonEtArray[0]);
                    int nEtMinute = Integer.valueOf(noonEtArray[1]);

                    // 中段
                    if (nowHour > nStHour && nowHour < nEtHour) {
                        return 1;
                    } else if (nowHour == nStHour && nowMinute >= nStMinute) {
                        return 1;
                    } else if (nowHour == nEtHour && nowMinute <= nEtMinute) {
                        return 1;
                    }

                    // 非营业时间
                    else {
                        return 0;
                    }
                }

                // 晚阶段
                else if (eveningUse) {
                    // 获取晚段开始的小时和分钟
                    String[] eveningStArray = eveningSt.split(":");
                    int eStHour = Integer.valueOf(eveningStArray[0]);
                    int eStMinute = Integer.valueOf(eveningStArray[1]);

                    // 获取晚段结束的小时和分钟
                    String[] eveningEtArray = eveningEt.split(":");
                    int eEtHour = Integer.valueOf(eveningEtArray[0]);
                    int eEtMinute = Integer.valueOf(eveningEtArray[1]);

                    // 晚段
                    if (nowHour > eStHour && nowHour < eEtHour) {
                        return 1;
                    } else if (nowHour == eStHour && nowMinute >= eStMinute) {
                        return 1;
                    } else if (nowHour == eEtHour && nowMinute <= eEtMinute) {
                        return 1;
                    }

                    // 非营业时间
                    else {
                        return 0;
                    }
                }
            }
        }

        return 1;
    }

    /**
     * 计算门店当前是否正在营业
     * <p>
     * 暂不支持跨天营业时间段设置
     *
     * @param storeDTO
     * @return 0-打烊 1-营业中
     */
    public static int ifOpening(StoreDTO storeDTO) {
        Store store = new Store.Builder().isOpening(storeDTO.getIsOpening())
                .isSegmented(storeDTO.getIsSegmented())
                .morningSt(storeDTO.getMorningSt())
                .morningEt(storeDTO.getMorningEt())
                .noonSt(storeDTO.getNoonSt())
                .noonEt(storeDTO.getNoonEt())
                .eveningSt(storeDTO.getEveningSt())
                .eveningEt(storeDTO.getEveningEt())
                .afternoonSt(storeDTO.getAfternoonSt())
                .afternoonEt(storeDTO.getAfternoonEt())
                .eveningSt(storeDTO.getEveningSt())
                .eveningEt(storeDTO.getEveningEt())
                .build();

        return ifOpening(store);
    }
}
