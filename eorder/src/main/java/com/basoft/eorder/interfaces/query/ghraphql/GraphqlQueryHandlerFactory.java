/*
package com.basoft.eorder.interfaces.query.ghraphql;

import com.basoft.eorder.application.framework.QueryHandler;
import com.basoft.eorder.interfaces.query.ProductDTO;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class GraphqlQueryHandlerFactory {


    public static TypeDefinitionRegistry initTypeDefinitionRegistry(File file){
        SchemaParser parser = new SchemaParser();
//        TypeDefinitionRegistry registry = parser.parse(new File("./src/org/code/resources/schema.graphql"));
        TypeDefinitionRegistry registry = parser.parse(file);
        return registry;
    }


    protected Map<String,DataFetcher> initDataFetcher(){
        Map<String,DataFetcher> dataFetcherMap = new HashMap<>();
        dataFetcherMap.put("hello", new DataFetcher() {
            @Override
            public Object get(DataFetchingEnvironment dEnv) throws Exception {
                return "woonill";
            }
        });
        return dataFetcherMap;
    }

    public QueryHandler init(TypeDefinitionRegistry registry){

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
//                .type("Query", wiring -> wiring.dataFetcher("articles", articlesDataFetcher))
                .type("Query",new UnaryOperator<graphql.schema.idl.TypeRuntimeWiring.Builder>(){
                    @Override
                    public TypeRuntimeWiring.Builder apply(TypeRuntimeWiring.Builder builder) {
                        return builder.dataFetcher("productList", new DataFetcher() {
                            @Override
                            public Object get(DataFetchingEnvironment env) throws Exception {
                                String fname = env.getField().getName();
                                System.out.println("\n\n\n\n fname:"+fname);


                                ProductDTO p = new ProductDTO();
                                p.setId("A");
                                p.setName("Gimqi");

                                Map<String,ProductDTO> mm = new HashMap<>();
                                mm.put("productList",p);


                                return mm;
                            }
                        }).dataFetcher("hello", new DataFetcher() {
                            @Override
                            public Object get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
                                return "www";
                            }
                        });
                    }
                })
                .build();


        SchemaGenerator generator = new SchemaGenerator();
        GraphQLSchema schema = generator.makeExecutableSchema(registry, runtimeWiring);
        GraphQL graphQl = GraphQL.newGraphQL(schema).build();





        return new QueryHandler() {
            @Override
            public Object handle(String query) {
                final ExecutionResult execute = graphQl.execute(query);
                if(!execute.getErrors().isEmpty()){

                    execute.getErrors().stream().forEach(new Consumer<GraphQLError>() {
                        @Override
                        public void accept(GraphQLError graphQLError) {
                            System.out.println(graphQLError.getMessage());
                        }
                    });

                    return null;
                }
                return execute.getData();

            }
        };
    }
}
*/
