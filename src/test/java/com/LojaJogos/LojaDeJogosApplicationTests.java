package com.LojaJogos;


import br.com.LojaJogos.client.HttpRequestService;
import br.com.LojaJogos.service.GameService;
import br.com.LojaJogos.model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class LojaDeJogosApplicationTests {


	private GameService gameService;
	private HttpRequestService httpRequestService;

	@BeforeEach
	void setUp() {
		httpRequestService = new HttpRequestService();
		gameService = new GameService(httpRequestService);
	}

	@Test
	void httpRequestServiceTest() {
		String response = "";

		try {
			response = httpRequestService.brlToUsdConvert();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Assertions.assertTrue(response.contains("\"ask\":"));
		Assertions.assertTrue(response.contains("\"bid\":"));

	}

	@Test
	void invalidDeletionWhenGameListIsNullTest() {
		final List<Game> gameList = null;
		Assertions.assertThrows(NullPointerException.class, () -> gameService.deleteGameById(1L, gameList));
	}

	@Test
	void validGameInsertionTest() {
		final List<Game> gameList = new ArrayList<>();
		Game game = new Game(1L, "Game", BigDecimal.TEN, 1, "producer", "platform", null);
		gameService.createGame(game, gameList);
        Assertions.assertFalse(gameList.isEmpty());
	}

	@Test
	void invalidGameInsertionWithWrongAmountNumberTest() {
		final List<Game> gameList = new ArrayList<>();
		Game game = new Game(1L, "Game", BigDecimal.TEN, 0, "producer", "platform", null);
		gameService.createGame(game, gameList);
		Assertions.assertTrue(gameList.isEmpty());
	}

}
