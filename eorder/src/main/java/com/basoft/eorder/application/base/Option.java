package com.basoft.eorder.application.base;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class Option implements java.io.Serializable{


    private static final long serialVersionUID = 4513690396477375692L;
    private Long id;
    private String name;
    private String chnName;
    private String group;
    private Option parent;
    private List<Option> children = new LinkedList<>();
    private int type;
    private int status;

    public Long id(){
        return this.id;
    }

    public String name(){
        return this.name;
    }

    public List<Option> children(){
        return Collections.unmodifiableList(this.children);
    }

    public String chnName(){
        return this.chnName;
    }

    public Option parent(){
        return this.parent;
    }

    public int type() {
        return this.type;
    }

    public int status(){
        return this.status;
    }

    private void addChild(Option childOption) {
        childOption.parent = this;
        children.add(childOption);
    }

    public Option get(Long id){


        if(this.id.equals(id)){
            return this;
        }


        for(Option child :this.children){

            Option reChild = child.get(id);
            if(reChild != null){
                return reChild;
            }

        }
        return null;

    }




    public static final class Builder{

        private Long id;
        private String name;
        private String chnName;
        private String group;
        private Option parent;
        private int type;
        private int status;
        private List<Option.Builder> children = new LinkedList<>();


        public Builder id(Long id){
            this.id = id;
            return this;
        }


        public Builder name(String name){
            this.name = name;
            return this;
        }


        public Builder chnName(String cname){
            this.chnName = cname;
            return this;
        }

        public Builder child(Option.Builder option) {
            children.add(option);
            return this;
        }

        public Builder children(List<Option.Builder> childArray){
            this.children.addAll(childArray);
            return this;
        }

        public Builder parent(Option parentOption){
            this.parent = parentOption;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }


        public Option build(){
            Option option = new Option();
            option.id = id;
            option.name = this.name;
            option.chnName = this.chnName;
            option.parent = this.parent;
            option.type = this.type;
            option.status = this.status;
            if (this.children != null && !this.children.isEmpty()){
                this.children.stream().forEach(new Consumer<Builder>() {
                    @Override
                    public void accept(Option.Builder builder) {
                        option.addChild(builder.build());
                    }
                });
            }
            return option;

        }
    }


}
