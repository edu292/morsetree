package com.morsetree;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    private final MorseTree arvore = MorseTree.preenchida();

    @Override
    public void start(Stage stage) {
        TextField campoMorse = new TextField();
        campoMorse.setPromptText("Escreva sua mensagem em código morse");

        TextField campoTexto = new TextField();
        campoTexto.setPromptText("Escreva sua mensagem para ser codificada");

        campoMorse.textProperty().addListener((obs, oldVal, newVal) -> {
            if (campoMorse.isFocused()) {
                campoTexto.setText(arvore.decodificar(newVal));
            }
        });

        campoTexto.textProperty().addListener((obs, oldVal, newVal) -> {
            if (campoTexto.isFocused()) {
                campoMorse.setText(arvore.codificar(newVal));
            }
        });

        VBox controles = new VBox(10, campoMorse, campoTexto);
        controles.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(controles);

        stage.setScene(new Scene(layout, 700, 500));
        stage.setTitle("MorseTree");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
