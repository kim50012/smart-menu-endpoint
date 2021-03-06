package com.basoft.eorder.foundation.jdbc.base;

import com.basoft.eorder.application.base.query.AdminQueryFacade;
import com.basoft.eorder.application.base.query.CategoryDTO;
import com.basoft.eorder.interfaces.query.OptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.basoft.eorder.foundation.jdbc.base.JbcBaseServiceImpl.*;

@Slf4j
@Component
public class JdbcBaseQueryFacade implements AdminQueryFacade {
    private JdbcTemplate jdbcTemplate;

    public JdbcBaseQueryFacade(){}

    @Autowired
    public JdbcBaseQueryFacade(DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public CategoryDTO getStoreCategory(Long storeId) {
        Map<Long,JdbcCategoryWrapper> childMap = new HashMap<>();
        final List<JdbcCategoryWrapper> builders =
            this.jdbcTemplate
                .query(
                    "SELECT  * from base_category bc \n" +
                        "where bc.status !=3 and bc.id in (select scm.category_id from store_category_map scm where store_id= ?) or is_root=1 \n" +
                        "UNION SELECT bc1.* from base_category bc LEFT JOIN   base_category bc1 on bc.parent_id=bc1.id\n" +
                        "where bc.status !=3 and bc.id in (select scm.category_id from store_category_map scm where store_id=?)"
                    ,new Object[]{storeId,storeId}
                    ,getCategoryLineMapper())
                .stream()
                .filter(new Predicate<JdbcCategoryWrapper>() {
                    @Override
                    public boolean test(JdbcCategoryWrapper jdbcCategoryWrapper) {
                        if (JbcBaseServiceImpl.ROOT_ID.longValue() == jdbcCategoryWrapper.getId().longValue()) {
                            childMap.put(ROOT_ID, jdbcCategoryWrapper);
                            return false;
                        }
                        if (jdbcCategoryWrapper.getFunctionType() == 2) {
                            return false;
                        }
                        return true;
                    }
                })
                .collect(Collectors.toList());
        return newCategoryDTO(childMap.get(ROOT_ID),builders);
    }

    /**
     * 20190808?????????????????????
     * ?????????getRootCategory(String type, String functionType)??????
     *
     * @param type
     * @return
     */
    // @Override
    @Deprecated
    public CategoryDTO getRootCategory(String type) {
        Map<Long,JdbcCategoryWrapper> childMap = new HashMap<>();
        final List<JdbcCategoryWrapper> builders =
                this.jdbcTemplate.query(" SELECT * FROM base_category ",getCategoryLineMapper())
                                    .stream()
                                    .filter(new Predicate<JdbcCategoryWrapper>() {
                                            @Override
                                            public boolean test(JdbcCategoryWrapper jdbcCategoryWrapper) {
                                                if (JbcBaseServiceImpl.ROOT_ID.longValue() == jdbcCategoryWrapper.getId().longValue()) {
                                                    childMap.put(ROOT_ID, jdbcCategoryWrapper);
                                                    return false;
                                                }else if(!String.valueOf(jdbcCategoryWrapper.getType()).equals(type)&&StringUtils.isNotEmpty(type)){
                                                    return false;
                                                }
                                                return true;
                                            }
                                        }
                                    )
                        .collect(Collectors.toList());

        return newCategoryDTO(childMap.get(ROOT_ID),builders);
    }

    /**
     * ADMIN-??????????????????|??????????????????|??????????????????
     *
     * @param type         ???????????? 1-?????? 2-?????? 3-?????? 4-??????
     * @param functionType ????????????????????? 1-???????????? 2-???????????? 3-????????????
     * @return
     */
    @Override
    public CategoryDTO getRootCategory(String type, String functionType) {
        Map<Long,JdbcCategoryWrapper> childMap = new HashMap<>();
        // ?????????????????????????????????????????????????????????ROOT,ROOT?????????????????????????????????????????????0
        List<JdbcCategoryWrapper> builders =
                this.jdbcTemplate.query("SELECT ID,PARENT_ID,NAME,DESCRIPTION,CREATED,DEPTH, STATUS,CHN_NAME,IS_ROOT, TYPE,FUNCTION_TYPE,CATEGORY_TYPE,MANAGE_TYPE,UPDATE_TIME\n" +
                                " FROM BASE_CATEGORY WHERE (TYPE = 0 OR TYPE = ?) AND (FUNCTION_TYPE = 0 OR FUNCTION_TYPE = ?) and status !=3 \n"
                                ,new Object[]{type,functionType}
                                ,getCategoryLineMapper())
                        .stream()
                        .filter(new Predicate<JdbcCategoryWrapper>() {
                                    @Override
                                    public boolean test(JdbcCategoryWrapper jdbcCategoryWrapper) {
                                        if (JbcBaseServiceImpl.ROOT_ID.longValue() == jdbcCategoryWrapper.getId().longValue()) {
                                            childMap.put(ROOT_ID, jdbcCategoryWrapper);
                                            return false;
                                        }
                                        return true;
                                    }
                                }
                        )
                        .collect(Collectors.toList());
        CategoryDTO categoryDTO = newCategoryDTO(childMap.get(ROOT_ID),builders);
        return categoryDTO;
    }

    public static CategoryDTO newCategoryDTO(JdbcCategoryWrapper dtoWrapper, List<JdbcCategoryWrapper> builders) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(dtoWrapper.getId());
        dto.setType(dtoWrapper.getType());
        dto.setName(dtoWrapper.getName());
        dto.setChnName(dtoWrapper.getChnName());
        dto.setFunctionType(dtoWrapper.getFunctionType());
        dto.setCategoryType(dtoWrapper.getCategoryType());
        dto.setManageType(dtoWrapper.getManageType());

        List<CategoryDTO> children = new LinkedList<>();
        for(JdbcCategoryWrapper war :builders){
            if(war.getParentId().longValue() == dto.getId().longValue()){
                children.add(newCategoryDTO(war,builders));
            }
        }
        dto.setChildren(children);
        return dto;
    }

