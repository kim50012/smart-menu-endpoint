package com.basoft.eorder.foundation.jdbc.repo.post;

import com.basoft.eorder.domain.model.post.CustPostAddress;
import com.basoft.eorder.domain.post.CustPostAddressRepository;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Desc:JdbcCustPostAddressRepoImpl
 *
 * @author Mentor
 * @version 1.0
 * @date 20200429
 */
@Repository
public class JdbcCustPostAddressRepoImpl extends BaseRepository implements CustPostAddressRepository {
    private static final String SAVE_CUST_POST_ADDRESS_SQL = "insert into POST_WX_CUST_ADDRESS (" +
            " WX_OPEN_ID" +
            ", ADDR_NAME" +
            ", COUNTRY_CHN" +
            ", COUNTRY_KOR" +
            ", COUNTRY_ENG " +
            ", COUNTRY_CODE" +
            ", CONSIGNEE" +
            ", MOBILE_COUNTRY_CODE" +
            ", MOBILE_NO" +
            ", ALTERNATE_MOBILE_COUNTRY_CODE" +
            ", ALTERNATE_MOBILE_NO" +
            ", REGION_NAME_CHN" +
            ", REGION_NAME_KOR" +
            ", REGION_NAME_ENG" +
            ", REGION_ONE_NAME_CHN" +
            ", REGION_ONE_NAME_KOR" +
            ", REGION_ONE_NAME_ENG" +
            ", REGION_ONE_CODE" +
            ", REGION_TWO_NAME_CHN" +
            ", REGION_TWO_NAME_KOR" +
            ", REGION_TWO_NAME_ENG" +
            ", REGION_TWO_CODE" +
            ", REGION_THREE_NAME_CHN" +
            ", REGION_THREE_NAME_KOR" +
            ", REGION_THREE_NAME_ENG" +
            ", REGION_THREE_CODE" +
            ", REGION_FOUR_NAME_CHN" +
            ", REGION_FOUR_NAME_KOR" +
            ", REGION_FOUR_NAME_ENG" +
            ", REGION_FOUR_CODE" +
            ", REGION_FIVE_NAME_CHN" +
            ", REGION_FIVE_NAME_KOR" +
            ", REGION_FIVE_NAME_ENG" +
            ", REGION_FIVE_CODE" +
            ", ADDR_DETAIL_CHN" +
            ", ADDR_DETAIL_KOR" +
            ", ADDR_DETAIL_ENG" +
            ", ADDR_POSTCODE" +
            ", EMAIL" +
            ", TAG" +
            ", IS_DEFAULT" +
            ", STATUS" +
            ", STATUS_TIME" +
            ", CREATE_USER" +
            ", UPDATE_USER" +
            ")" +
            " VALUES(?,?,?,?,?,?,?,?,?,?" +
            ",?,?,?,?,?,?,?,?,?,?" +
            ",?,?,?,?,?,?,?,?,?,?" +
            ",?,?,?,?,?,?,?,?,?,?" +
            ",?,1,now(),'H5','H5')";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 平台客户新增配送地址
     *
     * @param custPostAddress
     * @return
     */
    @Override
    @Transactional
    public int saveCustPostAddress(CustPostAddress custPostAddress) {
        // 新增地址为默认地址，则变更其他地址为非默认地址
        if (custPostAddress.getIsDefault() == 1) {
            updateCustPostAddressDefault(custPostAddress.getWxOpenId());
        }

        int result = this.jdbcTemplate.update(SAVE_CUST_POST_ADDRESS_SQL,
                new Object[]{
                        custPostAddress.getWxOpenId(),
                        custPostAddress.getAddrName(),
                        custPostAddress.getCountryChn(),
                        custPostAddress.getCountryKor(),
                        custPostAddress.getCountryEng(),
                        custPostAddress.getCountryCode(),
                        custPostAddress.getConsignee(),
                        custPostAddress.getMobileCountryCode(),
                        custPostAddress.getMobileNo(),
                        custPostAddress.getAlternateMobileCountryCode(),
                        custPostAddress.getAlternateMobileNo(),
                        custPostAddress.getRegionNameChn(),
                        custPostAddress.getRegionNameKor(),
                        custPostAddress.getRegionNameEng(),
                        custPostAddress.getRegionOneNameChn(),
                        custPostAddress.getRegionOneNameKor(),
                        custPostAddress.getRegionOneNameEng(),
                        custPostAddress.getRegionOneCode(),
                        custPostAddress.getRegionTwoNameChn(),
                        custPostAddress.getRegionTwoNameKor(),
                        custPostAddress.getRegionTwoNameEng(),
                        custPostAddress.getRegionTwoCode(),
                        custPostAddress.getRegionThreeNameChn(),
                        custPostAddress.getRegionThreeNameKor(),
                        custPostAddress.getRegionThreeNameEng(),
                        custPostAddress.getRegionThreeCode(),
                        custPostAddress.getRegionFourNameChn(),
                        custPostAddress.getRegionFourNameKor(),
                        custPostAddress.getRegionFourNameEng(),
                        custPostAddress.getRegionFourCode(),
                        custPostAddress.getRegionFiveNameChn(),
                        custPostAddress.getRegionFiveNameKor(),
                        custPostAddress.getRegionFiveNameEng(),
                        custPostAddress.getRegionFiveCode(),
                        custPostAddress.getAddrDetailChn(),
                        custPostAddress.getAddrDetailKor(),
                        custPostAddress.getAddrDetailEng(),
                        custPostAddress.getAddrPostcode(),
                        custPostAddress.getEmail(),
                        custPostAddress.getTag(),
                        custPostAddress.getIsDefault()
                });
        return result;
    }

