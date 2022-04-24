package com.example.pokedex.config;

import com.example.pokedex.proxi.api.PokedexApi;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class Config {

    @Value("#{'${url-base}'}")
    private String url;

    @Bean
    PokedexApi pokedexApi() {
        return getRetrofitConfiguration(url, HttpLoggingInterceptor.Level.BODY, 10, 10)
                .create(PokedexApi.class);
    }

    private Retrofit getRetrofitConfiguration(String url,
                                              HttpLoggingInterceptor.Level level,
                                              long readTimeout,
                                              long connectTimeout) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(getHttpClient(level, readTimeout, connectTimeout))
                .addConverterFactory(JacksonConverterFactory.create(getObjectMapper()))
                .build();
    }

    private OkHttpClient getHttpClient(HttpLoggingInterceptor.Level level, long readTimeout, long connectTimeout) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(level);
        return new OkHttpClient.Builder()
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    private ObjectMapper getObjectMapper() {
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setDateFormat(new StdDateFormat())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
                .enable(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS);
    }
}
