package com.morsetree;

import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MorseTreeView extends Pane {
    private final MorseTree arvore;
    private final double espacamentoVertical = 90;
    private final double largura;
    private final double raioNo = 22;

    private final Pane layerLinhas = new Pane();
    private final Pane layerNos = new Pane();

    public MorseTreeView(MorseTree arvore) {
        this.arvore = arvore;
        getChildren().addAll(layerLinhas, layerNos);
        int profundidadeMaxima = arvore.getAltura() - 1;
        double distanciaFolhas = raioNo * 2.0 + 16;
        this.largura = Math.pow(2, profundidadeMaxima) * distanciaFolhas;
        setPrefSize(largura, (profundidadeMaxima * this.espacamentoVertical));
        render();
    }

    public void render() {
        layerLinhas.getChildren().clear();
        layerNos.getChildren().clear();

        Node raiz = arvore.raiz;
        double xRaiz = largura / 2;
        renderNo(raiz, xRaiz, 60, xRaiz, 60, largura / 4.0);
    }

    private void renderNo(Node no, double x, double y, double xPai, double yPai, double larguraNivel) {
        if (no == null) {
            return;
        }

        if (x != xPai || y != yPai) {
            Line linha = new Line(xPai, yPai, x, y);
            Text labelLinha = new Text(x < xPai ? "." : "-");
            labelLinha.setFont(Font.font("Monospaced", FontWeight.BOLD, 20));

            labelLinha.setLayoutX((xPai + x) / 2.0 - 6);
            labelLinha.setLayoutY((yPai + y) / 2.0 - 6);
            layerLinhas.getChildren().addAll(linha, labelLinha);
        }

        Circle circulo = new Circle(raioNo);
        circulo.setStroke(Color.BLACK);
        circulo.setFill(Color.WHITE);
        circulo.setStrokeWidth(1.5);
        Text label = new Text(String.valueOf(no.caractere));
        label.setFont(Font.font("System", FontWeight.BOLD, 20));
        StackPane frameNo = new StackPane(circulo, label);
        frameNo.setLayoutX(x - raioNo);
        frameNo.setLayoutY(y - raioNo);
        layerNos.getChildren().add(frameNo);

        double proximoY = y + espacamentoVertical;
        double proximaLargura = larguraNivel / 2.0;
        renderNo(no.filho_esquerdo, x - larguraNivel, proximoY, x, y, proximaLargura);
        renderNo(no.filho_direito, x + larguraNivel, proximoY, x, y, proximaLargura);
    }
}
