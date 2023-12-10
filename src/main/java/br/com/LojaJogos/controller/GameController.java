package br.com.LojaJogos.controller;

import br.com.LojaJogos.service.GameService;
import br.com.LojaJogos.util.GlobalGameList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.inject.Inject;
import br.com.LojaJogos.model.Game;

import java.util.List;

import static spark.Spark.*;

public class GameController {

    @Inject
    private GameService gameService;

    @Inject
    private ObjectMapper objectMapper;

    public void initEndPoints() {

        List<Game> gameList = GlobalGameList.gameList;

        get("/games", (request, response) -> {
            response.type("application/json");
            long gameId = Long.parseLong(request.queryParams("gameId"));
            String bidAsk = request.queryParams("bidAsk");
            boolean showGameList = Boolean.parseBoolean(request.queryParams("showGameList"));
            return new Gson().toJson(gameService.convertGameValueToUSD(gameId, gameList, bidAsk, showGameList));
        });

        post("/games", (request, response) -> {
            response.type("application/json");
            Game gamePost =  objectMapper.readValue(request.body(), Game.class);
            return new Gson().toJson(gameService.createGame(gamePost, gameList));
        });

        delete("/games", (request, response) -> {
            response.type("application/json");
            long gameId = Long.parseLong(request.queryParams("gameId"));
            return new Gson().toJson(gameService.deleteGameById(gameId, gameList));
        });

        put("/games", (request, response) -> {
            response.type("application/json");
            Game gamePut =  objectMapper.readValue(request.body(), Game.class);
            long gameId = Long.parseLong(request.queryParams("gameId"));
            return new Gson().toJson(gameService.updateGame(gamePut, gameList, gameId));
        });
    }

}
