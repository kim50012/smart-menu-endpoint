package com.basoft.eorder.util;


import com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class HttpClientUtil {

    /** Log object for this class. */
    private static final Log LOG = LogFactory.getLog( HttpClientUtil.class );

    private static RestTemplate restTemplate;
    static{
        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        restTemplate = new RestTemplate(requestFactory);

        //set json DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
        for(HttpMessageConverter<?> httpMessageConverter : restTemplate.getMessageConverters()){
            if(httpMessageConverter instanceof MappingJackson2HttpMessageConverter){
                MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter)httpMessageConverter;
                jacksonConverter.getObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                break;
            }
        }
    }

    public RestTemplate restTemplate(){
        return restTemplate;
    }

    private static class ClientInstance {
        private static final HttpClientUtil instance = new HttpClientUtil();
    }

    public static HttpClientUtil getInstance() {
        return ClientInstance.instance;
    }



}
