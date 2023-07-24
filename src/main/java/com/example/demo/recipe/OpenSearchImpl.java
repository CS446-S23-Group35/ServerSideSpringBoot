package com.example.demo.recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch._types.query_dsl.FieldValueFactorScoreFunction;
import org.opensearch.client.opensearch._types.query_dsl.FunctionBoostMode;
import org.opensearch.client.opensearch._types.query_dsl.FunctionScore;
import org.opensearch.client.opensearch._types.query_dsl.FunctionScoreMode;
import org.opensearch.client.opensearch._types.query_dsl.FunctionScoreQuery;
import org.opensearch.client.opensearch._types.query_dsl.MatchQuery;
import org.opensearch.client.opensearch._types.query_dsl.MultiMatchQuery;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;

public class OpenSearchImpl implements Searcher{
    private OpenSearchClient client;

    public OpenSearchImpl(String addr) {
        final HttpHost host = new HttpHost(addr, 9200, "http");
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        //Only for demo purposes. Don't specify your credentials in code.
        credentialsProvider.setCredentials(new AuthScope(host), new UsernamePasswordCredentials("admin", "admin"));

        //Initialize the client with SSL and TLS enabled
        final RestClient restClient = RestClient.builder(host).
            setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
            }
        ).build();

        final OpenSearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        client = new OpenSearchClient(transport);
    }


    // Perform this query on the index "recipes" for each ingredient in excludedIngredients:
    // {
    //     "query": {
    //       "bool": {
    //         "must_not": [
    //           {"match": {"ingredients.name": "excludedIngredient1"}},
    //           ...
    //         ],
    //         "should": {
    //           "multi_match": {
    //             "query": "name",
    //             "fields": ["title^8", "description^4", "tags", "ingredients.name"],
    //             "fuzziness": "AUTO"
    //           }
    //         }
    //       }
    //     }
    //   }

    @Override
    public List<Recipe> SearchByName(String name, Filters filters) {
        MultiMatchQuery multiMatchQuery = new MultiMatchQuery.Builder()
        .query(name)
        .fields("title^8", "description^4", "tags", "ingredients.name")
        .fuzziness("AUTO")
        .build();

        BoolQuery boolQuery = createRestrictionFilter(filters)
        .should(multiMatchQuery._toQuery())
        .build();

        return doSearch(boolQuery._toQuery());
    }

    // Perform this query on the index "recipes" for each ingredient in excludedIngredients:
    // {
    //     "query": {
    //       "function_score": {
    //         "query": {
    //           "bool": {
    //             "must_not": [
    //               {"match": {"ingredients.name": "excludeIngredient1"}},
    //               ...
    //             ]
    //           }
    //         },
    //         "functions": [
    //           {
    //             "filter": { "match": { "ingredients.name": "ingredient" } },
    //             "weight": 2
    //           },
    //           ...
    //         ],
    //         "score_mode": "sum",
    //         "boost_mode": "replace"
    //       }
    //     }
    //   }

    @Override
    public List<Recipe> SearchByInventory(Filters filters) {
        BoolQuery boolQuery = createRestrictionFilter(filters).build();

        double baseWeight = 2.0;
        double expiringWeight = 1.0 + baseWeight * filters.inventoryIngredients.size();

        List<FunctionScore> functions = new ArrayList<>();
        for (String ingredient : filters.inventoryIngredients) {
            functions.add(createConstantIngredientScore(ingredient, baseWeight));
        }

        for (String ingredient : filters.expiringIngedients) {
            functions.add(createConstantIngredientScore(ingredient, expiringWeight));   
        }

        FunctionScoreQuery fsq =  new FunctionScoreQuery.Builder()
        .functions(functions)
        .query(boolQuery._toQuery())
        .scoreMode(FunctionScoreMode.Sum).boostMode(FunctionBoostMode.Replace).build();

        return doSearch(fsq._toQuery());
    }

    // Perform a query and performs it on the index "recipes".
    private List<Recipe> doSearch(Query query) {
        List<Recipe> recipes = new ArrayList<Recipe>();
        try {
            SearchResponse<Recipe> searchResponse = client.search(s -> s.index("recipes").query(query), Recipe.class);
            System.out.println("Search response: " + searchResponse.hits().total().value());
            for (int i = 0; i < searchResponse.hits().total().value(); i++) {
                recipes.add(searchResponse.hits().hits().get(i).source());
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getCause());
        }
        return recipes;
    }

    // createRestrictionFilter makes a BoolQuery builder and adds to it must_not queries for all excluded ingredients
    // and must queries for dietary restrictions.
    private BoolQuery.Builder createRestrictionFilter(Filters filters) {
        List<Query> mustNotQueries = new ArrayList<>();
        for (String ingredient : filters.excludedIngredients) {
            MatchQuery matchQuery = new MatchQuery.Builder()
            .field("ingredients.name")
            .query(FieldValue.of(ingredient))
            .build();

            mustNotQueries.add(matchQuery._toQuery());
        }

        List<Query> mustQueries = new ArrayList<>();
        for (DietaryRestrictions r : filters.dietaryRestrictions) {
            //{"match": {"metadata.dietary_restrictions.name": true}}

            MatchQuery matchQuery = new MatchQuery.Builder()
            .field("metadata.dietary_restrictions." + r.getBooleanName())
            .query(FieldValue.of(true))
            .build();

            mustQueries.add(matchQuery._toQuery());
        }

        return new BoolQuery.Builder().mustNot(mustNotQueries).must(mustQueries);
    }

    // A constant score function does not exist in this SDK for some reason ¯\_(ツ)_/¯ 
    private static FieldValueFactorScoreFunction constantScoreFunction = new FieldValueFactorScoreFunction.Builder()
        .missing(1.0)
        .factor(1.0)
        .field("field")
        .build();

    // createConstantIngredientScore creates a function score query that boosts recipes with the given ingredient
    // by the given weight.
    private FunctionScore createConstantIngredientScore(String ingredient, double weight) {
        MatchQuery matchQuery = new MatchQuery.Builder()
            .field("ingredients.name")
            .query(FieldValue.of(ingredient))
            .build();

            return new FunctionScore.Builder()
            .filter(matchQuery._toQuery())
            .weight(weight)
            .fieldValueFactor(constantScoreFunction)
            .build();
    }
}
