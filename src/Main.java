import java.io.IOException;
import java.util.Scanner;

public class Main {
    
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
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    public static Grafo criarGrafoPadrao() {
        Grafo g = new Grafo(6);
        int[][] arestas = {{0,1}, {0,2}, {1,3}, {1,4}, {2,4}, {3,5}, {4,5}};
        for (int[] aresta : arestas) {
            g.adicionarAresta(aresta[0], aresta[1]);
        }
        return g;
    }

    public static Grafo criarGrafoArvore() {
        Grafo g = new Grafo(5);
        int[][] arestas = {{0,1}, {0,2}, {1,3}, {1,4}};
        for (int[] aresta : arestas) {
            g.adicionarAresta(aresta[0], aresta[1]);
        }
        return g;
    }

    public static Grafo criarGrafoDesconexoComVMenos1Arestas() {
        Grafo g = new Grafo(4);
        int[][] arestas = {{0,1}, {1,2}, {2,0}};
        for (int[] aresta : arestas) {
            g.adicionarAresta(aresta[0], aresta[1]);
        }
        return g;
    }

    public static Grafo criarGrafoDirecionadoPadrao() {
        Grafo g = new Grafo(4, true);
        int[][] arcos = {{0,1}, {0,2}, {1,3}, {2,3}};
        for (int[] arco : arcos) {
            g.adicionarAresta(arco[0], arco[1]);
        }
        return g;
    }

