package io.quarkus.ahc;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;

import io.quarkus.runtime.ShutdownEvent;

@Singleton
public class AhcProducer {

    private AsyncHttpClient asyncHttpClient;

    @PostConstruct
    void postConstruct() {
        asyncHttpClient = Dsl.asyncHttpClient();
    }

    @Singleton
    @Produces
    AsyncHttpClient asyncHttpClient() {
        return asyncHttpClient;
    }

    void onStop(@Observes ShutdownEvent ev) {
        if (asyncHttpClient != null) {
            try {
                asyncHttpClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                asyncHttpClient = null;
            }
        }
    }

}
