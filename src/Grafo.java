import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Classe que representa um Grafo utilizando simultaneamente:
 * 1) Matriz de Adjacência (n x n)
 * 2) Lista de Adjacência com objetos do tipo Vertice e Aresta
 *
 * Implementa todos os algoritmos exigidos:
 * - Lista de Exercícios: Matriz, Lista, BFS com rastreamento, DFS Recursiva, DFS com Pilha.
 * - Estudo Dirigido (Aula 9): EhArvore, ArvoreGeradoraBFS, ArvoreGeradoraDFS, Comparação BFS x DFS, ArborescenciaBFS.
 * - Estudo Dirigido (DIM0549 - UFRN): Construção e Percurso em Grafos (Etapas 1, 2, 3, 6, 7, 8, 9 com Dijkstra e DFS Iterativa).
 */
public class Grafo {
    private int n;                          // Quantidade de vértices |V|
    private boolean direcionado;            // Indica se o grafo é direcionado (dígrafos) ou não
    private boolean ponderado;              // Indica se o grafo é ponderado ou não
    private int[][] matriz;                 // Matriz de adjacência
    private List<Vertice> vertices;         // Lista de vértices que compõem o grafo V
    private List<List<Vertice>> lista;      // Lista de adjacência (vizinhos de cada vértice)

    /**
     * Construtor para grafo não direcionado padrão.
     */
    public Grafo(int numVertices) {
        this(numVertices, false, false);
    }

    /**
     * Construtor especificando se é direcionado ou não.
     */
    public Grafo(int numVertices, boolean direcionado) {
        this(numVertices, direcionado, false);
    }

