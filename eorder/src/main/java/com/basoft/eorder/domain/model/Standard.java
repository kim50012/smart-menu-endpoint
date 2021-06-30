package com.basoft.eorder.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Standard {


    private Long code;
    private String name;
    private String chnName;
    private int standardType;

    public Standard(Long standardId, String standardNameProp) {
        this.code = standardId;
        this.name = standardNameProp;
    }

    public Standard(Long id, String name, String s,int standardType) {
        this(id,name);
        this.chnName = s;
        this.standardType = standardType;

    }

    public Long code(){
        return code;
    }
    public String name(){
        return name;
    }

    public String chnName(){
        return this.chnName;
    }

    public int standardType() {
        return standardType;
    }

    public String name(int languageType){
        return name.split(",")[0];
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if(this == obj){
            return true;
        }
        if(!(obj instanceof Standard)){
            return false;
        }
        Standard other = (Standard) obj;
        return code.equals(other.code);
    }

    public static final class Standars {

        private List<Standard> productStandardArray;

        private Standars(List<Standard> sts){
            this.productStandardArray = new ArrayList<>(sts);
        }

        public static final Standars geNew(List<Standard> standards){
            return new Standars(standards);
        }

        @Override
        public int hashCode() {
            return productStandardArray.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj){
                return true;
            }

            if(!(obj instanceof Standars)){
                return false;
            }
            Standars other = (Standars) obj;
            return productStandardArray.equals(other.productStandardArray);
        }

        public String toContentsString() {

            return this.productStandardArray.stream()
                    .map(new Function<Standard, String>() {
                        @Override
                        public String apply(Standard standard) {
                            return standard.toString();
                        }
                    })
                    .reduce(new BinaryOperator<String>() {
                        @Override
                        public String apply(String s, String s2) {
                            return new StringBuilder(s).append(",").append(s2).toString();
                        }
                    })
                    .get();
        }

        public List<Standard> toList() {
            return Collections.synchronizedList(productStandardArray);
        }
    }
}
