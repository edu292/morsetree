package com.morsetree;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MorseTree {
    No raiz;

    public record ResultadoCaminho<T>(T valor, List<No> caminho, boolean valido) {
    }

    public MorseTree() {
        this.raiz = new No();
    }

    public void inserir(String codigoCaractere, char caractere) {
        No no = this.raiz;
        for (int i = 0; i < codigoCaractere.length(); i++) {
            char c = codigoCaractere.charAt(i);
            if (c == '.') {
                if (no.filho_esquerdo == null) {
                    no.filho_esquerdo = new No();
                }

                no = no.filho_esquerdo;
            } else if (c == '-') {
                if (no.filho_direito == null) {
                    no.filho_direito = new No();
                }

                no = no.filho_direito;
            }
        }

        no.caractere = caractere;
    }

    public String decodificar(String mensagemMorse) {
        if (mensagemMorse == null || mensagemMorse.isEmpty()) {
            return "";
        }

        StringBuilder significado = new StringBuilder();
        int inicio = 0;
        int fim;
        while ((fim = mensagemMorse.indexOf(' ', inicio)) != -1) {
            char c = decodificarCaractere(mensagemMorse, inicio, fim);
            if (c != '\0') {
                significado.append(c);
            }

            inicio = fim + 1;
        }

        if (inicio < mensagemMorse.length()) {
            char c = decodificarCaractere(mensagemMorse, inicio, mensagemMorse.length());
            if (c != '\0') {
                significado.append(c);
            }
        }

        return significado.toString();
    }

    public char decodificarCaractere(String codigoCaractere) {
        return decodificarCaractere(codigoCaractere, 0, codigoCaractere.length());
    }

    public char decodificarCaractere(String codigoCaractere, int inicio, int fim) {
        if (fim - inicio == 1 && codigoCaractere.charAt(inicio) == '/') {
            return ' ';
        }

        No no = this.raiz;
        for (int i = inicio; i < fim; i++) {
            char c = codigoCaractere.charAt(i);
            if (c == '.') {
                no = no.filho_esquerdo;
            } else if (c == '-') {
                no = no.filho_direito;
            }

            if (no == null) {
                return '\0';
            }
        }

        return no.caractere;
    }

    public ResultadoCaminho<Character> decodificarCaractereComCaminho(String codigo) {
        if (codigo == null || codigo.isEmpty()) {
            return new ResultadoCaminho<>('\0', List.of(this.raiz), true);
        }

        if (codigo.equals("/")) {
            return new ResultadoCaminho<>(' ', Collections.emptyList(), true);
        }

        List<No> caminho = new ArrayList<>();
        No no = this.raiz;
        caminho.add(no);

        for (int i = 0; i < codigo.length(); i++) {
            char c = codigo.charAt(i);
            if (c == '.') {
                no = no.filho_esquerdo;
            } else if (c == '-') {
                no = no.filho_direito;
            } else {
                no = null;
            }

            if (no == null) {
                return new ResultadoCaminho<>('\0', caminho, false);
            }
            caminho.add(no);
        }

        boolean valido = no.caractere != '\0';
        return new ResultadoCaminho<>(no.caractere, caminho, valido);
    }

    public String codificar(String texto) {
        String normalizado = Normalizer.normalize(texto.toLowerCase(), Normalizer.Form.NFD);
        normalizado = normalizado.replaceAll("\\p{M}", "");

        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < normalizado.length(); i++) {
            char caractere = normalizado.charAt(i);
            if (caractere == ' ') {
                codigo.append(" / ");
                continue;
            }

            codificarEm(caractere, codigo);
            codigo.append(' ');
        }

        return codigo.toString();
    }

    public String codificarCaractere(char caractere) {
        return codificarCaractere(caractere, new StringBuilder());
    }

    public String codificarCaractere(char caractere, StringBuilder codigo) {
        return codificarHelper(this.raiz, caractere, codigo) ? codigo.toString() : null;
    }

    public boolean codificarEm(char caractere, StringBuilder codigo) {
        return codificarHelper(this.raiz, caractere, codigo);
    }

    private boolean codificarHelper(No no, char alvo, StringBuilder caminho) {
        if (no == null) {
            return false;
        }

        if (no.caractere == alvo) {
            return true;
        }

        caminho.append('.');
        Boolean encontrado = codificarHelper(no.filho_esquerdo, alvo, caminho);
        if (encontrado) {
            return true;
        }
        caminho.deleteCharAt(caminho.length() - 1);

        caminho.append('-');
        encontrado = codificarHelper(no.filho_direito, alvo, caminho);
        if (encontrado) {
            return true;
        }
        caminho.deleteCharAt(caminho.length() - 1);

        return false;
    }

    public int getAltura() {
        return getAlturaHelper(this.raiz);
    }

    private int getAlturaHelper(No no) {
        if (no == null) {
            return 0;
        }

        int alturaEsquerda = 1 + getAlturaHelper(no.filho_esquerdo);
        int alturaDireita = 1 + getAlturaHelper(no.filho_direito);
        return Math.max(alturaEsquerda, alturaDireita);
    }

    public ResultadoCaminho<String> codificarCaractereComCaminho(char caractere) {
        if (caractere == ' ') {
            return new ResultadoCaminho<>("/", Collections.emptyList(), true);
        }

        List<No> caminho = new ArrayList<>();
        StringBuilder morse = new StringBuilder();

        boolean encontrado = codificarCaminhoHelper(this.raiz, Character.toLowerCase(caractere), morse, caminho);
        if (!encontrado) {
            return new ResultadoCaminho<>(null, Collections.emptyList(), false);
        }

        return new ResultadoCaminho<>(morse.toString(), caminho, true);
    }

    private boolean codificarCaminhoHelper(No no, char alvo, StringBuilder morse, List<No> caminho) {
        if (no == null) {
            return false;
        }

        caminho.add(no);

        if (no.caractere == alvo) {
            return true;
        }

        morse.append('.');
        if (codificarCaminhoHelper(no.filho_esquerdo, alvo, morse, caminho)) {
            return true;
        }
        morse.deleteCharAt(morse.length() - 1);

        morse.append('-');
        if (codificarCaminhoHelper(no.filho_direito, alvo, morse, caminho)) {
            return true;
        }
        morse.deleteCharAt(morse.length() - 1);

        caminho.remove(caminho.size() - 1);
        return false;
    }

    public static MorseTree preenchida() {
        MorseTree arvore = new MorseTree();

        arvore.inserir(".", 'e');
        arvore.inserir("-", 't');

        arvore.inserir("..", 'i');
        arvore.inserir(".-", 'a');
        arvore.inserir("-.", 'n');
        arvore.inserir("--", 'm');

        arvore.inserir("...", 's');
        arvore.inserir("..-", 'u');
        arvore.inserir(".-.", 'r');
        arvore.inserir(".--", 'w');
        arvore.inserir("-..", 'd');
        arvore.inserir("-.-", 'k');
        arvore.inserir("--.", 'g');
        arvore.inserir("---", 'o');

        arvore.inserir("....", 'h');
        arvore.inserir("...-", 'v');
        arvore.inserir("..-.", 'f');
        arvore.inserir(".-..", 'l');
        arvore.inserir(".--.", 'p');
        arvore.inserir(".---", 'j');
        arvore.inserir("-...", 'b');
        arvore.inserir("-..-", 'x');
        arvore.inserir("-.-.", 'c');
        arvore.inserir("-.--", 'y');
        arvore.inserir("--..", 'z');
        arvore.inserir("--.-", 'q');

        arvore.inserir(".....", '5');
        arvore.inserir("....-", '4');
        arvore.inserir("...--", '3');
        arvore.inserir("..---", '2');
        arvore.inserir(".----", '1');
        arvore.inserir("-....", '6');
        arvore.inserir("--...", '7');
        arvore.inserir("---..", '8');
        arvore.inserir("----.", '9');
        arvore.inserir("-----", '0');

        return arvore;
    }
}
