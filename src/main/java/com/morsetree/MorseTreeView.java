package com.morsetree;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.morsetree.MorseTree.ResultadoCaminho;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MorseTreeView extends StackPane {
    private static final Color COR_LINHA = Color.web("#999999");
    private static final Color COR_NO = Color.WHITE;
    private static final Color COR_BORDA = Color.BLACK;
    private static final Color COR_CAMINHO = Color.web("#ff8c00");
    private static final Color COR_ENCONTRADO = Color.web("#2ecc71");
    private static final Color COR_INVALIDO = Color.web("#e74c3c");

    private final MorseTree arvore;
    private final double espacamentoY = 90;
    private final double larguraTotal;
    private final double alturaTotal;
    private final double raioNo = 22;

    private final Pane canvas = new Pane();
    private final Pane layerLinhas = new Pane();
    private final Pane layerNos = new Pane();

    private Consumer<Character> aoClicarCaractere;

    public MorseTreeView(MorseTree arvore) {
        this.arvore = arvore;
        int maxDepth = arvore.getAltura() - 1;
        this.larguraTotal = Math.pow(2, maxDepth) * (raioNo * 2.0 + 16);
        this.alturaTotal = maxDepth * espacamentoY + raioNo * 2.0 + 40;

        canvas.setPrefSize(larguraTotal, alturaTotal);
        canvas.getChildren().addAll(layerLinhas, layerNos);
        getChildren().add(new Group(canvas));

        widthProperty().addListener((obs, oldVal, newVal) -> ajustarEscala());
        heightProperty().addListener((obs, oldVal, newVal) -> ajustarEscala());

        destacarResultado(null);
    }

    public void setAoClicarCaractere(Consumer<Character> callback) {
        this.aoClicarCaractere = callback;
    }

    public void destacarResultado(ResultadoCaminho<?> resultado) {
        layerLinhas.getChildren().clear();
        layerNos.getChildren().clear();

        List<No> caminho = Collections.emptyList();
        boolean valido = false;

        if (resultado != null) {
            if (resultado.caminho() != null) {
                caminho = resultado.caminho();
            }
            valido = resultado.valido();
        }
        renderNo(arvore.raiz, larguraTotal / 2, 60, larguraTotal / 4.0, caminho, valido);
    }

    private void renderNo(No no, double x, double y, double larguraNivel, List<No> caminho, boolean valido) {
        if (no == null) {
            return;
        }

        boolean inPath = caminho.contains(no);
        boolean isTerminal = !caminho.isEmpty() && no == caminho.get(caminho.size() - 1);

        Color corNo = COR_NO;
        if (inPath) {
            if (isTerminal) {
                corNo = valido ? COR_ENCONTRADO : COR_INVALIDO;
            } else {
                corNo = COR_CAMINHO;
            }
        }

        Circle circulo = new Circle(raioNo, corNo);
        circulo.setStroke(inPath ? corNo.darker() : COR_BORDA);
        circulo.setStrokeWidth(inPath ? 2.5 : 1.5);

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

        double proximoY = y + espacamentoY;
        double proximaLargura = larguraNivel / 2.0;

        if (no.filho_esquerdo != null) {
            double proxX = x - larguraNivel;
            renderRamo(x, y, proxX, proximoY, ".", inPath && caminho.contains(no.filho_esquerdo));
            renderNo(no.filho_esquerdo, proxX, proximoY, proximaLargura, caminho, valido);
        }

        if (no.filho_direito != null) {
            double proxX = x + larguraNivel;
            renderRamo(x, y, proxX, proximoY, "-", inPath && caminho.contains(no.filho_direito));
            renderNo(no.filho_direito, proxX, proximoY, proximaLargura, caminho, valido);
        }
    }

    private void renderRamo(double x1, double y1, double x2, double y2, String simbolo, boolean ativo) {
        Line linha = new Line(x1, y1, x2, y2);
        linha.setStroke(ativo ? COR_CAMINHO : COR_LINHA);
        linha.setStrokeWidth(ativo ? 3.0 : 1.5);

        Text label = new Text(simbolo);
        label.setFont(Font.font("Monospaced", FontWeight.BOLD, 20));
        label.setLayoutX((x1 + x2) / 2.0 - 6);
        label.setLayoutY((y1 + y2) / 2.0 - 6);

        layerLinhas.getChildren().addAll(linha, label);
    }

    private void ajustarEscala() {
        Insets in = getInsets();
        double w = getWidth() - in.getLeft() - in.getRight();
        double h = getHeight() - in.getTop() - in.getBottom();
        if (w <= 0 || h <= 0) {
            return;
        }

        double scale = Math.min(w / larguraTotal, h / alturaTotal);
        canvas.setScaleX(scale);
        canvas.setScaleY(scale);
    }
}
