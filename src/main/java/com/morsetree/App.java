package com.morsetree;

import javafx.application.Application;
import javafx.geometry.Insets;
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

        MorseTreeView treeView = new MorseTreeView(arvore);
        treeView.setAoClicarCaractere(caractere -> campoTexto.appendText(String.valueOf(caractere)));

        campoTexto.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!campoTexto.isFocused())
                return;

            String morse = arvore.codificar(newVal);
            campoMorse.setText(morse.trim());

            if (!newVal.isEmpty()) {
                char ultimoChar = newVal.charAt(newVal.length() - 1);
                treeView.destacarResultado(arvore.codificarCaractereComCaminho(ultimoChar));
            } else {
                treeView.destacarResultado(null);
            }
        });

        campoMorse.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!campoMorse.isFocused())
                return;

            String texto = arvore.decodificar(newVal);
            campoTexto.setText(texto);

            String tokenAtual = ultimoSegmento(newVal);
            treeView.destacarResultado(arvore.decodificarCaractereComCaminho(tokenAtual));
        });

        treeView.setAoClicarCaractere(caractere -> {
            campoTexto.appendText(String.valueOf(caractere));
            var resultado = arvore.codificarCaractereComCaminho(caractere);
            if (resultado.valido()) {
                campoMorse.appendText((campoMorse.getText().isEmpty() ? "" : " ") + resultado.valor());
            }
            treeView.destacarResultado(resultado);
        });

        VBox controles = new VBox(10, campoMorse, campoTexto);
        controles.setPadding(new Insets(15));
        controles.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(controles);
        layout.setCenter(treeView);

        stage.setScene(new Scene(layout));
        stage.setTitle("MorseTree");
        stage.setMaximized(true);
        stage.show();
    }

    private String ultimoSegmento(String mensagemMorse) {
        if (mensagemMorse == null) {
            return "";
        }

        String semEspacosFinais = mensagemMorse.stripTrailing();
        if (semEspacosFinais.isEmpty()) {
            return "";
        }

        int ultimoEspaco = semEspacosFinais.lastIndexOf(' ');
        String segmento = ultimoEspaco == -1 ? semEspacosFinais : semEspacosFinais.substring(ultimoEspaco + 1);
        return "/".equals(segmento) ? "" : segmento;
    }

    public static void main(String[] args) {
        launch();
    }

}
