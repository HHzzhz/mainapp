package com.ashago.mainapp.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PreDestroy;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.ashago.mainapp.esrepository")
@EnableJpaRepositories(basePackages = "com.ashago.mainapp.repository")
@ComponentScan(basePackages = {"com.ashago.mainapp.service"})
public class ElasticConfig {

    private RestHighLevelClient client;

    @Bean
    public RestHighLevelClient prepareConnection() {

        RestClientBuilder restBuilder = RestClient.builder(new HttpHost(
                "quickstart-es-http", 9200, "https"));
//         RestClientBuilder restBuilder = RestClient.builder(new HttpHost(
//                 "es.cc2dbe1fd91f042528f96dc27c2dba5fe.cn-zhangjiakou.alicontainer.com", Integer.valueOf("80"), "https"));
        final CredentialsProvider creadential = new BasicCredentialsProvider();
        creadential.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "jVgOgT98EGYk906174a3wd6x"));
        restBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
            try {
                return httpClientBuilder.setDefaultCredentialsProvider(creadential)
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .setSSLContext(SSLContextBuilder.create().loadTrustMaterial(new TrustAllStrategy()).build())
                        .setKeepAliveStrategy((httpResponse, httpContext) -> 60L * 1000)
                        .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build());
            } catch (Exception e) {
                return httpClientBuilder.setDefaultCredentialsProvider(creadential)
                        .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build());
            }
        });

        restBuilder.setRequestConfigCallback(
                requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(10000).setSocketTimeout(60000)
                        .setConnectionRequestTimeout(0)); // time to fetch a connection from the connection pool 0 for
        // infinite.

        client = new RestHighLevelClient(restBuilder);
        return client;
    }

    @PreDestroy
    public void clientClose() {
        try {
            this.client.close();
        } catch (IOException e) {
            log.error("client close err", e);
        }
    }

}