import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    /**
     * Lê um vértice pelo rótulo (ex.: "A") ou pelo índice numérico (ex.: "0").
     */
    private static int lerIndiceVertice(Scanner scanner, Grafo grafo, String mensagem) {
        System.out.print(mensagem + " (" + grafo.getVertices() + "): ");
        String entrada = scanner.nextLine().trim();
        int idx = grafo.getIndiceVertice(entrada);
        if (idx != -1) {
            return idx;
        }
        try {
            int num = Integer.parseInt(entrada);
            if (num >= 0 && num < grafo.getNumVertices()) {
                return num;
            }
        } catch (NumberFormatException ignored) {
        }
        System.out.println("Vértice inválido: " + entrada);
        return -1;
    }

    /**
     * Executa toda a Atividade Prática de Consolidação da Aula 12 (Slides 6 a 19)
     * utilizando letras A..F no lugar de 0..5.
     */
    public static void executarAtividadeAula12() {
        System.out.println("================================================================");
        System.out.println("     AULA 12 — GRAFOS PONDERADOS (VÉRTICES A, B, C, D, E, F)   ");
        System.out.println("================================================================");
        System.out.println("Mapeamento: 0→A, 1→B, 2→C, 3→D, 4→E, 5→F");
        System.out.println("Arestas: A—B(4), A—C(2), B—D(5), B—E(1), C—E(7), D—F(3), E—F(2)");

        Grafo gAula12 = Grafo.criarGrafoAula12();

        System.out.println("\n[ATIVIDADE 01 e 02] Matriz Ponderada (— = INFINITO) e Lista Ponderada (vizinho, peso):");
        gAula12.mostrarMatriz();
        gAula12.mostrarLista();

        System.out.println("\n[ATIVIDADE 03] Execução de CustoCaminho (Slide 11 e Slide 12):");
        System.out.println("1) Trajeto válido: A → B → E → F");
        int custoExemplo = gAula12.calcularCustoCaminho(true, "A", "B", "E", "F");
        System.out.println("   Resultado CustoCaminho(A → B → E → F): " + custoExemplo);

        System.out.println("\n2) Validação de trajeto inválido: A → C → D (aresta C—D não existe)");
        int custoInvalido = gAula12.calcularCustoCaminho(true, "A", "C", "D");
        System.out.println("   Resultado: " + (custoInvalido == Grafo.CAMINHO_INVALIDO ? "CAMINHO_INVALIDO" : custoInvalido));

        System.out.println("\n[ATIVIDADE 04] Comparação dos três percursos de A até F (Slide 13):");
        String[][] caminhos = {
            {"A", "C", "E", "F"},
            {"A", "B", "E", "F"},
            {"A", "B", "D", "F"}
        };
        System.out.println("+-------------------------+---------------+-----------------+");
        System.out.println("| Caminho de A até F      | Nº de arestas | Soma dos pesos  |");
        System.out.println("+-------------------------+---------------+-----------------+");
        for (String[] caminho : caminhos) {
            int custo = gAula12.calcularCustoCaminho(caminho);
            String rota = String.join(" → ", caminho);
            System.out.printf("| %-23s | %-13d | %-15d |%n", rota, caminho.length - 1, custo);
        }
        System.out.println("+-------------------------+---------------+-----------------+");
        System.out.println("Conclusão: Os três caminhos possuem 3 arestas, mas custos diferentes (menos arestas ≠ menor peso).");

        System.out.println("\n[SLIDES 14–16] Preparação para Dijkstra (usando Relaxar) a partir de A:");
        Grafo.ResultadoDijkstra res = gAula12.dijkstra("A");
        Vertice destinoF = gAula12.getVerticePorRotulo("F");
        System.out.println("Menores custos a partir de A:");
        for (Map.Entry<Vertice, Integer> entry : res.distancias.entrySet()) {
            System.out.println("  dist[" + entry.getKey() + "] = " + entry.getValue());
        }
        System.out.println("Menor caminho de A até F por Dijkstra: " + res.reconstruirCaminho(destinoF)
                + " (custo total = " + res.distancias.get(destinoF) + ")");

        System.out.println("\n[ATIVIDADE 05 / SLIDE 18] Adaptação para Grafo Dirigido e Ponderado (A..F):");
        Grafo gDir = Grafo.criarGrafoAula12Direcionado();
        gDir.mostrarMatriz();
        gDir.mostrarLista();
        System.out.println("Observação: No grafo dirigido, cada arco (u → v, p) aparece apenas na lista de u (ex.: F → []).");
        System.out.println("================================================================");
    }

    /**
     * Calcula interativamente o custo de um caminho digitado pelo usuário (ex.: A B E F).
     */
    private static void executarCalculoCustoInterativo(Scanner scanner, Grafo grafo) {
        System.out.println("\n--- CustoCaminho Interativo ---");
        System.out.println("Vértices disponíveis: " + grafo.getVertices());
        System.out.print("Digite os vértices do caminho separados por espaço (ex: A B E F): ");
        String linha = scanner.nextLine().trim();
        if (linha.isEmpty()) {
            System.out.println("Nenhum caminho informado.");
            return;
        }
        String[] rotulos = linha.split("\\s+");
        int custo = grafo.calcularCustoCaminho(true, rotulos);
        if (custo == Grafo.CAMINHO_INVALIDO) {
            System.out.println("Resultado: CAMINHO_INVALIDO");
        } else {
            System.out.println("Custo Total do Caminho: " + custo);
        }
    }

    /**
     * Executa o algoritmo de Dijkstra a partir de um vértice escolhido no grafo atual.
     */
    private static void executarDijkstraNoGrafoAtual(Scanner scanner, Grafo grafo) {
        if (!grafo.isPonderado()) {
            System.out.println("\nAviso: O grafo atual não é ponderado (todas as arestas têm peso 1).");
            System.out.println("Use a opção 17 ou 18 para carregar o grafo ponderado da Aula 12 (A..F).");
        }
        int idx = lerIndiceVertice(scanner, grafo, "Vértice de origem para Dijkstra");
        if (idx == -1) return;

        Vertice origem = grafo.getVertice(idx);
        Grafo.ResultadoDijkstra res = grafo.dijkstra(origem);

        System.out.println("\n--- Resultado Dijkstra a partir de " + origem + " ---");
        for (Vertice v : grafo.getVertices()) {
            int d = res.distancias.get(v);
            String distStr = (d == Grafo.INFINITO) ? "INFINITO" : String.valueOf(d);
            System.out.println("  " + origem + " → " + v + " | dist = " + distStr + " | caminho = " + res.reconstruirCaminho(v));
        }
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
                    + " | " + (grafo.isPonderado() ? "Ponderado" : "Não Ponderado")
                    + " (" + grafo.getNumVertices() + " vértices " + grafo.getVertices()
                    + ", " + grafo.getQuantidadeArestas() + " arestas/arcos)");
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
            System.out.println("[ PARTE 3 — AULA 12: GRAFOS PONDERADOS (VÉRTICES A..F) ]");
            System.out.println("16 - Executar Atividade Completa da Aula 12 (Slides 6 a 19)");
            System.out.println("17 - Carregar Grafo Ponderado da Aula 12 (A..F) na Memória");
            System.out.println("18 - Carregar Grafo Dirigido Ponderado da Aula 12 (A..F) na Memória");
            System.out.println("19 - Calcular Custo de um Caminho (CustoCaminho passo a passo)");
            System.out.println("20 - Executar Dijkstra no Grafo Atual");
            System.out.println("----------------------------------------------------------------");
            System.out.println("[ CONFIGURAÇÃO ]");
            System.out.println("15 - Redefinir para o Grafo Padrão da Lista");
            System.out.println(" 0 - Sair");
            System.out.println("================================================================");

            System.out.print("Escolha uma opção: ");
            if (!scanner.hasNextLine()) break;
            String opcao = scanner.nextLine().trim();

            if (opcao.equals("0")) {
                System.out.println("Encerrando programa...");
                break;
            } else if (opcao.equals("1")) {
                grafo.mostrarMatriz();
            } else if (opcao.equals("2")) {
                grafo.mostrarLista();
            } else if (opcao.equals("3")) {
                int inicio = lerIndiceVertice(scanner, grafo, "Vértice de início");
                if (inicio != -1) grafo.bfs(inicio);
            } else if (opcao.equals("4")) {
                int inicio = lerIndiceVertice(scanner, grafo, "Vértice de início");
                if (inicio != -1) grafo.dfsRecursiva(inicio);
            } else if (opcao.equals("5")) {
                int inicio = lerIndiceVertice(scanner, grafo, "Vértice de início");
                if (inicio != -1) grafo.dfsPilha(inicio);
            } else if (opcao.equals("6")) {
                grafo.mostrarGrafo();
            } else if (opcao.equals("7")) {
                executarQuestao10();
            } else if (opcao.equals("8")) {
                executarDesafioQuestao13(scanner);
            } else if (opcao.equals("9")) {
                grafo.executarEhArvore();
            } else if (opcao.equals("10")) {
                int raiz = lerIndiceVertice(scanner, grafo, "Vértice raiz");
                if (raiz != -1) grafo.executarArvoreGeradoraBFS(raiz);
            } else if (opcao.equals("11")) {
                int raiz = lerIndiceVertice(scanner, grafo, "Vértice raiz");
                if (raiz != -1) grafo.executarArvoreGeradoraDFS(raiz);
            } else if (opcao.equals("12")) {
                int raiz = lerIndiceVertice(scanner, grafo, "Vértice raiz para comparação");
                if (raiz != -1) grafo.compararBfsEDfs(raiz);
            } else if (opcao.equals("13")) {
                int raiz = lerIndiceVertice(scanner, grafo, "Vértice raiz para arborescência");
                if (raiz != -1) grafo.executarArborescenciaBFS(raiz);
            } else if (opcao.equals("14")) {
                executarDemonstracaoEstudoDirigido();
            } else if (opcao.equals("15")) {
                grafo = criarGrafoPadrao();
                System.out.println("Grafo redefinido para o Grafo Padrão da Lista (6 vértices, 7 arestas).");
            } else if (opcao.equals("16")) {
                executarAtividadeAula12();
            } else if (opcao.equals("17")) {
                grafo = Grafo.criarGrafoAula12();
                System.out.println("Grafo Ponderado da Aula 12 (V = {A, B, C, D, E, F}) carregado na memória.");
                grafo.mostrarGrafo();
            } else if (opcao.equals("18")) {
                grafo = Grafo.criarGrafoAula12Direcionado();
                System.out.println("Grafo Dirigido e Ponderado da Aula 12 (V = {A, B, C, D, E, F}) carregado na memória.");
                grafo.mostrarGrafo();
            } else if (opcao.equals("19")) {
                executarCalculoCustoInterativo(scanner, grafo);
            } else if (opcao.equals("20")) {
                executarDijkstraNoGrafoAtual(scanner, grafo);
            } else {
                System.out.println("Opção inválida.");
            }

            System.out.print("\nPressione ENTER para continuar...");
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
}