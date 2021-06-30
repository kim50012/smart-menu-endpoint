package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.MenuItem;
import com.basoft.eorder.domain.model.Product;
import com.basoft.eorder.domain.model.ProductGroup;
import com.basoft.eorder.domain.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:41 2018/12/27
 **/
public class SaveProductGroup implements Command {

    static final String NAME = "saveProductGroup";

    private Logger logger = LoggerFactory.getLogger(SaveProductGroup.class.getName());

    private Long id;
    //modify by woonill 2018/12/27 使用了Menu的概念导致的设计变化
//    private Long categoryId;
    private String name;
    private String chnName;
    private int showIndex;
    private ProductGroup.Status status;

    private List<MenuItemWrapper> items = new LinkedList<>();


/*    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }

    public ProductGroup.Status getStatus() {
        return status;
    }

    public void setStatus(ProductGroup.Status status) {
        this.status = status;
    }

    /*
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
*/

/*    public List<MenuItemWrapper> getItems() {
        return items;
    }

    public void setItems(List<MenuItemWrapper> items) {
        this.items = items;
    }*/

    public List<MenuItemWrapper> getItems() {
        return items;
    }

    public void setItems(List<MenuItemWrapper> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductGroup build(Store store, Long id, List<Product> productListofStore) {

        if(store == null){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }


        if(id == null){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        List<MenuItem> menuItemList =
                this.items.stream()
                        .map((s) -> {
                            Product product = productListofStore.stream().filter( (prd) -> prd.id().equals(s.productId)).findFirst().orElseGet(()-> null);
                            if(product != null){
                                return s.build(product);
                            }
                            return null;
                        })
                        .filter((f) -> f != null)
                        .collect(Collectors.toList());


        if(menuItemList.size() != this.items.size()){
            logger.warn("Miss match MenuItem");
            throw new BizException(ErrorCode.PARAM_INVALID,"产品无法相对应");
        }


        return new ProductGroup.Builder()
            .id(id)
            .name(this.name)
            .storeId(store.id())
            .chnName(this.chnName)
            .items(menuItemList)
            .showIndex(this.showIndex)
            .status(this.status)
            .build();
    }



    public static final class MenuItemWrapper{

        private Long productId;
        private Integer showIndex;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }


        public Integer getShowIndex() {
            return showIndex;
        }

        public void setShowIndex(Integer showIndex) {
            this.showIndex = showIndex;
        }

        private MenuItem build(Product product) {
            return new MenuItem(this.productId,product.category().id(),showIndex);
        }
    }
}
