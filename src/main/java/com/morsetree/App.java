package com.morsetree;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    private final MorseTree arvore = MorseTree.preenchida();
    private boolean atualizandoProgramaticamente = false;

    @Override
    public void start(Stage stage) {
        TextField campoMorse = new TextField();
        campoMorse.setPromptText("Escreva sua mensagem em código morse");

        TextField campoTexto = new TextField();
        campoTexto.setPromptText("Escreva sua mensagem para ser codificada");

        MorseTreeView treeView = new MorseTreeView(arvore);
        treeView.setAoClicarCaractere(caractere -> campoTexto.appendText(String.valueOf(caractere)));

        campoMorse.textProperty().addListener((obs, oldVal, newVal) -> {
            treeView.destacarCaminho(ultimoSegmento(newVal));

            if (atualizandoProgramaticamente) {
                return;
            }
            atualizandoProgramaticamente = true;
            campoTexto.setText(arvore.decodificar(newVal));
            atualizandoProgramaticamente = false;
        });

        campoTexto.textProperty().addListener((obs, oldVal, newVal) -> {
            if (atualizandoProgramaticamente) {
                return;
            }
            atualizandoProgramaticamente = true;
            campoMorse.setText(arvore.codificar(newVal));
            atualizandoProgramaticamente = false;
        });

        VBox controles = new VBox(10, campoMorse, campoTexto);
        controles.setPadding(new Insets(15));
        controles.setAlignment(Pos.CENTER);

        // O Group evita que o StackPane tente redimensionar a árvore (que tem
        // coordenadas absolutas): assim ela mantém sempre o mesmo tamanho
        // "natural" e só é reduzida através da escala aplicada abaixo.
        Group grupoArvore = new Group(treeView);
        StackPane areaArvore = new StackPane(grupoArvore);
        areaArvore.setPadding(new Insets(20));

        Runnable ajustarEscala = () -> {
            Insets insets = areaArvore.getInsets();
            double larguraDisponivel = areaArvore.getWidth() - insets.getLeft() - insets.getRight();
            double alturaDisponivel = areaArvore.getHeight() - insets.getTop() - insets.getBottom();
            if (larguraDisponivel <= 0 || alturaDisponivel <= 0) {
                return;
            }

            double escala = Math.min(
                    larguraDisponivel / treeView.getLarguraTotal(),
                    alturaDisponivel / treeView.getAlturaTotal());
            escala *= 0.98;
            treeView.setScaleX(escala);
            treeView.setScaleY(escala);
        };

        areaArvore.widthProperty().addListener((obs, oldVal, newVal) -> ajustarEscala.run());
        areaArvore.heightProperty().addListener((obs, oldVal, newVal) -> ajustarEscala.run());
        stage.maximizedProperty().addListener((obs, oldVal, newVal) -> Platform.runLater(ajustarEscala));

        BorderPane layout = new BorderPane();
        layout.setTop(controles);
        layout.setCenter(areaArvore);

        // Tamanho inicial modesto: sem isso, a janela nasceria do tamanho
        // "natural" da árvore (bem larga) e maximizar quase não mudaria nada.
        stage.setScene(new Scene(layout, 1024, 700));
        stage.setTitle("MorseTree");
        stage.setMaximized(true);
        stage.show();

        Platform.runLater(ajustarEscala);
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