    /**
     * ADMIN-??????????????????|??????????????????
     *
     * @param type         ???????????? 1-?????? 2-?????? 3-?????? 4-??????
     * @param functionType ????????????????????? 2-???????????? 3-????????????
     * @param manageType   ??????????????? 1-Admin CMS 2-Manager CMS 0-??????Admin CMS???Manager CMS??????
     * @return
     */
    public CategoryDTO getTags(String type, String functionType, String manageType){
        /*// ??????SQL??????
        StringBuilder sbSQL = new StringBuilder("SELECT ID,PARENT_ID,NAME,DESCRIPTION,CREATED,DEPTH, STATUS,CHN_NAME,IS_ROOT, TYPE,FUNCTION_TYPE,CATEGORY_TYPE,MANAGE_TYPE,UPDATE_TIME FROM BASE_CATEGORY WHERE (TYPE = 0 OR TYPE = ?) AND (FUNCTION_TYPE = 0 OR FUNCTION_TYPE = ?)");

        // ??????Admin CMS???????????????
        if(CommonConstants.TAG_MANAGE_ADMIN_CMS.equals(manageType)){
            sbSQL.append(" AND MANAGE_TYPE = 1");
        }
        // ??????Manager CMS???????????????
        else if(CommonConstants.TAG_MANAGE_MANAGER_CMS.equals(manageType)){
            sbSQL.append(" AND MANAGE_TYPE = 2");
        }
        // ??????Admin CMS???Manager CMS???????????????
        else if(CommonConstants.TAG_MANAGE_ALL_CMS.equals(manageType)){
            sbSQL.append(" AND MANAGE_TYPE = 3");
        }
        log.info("???????????????SQL?????????[type][functionType][manageType]>>>[" + type + "][" + functionType + "][" + manageType + "]>>>" + sbSQL.toString());*/

        log.info("???????????????SQL?????????[type][functionType][manageType]>>>[" + type + "][" + functionType + "][" + manageType + "]");
        Map<Long,JdbcCategoryWrapper> childMap = new HashMap<>();
        // ?????????????????????????????????????????????????????????ROOT,ROOT?????????????????????????????????????????????0
        List<JdbcCategoryWrapper> builders =
                this.jdbcTemplate.query("SELECT ID,PARENT_ID,NAME,DESCRIPTION,CREATED,DEPTH, STATUS,CHN_NAME,IS_ROOT, TYPE,FUNCTION_TYPE,CATEGORY_TYPE,MANAGE_TYPE,UPDATE_TIME FROM BASE_CATEGORY WHERE (TYPE = 0 OR TYPE = ?) AND (FUNCTION_TYPE = 0 OR FUNCTION_TYPE = ?) AND (MANAGE_TYPE = 0 OR MANAGE_TYPE = ?)"
                        ,new Object[]{type,functionType,manageType}
                        ,getCategoryLineMapper())
                        .stream()
                        .filter(new Predicate<JdbcCategoryWrapper>() {
                                    @Override
                                    public boolean test(JdbcCategoryWrapper jdbcCategoryWrapper) {
                                        if (JbcBaseServiceImpl.ROOT_ID.longValue() == jdbcCategoryWrapper.getId().longValue()) {
                                            childMap.put(ROOT_ID, jdbcCategoryWrapper);
                                            return false;
                                        }
                                        return true;
                                    }
                                }
                        )
                        .collect(Collectors.toList());
        CategoryDTO categoryDTO = newCategoryDTO(childMap.get(ROOT_ID),builders);
        return categoryDTO;
    }

    // @Override
    public OptionDTO getRootOption(String type) {
        Map<Long,JdbcCategoryWrapper> childMap = new HashMap<>();
        final List<JdbcCategoryWrapper> builders =
                this.jdbcTemplate
                        .query(
                                " SELECT * FROM base_product_standard bps where bps.status != 3",
                                // getCategoryLineMapper())
                                getCategoryLineMapper4RootOption())
                        .stream()
                        .filter(new Predicate<JdbcCategoryWrapper>() {
                            @Override
                            public boolean test(JdbcCategoryWrapper jdbcCategoryWrapper) {
                                if (JbcBaseServiceImpl.ROOT_ID.longValue() == jdbcCategoryWrapper.getId().longValue()) {
                                    childMap.put(ROOT_ID, jdbcCategoryWrapper);
                                    return false;
                                }else if(!String.valueOf(jdbcCategoryWrapper.getType()).equals(type)&&StringUtils.isNotEmpty(type)&&jdbcCategoryWrapper.getType()>0){
                                    return false;
                                }
                                return true;
                            }
                        })
                        .collect(Collectors.toList());

        return newOptionDTO(childMap.get(ROOT_ID),builders);
    }

    public static OptionDTO newOptionDTO(JdbcCategoryWrapper dtoWrapper, List<JdbcCategoryWrapper> builders) {
        OptionDTO dto = new OptionDTO();
        dto.setId(dtoWrapper.getId());
        dto.setName(dtoWrapper.getName());
        dto.setChnName(dtoWrapper.getChnName());
        dto.setStatus(dto.getStatus());
        dto.setType(dtoWrapper.getType());

        List<OptionDTO> children = new LinkedList<>();
        for(JdbcCategoryWrapper war :builders){
//            System.out.println(war.getParentId()+":"+dto.getId());
            if(war.getParentId().longValue() == dto.getId().longValue()){
                children.add(newOptionDTO(war,builders));
            }
        }
        dto.setChildren(children);
        return dto;
    }
}
