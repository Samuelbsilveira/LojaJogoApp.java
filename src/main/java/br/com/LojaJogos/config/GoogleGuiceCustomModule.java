package br.com.LojaJogos.config;

import br.com.LojaJogos.service.GameService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import br.com.LojaJogos.client.HttpRequestService;

public class GoogleGuiceCustomModule extends AbstractModule {

    @Provides
    public GameService gameService() {
        HttpRequestService httpRequestService = new HttpRequestService();
        return new GameService(httpRequestService);
    }

    @Provides
    public HttpRequestService httpRequestService() {
        HttpRequestService httpRequestService = new HttpRequestService();
        return new HttpRequestService();
    }

    @Provides
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
