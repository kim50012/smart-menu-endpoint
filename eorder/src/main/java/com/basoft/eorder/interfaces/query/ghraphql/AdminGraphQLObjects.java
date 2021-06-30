package com.basoft.eorder.interfaces.query.ghraphql;

import graphql.Scalars;
import graphql.language.ObjectTypeDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;

import java.util.function.Consumer;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

class AdminGraphQLObjects {






    final static GraphQLObjectType BASE_CATEGORY_TYPE_LIST = GraphQLObjectType.newObject()
            .name("RootCategory")
            .field(newFieldDefinition()
                    .name("id")
                    .description("The name of the character.")
                    .type(Scalars.GraphQLLong))
            .field(newFieldDefinition().name("name").type(Scalars.GraphQLString))
            .field(newFieldDefinition().name("chnName").type(Scalars.GraphQLString))
            .build();


    /**
     * @author woonill
     * Category -> GraphQLObject
     */
/*    final static GraphQLObjectType CATEGORY_TYPE_LIST = GraphQLObjectType.newObject(BASE_CATEGORY_TYPE_LIST)
            .name("CategoryVO2")
            .field(newFieldDefinition().name("children").type(GraphQLList.list(GraphQLCompositeType)))
            .build();*/


    final static GraphQLObjectType CATEGORY_TYPE_LIST_2 = GraphQLObjectType.newObject(BASE_CATEGORY_TYPE_LIST)
            .definition(ObjectTypeDefinition.newObjectTypeDefinition().build().transform(new Consumer<ObjectTypeDefinition.Builder>() {
                @Override
                public void accept(ObjectTypeDefinition.Builder builder) {

                }
            }))
            .name("CategoryVO")
            .field(newFieldDefinition().name("children").type(GraphQLList.list(Scalars.GraphQLString)))
            .build();



/*    final static GraphQLObjectType CATEGORY_TYPE_LIST_2 = GraphQLObjectType.newObject(BASE_CATEGORY_TYPE_LIST)
            .definition(ObjectTypeDefinition.newObjectTypeDefinition().build().transform(new Consumer<ObjectTypeDefinition.Builder>() {
                @Override
                public void accept(ObjectTypeDefinition.Builder builder) {

                }
            }))
            .name("CategoryVO")
            .field(newFieldDefinition().name("children").type(GraphQLList.list(
                    new GraphQLUnionType.Builder()
                            .name("CATEGORY_TYPE")
                            .possibleType(CATEGORY_TYPE_LIST)
                            .typeResolver(new TypeResolver() {
                                @Override
                                public GraphQLObjectType getType(TypeResolutionEnvironment env) {

                                    System.out.println("\n\n\n start");
                                    System.out.println("ENV:"+env.getObject());

                                    return CATEGORY_TYPE_LIST_2;
                                }
                            })
                            .build())))
            .build();*/


/*    static class GraphQLCategoryType implements GraphQLType, GraphQLOutputType {
        @Override
        public String getName() {
            return "LoopType";
        }
        @Override
        public TraversalControl accept(TraverserContext<GraphQLType> traverserContext, GraphQLTypeVisitor graphQLTypeVisitor) {
            graphQLTypeVisitor.visitGraphQLCompositeType()
            return null;
        }
    }*/

}
