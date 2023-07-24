package com.example.demo.recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
import org.opensearch.client.opensearch._types.Script;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch._types.query_dsl.FieldValueFactorScoreFunction;
import org.opensearch.client.opensearch._types.query_dsl.FunctionBoostMode;
import org.opensearch.client.opensearch._types.query_dsl.FunctionScore;
import org.opensearch.client.opensearch._types.query_dsl.FunctionScoreMode;
import org.opensearch.client.opensearch._types.query_dsl.FunctionScoreQuery;
import org.opensearch.client.opensearch._types.query_dsl.MatchQuery;
import org.opensearch.client.opensearch._types.query_dsl.MultiMatchQuery;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch._types.query_dsl.QueryBuilders;
import org.opensearch.client.opensearch._types.query_dsl.ScoreFunctionBase;
import org.opensearch.client.opensearch._types.query_dsl.ScriptScoreFunction;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.opensearch.client.util.ObjectBuilder;

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
        List<Query> mustNotQueries = new ArrayList<>();
        for (String ingredient : filters.excludedIngredients) {
            MatchQuery matchQuery = new MatchQuery.Builder()
            .field("ingredients.name")
            .query(FieldValue.of(ingredient))
            .build();

            mustNotQueries.add(matchQuery._toQuery());
        }

        MultiMatchQuery multiMatchQuery = new MultiMatchQuery.Builder()
        .query(name)
        .fields("title^8", "description^4", "tags", "ingredients.name")
        .fuzziness("AUTO")
        .build();

        BoolQuery boolQuery = new BoolQuery.Builder()
        .mustNot(mustNotQueries)
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
        List<Query> mustNotQueries = new ArrayList<>();
        for (String ingredient : filters.excludedIngredients) {
            MatchQuery matchQuery = new MatchQuery.Builder()
            .field("ingredients.name")
            .query(FieldValue.of(ingredient))
            .build();

            mustNotQueries.add(matchQuery._toQuery());
        }
        BoolQuery boolQuery = new BoolQuery.Builder().mustNot(mustNotQueries).build();

        FieldValueFactorScoreFunction scoreFunc = new FieldValueFactorScoreFunction.Builder()
        .missing(1.0)
        .factor(1.0)
        .field("rating")
        .build();

        List<FunctionScore> functions = new ArrayList<>();
        for (String ingredient : filters.inventoryIngredients) {
            MatchQuery matchQuery = new MatchQuery.Builder()
            .field("ingredients.name")
            .query(FieldValue.of(ingredient))
            .build();

            functions.add(
                new FunctionScore.Builder()
                .filter(matchQuery._toQuery())
                .weight(2.0)
                .fieldValueFactor(scoreFunc)
                .build()
            );
        }

        for (String ingredient : filters.expiringIngedients) {
            MatchQuery matchQuery = new MatchQuery.Builder()
            .field("ingredients.name")
            .query(FieldValue.of(ingredient))
            .build();

            functions.add(
                new FunctionScore.Builder()
                .filter(matchQuery._toQuery())
                .weight(1.0 + 2.0 * filters.inventoryIngredients.size())
                .fieldValueFactor(scoreFunc)
                .build()
            );
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
}
