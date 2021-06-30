package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.domain.StoreOptionRepository;
import com.basoft.eorder.domain.model.StoreOption;
import com.basoft.eorder.interfaces.query.StoreOptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;



/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:08 2019/7/8
 **/

@Repository
@Transactional
public class JdbcStoreOptionRepoImpl  extends BaseRepository implements StoreOptionRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;


    static final Long ROOT_ID =new Long(0);

    static final class StoreOptionWrapper {
        private Long pcId;
        private Long storeId;
        private String name;
        private int type;
        private Long parentId;
        private int status;
        private int depth;
        private String chnName;
        private boolean isRoot;

        public StoreOptionWrapper(Long pcId,Long storeId, String name, Long parentId, String chnName,boolean isRoot) {
            this.pcId = pcId;
            this.storeId = storeId;
            this.name = name;
            this.status = status;
            this.depth = depth;
            this.parentId = parentId;
            this.chnName = chnName;
            this.isRoot = isRoot;
        }


        public StoreOption.Builder build(List<StoreOptionWrapper> childrenBuilder) {

            StoreOption.Builder builder= new StoreOption.Builder()
                .pcId(this.pcId)
                .storeId(this.storeId)
                .name(this.name)
                .chnName(this.chnName)
                .type(this.type)
                .status(this.status);

            List<StoreOption.Builder> childOptionArray = new LinkedList<>();

            for(StoreOptionWrapper wrapper:childrenBuilder){
                if(wrapper.parentId.equals(this.pcId)){
                    final StoreOption.Builder builder1 = wrapper.build(childrenBuilder);
                    childOptionArray.add(builder1);
                }
            }
            return builder.children(childOptionArray);
        }
    }

    static final class JdbcCategoryWrapper {
        private Long pcId;
        private Long storeId;
        private String name;
        private int status;
        private Long parentId;
        private int depth;
        private String chnName;
        private int showIndex;
        private String description;
        private boolean isRoot;

        public Long getStoreId() {
            return storeId;
        }

        public void setStoreId(Long storeId) {
            this.storeId = storeId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getChnName() {
            return chnName;
        }

        public void setChnName(String chnName) {
            this.chnName = chnName;
        }



        public void setName(String name) {
            this.name = name;
        }

        public void setParentId(Long parentId) {
            this.parentId = parentId;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public int getShowIndex() {
            return showIndex;
        }

        public void setShowIndex(int showIndex) {
            this.showIndex = showIndex;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public StoreOption.Builder build() {
            return new StoreOption.Builder()
                .pcId(pcId)
                .name(name)
                .chnName(this.chnName)
                .description(this.description)
                .showIndex(this.showIndex)
                .status(status);
        }


        public Long getPcId() {
            return pcId;
        }

        public void setPcId(Long pcId) {
            this.pcId = pcId;
        }

        public String getName() {
            return name;
        }

        public Long getParentId() {
            return parentId;
        }

        public int getDepth() {
            return depth;
        }
    }


    static RowMapper<JdbcCategoryWrapper> getCategoryLineMapper(){
        return new RowMapper<JdbcCategoryWrapper>() {
            @Override
            public JdbcCategoryWrapper mapRow(ResultSet resultSet, int i) throws SQLException {
                JdbcCategoryWrapper cateWrapper = new JdbcCategoryWrapper();
                cateWrapper.pcId = resultSet.getLong("pc_id");
                cateWrapper.storeId = resultSet.getLong("store_id");
                cateWrapper.name = resultSet.getString("name");
                //cateWrapper.status = resultSet.getInt("status");
                cateWrapper.parentId = resultSet.getLong("parent_id");
                cateWrapper.depth = resultSet.getInt("depth");
                cateWrapper.chnName = resultSet.getString("chn_name");
                cateWrapper.status = resultSet.getInt("status");
                cateWrapper.isRoot = resultSet.getBoolean("is_root");
                //cateWrapper.description = resultSet.getString("description");
                return cateWrapper;
            }
        };
    }

    @Override
    public StoreOption getRootOption() {

        List<StoreOptionWrapper> rootArray = new LinkedList<>();
        List<StoreOptionWrapper> childrenBuilder = this.jdbcTemplate.query(
            " SELECT * FROM product_standard ca ",
            new RowMapper<StoreOptionWrapper>() {
                @Override
                public StoreOptionWrapper mapRow(ResultSet resultSet, int i) throws SQLException {
                    Long id = resultSet.getLong("pc_id");
                    Long storeId = resultSet.getLong("store_id");
                    String name = resultSet.getString("name");
                    Long parentId = resultSet.getLong("parent_id");
                    String chnName = resultSet.getString("chn_name");
                    boolean isRoot = resultSet.getBoolean("is_root");
                    return new StoreOptionWrapper(id,storeId,name,parentId,chnName,isRoot);
                }
            })
            .stream()
            .filter(new Predicate<StoreOptionWrapper>() {
                @Override
                public boolean test(StoreOptionWrapper optionWrapper) {
                    if(optionWrapper.isRoot){
                        rootArray.add(optionWrapper);
                        return false;
                    }
                    return true;
                }
            })
            .collect(Collectors.toList());



        return rootArray.get(0)
            .build(childrenBuilder)
            .build();
    }

    private static final String storeOptionSql = "SELECT id as pc_id,0 as store_id,name, parent_id,depth,chn_name,`status`,is_root\n" +
            " from base_product_standard bps where bps.status !=3 and bps.type=? or bps.type<1  or bps.type is null " +
        " UNION  SELECT  pc_id ,ps.store_id,ps.name,ps.parent_id,ps.depth,ps.chn_name,ps.`status`,ps.is_root \n" +
            " from product_standard ps" +
        "  where ps.store_id=? and ps.status !=3 ";
    @Override
    public StoreOptionDTO getOptionDto(Long storeId,int type) {

        Map<Long,JdbcCategoryWrapper> childMap = new HashMap<>();
        final List<JdbcCategoryWrapper> builders =
            this.jdbcTemplate
                .query(
                    storeOptionSql,new Object[]{type,storeId},
                    getCategoryLineMapper())
                .stream()
                .filter(new Predicate<JdbcCategoryWrapper>() {
                    @Override
                    public boolean test(JdbcCategoryWrapper jdbcCategoryWrapper) {
                        if (ROOT_ID.longValue() == jdbcCategoryWrapper.getPcId().longValue()) {
                            childMap.put(ROOT_ID, jdbcCategoryWrapper);
                            return false;
                        }
                        return true;
                    }
                })
                .collect(Collectors.toList());

        return newOptionDTO(childMap.get(ROOT_ID),builders);
    }


    public static StoreOptionDTO newOptionDTO(JdbcCategoryWrapper dtoWrapper, List<JdbcCategoryWrapper> builders) {
        StoreOptionDTO dto = new StoreOptionDTO();
        dto.setPcId(dtoWrapper.getPcId());
        dto.setStoreId(dtoWrapper.getStoreId());
        dto.setName(dtoWrapper.getName());
        dto.setChnName(dtoWrapper.getChnName());
        dto.setStatus(dtoWrapper.getStatus());
        dto.setDescription(dtoWrapper.getDescription());

        List<StoreOptionDTO> children = new LinkedList<>();
        for(JdbcCategoryWrapper war :builders){
//            System.out.println(war.getParentId()+":"+dto.getId());
            if(war.getParentId().longValue() == dto.getPcId().longValue()){
                children.add(newOptionDTO(war,builders));
            }
        }
        dto.setChildren(children);
        return dto;
    }




    @Override
    @Transactional
    public StoreOption getOption(Long id,Long storeId) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id as pc_id,0 as store_id,name, parent_id,depth,chn_name,`status`,is_root from base_product_standard bps where bps.id=? and status != 3");
        sql.append(" UNION ");
        sql.append("  SELECT  pc_id ,ps.store_id,ps.name,ps.parent_id,ps.depth,ps.chn_name,ps.`status`,ps.is_root  from product_standard ps");
        sql.append("  where ps.store_id=? and ps.pc_id=? ");

//        Map<Long,OptionWrapper> childMap = new HashMap<>();
        List<StoreOptionWrapper> rootArray = new LinkedList<>();
        List<StoreOptionWrapper> childrenBuilder = this.jdbcTemplate.query(
            sql.toString(),
            new Object[]{id, storeId,id},
            new RowMapper<StoreOptionWrapper>() {
                @Override
                public StoreOptionWrapper mapRow(ResultSet resultSet, int i) throws SQLException {
                    Long pcId = resultSet.getLong("pc_id");
                    Long storeId = resultSet.getLong("store_id");
                    String name = resultSet.getString("name");
                    Long parentId = resultSet.getLong("parent_id");
                    String chnName = resultSet.getString("chn_name");
                    boolean isRoot = resultSet.getBoolean("is_root");
                    return new StoreOptionWrapper(pcId,storeId,name,parentId,chnName,isRoot);
                }
            })
            .stream()
            .filter(new Predicate<StoreOptionWrapper>() {
                @Override
                public boolean test(StoreOptionWrapper optionWrapper) {
                    if(optionWrapper.pcId.equals(id)){
                        rootArray.add(optionWrapper);
                        return false;
                    }
                    return true;
                }
            })
            .collect(Collectors.toList());
        return rootArray.get(0)
            .build(childrenBuilder)
            .build();
    }



    @Override
    @Transactional
    public StoreOption saveOption(StoreOption option) {

        this.jdbcTemplate.update(
            "insert into product_standard (pc_id,store_id,parent_id,name,chn_name,show_index,description,base_extend,created) values(?,?,?,?,?,?,?,?,now())",
            option.pcId(),option.storeId(),option.parent().pcId(),option.name(),option.chnName(),option.showIndex(),option.description(),option.baseExtend());
        return option;
    }

    @Override
    @Transactional
    public StoreOption updateOption(StoreOption option) {
        this.jdbcTemplate.update(
            "update product_standard set name=?, chn_name=?,show_index=?,description=?,update_time=now() where pc_id = ?",
            new Object[]{option.name(), option.chnName(),option.showIndex(),option.description(),option.pcId()});
        return option;

    }
}
