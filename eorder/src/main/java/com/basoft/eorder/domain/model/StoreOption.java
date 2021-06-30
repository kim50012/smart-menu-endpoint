package com.basoft.eorder.domain.model;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @Author:DongXifu
 * @Description: 店铺规格
 * @Date Created in 上午11:11 2019/7/8
 **/
public class StoreOption {

    private Long pcId;
    private Long storeId;
    private String name;
    private String chnName;
    private int showIndex;
    private String description;
    private int status;
    private int baseExtend;
    private StoreOption parent;
    private List<StoreOption> children = new LinkedList<>();
    private int type;

    public Long pcId(){
        return this.pcId;
    }

    public Long storeId() {
        return this.storeId;
    }

    public String name(){
        return this.name;
    }

    public List<StoreOption> children(){
        return Collections.unmodifiableList(this.children);
    }

    public String chnName(){
        return this.chnName;
    }

    public int showIndex() {
        return this.showIndex;
    }

    public String description() {
        return this.description;
    }

    public int status() {
        return this.status;
    }

    public int baseExtend() {
        return this.baseExtend;
    }

    public StoreOption parent(){
        return this.parent;
    }

    public int type() {
        return this.type;
    }

    private void addChild(StoreOption childOption) {
        childOption.parent = this;
        children.add(childOption);
    }

    public StoreOption get(Long pcId){


        if(this.pcId.equals(pcId)){
            return this;
        }


        for(StoreOption child :this.children){

            StoreOption reChild = child.get(pcId);
            if(reChild != null){
                return reChild;
            }

        }
        return null;

    }




    public static final class Builder{

        private Long pcId;
        private Long storeId;
        private String name;
        private String chnName;
        private int showIndex;
        private String description;
        private StoreOption parent;
        private int type;
        private int baseExtend;
        private int status;
        private List<StoreOption.Builder> children = new LinkedList<>();


        public StoreOption.Builder pcId(Long pcId){
            this.pcId = pcId;
            return this;
        }


        public StoreOption.Builder storeId(Long storeId) {
            this.storeId = storeId;
            return this;
        }
        public StoreOption.Builder name(String name){
            this.name = name;
            return this;
        }


        public StoreOption.Builder chnName(String cname){
            this.chnName = cname;
            return this;
        }

        public StoreOption.Builder showIndex(int showIndex) {
            this.showIndex = showIndex;
            return this;
        }

        public StoreOption.Builder description(String description) {
            this.description = description;
            return this;
        }

        public StoreOption.Builder child(StoreOption.Builder option) {
            children.add(option);
            return this;
        }

        public StoreOption.Builder children(List<StoreOption.Builder> childArray){
            this.children.addAll(childArray);
            return this;
        }

        public StoreOption.Builder parent(StoreOption parentOption){
            this.parent = parentOption;
            return this;
        }

        public StoreOption.Builder type(int type) {
            this.type = type;
            return this;
        }

        public StoreOption.Builder status(int status) {
            this.status = status;
            return this;
        }
        public StoreOption.Builder baseExtend(int baseExtend) {
            this.baseExtend = baseExtend;
            return this;
        }


        public StoreOption build(){
            StoreOption option = new StoreOption();
            option.pcId = pcId;
            option.storeId = storeId;
            option.name = this.name;
            option.chnName = this.chnName;
            option.parent = this.parent;
            option.showIndex = this.showIndex;
            option.description = this.description;
            option.status = this.status;
            option.baseExtend = this.baseExtend;

            if (this.children != null && !this.children.isEmpty()){
                this.children.stream().forEach(new Consumer<StoreOption.Builder>() {
                    @Override
                    public void accept(StoreOption.Builder builder) {
                        option.addChild(builder.build());
                    }
                });
            }
            return option;

        }
    }
}