    public static void executarDemonstracaoEstudoDirigido() {
        System.out.println("================================================================");
        System.out.println("       DEMONSTRAÇÃO COMPLETA — ESTUDO DIRIGIDO (AULA 9)");
        System.out.println("================================================================");

        System.out.println("\n[PARTE II - ATIVIDADE 1: TESTES DE EHARVORE]");
        System.out.println("\n--- Caso 1.1: Grafo que é árvore válida ---");
        Grafo gArvore = criarGrafoArvore();
        gArvore.mostrarLista();
        gArvore.executarEhArvore();

        System.out.println("\n--- Caso 1.2: Grafo padrão com ciclos (|E| != |V| - 1) ---");
        Grafo gPadrao = criarGrafoPadrao();
        gPadrao.mostrarLista();
        gPadrao.executarEhArvore();

        System.out.println("\n--- Caso 1.3: Grafo desconexo com |E| == |V| - 1 (triângulo + vértice isolado) ---");
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
        System.out.println("[PARTE VI - ATIVIDADE 4: ARBORESCÊNCIA POR BFS (DIRECIONADO)]");
        Grafo gDir = criarGrafoDirecionadoPadrao();
        gDir.mostrarLista();
        System.out.println("\n--- Caso 4.1: Raiz 0 (todos alcançáveis) ---");
        gDir.executarArborescenciaBFS(0);

        System.out.println("\n--- Caso 4.2: Raiz 3 (vértices inalcançáveis -> falha) ---");
        gDir.executarArborescenciaBFS(3);
        System.out.println("================================================================");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grafo grafo = criarGrafoPadrao();

        while (true) {
            limparTela();

            System.out.println("=======================================");
            System.out.println("        ALGORITMOS DE GRAFOS");
            System.out.println("=======================================");
            System.out.println("Tipo atual: " + (grafo.isDirecionado() ? "Direcionado" : "Não Direcionado") + " (" + grafo.getNumVertices() + " vértices, " + grafo.getQuantidadeArestas() + " arestas/arcos)");
            System.out.println("1  - Mostrar Matriz de Adjacência");
            System.out.println("2  - Mostrar Lista de Adjacência");
            System.out.println("3  - Executar BFS (Fila)");
            System.out.println("4  - Executar DFS Recursiva");
            System.out.println("5  - Executar DFS com Pilha (Iterativa)");
            System.out.println("6  - Adicionar aresta");
            System.out.println("7  - Criar novo grafo");
            System.out.println("8  - Verificar se é Árvore (EhArvore)");
            System.out.println("9  - Gerar Árvore Geradora por BFS");
            System.out.println("10 - Gerar Árvore Geradora por DFS");
            System.out.println("11 - Comparar Árvores BFS e DFS");
            System.out.println("12 - Gerar Arborescência por BFS");
            System.out.println("13 - Carregar Grafo de Exemplo");
            System.out.println("14 - Executar Demonstração Completa (Estudo Dirigido)");
            System.out.println("0  - Sair");

            System.out.print("\nEscolha uma opção: ");
            String opcao = scanner.nextLine().trim();

            if (opcao.equals("0")) {
                System.out.println("Encerrando programa...");
                break;
            } else if (opcao.equals("1")) {
                grafo.mostrarMatriz();
            } else if (opcao.equals("2")) {
                grafo.mostrarLista();
            } else if (opcao.equals("3")) {
                System.out.print("Vértice de início: ");
                int inicio = Integer.parseInt(scanner.nextLine().trim());
                grafo.bfs(inicio);
            } else if (opcao.equals("4")) {
                System.out.print("Vértice de início: ");
                int inicio = Integer.parseInt(scanner.nextLine().trim());
                grafo.dfsRecursiva(inicio);
            } else if (opcao.equals("5")) {
                System.out.print("Vértice de início: ");
                int inicio = Integer.parseInt(scanner.nextLine().trim());
                grafo.dfsPilha(inicio);
            } else if (opcao.equals("6")) {
                System.out.print("Vértice U: ");
                int u = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Vértice V: ");
                int v = Integer.parseInt(scanner.nextLine().trim());
                grafo.adicionarAresta(u, v);
                System.out.println("Aresta/arco adicionado com sucesso!");
            } else if (opcao.equals("7")) {
                System.out.print("Número de vértices: ");
                int n = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("É direcionado? (1 - Sim, 0 - Não): ");
                int dir = Integer.parseInt(scanner.nextLine().trim());
                grafo = new Grafo(n, dir == 1);
                
                System.out.print("Número de arestas: ");
                int m = Integer.parseInt(scanner.nextLine().trim());
                
                for (int i = 0; i < m; i++) {
                    System.out.print("Vértice U: ");
                    int u = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Vértice V: ");
                    int v = Integer.parseInt(scanner.nextLine().trim());
                    grafo.adicionarAresta(u, v);
                }
            } else if (opcao.equals("8")) {
                grafo.executarEhArvore();
            } else if (opcao.equals("9")) {
                System.out.print("Vértice raiz: ");
                int inicio = Integer.parseInt(scanner.nextLine().trim());
                grafo.executarArvoreGeradoraBFS(inicio);
            } else if (opcao.equals("10")) {
                System.out.print("Vértice raiz: ");
                int inicio = Integer.parseInt(scanner.nextLine().trim());
                grafo.executarArvoreGeradoraDFS(inicio);
            } else if (opcao.equals("11")) {
                System.out.print("Vértice raiz para comparação: ");
                int inicio = Integer.parseInt(scanner.nextLine().trim());
                grafo.compararBfsEDfs(inicio);
            } else if (opcao.equals("12")) {
                System.out.print("Vértice raiz para arborescência: ");
                int raiz = Integer.parseInt(scanner.nextLine().trim());
                grafo.executarArborescenciaBFS(raiz);
            } else if (opcao.equals("13")) {
                System.out.println("\nEscolha o exemplo:");
                System.out.println("1 - Grafo Padrão com Ciclos (6 vértices, 7 arestas)");
                System.out.println("2 - Árvore Válida (5 vértices, 4 arestas)");
                System.out.println("3 - Grafo Desconexo com |E| = |V| - 1 (4 vértices, 3 arestas)");
                System.out.println("4 - Grafo Direcionado para Arborescência (4 vértices, 4 arcos)");
                System.out.print("Opção: ");
                String sub = scanner.nextLine().trim();
                if (sub.equals("1")) {
                    grafo = criarGrafoPadrao();
                    System.out.println("Grafo Padrão carregado.");
                } else if (sub.equals("2")) {
                    grafo = criarGrafoArvore();
                    System.out.println("Grafo Árvore carregado.");
                } else if (sub.equals("3")) {
                    grafo = criarGrafoDesconexoComVMenos1Arestas();
                    System.out.println("Grafo Desconexo carregado.");
                } else if (sub.equals("4")) {
                    grafo = criarGrafoDirecionadoPadrao();
                    System.out.println("Grafo Direcionado carregado.");
                }
            } else if (opcao.equals("14")) {
                executarDemonstracaoEstudoDirigido();
            } else {
                System.out.println("Opção inválida.");
            }

            System.out.print("\nPressione ENTER para continuar...");
            scanner.nextLine();
        }
        
        scanner.close();
    }
}