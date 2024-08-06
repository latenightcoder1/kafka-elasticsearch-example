package org.example.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.host:localhost}")
    private String elasticsearchHost;

    @Value("${spring.elasticsearch.port:9200}")
    private int elasticsearchPort;

    @Value("${spring.elasticsearch.username}")
    private String elasticsearchUsername;

    @Value("${spring.elasticsearch.password}")
    private String elasticsearchPassword;

    @Value("${spring.elasticsearch.scheme}")
    private String scheme;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials(elasticsearchUsername, elasticsearchPassword));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(elasticsearchHost, elasticsearchPort, scheme))
            .setHttpClientConfigCallback(
                httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(
                    credentialsProvider));

        RestClient restClient = builder.build();

        // Customize ObjectMapper to ignore unknown properties
        ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT,
                false).setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ElasticsearchTransport transport = new RestClientTransport(restClient,
            new JacksonJsonpMapper(objectMapper));
        return new ElasticsearchClient(transport);
    }


}
