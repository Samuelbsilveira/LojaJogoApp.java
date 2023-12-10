package br.com.LojaJogos.service;

import br.com.LojaJogos.response.ApiResponse;
import br.com.LojaJogos.util.GlobalGameList;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import br.com.LojaJogos.client.HttpRequestService;
import br.com.LojaJogos.model.Game;
import br.com.LojaJogos.response.StandardResponse;
import br.com.LojaJogos.response.StatusResponse;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
public class GameService {

    private HttpRequestService httpRequestService;

    public GameService(HttpRequestService httpRequestService) {
        this.httpRequestService = httpRequestService;
    }

    public StandardResponse createGame(Game game, List<Game> gameList) {
        if (game.getAmount() <= 0) {
            log.error("The game amount should be bigger than 0");
            return new StandardResponse(StatusResponse.ERROR, new Gson().toJsonTree("The game amount should be bigger than 0"));
        }
        gameList.add(game);
        GlobalGameList.gameList.add(game);

        return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree("Success! We got the new game called " + game.getName()));
    }

    public StandardResponse convertGameValueToUSD(Long gameId, List<Game> gameList, String bidAsk, boolean showGameList) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ApiResponse apiResponse = null;
        try {
            String rawBody = httpRequestService.brlToUsdConvert();
            rawBody = rawBody.split("USDBRL\":")[1].replace("}}", "}");
            apiResponse = objectMapper.readValue(rawBody, ApiResponse.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        Game game = gameList.stream().filter(g -> g.getId() == gameId).findFirst().orElse(null);

        if (showGameList) {
            log.info(gameList.toString());
        }

        if (game == null) {
            log.error("Game not found!");
            return new StandardResponse(StatusResponse.ERROR, new Gson().toJsonTree("Game not found!"));
        }

        BigDecimal dollarRate = apiResponse.getAsk();
        dollarRate = bidAsk.contains("bid") ? apiResponse.getBid() : dollarRate;
        BigDecimal convertedValue = game.getPrice().divide(dollarRate, RoundingMode.HALF_DOWN);
        log.info("Game value converted: $" + convertedValue);

        return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree("$" + convertedValue));
    }

    public StandardResponse deleteGameById(Long gameId, List<Game> gameList) {
        Game targetGame = gameList.stream().filter(g -> g.getId() == gameId).findFirst().orElse(null);
        if (targetGame == null) {
            return new StandardResponse(StatusResponse.ERROR, new Gson().toJsonTree("Game not found!"));
        }
        gameList.remove(targetGame);
        return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree("Game deleted with success!"));
    }

    public StandardResponse updateGame(Game game, List<Game> gameList, Long gameId) {
        Game targetGame = gameList.stream().filter(g -> g.getId() == gameId).findFirst().orElse(null);
        if (targetGame == null) {
            return new StandardResponse(StatusResponse.ERROR, new Gson().toJsonTree("Game not found!"));
        }
        targetGame.setAmount(game.getAmount());
        targetGame.setName(game.getName());
        targetGame.setPrice(game.getPrice());
        targetGame.setPlatform(game.getPlatform());
        targetGame.setStaffNameList(game.getStaffNameList());

        log.info(gameList.toString());
        return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree("Game update with success!"));
    }
}
