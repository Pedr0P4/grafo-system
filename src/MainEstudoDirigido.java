import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * UFRN | DIM0549 – TEORIA DOS GRAFOS | ESTUDO DIRIGIDO
 * Estudo dirigido – construção e percurso em grafos
 * Grafos direcionados, ponderados e direcionados ponderados | BFS e DFS
 *
 * Módulo executável dedicado às Etapas 1 a 9 do documento.
 */
public class MainEstudoDirigido {

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
            for (int i = 0; i < 25; i++) System.out.println();
        }
    }

    // =========================================================================
    // SEÇÃO 3: ETAPA 1 – GRAFO DIRECIONADO
    // =========================================================================
    public static void executarEtapa1() {
        System.out.println("========================================================================");
        System.out.println("               3. ETAPA 1 – GRAFO DIRECIONADO (FIGURA 1)                 ");
        System.out.println("========================================================================");
        System.out.println("Arestas orientadas: A→B; A→C; B→D; C→D; D→E");

        Grafo g1 = Grafo.criarGrafoEtapa1();

        System.out.println("\n[PASSO 3] Conferência da Representação (u → G.adj[u]):");
        g1.conferirRepresentacao();

        System.out.println("\n--- [ATIVIDADE 1] Transcrição para Lista de Adjacência: ---");
        System.out.println("A → [B, C]");
        System.out.println("B → [D]");
        System.out.println("C → [D]");
        System.out.println("D → [E]");
        System.out.println("E → []");

        System.out.println("\n--- [ATIVIDADE 2] Explicação Conceitual: ---");
        System.out.println("Cada aresta direcionada (u, v) é registrada EXCLUSIVAMENTE na lista de u.");
        System.out.println("Não há inserção na lista de v, garantindo que o sentido u → v seja respeitado.");
        System.out.println("Como o grafo não é ponderado, cada entrada armazena apenas o vértice vizinho.");

        System.out.println("\n--- [ATIVIDADE 3] Buscas a partir do vértice inicial 'A': ---");
        List<Vertice> bfs1 = g1.bfsEstudoDirigido("A");
        List<Vertice> dfs1 = g1.dfsEstudoDirigido("A");

        System.out.println("\nResumo da Etapa 1:");
        System.out.println("Ordem BFS a partir de A: " + bfs1);
        System.out.println("Ordem DFS a partir de A: " + dfs1);
    }

    // =========================================================================
    // SEÇÃO 4: ETAPA 2 – GRAFO PONDERADO
    // =========================================================================
    public static void executarEtapa2() {
        System.out.println("========================================================================");
        System.out.println("               4. ETAPA 2 – GRAFO PONDERADO (FIGURA 2)                  ");
        System.out.println("========================================================================");
        System.out.println("Arestas não direcionadas com pesos:");
        System.out.println("A—B(4); A—C(2); B—C(1); B—D(5); C—D(8); C—E(10); D—E(2)");

        Grafo g2 = Grafo.criarGrafoEtapa2();

        System.out.println("\n[PASSO 3] Conferência da Lista Ponderada (u → [(vizinho, peso)]):");
        g2.conferirRepresentacao();

        System.out.println("\n--- [ATIVIDADE 1] Transcrição para Lista de Adjacência: ---");
        System.out.println("A → [(B, 4), (C, 2)]");
        System.out.println("B → [(A, 4), (C, 1), (D, 5)]");
        System.out.println("C → [(A, 2), (B, 1), (D, 8), (E, 10)]");
        System.out.println("D → [(B, 5), (C, 8), (E, 2)]");
        System.out.println("E → [(C, 10), (D, 2)]");

        System.out.println("\n--- [ATIVIDADE 2] Explicação Conceitual: ---");
        System.out.println("Como o grafo é NÃO DIRECIONADO, cada aresta entre u e v é inserida simetricamente");
        System.out.println("nas duas listas: (v, peso) em G.adj[u] e (u, peso) em G.adj[v].");
        System.out.println("Por ser ponderado, cada entrada armazena a tupla (vizinho, peso).");

        System.out.println("\n--- [ATIVIDADE 3] Buscas a partir do vértice inicial 'A': ---");
        System.out.println("(Observação: BFS e DFS percorrem vizinhos sem priorizar o peso)");
        List<Vertice> bfs2 = g2.bfsEstudoDirigido("A");
        List<Vertice> dfs2 = g2.dfsEstudoDirigido("A");

        System.out.println("\nResumo da Etapa 2:");
        System.out.println("Ordem BFS a partir de A: " + bfs2);
        System.out.println("Ordem DFS a partir de A: " + dfs2);
    }

    // =========================================================================
    // SEÇÃO 5: ETAPA 3 – GRAFO DIRECIONADO E PONDERADO
    // =========================================================================
    public static void executarEtapa3() {
        System.out.println("========================================================================");
        System.out.println("         5. ETAPA 3 – GRAFO DIRECIONADO E PONDERADO (FIGURA 3)          ");
        System.out.println("========================================================================");
        System.out.println("Arcos com pesos: A→B(4); A→C(2); C→B(1); B→D(5); C→D(8); C→E(10); D→E(2)");

        Grafo g3 = Grafo.criarGrafoEtapa3();

        System.out.println("\n[PASSO 3] Verificação das Saídas de Cada Vértice:");
        g3.conferirRepresentacao();

        System.out.println("\n--- [ATIVIDADE 1] Transcrição para Lista de Adjacência: ---");
        System.out.println("A → [(B, 4), (C, 2)]");
        System.out.println("B → [(D, 5)]");
        System.out.println("C → [(B, 1), (D, 8), (E, 10)]");
        System.out.println("D → [(E, 2)]");
        System.out.println("E → []");

        System.out.println("\n--- [ATIVIDADE 2] Explicação Conceitual: ---");
        System.out.println("Cada arco u → v com custo p é inserido como (v, p) exclusivamente na lista G.adj[u].");
        System.out.println("Não há inserção na lista de v. Exemplo: C → B com peso 1 não implica B → C.");

        System.out.println("\n--- [ATIVIDADE 3] Buscas a partir do vértice inicial 'A': ---");
        List<Vertice> bfs3 = g3.bfsEstudoDirigido("A");
        List<Vertice> dfs3 = g3.dfsEstudoDirigido("A");

        System.out.println("\nResumo da Etapa 3:");
        System.out.println("Ordem BFS a partir de A: " + bfs3);
        System.out.println("Ordem DFS a partir de A: " + dfs3);
    }

    // =========================================================================
    // SEÇÃO 8: COMPARAÇÃO DOS PERCURSOS E INTERPRETAÇÃO
    // =========================================================================
    public static void executarSecao8() {
        System.out.println("========================================================================");
        System.out.println("         8. COMPARAÇÃO DOS PERCURSOS E INTERPRETAÇÃO (TABELA)          ");
        System.out.println("========================================================================");

        Grafo g1 = Grafo.criarGrafoEtapa1();
        Grafo g2 = Grafo.criarGrafoEtapa2();
        Grafo g3 = Grafo.criarGrafoEtapa3();

        List<Vertice> bfs1 = g1.bfsEstudoDirigido("A");
        List<Vertice> dfs1 = g1.dfsEstudoDirigido("A");
        List<Vertice> bfs2 = g2.bfsEstudoDirigido("A");
        List<Vertice> dfs2 = g2.dfsEstudoDirigido("A");
        List<Vertice> bfs3 = g3.bfsEstudoDirigido("A");
        List<Vertice> dfs3 = g3.dfsEstudoDirigido("A");

        System.out.println("\n+--------------------------------+----------------------------+----------------------------+");
        System.out.println("| Tipo de Grafo                  | Ordem BFS a partir de A    | Ordem DFS a partir de A    |");
        System.out.println("+--------------------------------+----------------------------+----------------------------+");
        System.out.printf("| %-30s | %-26s | %-26s |\n", "Direcionado", bfs1.toString(), dfs1.toString());
        System.out.printf("| %-30s | %-26s | %-26s |\n", "Ponderado", bfs2.toString(), dfs2.toString());
        System.out.printf("| %-30s | %-26s | %-26s |\n", "Direcionado e ponderado", bfs3.toString(), dfs3.toString());
        System.out.println("+--------------------------------+----------------------------+----------------------------+");

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("RESPOSTAS FUNDAMENTADAS ÀS PERGUNTAS PARA ANÁLISE (SEÇÃO 8):");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("1. BFS e DFS visitam necessariamente os vértices na mesma ordem? Justifique.");
        System.out.println("   R: NÃO. A BFS explora por camadas radiais (níveis de distância da raiz em número");
        System.out.println("      de arestas) usando uma fila FIFO. A DFS explora ramos em profundidade máxima antes");
        System.out.println("      de retornar (backtracking), usando pilha LIFO/chamadas recursivas.");
        System.out.println("      No grafo direcionado, por exemplo, BFS visita [A, B, C, D, E] e DFS visita [A, B, D, E, C].");

        System.out.println("\n2. O que pode mudar em um percurso quando uma aresta passa de não direcionada para direcionada?");
        System.out.println("   R: A restrição de sentido elimina a possibilidade de retorno simétrico (v -> u).");
        System.out.println("      Isso altera os graus de entrada e saída, pode tornar vértices inalcançáveis a partir de");
        System.out.println("      determinadas origens e modifica drasticamente a árvore de busca gerada.");

        System.out.println("\n3. Por que o peso de uma aresta não é utilizado para ordenar a fila de BFS ou as chamadas da DFS?");
        System.out.println("   R: Porque BFS e DFS são algoritmos estritamente topológicos: exploram apenas a conectividade");
        System.out.println("      (existência da aresta), desconsiderando grandezas de custo w(e). Para priorizar pesos,");
        System.out.println("      utiliza-se fila de prioridade com o algoritmo de Dijkstra ou Busca de Custo Uniforme.");

        System.out.println("\n4. No grafo ponderado, o caminho com menos arestas é sempre o de menor custo? Apresente um contraexemplo.");
        System.out.println("   R: NÃO. Contraexemplo no Grafo Direcionado e Ponderado (Figura 3):");
        System.out.println("      - Caminho BFS: A → C → E (2 arestas, custo ponderado 2 + 10 = 12).");
        System.out.println("      - Caminho de menor custo (Dijkstra): A → C → B → D → E (4 arestas, custo 2 + 1 + 5 + 2 = 10).");
        System.out.println("      Portanto, o caminho com mais arestas possui custo estritamente menor.");

        System.out.println("\n5. Como adaptar o programa para ler do teclado os vértices, as arestas e seus pesos?");
        System.out.println("   R: Utilizando a classe Scanner para ler o total de vértices/arestas e em seguida iterar lendo");
        System.out.println("      triplas no formato 'origem destino peso', chamando adicionarArestaPonderada(u, v, peso).");

        System.out.println("\n6. Como tratar vértices não alcançáveis a partir da origem?");
        System.out.println("   R: Ao término da busca, verifica-se o mapa/vetor de visitados. Se visitado[v] for false,");
        System.out.println("      o vértice é inalcançável. Se o objetivo for cobrir todo o grafo (floresta de busca),");
        System.out.println("      itera-se sobre todos os vértices de V reiniciando a busca para cada vértice não visitado.");
        System.out.println("========================================================================");
    }

    // =========================================================================
    // SEÇÃO 9: DESAFIO DE INTEGRAÇÃO – DIJKSTRA E DFS ITERATIVA
    // =========================================================================
    public static void executarSecao9() {
        System.out.println("========================================================================");
        System.out.println(" 9. DESAFIO DE INTEGRAÇÃO – CONECTANDO COM A AULA DE DIJKSTRA           ");
        System.out.println("========================================================================");
        System.out.println("Aplicando o algoritmo de Dijkstra a partir de A no Grafo Direcionado e Ponderado:");

        Grafo g3 = Grafo.criarGrafoEtapa3();
        Grafo.ResultadoDijkstra dijkstra = g3.dijkstra("A");

        System.out.println("\n[VETOR DE DISTÂNCIAS MÍNIMAS A PARTIR DE A]:");
        for (Map.Entry<Vertice, Integer> entry : dijkstra.distancias.entrySet()) {
            System.out.println("  dist[" + entry.getKey() + "] = " + entry.getValue());
        }

        System.out.println("\n[VETOR DE PREDECESSORES]:");
        for (Map.Entry<Vertice, Vertice> entry : dijkstra.predecessores.entrySet()) {
            System.out.println("  pred[" + entry.getKey() + "] = " + (entry.getValue() != null ? entry.getValue() : "NULO (Origem)"));
        }

        Vertice destinoE = g3.getVerticePorRotulo("E");
        List<Vertice> caminhoDijkstra = dijkstra.reconstruirCaminho(destinoE);
        int custoDijkstra = dijkstra.distancias.get(destinoE);

        System.out.println("\n[COMPARAÇÃO: DIJKSTRA vs BFS (Destino = E)]:");
        System.out.println("1) Caminho por Dijkstra (Custo Mínimo): " + caminhoDijkstra);
        System.out.println("   - Quantidade de arestas: " + (caminhoDijkstra.size() - 1));
        System.out.println("   - Custo ponderado total: " + custoDijkstra + " (2 + 1 + 5 + 2 = 10)");

        Vertice origemA = g3.getVerticePorRotulo("A");
        List<Vertice> caminhoBFS = g3.encontrarCaminhoBFS(origemA, destinoE);
        int custoBFS = g3.calcularCustoCaminho(caminhoBFS);

        System.out.println("\n2) Caminho por Árvore de Busca BFS: " + caminhoBFS);
        System.out.println("   - Quantidade de arestas: " + (caminhoBFS.size() - 1));
        System.out.println("   - Custo ponderado total: " + custoBFS + " (2 + 10 = 12)");

        System.out.println("\nCONCLUSÃO DO DESAFIO:");
        System.out.println("A BFS encontra o caminho com o MENOR NÚMERO DE ARESTAS (2 arestas: A → C → E).");
        System.out.println("O Dijkstra encontra o caminho de MENOR CUSTO PONDERADO (custo 10: A → C → B → D → E).");
        System.out.println("Isso comprova que em grafos ponderados, a BFS NÃO garante custo mínimo!");

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("[EXTENSÃO OPCIONAL: DFS ITERATIVA COM PILHA EXPLÍCITA]");
        System.out.println("------------------------------------------------------------------------");
        List<Vertice> dfsIterativa = g3.dfsIterativaEstudoDirigido("A");
        System.out.println("Ordem produzida pela DFS Iterativa com pilha explícita: " + dfsIterativa);
        System.out.println("Equivalência com a DFS Recursiva: " + dfsIterativa.equals(g3.dfsEstudoDirigido("A")));
        System.out.println("========================================================================");
    }

    public static void executarTudo() {
        executarEtapa1();
        System.out.println();
        executarEtapa2();
        System.out.println();
        executarEtapa3();
        System.out.println();
        executarSecao8();
        System.out.println();
        executarSecao9();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            limparTela();
            System.out.println("========================================================================");
            System.out.println("        UFRN | DIM0549 – TEORIA DOS GRAFOS | ESTUDO DIRIGIDO            ");
            System.out.println("         Construção e Percurso em Grafos | BFS, DFS e Dijkstra          ");
            System.out.println("========================================================================");
            System.out.println(" 1 - Etapa 1: Grafo Direcionado (Construção, Conferência, BFS e DFS)");
            System.out.println(" 2 - Etapa 2: Grafo Ponderado (Construção, Conferência, BFS e DFS)");
            System.out.println(" 3 - Etapa 3: Grafo Direcionado e Ponderado (Construção, Conferência, BFS e DFS)");
            System.out.println(" 4 - Seção 8: Comparação dos Percursos (Tabela e Perguntas 1 a 6)");
            System.out.println(" 5 - Seção 9: Desafio de Dijkstra + Comparação BFS + DFS Iterativa");
            System.out.println(" 6 - Executar Demonstração Completa (Etapas 1 a 9)");
            System.out.println(" 0 - Sair");
            System.out.println("========================================================================");
            System.out.print("Escolha uma opção: ");
            if (!scanner.hasNextLine()) break;
            String opcao = scanner.nextLine().trim();

            if (opcao.equals("0")) {
                System.out.println("Encerrando Estudo Dirigido...");
                break;
            } else if (opcao.equals("1")) {
                executarEtapa1();
            } else if (opcao.equals("2")) {
                executarEtapa2();
            } else if (opcao.equals("3")) {
                executarEtapa3();
            } else if (opcao.equals("4")) {
                executarSecao8();
            } else if (opcao.equals("5")) {
                executarSecao9();
            } else if (opcao.equals("6")) {
                executarTudo();
            } else {
                System.out.println("Opção inválida!");
            }

            System.out.print("\nPressione ENTER para continuar...");
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }

        scanner.close();
    }
}
