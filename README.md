# 🌳 MorseTree

Codificador e decodificador de **código Morse** com uma **árvore binária interativa**, feito em **JavaFX**. A árvore inteira do alfabeto morse é desenhada na tela, se adapta a qualquer resolução, e reage em tempo real conforme você digita — mostrando visualmente o caminho percorrido para cada caractere.

![Java](https://img.shields.io/badge/Java-11+-orange?logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-27-blue?logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/build-Maven-C71A36?logo=apachemaven&logoColor=white)

## Funcionalidades

- **Codificação e decodificação simultâneas** — escreva uma mensagem normal e veja o código morse aparecer (e vice-versa), com os dois campos sempre sincronizados.
- **Árvore binária completa na tela** — todo o alfabeto (letras, números e separador de palavra) é renderizado como uma árvore, onde ir para a esquerda é um ponto (`.`) e para a direita é um traço (`-`).
- **Sempre cabe na tela** — a árvore recalcula sua escala automaticamente conforme o tamanho da janela, então ela nunca fica cortada, independente da resolução do monitor.
- **Percurso visual em tempo real** — enquanto você digita, o caminho da raiz até o caractere atual é destacado na árvore:
  - 🟠 **laranja** → caminho sendo percorrido
  - 🟢 **verde** → caractere encontrado
  - 🔴 **vermelho** → código morse inválido
- **Clique para digitar** — clicar em qualquer letra/número da árvore insere esse caractere direto no campo de mensagem.

## 🖥️ Como executar

### Pré-requisitos

- [JDK 11+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)

### Rodando o projeto

```bash
mvn clean javafx:run
```

O Maven baixa o JavaFX automaticamente (via dependência `javafx-controls`) e abre a aplicação já maximizada.

## 🌲 Como a árvore funciona

O código Morse é naturalmente uma árvore binária: partindo da raiz, cada símbolo decide para qual lado ir.

```
        ┌─┐
    .───┤ ├───-
   ┌┴┐       ┌┴┐
   │e│       │t│
  .┴.-      .┴.-
 ┌┴┐ ┌┴┐  ┌┴┐ ┌┴┐
 │i│ │a│  │n│ │m│
 ...
```

- Ir para a **esquerda** = ponto (`.`)
- Ir para a **direita** = traço (`-`)
- O caractere de cada nó é o que aquela sequência de pontos/traços representa

Essa estrutura é montada em [`MorseTree.java`](src/main/java/com/morsetree/MorseTree.java) e desenhada em [`MorseTreeView.java`](src/main/java/com/morsetree/MorseTreeView.java).

## 📁 Estrutura do projeto

```
morsetree/
├── pom.xml
└── src/main/java/com/morsetree/
    ├── App.java            # Interface principal, campos de texto e integração com a árvore
    ├── MorseTree.java       # Estrutura da árvore + lógica de codificação/decodificação
    ├── MorseTreeView.java    # Desenho da árvore, escala automática, destaque e cliques
    ├── Node.java             # Nó da árvore (caractere + filhos esquerdo/direito)
    └── module-info.java
```

## 🛠️ Tecnologias

- **Java 11**
- **JavaFX** (Controls) para a interface gráfica
- **Maven** com o plugin `javafx-maven-plugin` para build e execução

## 👥 Autores

- [Joao Mosson](https://github.com/jpgmosson)
- [edu292](https://github.com/edu292)
