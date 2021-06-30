package com.basoft.eorder;

import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLUnionType;

import java.util.Map;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

class Dog4Union{
    private String name;
    private Integer legs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLegs() {
        return legs;
    }

    public void setLegs(Integer legs) {
        this.legs = legs;
    }
}


class Fish4Union{
    private String name;
    private String tailColor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTailColor() {
        return tailColor;
    }

    public void setTailColor(String tailColor) {
        this.tailColor = tailColor;
    }
}


public class GraphQL_union {
    public static void main(String[] args) {
        //服务端示例数据
        Dog4Union dog=new Dog4Union();
        dog.setName("dog");
        dog.setLegs(4);

        Fish4Union fish=new Fish4Union();
        fish.setName("fish");
        fish.setTailColor("red");

        Object[] anmials={dog,fish};

        //定义GraphQL类型
        GraphQLObjectType dogType = GraphQLObjectType.newObject()//定义Dog类型
                .name("Dog4Union")
                .field(newFieldDefinition().name("name").type(GraphQLString))
                .field(newFieldDefinition().name("legs").type(Scalars.GraphQLInt))
                .build();

        GraphQLObjectType fishType = GraphQLObjectType.newObject()//定义Fish类型
                .name("Fish4Union")
                .field(newFieldDefinition().name("name").type(GraphQLString))
                .field(newFieldDefinition().name("tailColor").type(GraphQLString))
                .build();

        GraphQLUnionType animalUnion = GraphQLUnionType.newUnionType()//定义联合类型(union)
                .name("IAnimal")
                .possibleType(dogType)
                .possibleType(fishType)
                .description("动物联合")
                .typeResolver(env -> {
                    if(env.getObject() instanceof Dog4Union){
                        return dogType;
                    }if(env.getObject() instanceof Fish4Union){
                        return fishType;
                    }
                    return  null;
                })
                .build();


        //定义暴露给客户端的查询query api
        GraphQLObjectType queryType = GraphQLObjectType.newObject()
                .name("animalQuery")
                .field(newFieldDefinition()
                        .type(new GraphQLList(animalUnion))
                        .name("animals")
                        .dataFetcher(evn -> {
                            return anmials;
                        }))
                .build();

        //创建Schema
        GraphQLSchema schema = GraphQLSchema.newSchema()
                .query(queryType)
                .build();

        //测试输出
        GraphQL graphQL = GraphQL.newGraphQL(schema).build();
        //查询动物列表
        Map<String, Object> result = graphQL.execute("{animals{... on Dog4Union{name,legs} ... on Fish4Union{name,tailColor}}}").getData();
        System.out.println(result);
    }
}
