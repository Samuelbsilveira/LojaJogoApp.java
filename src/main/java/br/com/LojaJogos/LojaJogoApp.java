package br.com.LojaJogos;

import com.google.inject.Guice;
import com.google.inject.Injector;
import br.com.LojaJogos.controller.GameController;
import br.com.LojaJogos.config.GoogleGuiceCustomModule;
import spark.Spark;

public class LojaJogoApp {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new GoogleGuiceCustomModule());

        LojaJogoApp.initServer();
        LojaJogoApp.initRestControllers(injector);
    }

    public static void initServer() {
        Spark.port(8080);
    }

    public static void initRestControllers(Injector injector) {
        GameController gameSparkController = injector.getInstance(GameController.class);
        gameSparkController.initEndPoints();
    }
}