    /**
     * 平台客户修改配送地址
     *
     * @param custPostAddress
     * @return
     */
    @Override
    @Transactional
    public int updateCustPostAddress(CustPostAddress custPostAddress) {
        // 修改地址为默认地址，则变更其他地址为非默认地址
        if (custPostAddress.getIsDefault() == 1) {
            updateCustPostAddressDefault(custPostAddress.getWxOpenId());
        }
        int updateCount = this.getJdbcTemplate().update("UPDATE POST_WX_CUST_ADDRESS SET ADDR_NAME = ?, COUNTRY_CHN = ?, COUNTRY_KOR = ?, COUNTRY_ENG = ?, COUNTRY_CODE = ?, CONSIGNEE = ?, MOBILE_COUNTRY_CODE = ?, MOBILE_NO = ?, ALTERNATE_MOBILE_COUNTRY_CODE = ?, ALTERNATE_MOBILE_NO = ?, REGION_NAME_CHN = ?, REGION_NAME_KOR = ?, REGION_NAME_ENG = ?, REGION_ONE_NAME_CHN = ?, REGION_ONE_NAME_KOR = ?, REGION_ONE_NAME_ENG = ?, REGION_ONE_CODE = ?, REGION_TWO_NAME_CHN = ?, REGION_TWO_NAME_KOR = ?, REGION_TWO_NAME_ENG = ?, REGION_TWO_CODE = ?, REGION_THREE_NAME_CHN = ?, REGION_THREE_NAME_KOR = ?, REGION_THREE_NAME_ENG = ?, REGION_THREE_CODE = ?, REGION_FOUR_NAME_CHN = ?, REGION_FOUR_NAME_KOR = ?, REGION_FOUR_NAME_ENG = ?, REGION_FOUR_CODE = ?, REGION_FIVE_NAME_CHN = ?, REGION_FIVE_NAME_KOR = ?, REGION_FIVE_NAME_ENG = ?, REGION_FIVE_CODE = ?, ADDR_DETAIL_CHN = ?, ADDR_DETAIL_KOR = ?, ADDR_DETAIL_ENG = ?, ADDR_POSTCODE = ?, EMAIL = ?, TAG = ?, IS_DEFAULT = ?, UPDATE_TIME = NOW() WHERE ADDR_ID = ? AND WX_OPEN_ID = ?"
                , new Object[]{
                        custPostAddress.getAddrName(),
                        custPostAddress.getCountryChn(),
                        custPostAddress.getCountryKor(),
                        custPostAddress.getCountryEng(),
                        custPostAddress.getCountryCode(),
                        custPostAddress.getConsignee(),
                        custPostAddress.getMobileCountryCode(),
                        custPostAddress.getMobileNo(),
                        custPostAddress.getAlternateMobileCountryCode(),
                        custPostAddress.getAlternateMobileNo(),
                        custPostAddress.getRegionNameChn(),
                        custPostAddress.getRegionNameKor(),
                        custPostAddress.getRegionNameEng(),
                        custPostAddress.getRegionOneNameChn(),
                        custPostAddress.getRegionOneNameKor(),
                        custPostAddress.getRegionOneNameEng(),
                        custPostAddress.getRegionOneCode(),
                        custPostAddress.getRegionTwoNameChn(),
                        custPostAddress.getRegionTwoNameKor(),
                        custPostAddress.getRegionTwoNameEng(),
                        custPostAddress.getRegionTwoCode(),
                        custPostAddress.getRegionThreeNameChn(),
                        custPostAddress.getRegionThreeNameKor(),
                        custPostAddress.getRegionThreeNameEng(),
                        custPostAddress.getRegionThreeCode(),
                        custPostAddress.getRegionFourNameChn(),
                        custPostAddress.getRegionFourNameKor(),
                        custPostAddress.getRegionFourNameEng(),
                        custPostAddress.getRegionFourCode(),
                        custPostAddress.getRegionFiveNameChn(),
                        custPostAddress.getRegionFiveNameKor(),
                        custPostAddress.getRegionFiveNameEng(),
                        custPostAddress.getRegionFiveCode(),
                        custPostAddress.getAddrDetailChn(),
                        custPostAddress.getAddrDetailKor(),
                        custPostAddress.getAddrDetailEng(),
                        custPostAddress.getAddrPostcode(),
                        custPostAddress.getEmail(),
                        custPostAddress.getTag(),
                        custPostAddress.getIsDefault(),
                        custPostAddress.getAddrId(),
                        custPostAddress.getWxOpenId()
                });
        return updateCount;
    }

    private int updateCustPostAddressDefault(String wxOpenId) {
        String upReviewSql = "UPDATE POST_WX_CUST_ADDRESS SET IS_DEFAULT = 0,UPDATE_TIME= NOW() WHERE WX_OPEN_ID = ?";
        return this.jdbcTemplate.update(upReviewSql,
                new Object[]{wxOpenId}
        );
    }

    /**
     * 删除配送地址
     *
     * @param custPostAddress
     * @return
     */
    public int deleteCustPostAddress(CustPostAddress custPostAddress) {
        int deleteCount = this.getJdbcTemplate().update("DELETE FROM POST_WX_CUST_ADDRESS WHERE ADDR_ID = ? AND WX_OPEN_ID = ?",
                new Object[]{
                        custPostAddress.getAddrId(),
                        custPostAddress.getWxOpenId()
                });
        return deleteCount;
    }
}