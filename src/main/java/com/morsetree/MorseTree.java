package com.morsetree;

import java.text.Normalizer;

class Node {
    Node filho_esquerdo;
    char caractere;
    Node filho_direito;
}

public class MorseTree {
    Node raiz;

    public MorseTree() {
        this.raiz = new Node();
    }

    public void inserir(String codigoCaractere, char caractere) {
        Node no = this.raiz;
        for (int i = 0; i < codigoCaractere.length(); i++) {
            char c = codigoCaractere.charAt(i);
            if (c == '.') {
                if (no.filho_esquerdo == null) {
                    no.filho_esquerdo = new Node();
                }

                no = no.filho_esquerdo;
            } else if (c == '-') {
                if (no.filho_direito == null) {
                    no.filho_direito = new Node();
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

        Node no = this.raiz;
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

    public String codificar(String texto) {
        String normalizado = Normalizer.normalize(texto.toLowerCase(), Normalizer.Form.NFD);
        normalizado.replaceAll("\\p{M}", "");

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
        return codificar_helper(this.raiz, caractere, codigo) ? codigo.toString() : null;
    }

    public boolean codificarEm(char caractere, StringBuilder codigo) {
        return codificar_helper(this.raiz, caractere, codigo);
    }

    private boolean codificar_helper(Node no, char alvo, StringBuilder caminho) {
        if (no == null) {
            return false;
        }

        if (no.caractere == alvo) {
            return true;
        }

        caminho.append('.');
        Boolean encontrado = codificar_helper(no.filho_esquerdo, alvo, caminho);
        if (encontrado) {
            return true;
        }
        caminho.deleteCharAt(caminho.length() - 1);

        caminho.append('-');
        encontrado = codificar_helper(no.filho_direito, alvo, caminho);
        if (encontrado) {
            return true;
        }
        caminho.deleteCharAt(caminho.length() - 1);

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
        arvore.inserir(".-...", '&');
        arvore.inserir(".----", '1');
        arvore.inserir("-....", '6');
        arvore.inserir("-...-", '=');
        arvore.inserir("-..-.", '/');
        arvore.inserir("--...", '7');
        arvore.inserir("---..", '8');
        arvore.inserir("----.", '9');
        arvore.inserir("-----", '0');

        arvore.inserir(".-.-.-", '.');
        arvore.inserir("--..--", ',');
        arvore.inserir("---...", ':');
        arvore.inserir("..--..", '?');
        arvore.inserir(".----.", '\'');
        arvore.inserir("-....-", '-');
        arvore.inserir("-.--.-", '(');
        arvore.inserir(".-..-.", '"');
        arvore.inserir(".--.-.", '@');
        arvore.inserir("-.-.--", '!');
        return arvore;
    }
}
