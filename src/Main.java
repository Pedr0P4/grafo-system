import java.io.IOException;
import java.util.Scanner;

/**
 * Classe principal do sistema de Grafos.
 * Integra todas as atividades da:
 * 1) LISTA DE EXERCÍCIOS (Matriz, Lista, BFS, DFS Recursiva, DFS com Pilha, Questão 10, Desafio Q13).
 * 2) ESTUDO DIRIGIDO - AULA 9 (EhArvore, Árvore Geradora BFS, Árvore Geradora DFS, Comparação BFS x DFS, Arborescência).
 */
public class Main {
    
    /**
     * Limpa a tela do console para melhor legibilidade.
     */
    public static void limparTela() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            for (int i = 0; i < 30; i++) System.out.println();
        }
    }

    /**
     * Cria o grafo simples e não direcionado padrão de toda a Lista de Exercícios:
     * V = {0, 1, 2, 3, 4, 5} (n = 6)
     * E = {(0,1), (0,2), (1,3), (1,4), (2,4), (3,5), (4,5)} (m = 7)
     */
    public static Grafo criarGrafoPadrao() {
        Grafo g = new Grafo(6);
        int[][] arestas = {{0,1}, {0,2}, {1,3}, {1,4}, {2,4}, {3,5}, {4,5}};
        for (int[] aresta : arestas) {
            g.adicionarAresta(aresta[0], aresta[1]);
        }
        return g;
    }

    /**
     * Cria o grafo da QUESTÃO 10: Grafo original acrescido da aresta (2,5).
     * V = {0, 1, 2, 3, 4, 5} (n = 6)
     * E = {(0,1), (0,2), (1,3), (1,4), (2,4), (3,5), (4,5), (2,5)} (m = 8)
     */
    public static Grafo criarGrafoQuestao10() {
        Grafo g = criarGrafoPadrao();
        g.adicionarAresta(2, 5);
        return g;
    }

    /**
     * Cria grafo que é uma Árvore válida para testes do Algoritmo 1 (Aula 9):
     * V = 5, E = 4, acíclico e conexo.
     */
    public static Grafo criarGrafoArvore() {
        Grafo g = new Grafo(5);
        int[][] arestas = {{0,1}, {0,2}, {1,3}, {1,4}};
        for (int[] aresta : arestas) {
            g.adicionarAresta(aresta[0], aresta[1]);
        }
        return g;
    }

    /**
     * Cria grafo desconexo com |E| == |V| - 1 para demonstrar por que
     * a contagem de arestas isolada NÃO garante que o grafo seja árvore:
     * V = {0, 1, 2, 3} (n = 4)
     * E = {(0,1), (1,2), (2,0)} (m = 3 = 4 - 1), mas possui ciclo e vértice 3 isolado.
     */
    public static Grafo criarGrafoDesconexoComVMenos1Arestas() {
        Grafo g = new Grafo(4);
        int[][] arestas = {{0,1}, {1,2}, {2,0}};
        for (int[] aresta : arestas) {
            g.adicionarAresta(aresta[0], aresta[1]);
        }
        return g;
    }

    /**
     * Cria grafo direcionado para testes do Algoritmo 4 (Arborescência por BFS):
     * V = {0, 1, 2, 3}
     * Arcos = {(0,1), (0,2), (1,3), (2,3)}
     */
    public static Grafo criarGrafoDirecionadoPadrao() {
        Grafo g = new Grafo(4, true);
        int[][] arcos = {{0,1}, {0,2}, {1,3}, {2,3}};
        for (int[] arco : arcos) {
            g.adicionarAresta(arco[0], arco[1]);
        }
        return g;
    }

    /**
     * Execução completa da QUESTÃO 10 da Lista de Exercícios:
     * Adiciona a aresta (2,5) ao grafo original, executa todas as rotinas e explica as diferenças.
     */
    public static void executarQuestao10() {
        System.out.println("================================================================");
        System.out.println("   QUESTÃO 10 — MODIFICAÇÃO DO GRAFO (ADICIONANDO ARESTA 2--5)   ");
        System.out.println("================================================================");

        Grafo gModificado = criarGrafoQuestao10();

        System.out.println("\n[1] MATRIZ DE ADJACÊNCIA COM A NOVA ARESTA (2,5):");
        gModificado.mostrarMatriz();

        System.out.println("\n[2] LISTA DE ADJACÊNCIA COM A NOVA ARESTA (2,5):");
        gModificado.mostrarLista();

        System.out.println("\n[3] BUSCA EM LARGURA (BFS) INICIANDO EM 0:");
        gModificado.bfs(0);

        System.out.println("\n[4] BUSCA EM PROFUNDIDADE RECURSIVA INICIANDO EM 0:");
        gModificado.dfsRecursiva(0);

        System.out.println("\n[5] BUSCA EM PROFUNDIDADE COM PILHA INICIANDO EM 0:");
        gModificado.dfsPilha(0);

        System.out.println("\n----------------------------------------------------------------");
        System.out.println("RELATÓRIO COMPARATIVO DAS ALTERAÇÕES OBSERVADAS (QUESTÃO 10):");
        System.out.println("----------------------------------------------------------------");
        System.out.println("1. Matriz de Adjacência: as posições [2][5] e [5][2] passaram de 0 para 1.");
        System.out.println("2. Lista de Adjacência:");
        System.out.println("   - Vértice 2 agora possui vizinhos: [0, 4, 5] (anteriormente [0, 4]).");
        System.out.println("   - Vértice 5 agora possui vizinhos: [2, 3, 4] (anteriormente [3, 4]).");
        System.out.println("3. Busca em Largura (BFS):");
        System.out.println("   - A distância mínima entre 0 e 5 foi reduzida de 3 para 2 arestas (caminho 0 -> 2 -> 5).");
        System.out.println("   - Ao desenfileirar o vértice 2, o vértice 5 é descoberto e enfileirado imediatamente,");
        System.out.println("     alterando os momentos de visita e o nível dos vértices na árvore geradora da BFS.");
        System.out.println("4. Buscas em Profundidade (DFS Recursiva e com Pilha):");
        System.out.println("   - A presença da aresta (2,5) introduz novos ciclos fundamentais no grafo (ciclo 0-2-5-3-1-0 e ciclo 2-4-5-2).");
        System.out.println("================================================================");
    }

    /**
     * Execução interativa da QUESTÃO 13 (Desafio: Entrada de Grafo pelo Usuário):
     * Lê número de vértices, arestas e vértice inicial, e executa Matriz, Lista, BFS e as duas DFS.
     */
    public static void executarDesafioQuestao13(Scanner scanner) {
        System.out.println("\n========================================================");
        System.out.println("   QUESTÃO 13 (DESAFIO) — ENTRADA DE GRAFO PELO USUÁRIO  ");
        System.out.println("========================================================");

        try {
            System.out.print("Informe o número de vértices (|V|): ");
            int n = Integer.parseInt(scanner.nextLine().trim());
            if (n <= 0) {
                System.out.println("Número de vértices deve ser maior que 0.");
                return;
            }

            Grafo g = new Grafo(n, false);

            System.out.print("Informe o número de arestas (|E|): ");
            int m = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("\nDigite as arestas no formato: vérticeU vérticeV (ex: 0 1):");
            for (int i = 0; i < m; i++) {
                System.out.print("Aresta " + (i + 1) + " de " + m + ": ");
                String linha = scanner.nextLine().trim();
                String[] partes = linha.split("\\s+");
                if (partes.length >= 2) {
                    int u = Integer.parseInt(partes[0]);
                    int v = Integer.parseInt(partes[1]);
                    if (u >= 0 && u < n && v >= 0 && v < n) {
                        g.adicionarAresta(u, v);
                    } else {
                        System.out.println("Vértices inválidos! Devem estar entre 0 e " + (n - 1) + ". Aresta ignorada.");
                    }
                }
            }

            System.out.print("\nInforme o vértice inicial das buscas (0 a " + (n - 1) + "): ");
            int inicio = Integer.parseInt(scanner.nextLine().trim());
            if (inicio < 0 || inicio >= n) {
                inicio = 0;
                System.out.println("Vértice inicial inválido. Utilizando 0 como padrão.");
            }

            System.out.println("\n--- [PASSO 1] Matriz de Adjacência Gerada ---");
            g.mostrarMatriz();

            System.out.println("\n--- [PASSO 2] Lista de Adjacência Gerada ---");
            g.mostrarLista();

            System.out.println("\n--- [PASSO 3] Execução da BFS ---");
            g.bfs(inicio);

            System.out.println("\n--- [PASSO 4] Execução da DFS Recursiva ---");
            g.dfsRecursiva(inicio);

            System.out.println("\n--- [PASSO 5] Execução da DFS com Pilha ---");
            g.dfsPilha(inicio);

        } catch (NumberFormatException e) {
            System.out.println("Erro na leitura dos dados. Certifique-se de digitar números inteiros válidos.");
        }
    }

    /**
     * Demonstração completa cobrindo todas as partes do ESTUDO DIRIGIDO (AULA 9).
     */
    public static void executarDemonstracaoEstudoDirigido() {
        System.out.println("================================================================");
        System.out.println("       DEMONSTRAÇÃO COMPLETA — ESTUDO DIRIGIDO (AULA 9)");
        System.out.println("================================================================");

        System.out.println("\n[PARTE II - ATIVIDADE 1: TESTES DO ALGORITMO EHARVORE]");
        System.out.println("\n--- Caso 1.1: Grafo que é árvore válida (|E| = |V|-1 e Conexo) ---");
        Grafo gArvore = criarGrafoArvore();
        gArvore.mostrarLista();
        gArvore.executarEhArvore();

        System.out.println("\n--- Caso 1.2: Grafo padrão com ciclos (|E| != |V| - 1) ---");
        Grafo gPadrao = criarGrafoPadrao();
        gPadrao.mostrarLista();
        gPadrao.executarEhArvore();

        System.out.println("\n--- Caso 1.3: Grafo desconexo com |E| == |V| - 1 (triângulo + vértice isolado) ---");
        System.out.println("Contraexemplo demonstrando por que a contagem V-1 isolada NÃO basta:");
        Grafo gDesconexo = criarGrafoDesconexoComVMenos1Arestas();
        gDesconexo.mostrarLista();
        gDesconexo.executarEhArvore();

        System.out.println("\n----------------------------------------------------------------");
        System.out.println("[PARTE III - ATIVIDADE 2: ÁRVORE GERADORA POR BFS]");
        gPadrao.executarArvoreGeradoraBFS(0);

        System.out.println("\n----------------------------------------------------------------");
        System.out.println("[PARTE IV - ATIVIDADE 3: ÁRVORE GERADORA POR DFS]");
        gPadrao.executarArvoreGeradoraDFS(0);

        System.out.println("\n----------------------------------------------------------------");
        System.out.println("[PARTE V: COMPARAÇÃO ESTRUTURAL ENTRE BFS E DFS]");
        gPadrao.compararBfsEDfs(0);

        System.out.println("\n----------------------------------------------------------------");
        System.out.println("[PARTE VI - ATIVIDADE 4: ARBORESCÊNCIA POR BFS (GRAFO DIRECIONADO)]");
        Grafo gDir = criarGrafoDirecionadoPadrao();
        gDir.mostrarLista();
        System.out.println("\n--- Caso 4.1: Raiz 0 (todos os vértices alcançáveis -> Sucesso) ---");
        gDir.executarArborescenciaBFS(0);

        System.out.println("\n--- Caso 4.2: Raiz 3 (vértices inalcançáveis -> Falha detectada) ---");
        gDir.executarArborescenciaBFS(3);
        System.out.println("================================================================");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grafo grafo = criarGrafoPadrao();

        while (true) {
            limparTela();

            System.out.println("================================================================");
            System.out.println("                     SISTEMA DE GRAFOS                          ");
            System.out.println("================================================================");
            System.out.println("Grafo em memória: " + (grafo.isDirecionado() ? "Direcionado" : "Não Direcionado") 
                    + " (" + grafo.getNumVertices() + " vértices, " + grafo.getQuantidadeArestas() + " arestas/arcos)");
            System.out.println("----------------------------------------------------------------");
            System.out.println("[ PARTE 1 — LISTA DE EXERCÍCIOS ]");
            System.out.println(" 1 - Matriz de Adjacência (Questão 1)");
            System.out.println(" 2 - Lista de Adjacência (Questão 2)");
            System.out.println(" 3 - Executar BFS com Rastreamento de Fila (Questões 3 e 7)");
            System.out.println(" 4 - Executar DFS Recursiva (Questão 4)");
            System.out.println(" 5 - Executar DFS com Pilha (Questões 5 e 8)");
            System.out.println(" 6 - Mostrar Grafo Completo - Matriz e Lista (Questão 9)");
            System.out.println(" 7 - Questão 10: Grafo Modificado (+ aresta 2--5)");
            System.out.println(" 8 - Questão 13: Desafio com Grafo do Usuário");
            System.out.println("----------------------------------------------------------------");
            System.out.println("[ PARTE 2 — ESTUDO DIRIGIDO (AULA 9) ]");
            System.out.println(" 9 - Algoritmo 1: Verificar se é Árvore (EhArvore)");
            System.out.println("10 - Algoritmo 2: Árvore Geradora por BFS");
            System.out.println("11 - Algoritmo 3: Árvore Geradora por DFS com Pilha");
            System.out.println("12 - Comparar Árvores BFS e DFS (Tabela e Métricas)");
            System.out.println("13 - Algoritmo 4: Arborescência por BFS (Dígrafo)");
            System.out.println("14 - Executar Demonstração Completa da Aula 9");
            System.out.println("----------------------------------------------------------------");
            System.out.println("[ CONFIGURAÇÃO ]");
            System.out.println("15 - Redefinir para o Grafo Padrão da Lista");
            System.out.println(" 0 - Sair");
            System.out.println("================================================================");

            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine().trim();

            if (opcao.equals("0")) {
                System.out.println("Encerrando programa...");
                break;
            } else if (opcao.equals("1")) {
                grafo.mostrarMatriz();
            } else if (opcao.equals("2")) {
                grafo.mostrarLista();
            } else if (opcao.equals("3")) {
                System.out.print("Vértice de início (0 a " + (grafo.getNumVertices() - 1) + "): ");
                try {
                    int inicio = Integer.parseInt(scanner.nextLine().trim());
                    grafo.bfs(inicio);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida.");
                }
            } else if (opcao.equals("4")) {
                System.out.print("Vértice de início (0 a " + (grafo.getNumVertices() - 1) + "): ");
                try {
                    int inicio = Integer.parseInt(scanner.nextLine().trim());
                    grafo.dfsRecursiva(inicio);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida.");
                }
            } else if (opcao.equals("5")) {
                System.out.print("Vértice de início (0 a " + (grafo.getNumVertices() - 1) + "): ");
                try {
                    int inicio = Integer.parseInt(scanner.nextLine().trim());
                    grafo.dfsPilha(inicio);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida.");
                }
            } else if (opcao.equals("6")) {
                grafo.mostrarGrafo();
            } else if (opcao.equals("7")) {
                executarQuestao10();
            } else if (opcao.equals("8")) {
                executarDesafioQuestao13(scanner);
            } else if (opcao.equals("9")) {
                grafo.executarEhArvore();
            } else if (opcao.equals("10")) {
                System.out.print("Vértice raiz (0 a " + (grafo.getNumVertices() - 1) + "): ");
                try {
                    int raiz = Integer.parseInt(scanner.nextLine().trim());
                    grafo.executarArvoreGeradoraBFS(raiz);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida.");
                }
            } else if (opcao.equals("11")) {
                System.out.print("Vértice raiz (0 a " + (grafo.getNumVertices() - 1) + "): ");
                try {
                    int raiz = Integer.parseInt(scanner.nextLine().trim());
                    grafo.executarArvoreGeradoraDFS(raiz);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida.");
                }
            } else if (opcao.equals("12")) {
                System.out.print("Vértice raiz para comparação (0 a " + (grafo.getNumVertices() - 1) + "): ");
                try {
                    int raiz = Integer.parseInt(scanner.nextLine().trim());
                    grafo.compararBfsEDfs(raiz);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida.");
                }
            } else if (opcao.equals("13")) {
                System.out.print("Vértice raiz para arborescência: ");
                try {
                    int raiz = Integer.parseInt(scanner.nextLine().trim());
                    grafo.executarArborescenciaBFS(raiz);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida.");
                }
            } else if (opcao.equals("14")) {
                executarDemonstracaoEstudoDirigido();
            } else if (opcao.equals("15")) {
                grafo = criarGrafoPadrao();
                System.out.println("Grafo redefinido para o Grafo Padrão da Lista (6 vértices, 7 arestas).");
            } else {
                System.out.println("Opção inválida.");
            }

            System.out.print("\nPressione ENTER para continuar...");
            scanner.nextLine();
        }
        
        scanner.close();
    }
}