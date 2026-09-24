package com.morsetree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MorseTreeView extends Pane {
    private static final Color COR_LINHA_PADRAO = Color.web("#999999");
    private static final Color COR_NO_PADRAO = Color.WHITE;
    private static final Color COR_BORDA_PADRAO = Color.BLACK;
    private static final Color COR_CAMINHO = Color.web("#ff8c00");
    private static final Color COR_ENCONTRADO = Color.web("#2ecc71");
    private static final Color COR_INVALIDO = Color.web("#e74c3c");

    private final MorseTree arvore;
    private final double espacamentoVertical = 90;
    private final double largura;
    private final double alturaTotal;
    private final double raioNo = 22;

    private final Pane layerLinhas = new Pane();
    private final Pane layerNos = new Pane();

    private final Map<Node, Circle> circulosPorNo = new HashMap<>();
    private final Map<Node, Line> linhasPorNo = new HashMap<>();
    private final List<Node> nosDestacados = new ArrayList<>();

    private Consumer<Character> aoClicarCaractere;

    public MorseTreeView(MorseTree arvore) {
        this.arvore = arvore;
        getChildren().addAll(layerLinhas, layerNos);
        int profundidadeMaxima = arvore.getAltura() - 1;
        double distanciaFolhas = raioNo * 2.0 + 16;
        this.largura = Math.pow(2, profundidadeMaxima) * distanciaFolhas;
        this.alturaTotal = profundidadeMaxima * espacamentoVertical + raioNo * 2.0 + 40;
        setPrefSize(largura, alturaTotal);
        render();
    }

    public double getLarguraTotal() {
        return largura;
    }

    public double getAlturaTotal() {
        return alturaTotal;
    }

    public void setAoClicarCaractere(Consumer<Character> callback) {
        this.aoClicarCaractere = callback;
    }

    public void render() {
        layerLinhas.getChildren().clear();
        layerNos.getChildren().clear();
        circulosPorNo.clear();
        linhasPorNo.clear();
        nosDestacados.clear();

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
            linha.setStroke(COR_LINHA_PADRAO);
            linha.setStrokeWidth(1.5);
            Text labelLinha = new Text(x < xPai ? "." : "-");
            labelLinha.setFont(Font.font("Monospaced", FontWeight.BOLD, 20));

            labelLinha.setLayoutX((xPai + x) / 2.0 - 6);
            labelLinha.setLayoutY((yPai + y) / 2.0 - 6);
            layerLinhas.getChildren().addAll(linha, labelLinha);
            linhasPorNo.put(no, linha);
        }

        Circle circulo = new Circle(raioNo);
        circulo.setStroke(COR_BORDA_PADRAO);
        circulo.setFill(COR_NO_PADRAO);
        circulo.setStrokeWidth(1.5);
        circulosPorNo.put(no, circulo);

        Text label = new Text(String.valueOf(no.caractere));
        label.setFont(Font.font("System", FontWeight.BOLD, 20));
        StackPane frameNo = new StackPane(circulo, label);
        frameNo.setLayoutX(x - raioNo);
        frameNo.setLayoutY(y - raioNo);

        if (no.caractere != '\0') {
            frameNo.setCursor(Cursor.HAND);
            frameNo.setOnMouseClicked(e -> {
                if (aoClicarCaractere != null) {
                    aoClicarCaractere.accept(no.caractere);
                }
            });
        }

        layerNos.getChildren().add(frameNo);

        double proximoY = y + espacamentoVertical;
        double proximaLargura = larguraNivel / 2.0;
        renderNo(no.filho_esquerdo, x - larguraNivel, proximoY, x, y, proximaLargura);
        renderNo(no.filho_direito, x + larguraNivel, proximoY, x, y, proximaLargura);
    }

    public void limparDestaque() {
        for (Node no : nosDestacados) {
            Circle circulo = circulosPorNo.get(no);
            if (circulo != null) {
                circulo.setFill(COR_NO_PADRAO);
                circulo.setStroke(COR_BORDA_PADRAO);
                circulo.setStrokeWidth(1.5);
            }
            Line linha = linhasPorNo.get(no);
            if (linha != null) {
                linha.setStroke(COR_LINHA_PADRAO);
                linha.setStrokeWidth(1.5);
            }
        }
        nosDestacados.clear();
    }

    public void destacarCaminho(String codigoMorse) {
        limparDestaque();
        if (codigoMorse == null || codigoMorse.isEmpty()) {
            return;
        }

        Node no = arvore.raiz;
        destacarNo(no, COR_CAMINHO);

        boolean caminhoValido = true;
        for (int i = 0; i < codigoMorse.length(); i++) {
            char c = codigoMorse.charAt(i);
            Node proximo = c == '.' ? no.filho_esquerdo : c == '-' ? no.filho_direito : null;
            if (proximo == null) {
                caminhoValido = false;
                break;
            }

            destacarLinha(proximo, COR_CAMINHO);
            no = proximo;

            boolean ultimo = i == codigoMorse.length() - 1;
            destacarNo(no, ultimo && no.caractere != '\0' ? COR_ENCONTRADO : COR_CAMINHO);
        }

        if (!caminhoValido) {
            destacarNo(no, COR_INVALIDO);
        }
    }

    private void destacarNo(Node no, Color cor) {
        Circle circulo = circulosPorNo.get(no);
        if (circulo != null) {
            circulo.setFill(cor);
            circulo.setStroke(cor.darker());
            circulo.setStrokeWidth(2.5);
        }
        nosDestacados.add(no);
    }

    private void destacarLinha(Node no, Color cor) {
        Line linha = linhasPorNo.get(no);
        if (linha != null) {
            linha.setStroke(cor);
            linha.setStrokeWidth(3);
        }
    }
}
