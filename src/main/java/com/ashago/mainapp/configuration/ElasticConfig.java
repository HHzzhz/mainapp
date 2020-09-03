package com.ashago.mainapp.configuration;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.ashago.mainapp.esrepository")
@EnableJpaRepositories(basePackages = "com.ashago.mainapp.repository")
@ComponentScan(basePackages = { "com.ashago.mainapp.service" })
public class ElasticConfig {

    @Autowired
    Environment environment;

    private RestHighLevelClient client;

    @Bean
    public RestHighLevelClient prepareConnection() {
        RestClientBuilder restBuilder = RestClient.builder(new HttpHost(
                "quickstart-es-http", Integer.valueOf("9200"), "http"));
        // RestClientBuilder restBuilder = RestClient.builder(new HttpHost(
        //         "es.cc2dbe1fd91f042528f96dc27c2dba5fe.cn-zhangjiakou.alicontainer.com", Integer.valueOf("80"), "http"));
        final CredentialsProvider creadential = new BasicCredentialsProvider();
        creadential.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "jVgOgT98EGYk906174a3wd6x"));
        restBuilder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {

                return httpClientBuilder.setDefaultCredentialsProvider(creadential)
                        .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build());
            }
        });

        restBuilder.setRequestConfigCallback(
                requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(10000).setSocketTimeout(60000) 
                        .setConnectionRequestTimeout(0)); // time to fetch a connection from the connection pool 0 for
                                                          // infinite.

        client = new RestHighLevelClient(restBuilder);
        System.out.print(client);
        return client;
    }

    @PreDestroy
    public void clientClose() {
        try {
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}