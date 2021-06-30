package com.basoft.eorder.application.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Category {
    private static final int ROOT_DEPTH = 0;

    public enum Status {
        NORMAL(0), OPEN(1), CLOSED(2), DELETE(3);

        private int code;

        private Status(int code) {
            this.code = code;
        }

        public int code() {
            return code;
        }

        private static Status get(int code) {
            for (Status status : Status.values()) {
                if (status.code == code) {
                    return status;
                }
            }
            return null;
        }
    }

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private Category parent;
    private int type;
    private int depth;
    private Status status = Status.NORMAL;
    private String chnName;
    private Set<Category> children = new LinkedHashSet<>();
    private int functionType;
    private int categoryType;
    private int manageType;

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Category getParent() {
        return parent;
    }

    public int type() {
        return type;
    }

    public int depth() {
        return this.depth;
    }

    public Status status() {
        return this.status;
    }

    public String chnName() {
        return this.chnName;
    }

    public int functionType() {
        return functionType;
    }

    public int categoryType() {
        return categoryType;
    }

    public int manageType() {
        return manageType;
    }

    public boolean isRoot() {
        return this.depth == ROOT_DEPTH;
    }

    private void addChild(Category child) {
        synchronized (children) {
            this.children.add(child);
            child.parent = this;
        }
    }

    public Collection<Category> getChildren() {
        return Collections.unmodifiableSet(this.children);
    }

    public Category getCategory(Long id) {
        if (this.id.equals(id)) {
            return this;
        }
        if (!this.children.isEmpty()) {
            for (Category child : this.children) {
                Category targetCategory = child.getCategory(id);
                if (targetCategory != null) {
                    return targetCategory;
                }
            }
        }
        return null;
    }

    /*public Set<Category> getChildren() {
        return children;
    }
    Category addChild(Category category){
        return this;
    }*/

    public static class Builder {
        private Long id;
        private String name;
        private int type;
        private String chnName;
        private Category parent;
        private List<Category.Builder> children = new LinkedList<>();
        private int depth;
        private Status status;
        private int functionType;
        private int categoryType;
        private int manageType;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder parent(Category category) {
            this.parent = category;
            return this;
        }

        public Builder child(Builder builder) {
            this.children.add(builder);
            return this;
        }

        public Builder depth(int depth) {
            this.depth = depth;
            return this;
        }

        public Builder chnName(String chnName) {
            this.chnName = chnName;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder statusInt(int status) {
            this.status = Status.get(status);
            return this;
        }

        public Builder functionType(int functionType) {
            this.functionType = functionType;
            return this;
        }

        public Builder categoryType(int categoryType) {
            this.categoryType = categoryType;
            return this;
        }

        public Builder manageType(int manageType) {
            this.manageType = manageType;
            return this;
        }

        public Category build() {
            Category category = new Category();
            category.id = this.id;
            category.name = this.name;
            category.type = this.type;
            category.parent = this.parent;
            category.chnName = this.chnName;
            category.functionType = this.functionType;
            category.categoryType = this.categoryType;
            category.manageType = this.manageType;

            if (this.status != null) {
                category.status = this.status;
            }

            if (this.parent != null) {
                category.depth = this.parent.depth + 1;
            } else {
                category.depth = this.depth;
            }

            if (this.children != null && !this.children.isEmpty()) {
                this.children.stream().forEach(new Consumer<Builder>() {
                    @Override
                    public void accept(Builder builder) {
                        category.addChild(builder.build());
                    }
                });
            }
            return category;
        }
    }
}