    /**
     * Construtor geral especificando se é direcionado ou não e se é ponderado ou não.
     */
    public Grafo(int numVertices, boolean direcionado, boolean ponderado) {
        this.n = numVertices;
        this.direcionado = direcionado;
        this.ponderado = ponderado;
        
        this.matriz = new int[n][n];
        this.vertices = new ArrayList<>();
        this.lista = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            this.vertices.add(new Vertice(i));
            this.lista.add(new ArrayList<>());
        }
    }

    /**
     * Construtor alternativo permitindo especificar rótulos personalizados para os vértices.
     */
    public Grafo(List<String> rotulos, boolean direcionado, boolean ponderado) {
        this.n = rotulos.size();
        this.direcionado = direcionado;
        this.ponderado = ponderado;
        
        this.matriz = new int[n][n];
        this.vertices = new ArrayList<>();
        this.lista = new ArrayList<>();
        for (String rotulo : rotulos) {
            this.vertices.add(new Vertice(rotulo));
            this.lista.add(new ArrayList<>());
        }
    }

    /**
     * Construtor dinâmico conforme a Seção 2 do Estudo Dirigido:
     * vertices <- conjunto vazio
     * adj <- dicionário vazio
     */
    public Grafo(boolean direcionado, boolean ponderado) {
        this.n = 0;
        this.direcionado = direcionado;
        this.ponderado = ponderado;
        this.matriz = new int[0][0];
        this.vertices = new ArrayList<>();
        this.lista = new ArrayList<>();
    }

    public int getNumVertices() {
        return this.n;
    }

    public boolean isDirecionado() {
        return this.direcionado;
    }

    public boolean isPonderado() {
        return this.ponderado;
    }

    public List<Vertice> getVertices() {
        return Collections.unmodifiableList(this.vertices);
    }

    public Vertice getVertice(int indice) {
        if (indice < 0 || indice >= this.n) {
            throw new IndexOutOfBoundsException("Índice de vértice inválido: " + indice);
        }
        return this.vertices.get(indice);
    }

    public Vertice getVerticePorRotulo(String rotulo) {
        if (rotulo == null) return null;
        for (Vertice v : this.vertices) {
            if (v.getRotulo().equalsIgnoreCase(rotulo.trim())) {
                return v;
            }
        }
        return null;
    }

    public int getIndiceVertice(Vertice v) {
        return this.vertices.indexOf(v);
    }

    public int getIndiceVertice(String rotulo) {
        for (int i = 0; i < this.vertices.size(); i++) {
            if (this.vertices.get(i).getRotulo().equalsIgnoreCase(rotulo.trim())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * PASSO 2 – CADASTRO DE UM VÉRTICE (Seção 2 do Estudo Dirigido)
     * procedimento Adicionar_Vertice(G, v):
     *   se v não pertence a G.vertices então
     *     inserir v em G.vertices
     *     G.adj[v] <- lista vazia
     *   fim-se
     * fim-procedimento
     */
    public Vertice adicionarVertice(Vertice v) {
        if (!this.vertices.contains(v)) {
            this.vertices.add(v);
            this.lista.add(new ArrayList<>());
            this.n = this.vertices.size();
            redimensionarMatriz(this.n);
            return v;
        }
        return this.vertices.get(this.vertices.indexOf(v));
    }

    public Vertice adicionarVertice(String rotulo) {
        return adicionarVertice(new Vertice(rotulo));
    }

    private void redimensionarMatriz(int novoTamanho) {
        int[][] novaMatriz = new int[novoTamanho][novoTamanho];
        if (this.matriz != null) {
            for (int i = 0; i < this.matriz.length && i < novoTamanho; i++) {
                for (int j = 0; j < this.matriz[i].length && j < novoTamanho; j++) {
                    novaMatriz[i][j] = this.matriz[i][j];
                }
            }
        }
        this.matriz = novaMatriz;
    }

    /**
     * Calcula o total de arestas/arcos no grafo.
     * Para grafos não direcionados, divide o total de ligações por 2 pois cada aresta aparece em ambas as listas.
     */
    public int getQuantidadeArestas() {
        int total = 0;
        for (int i = 0; i < this.n; i++) {
            total += this.lista.get(i).size();
        }
        return this.direcionado ? total : total / 2;
    }

    // =========================================================================
    // MÉTODOS DE INSERÇÃO DE ARESTAS DO ESTUDO DIRIGIDO (DIM0549)
    // =========================================================================

    /**
     * ETAPA 1 - PASSO 1: Função para inserir um arco orientado (Seção 3)
     * procedimento Adicionar_Aresta_Dirigida(G, u, v):
     *   Adicionar_Vertice(G, u)
     *   Adicionar_Vertice(G, v)
     *   inserir v em G.adj[u]
     * fim-procedimento
     */
    public void adicionarArestaDirigida(Vertice u, Vertice v) {
        Vertice orig = adicionarVertice(u);
        Vertice dest = adicionarVertice(v);

        int uIdx = this.vertices.indexOf(orig);
        int vIdx = this.vertices.indexOf(dest);

        this.matriz[uIdx][vIdx] = 1;

        if (!this.lista.get(uIdx).contains(dest)) {
            this.lista.get(uIdx).add(dest);
            Collections.sort(this.lista.get(uIdx));
        }
    }

    public void adicionarArestaDirigida(String u, String v) {
        adicionarArestaDirigida(new Vertice(u), new Vertice(v));
    }

    /**
     * ETAPA 2 - PASSO 1: Função para aresta não direcionada com peso (Seção 4)
     * procedimento Adicionar_Aresta_Ponderada(G, u, v, peso):
     *   Adicionar_Vertice(G, u)
     *   Adicionar_Vertice(G, v)
     *   inserir (v, peso) em G.adj[u]
     *   inserir (u, peso) em G.adj[v]
     * fim-procedimento
     */
    public void adicionarArestaPonderada(Vertice u, Vertice v, int peso) {
        Vertice orig = adicionarVertice(u);
        Vertice dest = adicionarVertice(v);

        int uIdx = this.vertices.indexOf(orig);
        int vIdx = this.vertices.indexOf(dest);

        this.matriz[uIdx][vIdx] = peso;
        this.matriz[vIdx][uIdx] = peso;

        if (!this.lista.get(uIdx).contains(dest)) {
            this.lista.get(uIdx).add(dest);
            Collections.sort(this.lista.get(uIdx));
        }

        if (!this.lista.get(vIdx).contains(orig)) {
            this.lista.get(vIdx).add(orig);
            Collections.sort(this.lista.get(vIdx));
        }
    }

    public void adicionarArestaPonderada(String u, String v, int peso) {
        adicionarArestaPonderada(new Vertice(u), new Vertice(v), peso);
    }

    /**
     * ETAPA 3 - PASSO 1: Inserção de arco dirigido e ponderado (Seção 5)
     * procedimento Adicionar_Arco_Ponderado(G, u, v, peso):
     *   Adicionar_Vertice(G, u)
     *   Adicionar_Vertice(G, v)
     *   inserir (v, peso) em G.adj[u]
     * fim-procedimento
     */
    public void adicionarArcoPonderado(Vertice u, Vertice v, int peso) {
        Vertice orig = adicionarVertice(u);
        Vertice dest = adicionarVertice(v);

        int uIdx = this.vertices.indexOf(orig);
        int vIdx = this.vertices.indexOf(dest);

        this.matriz[uIdx][vIdx] = peso;

        if (!this.lista.get(uIdx).contains(dest)) {
            this.lista.get(uIdx).add(dest);
            Collections.sort(this.lista.get(uIdx));
        }
    }

    public void adicionarArcoPonderado(String u, String v, int peso) {
        adicionarArcoPonderado(new Vertice(u), new Vertice(v), peso);
    }

    /**
     * Registra a ligação na Matriz e na Lista de Adjacência.
     * Como o grafo não direcionado possui ligações bidirecionais,
     * registra tanto (u -> v) quanto (v -> u).
     * Os vizinhos são mantidos em ordem para garantir percursos comparáveis e determinísticos.
     */
    public void adicionarAresta(int u, int v) {
        if (this.ponderado) {
            System.out.println("O grafo é ponderado. Adicione peso à aresta!");
            return;
        }

        adicionarAresta(u, v, 1);
    }

    public void adicionarAresta(int u, int v, int peso) {
        if (!this.ponderado && peso > 1) {
            System.out.println("O grafo não é ponderado, o peso não deve ser especificado ou deve ser 1!");
            return;
        }

        // Registra na Matriz de Adjacência
        this.matriz[u][v] = this.ponderado ? peso : 1;

        Vertice verticeU = this.vertices.get(u);
        Vertice verticeV = this.vertices.get(v);

        // Registra na Lista de Adjacência de u
        if (!this.lista.get(u).contains(verticeV)) {
            this.lista.get(u).add(verticeV);
            Collections.sort(this.lista.get(u));
        }

        // Se o grafo for não direcionado, a aresta é simétrica
        if (!this.direcionado) {
            this.matriz[v][u] = this.ponderado ? peso : 1;
            if (!this.lista.get(v).contains(verticeU)) {
                this.lista.get(v).add(verticeU);
                Collections.sort(this.lista.get(v));
            }
        }
    }

    public void adicionarAresta(Vertice u, Vertice v) {
        if (this.direcionado) {
            adicionarArestaDirigida(u, v);
        } else {
            adicionarAresta(this.vertices.indexOf(u), this.vertices.indexOf(v));
        }
    }

    public void adicionarAresta(Vertice u, Vertice v, int peso) {
        if (this.direcionado) {
            adicionarArcoPonderado(u, v, peso);
        } else {
            adicionarArestaPonderada(u, v, peso);
        }
    }

    /**
     * PASSO 3 – CONFERÊNCIA DA REPRESENTAÇÃO (Etapas 1, 2 e 3 do Estudo Dirigido)
     * para cada u em G.vertices em ordem alfabética faça
     *   Escrever u, " → ", G.adj[u]
     * fim-para
     */
    public void conferirRepresentacao() {
        System.out.println("\n--- Conferência da Lista de Adjacência ---");
        List<Vertice> ordenados = new ArrayList<>(this.vertices);
        Collections.sort(ordenados);

        for (Vertice u : ordenados) {
            int uIdx = this.vertices.indexOf(u);
            List<Vertice> vizinhos = this.lista.get(uIdx);

            if (!this.ponderado) {
                System.out.println(u + " → " + vizinhos);
            } else {
                List<String> entradasPonderadas = new ArrayList<>();
                for (Vertice v : vizinhos) {
                    int vIdx = this.vertices.indexOf(v);
                    int peso = this.matriz[uIdx][vIdx];
                    entradasPonderadas.add("(" + v + ", " + peso + ")");
                }
                System.out.println(u + " → " + entradasPonderadas);
            }
        }
    }

    /**
     * Percorre linha por linha exibindo os valores da matriz de adjacência.
     */
    public void mostrarMatriz() {
        System.out.println("\n--- Matriz de Adjacência (" + this.n + "x" + this.n + ") ---");
        System.out.print("     ");
        for (Vertice v : this.vertices) {
            System.out.printf("%4s ", v);
        }
        System.out.println();
        for (int i = 0; i < this.n; i++) {
            System.out.printf("%-4s [", this.vertices.get(i));
            for (int j = 0; j < this.n; j++) {
                System.out.printf("%4d%s", this.matriz[i][j], (j < this.n - 1 ? ", " : ""));
            }
            System.out.println("]");
        }
    }

    /**
     * Percorre cada vértice exibindo a coleção contendo seus respectivos vizinhos.
     */
    public void mostrarLista() {
        conferirRepresentacao();
    }

    /**
     * Exibição completa das duas representações.
     */
    public void mostrarGrafo() {
        System.out.println("\n=======================================");
        System.out.println("     REPRESENTAÇÃO COMPLETA DO GRAFO   ");
        System.out.println("=======================================");
        mostrarMatriz();
        conferirRepresentacao();
    }

    // =========================================================================
    // SEÇÃO 6: ALGORITMO BFS (Busca em Largura) – Rastreamento do Estudo Dirigido
    // =========================================================================

    public List<Vertice> bfsEstudoDirigido(String rotuloOrigem) {
        Vertice origem = getVerticePorRotulo(rotuloOrigem);
        if (origem == null) {
            throw new IllegalArgumentException("Vértice de origem não encontrado: " + rotuloOrigem);
        }
        return bfsEstudoDirigido(origem);
    }

    /**
     * Algoritmo BFS conforme Seção 6 do Estudo Dirigido:
     * - Visitados inicializados como falso
     * - Fila FIFO Q
     * - Rastreamento: "Processando: u | Fila: Q"
     * - Vizinhos em ordem alfabética
     */
    public List<Vertice> bfsEstudoDirigido(Vertice origem) {
        System.out.println("\n--- [SEÇÃO 6] Busca em Largura (BFS) a partir de " + origem + " ---");
        
        // PASSO 1 – PREPARAÇÃO DO CONTROLE DE VISITAS
        Map<Vertice, Boolean> visitado = new HashMap<>();
        for (Vertice v : this.vertices) {
            visitado.put(v, false);
        }
        Queue<Vertice> fila = new LinkedList<>();
        List<Vertice> ordem = new ArrayList<>();

        // PASSO 2 – INICIALIZAÇÃO NA ORIGEM
        visitado.put(origem, true);
        fila.add(origem);

        // PASSO 3 – PERCURSO POR CAMADAS
        while (!fila.isEmpty()) {
            Vertice u = fila.poll();
            ordem.add(u);
            System.out.println("Processando: " + u + " | Fila: " + fila);

            int uIdx = this.vertices.indexOf(u);
            List<Vertice> vizinhos = this.lista.get(uIdx);

            for (Vertice v : vizinhos) {
                if (!visitado.get(v)) {
                    visitado.put(v, true);
                    fila.add(v);
                }
            }
        }

        System.out.println("Sequência final BFS: " + ordem);
        return ordem;
    }

    // =========================================================================
    // SEÇÃO 7: ALGORITMO DFS (Busca em Profundidade) – Rastreamento do Estudo Dirigido
    // =========================================================================

    public List<Vertice> dfsEstudoDirigido(String rotuloOrigem) {
        Vertice origem = getVerticePorRotulo(rotuloOrigem);
        if (origem == null) {
            throw new IllegalArgumentException("Vértice de origem não encontrado: " + rotuloOrigem);
        }
        return dfsEstudoDirigido(origem);
    }

    /**
     * Algoritmo DFS Recursiva conforme Seção 7 do Estudo Dirigido:
     * - Visitados inicializados como falso
     * - Mensagens: "Entrando no vértice: u" e "Retornando do vértice: u"
     * - Vizinhos em ordem alfabética
     */
    public List<Vertice> dfsEstudoDirigido(Vertice origem) {
        System.out.println("\n--- [SEÇÃO 7] Busca em Profundidade (DFS Recursiva) a partir de " + origem + " ---");
        
        // PASSO 1 – PREPARAÇÃO
        Map<Vertice, Boolean> visitado = new HashMap<>();
        for (Vertice v : this.vertices) {
            visitado.put(v, false);
        }
        List<Vertice> ordem = new ArrayList<>();

        // PASSO 2 – INÍCIO DA BUSCA EM PROFUNDIDADE
        buscaProfundidade(origem, visitado, ordem);

        System.out.println("Sequência final DFS Recursiva: " + ordem);
        return ordem;
    }

    private void buscaProfundidade(Vertice u, Map<Vertice, Boolean> visitado, List<Vertice> ordem) {
        visitado.put(u, true);
        ordem.add(u);
        System.out.println("Entrando no vértice: " + u);

        int uIdx = this.vertices.indexOf(u);
        List<Vertice> vizinhos = this.lista.get(uIdx);

        for (Vertice v : vizinhos) {
            if (!visitado.get(v)) {
                buscaProfundidade(v, visitado, ordem);
            }
        }

        System.out.println("Retornando do vértice: " + u);
    }

    // =========================================================================
    // SEÇÃO 9: EXTENSÃO OPCIONAL – DFS Iterativa com Pilha Explícita
    // =========================================================================

    public List<Vertice> dfsIterativaEstudoDirigido(String rotuloOrigem) {
        Vertice origem = getVerticePorRotulo(rotuloOrigem);
        if (origem == null) {
            throw new IllegalArgumentException("Vértice de origem não encontrado: " + rotuloOrigem);
        }
        return dfsIterativaEstudoDirigido(origem);
    }

    /**
     * DFS iterativa usando pilha explícita (Seção 9 - Extensão opcional).
     * Documenta a ordem de empilhamento e inverte a inserção dos vizinhos
     * para reproduzir exatamente o mesmo percurso da DFS recursiva.
     */
    public List<Vertice> dfsIterativaEstudoDirigido(Vertice origem) {
        System.out.println("\n--- [SEÇÃO 9] DFS Iterativa com Pilha Explícita a partir de " + origem + " ---");
        Map<Vertice, Boolean> visitado = new HashMap<>();
        for (Vertice v : this.vertices) {
            visitado.put(v, false);
        }
        Stack<Vertice> pilha = new Stack<>();
        List<Vertice> ordem = new ArrayList<>();

        pilha.push(origem);
        System.out.println("Empilhando raiz inicial: " + origem);

        while (!pilha.isEmpty()) {
            Vertice u = pilha.pop();

            if (!visitado.get(u)) {
                visitado.put(u, true);
                ordem.add(u);
                System.out.println("\nVértice retirado do topo (visitado): " + u);

                int uIdx = this.vertices.indexOf(u);
                List<Vertice> vizinhos = this.lista.get(uIdx);

                // Empilha em ordem inversa para que o menor vizinho alfabético fique no topo
                List<Vertice> empilhados = new ArrayList<>();
                for (int i = vizinhos.size() - 1; i >= 0; i--) {
                    Vertice v = vizinhos.get(i);
                    if (!visitado.get(v)) {
                        pilha.push(v);
                        empilhados.add(v);
                    }
                }
                System.out.println("Vizinhos empilhados (ordem de inserção na pilha): " + empilhados);
                System.out.println("Estado atual da pilha: " + pilha);
            }
        }

        System.out.println("\nSequência final DFS Iterativa: " + ordem);
        return ordem;
    }

    // =========================================================================
    // SEÇÃO 9: DESAFIO DE INTEGRAÇÃO – ALGORITMO DE DIJKSTRA
    // =========================================================================

    public static class ResultadoDijkstra {
        public Vertice origem;
        public Map<Vertice, Integer> distancias = new LinkedHashMap<>();
        public Map<Vertice, Vertice> predecessores = new LinkedHashMap<>();

        public List<Vertice> reconstruirCaminho(Vertice destino) {
            List<Vertice> caminho = new ArrayList<>();
            Vertice atual = destino;
            while (atual != null) {
                caminho.add(0, atual);
                atual = predecessores.get(atual);
            }
            if (caminho.isEmpty() || !caminho.get(0).equals(origem)) {
                return Collections.emptyList();
            }
            return caminho;
        }
    }

    public ResultadoDijkstra dijkstra(String rotuloOrigem) {
        Vertice orig = getVerticePorRotulo(rotuloOrigem);
        if (orig == null) {
            throw new IllegalArgumentException("Vértice de origem não encontrado: " + rotuloOrigem);
        }
        return dijkstra(orig);
    }

    /**
     * Aplica o algoritmo de Dijkstra a partir do vértice de origem (Seção 9).
     * Exige pesos não negativos.
     */
    public ResultadoDijkstra dijkstra(Vertice origem) {
        ResultadoDijkstra res = new ResultadoDijkstra();
        res.origem = origem;

        for (Vertice v : this.vertices) {
            res.distancias.put(v, Integer.MAX_VALUE);
            res.predecessores.put(v, null);
        }
        res.distancias.put(origem, 0);

        Set<Vertice> naoVisitados = new HashSet<>(this.vertices);

        while (!naoVisitados.isEmpty()) {
            Vertice u = null;
            int menorDist = Integer.MAX_VALUE;

            for (Vertice v : naoVisitados) {
                int d = res.distancias.get(v);
                if (d < menorDist) {
                    menorDist = d;
                    u = v;
                }
            }

            if (u == null || menorDist == Integer.MAX_VALUE) {
                break;
            }

            naoVisitados.remove(u);

            int uIdx = this.vertices.indexOf(u);
            List<Vertice> vizinhos = this.lista.get(uIdx);

            for (Vertice v : vizinhos) {
                if (naoVisitados.contains(v)) {
                    int vIdx = this.vertices.indexOf(v);
                    int peso = this.matriz[uIdx][vIdx];
                    if (peso < 0) {
                        throw new IllegalStateException("Dijkstra não suporta arestas com peso negativo!");
                    }
                    int alt = res.distancias.get(u) + peso;
                    if (alt < res.distancias.get(v)) {
                        res.distancias.put(v, alt);
                        res.predecessores.put(v, u);
                    }
                }
            }
        }

        return res;
    }

    /**
     * Reconstitui o caminho gerado por BFS da origem até o destino
     * para comparação com o caminho mínimo ponderado do Dijkstra.
     */
    public List<Vertice> encontrarCaminhoBFS(Vertice origem, Vertice destino) {
        Map<Vertice, Vertice> pai = new HashMap<>();
        Map<Vertice, Boolean> visitado = new HashMap<>();
        for (Vertice v : this.vertices) {
            visitado.put(v, false);
            pai.put(v, null);
        }

        Queue<Vertice> fila = new LinkedList<>();
        visitado.put(origem, true);
        fila.add(origem);

        while (!fila.isEmpty()) {
            Vertice u = fila.poll();
            if (u.equals(destino)) break;

            int uIdx = this.vertices.indexOf(u);
            for (Vertice v : this.lista.get(uIdx)) {
                if (!visitado.get(v)) {
                    visitado.put(v, true);
                    pai.put(v, u);
                    fila.add(v);
                }
            }
        }

        List<Vertice> caminho = new ArrayList<>();
        Vertice atual = destino;
        while (atual != null) {
            caminho.add(0, atual);
            atual = pai.get(atual);
        }

        if (caminho.isEmpty() || !caminho.get(0).equals(origem)) {
            return Collections.emptyList();
        }
        return caminho;
    }

    /**
     * Calcula o custo ponderado total de um dado caminho.
     */
    public int calcularCustoCaminho(List<Vertice> caminho) {
        if (caminho == null || caminho.size() < 2) return 0;
        int custo = 0;
        for (int i = 0; i < caminho.size() - 1; i++) {
            int uIdx = this.vertices.indexOf(caminho.get(i));
            int vIdx = this.vertices.indexOf(caminho.get(i + 1));
            custo += this.matriz[uIdx][vIdx];
        }
        return custo;
    }

    // =========================================================================
    // FÁBRICAS DOS TRÊS GRAFOS DO ESTUDO DIRIGIDO
    // =========================================================================

    /**
     * ETAPA 1 (Figura 1): Grafo direcionado simples
     * Vértices: A, B, C, D, E
     * Arcos: A->B, A->C, B->D, C->D, D->E
     */
    public static Grafo criarGrafoEtapa1() {
        Grafo g = new Grafo(true, false);
        g.adicionarArestaDirigida("A", "B");
        g.adicionarArestaDirigida("A", "C");
        g.adicionarArestaDirigida("B", "D");
        g.adicionarArestaDirigida("C", "D");
        g.adicionarArestaDirigida("D", "E");
        return g;
    }

    /**
     * ETAPA 2 (Figura 2): Grafo ponderado não direcionado
     * Vértices: A, B, C, D, E
     * Arestas: A-B(4), A-C(2), B-C(1), B-D(5), C-D(8), C-E(10), D-E(2)
     */
    public static Grafo criarGrafoEtapa2() {
        Grafo g = new Grafo(false, true);
        g.adicionarArestaPonderada("A", "B", 4);
        g.adicionarArestaPonderada("A", "C", 2);
        g.adicionarArestaPonderada("B", "C", 1);
        g.adicionarArestaPonderada("B", "D", 5);
        g.adicionarArestaPonderada("C", "D", 8);
        g.adicionarArestaPonderada("C", "E", 10);
        g.adicionarArestaPonderada("D", "E", 2);
        return g;
    }

    /**
     * ETAPA 3 (Figura 3): Grafo direcionado e ponderado
     * Vértices: A, B, C, D, E
     * Arcos: A->B(4), A->C(2), C->B(1), B->D(5), C->D(8), C->E(10), D->E(2)
     */
    public static Grafo criarGrafoEtapa3() {
        Grafo g = new Grafo(true, true);
        g.adicionarArcoPonderado("A", "B", 4);
        g.adicionarArcoPonderado("A", "C", 2);
        g.adicionarArcoPonderado("C", "B", 1);
        g.adicionarArcoPonderado("B", "D", 5);
        g.adicionarArcoPonderado("C", "D", 8);
        g.adicionarArcoPonderado("C", "E", 10);
        g.adicionarArcoPonderado("D", "E", 2);
        return g;
    }

    // =========================================================================
    // MÉTODOS EXISTENTES DA LISTA E AULA 9 (PRESERVADOS COM COMENTÁRIOS ORIGINAIS)
    // =========================================================================

    /**
     * Estrutura auxiliar: FILA (Queue) com política FIFO (First In, First Out).
     * Controle de visitados: Vetor booleano visitado[].
     * Os vértices são marcados antes de enfileirar para evitar inserções duplicadas.
     * Exibe o rastreamento passo a passo a cada iteração:
     * - Vértice retirado da fila
     * - Vizinhos encontrados
     * - Novos vértices adicionados
     * - Conteúdo atual da fila
     * - Vértices já visitados
     * Salva tudo em uma lista global de ordem de visitação para exibição final e/ou utilização.
     */
    public void bfs(int inicio, List<Integer> ordemVisitacao) {
        System.out.println("\n--- Busca em Largura (BFS) ---");
        boolean[] visitado = new boolean[this.n];
        Queue<Integer> fila = new LinkedList<>();

        visitado[inicio] = true;
        fila.add(inicio);

        while (!fila.isEmpty()) {
            int u = fila.poll();
            ordemVisitacao.add(u);

            System.out.println("\nVértice retirado da fila: " + this.vertices.get(u));

            List<Vertice> vizinhosEncontrados = this.lista.get(u);
            List<Vertice> novosAdicionados = new ArrayList<>();

            for (Vertice vizinho : vizinhosEncontrados) {
                int v = this.vertices.indexOf(vizinho);
                if (!visitado[v]) {
                    visitado[v] = true;
                    fila.add(v);
                    novosAdicionados.add(vizinho);
                }
            }

            System.out.println("Vizinhos encontrados: " + vizinhosEncontrados);
            System.out.println("Novos vértices adicionados: " + novosAdicionados);
            System.out.println("Conteúdo atual da fila: " + fila);
            System.out.println("Vértices já visitados (ordem): " + ordemVisitacao);
        }

        System.out.println("\nSequência final da BFS: " + ordemVisitacao);
    }

    public void bfs(int inicio) {
        List<Integer> ordemVisitacao = new ArrayList<>();
        bfs(inicio, ordemVisitacao);
    }

    /**
     * Utiliza a pilha de chamadas do sistema (call stack).
     * Emite as mensagens exigidas: "Entrando no vértice..." e "Retornando do vértice...".
     * Salva tudo em uma lista global de ordem de visitação para exibição final e/ou utilização.
     */
    public void dfsRecursiva(int inicio, List<Integer> ordemVisitacao) {
        System.out.println("\n--- Busca em Profundidade (DFS Recursiva) ---");
        boolean[] visitado = new boolean[this.n];

        dfs(inicio, visitado, ordemVisitacao);
        
        System.out.println("\nSequência final de vértices visitados: " + ordemVisitacao);
    }

    private void dfs(int u, boolean[] visitado, List<Integer> ordem) {
        visitado[u] = true;
        ordem.add(u);
        System.out.println("Entrando no vértice... " + this.vertices.get(u));

        for (Vertice vizinho : this.lista.get(u)) {
            int v = this.vertices.indexOf(vizinho);
            if (!visitado[v]) {
                dfs(v, visitado, ordem);
            }
        }

        System.out.println("Retornando do vértice... " + this.vertices.get(u));
    }

    public void dfsRecursiva(int inicio) {
        List<Integer> ordemVisitacao = new ArrayList<>();
        dfsRecursiva(inicio, ordemVisitacao);
    }

    /**
     * Estrutura auxiliar: PILHA (Stack) seguindo o princípio LIFO (Last In, First Out).
     * Para visitar os menores vértices primeiro (ordem crescente), os vizinhos
     * são empilhados em ordem decrescente (inversa), pois o último empilhado será o primeiro a sair.
     * Exibe passo a passo a cada iteração:
     * - Vértice retirado do topo
     * - Vizinhos encontrados
     * - Vértices empilhados (ordem de inserção)
     * - Estado atual da pilha
     * - Vértices já visitados
     * Salva tudo em uma lista global de ordem de visitação para exibição final e/ou utilização.
     */
    public void dfsPilha(int inicio, List<Integer> ordemVisitacao) {
        System.out.println("\n--- Busca em Profundidade (DFS com Pilha) ---");
        boolean[] visitado = new boolean[this.n];
        Stack<Integer> pilha = new Stack<>();

        pilha.push(inicio);

        while (!pilha.isEmpty()) {
            int u = pilha.pop();

            if (!visitado[u]) {
                visitado[u] = true;
                ordemVisitacao.add(u);

                System.out.println("\nVértice retirado do topo: " + this.vertices.get(u));

                List<Vertice> vizinhos = this.lista.get(u);
                List<Vertice> vizinhosReversos = new ArrayList<>(vizinhos);
                Collections.reverse(vizinhosReversos);
                
                List<Vertice> empilhados = new ArrayList<>();

                for (Vertice vizinho : vizinhosReversos) {
                    int v = this.vertices.indexOf(vizinho);
                    if (!visitado[v]) {
                        pilha.push(v);
                        empilhados.add(vizinho);
                    }
                }

                System.out.println("Vizinhos encontrados: " + vizinhos);
                System.out.println("Vértices empilhados (ordem de inserção): " + empilhados);
                System.out.println("Estado atual da pilha: " + pilha);
                System.out.println("Vértices já visitados: " + ordemVisitacao);
            }
        }

        System.out.println("\nSequência final da DFS com Pilha: " + ordemVisitacao);
    }

    public void dfsPilha(int inicio) {
        List<Integer> ordemVisitacao = new ArrayList<>();
        dfsPilha(inicio, ordemVisitacao);
    }

    /**
     * Finalidade: Verificar se um grafo não direcionado satisfaz simultaneamente
     * a contagem de arestas e a conectividade.
     *
     * EXPLICACÃO EXIGIDA NO ESTUDO DIRIGIDO (Atividade 1):
     * "Por que a contagem |E| = |V| - 1 sozinha não é suficiente?"
     * RESPOSTA TÉCNICA:
     * A condição |E| = |V| - 1 é necessária para que um grafo seja uma árvore,
     * mas NÃO é suficiente por si só. Um grafo pode ter exatamente |V| - 1 arestas
     * e ainda assim ser DESCONEXO. Quando isso ocorre, as arestas 'em excesso' formam
     * ciclos em uma ou mais componentes conexas, deixando outros vértices isolados
     * (por exemplo: um grafo com 4 vértices onde 3 formam um triângulo cíclico C3
     * e o 4º vértice é isolado possui |V| = 4 e |E| = 3; embora 3 = 4 - 1, o grafo não é
     * conexo e possui ciclo, logo NÃO é uma árvore).
     * Por essa razão fundamental, o algoritmo testa primeiro |E| == |V| - 1 e,
     * em seguida, executa obrigatoriamente um teste de conectividade (via BFS ou DFS)
     * para garantir que todos os |V| vértices são alcançáveis a partir de um vértice inicial.
     */
    public boolean ehArvore() {
        if (this.direcionado) return false;
        if (this.n == 0) return false;

        int m = getQuantidadeArestas();
        if (m != this.n - 1) {
            return false;
        }

        int s = 0;
        List<Integer> visitados = new ArrayList<>();
        bfs(s, visitados);
        return visitados.size() == this.n;
    }

    public void executarEhArvore() {
        System.out.println("\n--- Verificação de Árvore (EhArvore) ---");
        System.out.println("Grafo direcionado: " + (this.direcionado ? "Sim" : "Não"));
        System.out.println("Quantidade de vértices (|V|): " + this.n);
        int m = getQuantidadeArestas();
        System.out.println("Quantidade de arestas (|E|): " + m);
        System.out.println("Condição necessária |E| == |V| - 1: " + (m == this.n - 1 ? "Atendida (" + m + " == " + (this.n - 1) + ")" : "Não atendida (" + m + " != " + (this.n - 1) + ")"));

        boolean resultado = ehArvore();
        if (resultado) {
            System.out.println("Conectividade: Grafo conexo alcançando todos os " + this.n + " vértices.");
            System.out.println("Resultado: O grafo É uma ÁRVORE.");
        } else {
            if (this.direcionado) {
                System.out.println("Motivo: O algoritmo EhArvore é definido para grafos não direcionados.");
            } else if (m != this.n - 1) {
                System.out.println("Motivo: A quantidade de arestas não é igual a |V| - 1.");
            } else {
                System.out.println("Motivo: O grafo possui |V| - 1 arestas, porém é desconexo (possui ciclo em componente conexa e vértice isolado).");
            }
            System.out.println("Resultado: O grafo NÃO É uma ÁRVORE.");
        }
    }

    /**
     * Finalidade: Construir uma árvore geradora a partir das arestas de descoberta da BFS.
     *
     * ESTRUTURAS DE DADOS:
     * - Fila FIFO (Q): gerencia os vértices a serem explorados em largura (nível por nível).
     * - Vetor de visitados (visitado[]): impede reprocessamento e ciclos.
     * - Conjunto T (Lista<Aresta>): armazena somente as arestas de descoberta da árvore geradora.
     *
     * MOMENTO DA INCLUSÃO DA ARESTA:
     * A aresta (u, v) é adicionada a T exatamente no momento em que um vértice 'v'
     * ainda NÃO VISITADO é descoberto pela primeira vez a partir de 'u'.
     */
    public List<Aresta> arvoreGeradoraBFS(int inicio) {
        boolean[] visitado = new boolean[this.n];
        Queue<Integer> fila = new LinkedList<>();
        List<Aresta> t = new ArrayList<>();

        visitado[inicio] = true;
        fila.add(inicio);

        while (!fila.isEmpty()) {
            int u = fila.poll();
            for (Vertice vizinho : this.lista.get(u)) {
                int v = this.vertices.indexOf(vizinho);
                if (!visitado[v]) {
                    visitado[v] = true;
                    int peso = this.matriz[u][v];
                    t.add(new Aresta(this.vertices.get(u), vizinho, peso));
                    fila.add(v);
                }
            }
        }

        return t;
    }

    public void executarArvoreGeradoraBFS(int inicio) {
        System.out.println("\n--- Árvore Geradora por BFS ---");
        System.out.println("Vértice inicial (raiz): " + this.vertices.get(inicio));

        List<Aresta> t = arvoreGeradoraBFS(inicio);

        System.out.println("Arestas adicionadas a T (ordem de descoberta): " + t);
        
        boolean[] verticesAlcancados = new boolean[this.n];
        verticesAlcancados[inicio] = true;
        for (Aresta a : t) {
            int uIdx = this.vertices.indexOf(a.getOrigem());
            int vIdx = this.vertices.indexOf(a.getDestino());
            if (uIdx != -1) verticesAlcancados[uIdx] = true;
            if (vIdx != -1) verticesAlcancados[vIdx] = true;
        }

        int countVertices = 0;
        for (boolean b : verticesAlcancados) {
            if (b) countVertices++;
        }

        System.out.println("Quantidade de vértices na árvore: " + countVertices + " de " + this.n);
        System.out.println("Quantidade de arestas na árvore: " + t.size());
        System.out.println("Verificação (|E(T)| == |V| - 1): " + (t.size() == this.n - 1));
        if (t.size() == this.n - 1 && countVertices == this.n) {
            System.out.println("Status: Árvore geradora válida cobrindo todos os vértices.");
        } else {
            System.out.println("Status: O grafo é desconexo; cobriu apenas a componente conexa do vértice " + this.vertices.get(inicio) + ".");
        }
    }

    /**
     * Finalidade: Construir uma árvore geradora utilizando busca em profundidade com pilha explícita.
     *
     * ESTRUTURAS DE DADOS:
     * - Pilha LIFO (P): armazena pares (vértice, pai) para rastrear de onde cada vértice foi alcançado.
     * - Vetor de visitados (visitado[]): garante que cada vértice seja visitado apenas uma vez.
     * - Conjunto T (Lista<Aresta>): armazena as arestas de descoberta da DFS.
     *
     * MOMENTO DA INCLUSÃO DA ARESTA:
     * Quando um elemento (u, pai) é desempilhado e 'u' ainda não foi visitado,
     * marca-se 'u' como visitado e, se pai != NULO (-1), adiciona-se a aresta (pai, u) a T.
     * Os vizinhos são empilhados em ordem inversa para preservar a ordem desejada ao desempilhar.
     */
    public List<Aresta> arvoreGeradoraDFS(int inicio) {
        boolean[] visitado = new boolean[this.n];
        Stack<int[]> pilha = new Stack<>();
        List<Aresta> t = new ArrayList<>();

        pilha.push(new int[]{inicio, -1});

        while (!pilha.isEmpty()) {
            int[] par = pilha.pop();
            int u = par[0];
            int pai = par[1];

            if (!visitado[u]) {
                visitado[u] = true;
                if (pai != -1) {
                    int peso = this.matriz[pai][u];
                    t.add(new Aresta(this.vertices.get(pai), this.vertices.get(u), peso));
                }

                List<Vertice> vizinhos = this.lista.get(u);
                for (int i = vizinhos.size() - 1; i >= 0; i--) {
                    Vertice vizinho = vizinhos.get(i);
                    int v = this.vertices.indexOf(vizinho);
                    if (!visitado[v]) {
                        pilha.push(new int[]{v, u});
                    }
                }
            }
        }

        return t;
    }

    public void executarArvoreGeradoraDFS(int inicio) {
        System.out.println("\n--- Árvore Geradora por DFS (Pilha Explícita) ---");
        System.out.println("Vértice inicial (raiz): " + this.vertices.get(inicio));

        List<Aresta> t = arvoreGeradoraDFS(inicio);

        System.out.println("Arestas adicionadas a T (ordem de inserção): " + t);

        boolean[] verticesAlcancados = new boolean[this.n];
        verticesAlcancados[inicio] = true;
        for (Aresta a : t) {
            int uIdx = this.vertices.indexOf(a.getOrigem());
            int vIdx = this.vertices.indexOf(a.getDestino());
            if (uIdx != -1) verticesAlcancados[uIdx] = true;
            if (vIdx != -1) verticesAlcancados[vIdx] = true;
        }

        int countVertices = 0;
        for (boolean b : verticesAlcancados) {
            if (b) countVertices++;
        }

        System.out.println("Quantidade de vértices na árvore: " + countVertices + " de " + this.n);
        System.out.println("Quantidade de arestas na árvore: " + t.size());
        System.out.println("Verificação (|E(T)| == |V| - 1): " + (t.size() == this.n - 1));
        if (t.size() == this.n - 1 && countVertices == this.n) {
            System.out.println("Status: Árvore geradora válida cobrindo todos os vértices.");
        } else {
            System.out.println("Status: O grafo é desconexo; cobriu apenas a componente conexa do vértice " + this.vertices.get(inicio) + ".");
        }
    }

    /**
     * Auxiliar para calcular os níveis (distância da raiz) de cada vértice na árvore geradora.
     */
    private int[] calcularNiveisArvore(List<Aresta> arestas, int raiz) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < this.n; i++) {
            adj.add(new ArrayList<>());
        }
        for (Aresta a : arestas) {
            int u = this.vertices.indexOf(a.getOrigem());
            int v = this.vertices.indexOf(a.getDestino());
            if (u != -1 && v != -1) {
                adj.get(u).add(v);
                adj.get(v).add(u);
            }
        }

        int[] niveis = new int[this.n];
        Arrays.fill(niveis, -1);
        Queue<Integer> q = new LinkedList<>();

        niveis[raiz] = 0;
        q.add(raiz);

        while (!q.isEmpty()) {
            int curr = q.poll();
            for (int prox : adj.get(curr)) {
                if (niveis[prox] == -1) {
                    niveis[prox] = niveis[curr] + 1;
                    q.add(prox);
                }
            }
        }
        return niveis;
    }

    /**
     * Analisa as diferenças entre as árvores geradoras geradas:
     * - Estratégia de busca (Níveis vs Profundidade)
     * - Estrutura auxiliar (Fila FIFO vs Pilha LIFO)
     * - Altura/Profundidade máxima
     * - Largura máxima por nível
     * - Preservação de caminhos mínimos
     */
    public void compararBfsEDfs(int inicio) {
        System.out.println("\n========================================================");
        System.out.println("         COMPARAÇÃO ENTRE BFS E DFS (Raiz = " + this.vertices.get(inicio) + ")");
        System.out.println("========================================================");

        List<Aresta> tBfs = arvoreGeradoraBFS(inicio);
        List<Aresta> tDfs = arvoreGeradoraDFS(inicio);

        int[] niveisBfs = calcularNiveisArvore(tBfs, inicio);
        int[] niveisDfs = calcularNiveisArvore(tDfs, inicio);

        int profBfs = 0;
        int profDfs = 0;
        int[] contagemNivelBfs = new int[this.n + 1];
        int[] contagemNivelDfs = new int[this.n + 1];

        for (int i = 0; i < this.n; i++) {
            if (niveisBfs[i] != -1) {
                if (niveisBfs[i] > profBfs) profBfs = niveisBfs[i];
                contagemNivelBfs[niveisBfs[i]]++;
            }
            if (niveisDfs[i] != -1) {
                if (niveisDfs[i] > profDfs) profDfs = niveisDfs[i];
                contagemNivelDfs[niveisDfs[i]]++;
            }
        }

        int larguraMaxBfs = 0;
        for (int c : contagemNivelBfs) {
            if (c > larguraMaxBfs) larguraMaxBfs = c;
        }

        int larguraMaxDfs = 0;
        for (int c : contagemNivelDfs) {
            if (c > larguraMaxDfs) larguraMaxDfs = c;
        }

        System.out.println("\nArestas produzidas pela BFS: " + tBfs);
        System.out.println("Arestas produzidas pela DFS: " + tDfs);

        System.out.println("\n+-----------------------------------+--------------------+--------------------+");
        System.out.println("| Aspecto                           | BFS                | DFS                |");
        System.out.println("+-----------------------------------+--------------------+--------------------+");
        System.out.println("| Estratégia                        | Níveis (largura)   | Ramos (profund.)   |");
        System.out.println("| Estrutura Auxiliar                | Fila (FIFO)        | Pilha (LIFO)       |");
        System.out.printf("| Quantidade de Arestas             | %-18d | %-18d |\n", tBfs.size(), tDfs.size());
        System.out.printf("| Profundidade Máxima (altura)      | %-18d | %-18d |\n", profBfs, profDfs);
        System.out.printf("| Largura Máxima por Nível          | %-18d | %-18d |\n", larguraMaxBfs, larguraMaxDfs);
        System.out.println("| Menores Distâncias da Raiz        | Preserva           | Não garante        |");
        System.out.println("| Complexidade                      | O(V + E)           | O(V + E)           |");
        System.out.println("+-----------------------------------+--------------------+--------------------+");

        System.out.println("\nConclusão da análise estrutural:");
        if (profDfs > profBfs) {
            System.out.println("- Árvore mais profunda: DFS (altura " + profDfs + " vs " + profBfs + " da BFS)");
        } else if (profBfs > profDfs) {
            System.out.println("- Árvore mais profunda: BFS (altura " + profBfs + " vs " + profDfs + " da DFS)");
        } else {
            System.out.println("- Ambas as árvores possuem a mesma profundidade (" + profBfs + ")");
        }

        if (larguraMaxBfs > larguraMaxDfs) {
            System.out.println("- Árvore mais larga: BFS (largura " + larguraMaxBfs + " vs " + larguraMaxDfs + " da DFS)");
        } else if (larguraMaxDfs > larguraMaxBfs) {
            System.out.println("- Árvore mais larga: DFS (largura " + larguraMaxDfs + " vs " + larguraMaxBfs + " da BFS)");
        } else {
            System.out.println("- Ambas as árvores possuem a mesma largura máxima (" + larguraMaxBfs + ")");
        }
    }

    /**
     * Finalidade: Construir uma arborescência em um grafo direcionado quando todos os vértices
     * são alcançáveis a partir da raiz escolhida.
     *
     * DIFERENÇA PARA ÁRVORE GERADORA NÃO DIRECIONADA:
     * Em um grafo direcionado, os arcos possuem sentido definido. Uma arborescência com raiz 'r'
     * exige que exista um caminho direcionado de 'r' para qualquer outro vértice do grafo,
     * e o grau de entrada de todos os vértices (exceto a raiz) deve ser exatamente 1.
     * Se algum vértice for inalcançável a partir de 'r', a construção FALHA.
     */
    public List<Aresta> arborescenciaBFS(int raiz) {
        boolean[] visitado = new boolean[this.n];
        Queue<Integer> fila = new LinkedList<>();
        List<Aresta> a = new ArrayList<>();

        visitado[raiz] = true;
        fila.add(raiz);

        while (!fila.isEmpty()) {
            int u = fila.poll();
            for (Vertice vizinho : this.lista.get(u)) {
                int v = this.vertices.indexOf(vizinho);
                if (!visitado[v]) {
                    visitado[v] = true;
                    int peso = this.matriz[u][v];
                    a.add(new Aresta(this.vertices.get(u), vizinho, peso));
                    fila.add(v);
                }
            }
        }

        for (int i = 0; i < this.n; i++) {
            if (!visitado[i]) {
                return null;
            }
        }

        return a;
    }

    public void executarArborescenciaBFS(int raiz) {
        System.out.println("\n--- Arborescência por BFS (Grafo Direcionado) ---");
        System.out.println("Vértice raiz escolhido: " + this.vertices.get(raiz));
        if (!this.direcionado) {
            System.out.println("Aviso: O grafo atual é não direcionado. Arborescência é definida sobre grafos direcionados.");
        }

        List<Aresta> a = arborescenciaBFS(raiz);

        if (a == null) {
            System.out.println("Resultado: FALHA");
            System.out.println("Existem vértices inalcançáveis a partir da raiz " + this.vertices.get(raiz) + ".");
            System.out.println("Não é possível construir uma arborescência com essa raiz.");
        } else {
            System.out.println("Resultado: SUCESSO");
            System.out.println("Arcos pertencentes à arborescência (ordem de descoberta): " + a);
            System.out.println("Quantidade de vértices alcançados: " + this.n);
            System.out.println("Quantidade de arcos na arborescência: " + a.size());
            System.out.println("Verificação (|A| == |V| - 1): " + (a.size() == this.n - 1));
        }
    }
}