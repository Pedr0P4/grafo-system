import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * Classe que representa um Grafo utilizando simultaneamente:
 * 1) Matriz de Adjacência (n x n)
 * 2) Lista de Adjacência com objetos do tipo Vertice
 *
 * Implementa todos os algoritmos exigidos:
 * - Lista de Exercícios: Matriz, Lista, BFS com rastreamento, DFS Recursiva, DFS com Pilha.
 * - Estudo Dirigido (Aula 9): EhArvore, ArvoreGeradoraBFS, ArvoreGeradoraDFS, Comparação BFS x DFS, ArborescenciaBFS.
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

    public int getIndiceVertice(Vertice v) {
        return this.vertices.indexOf(v);
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
        adicionarAresta(this.vertices.indexOf(u), this.vertices.indexOf(v));
    }

    public void adicionarAresta(Vertice u, Vertice v, int peso) {
        adicionarAresta(this.vertices.indexOf(u), this.vertices.indexOf(v), peso);
    }

    /**
     * Percorre linha por linha exibindo os valores da matriz de adjacência.
     */
    public void mostrarMatriz() {
        System.out.println("\n--- Matriz de Adjacência (" + this.n + "x" + this.n + ") ---");
        for (int i = 0; i < this.n; i++) {
            System.out.print("[");
            for (int j = 0; j < this.n; j++) {
                System.out.print(this.matriz[i][j] + (j < this.n - 1 ? ", " : ""));
            }
            System.out.println("]");
        }
    }

    /**
     * Percorre cada vértice exibindo a coleção contendo seus respectivos vizinhos.
     */
    public void mostrarLista() {
        System.out.println("\n--- Lista de Adjacência ---");
        for (int u = 0; u < this.n; u++) {
            System.out.println(this.vertices.get(u) + " -> " + this.lista.get(u));
        }
    }

    /**
     * Exibição completa das duas representações.
     */
    public void mostrarGrafo() {
        System.out.println("\n=======================================");
        System.out.println("     REPRESENTAÇÃO COMPLETA DO GRAFO   ");
        System.out.println("=======================================");
        mostrarMatriz();
        mostrarLista();
    }

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
        // Vetor de visitados: inicializa todos como falso
        boolean[] visitado = new boolean[this.n];
        // Fila FIFO que gerencia a exploração por níveis
        Queue<Integer> fila = new LinkedList<>();

        // Marca o vértice inicial e enfileira
        visitado[inicio] = true;
        fila.add(inicio);

        while (!fila.isEmpty()) {
            // Retira o primeiro vértice da fila (FIFO)
            int u = fila.poll();
            ordemVisitacao.add(u);

            System.out.println("\nVértice retirado da fila: " + this.vertices.get(u));

            List<Vertice> vizinhosEncontrados = this.lista.get(u);
            List<Vertice> novosAdicionados = new ArrayList<>();

            // Analisa todos os vizinhos do vértice u
            for (Vertice vizinho : vizinhosEncontrados) {
                int v = this.vertices.indexOf(vizinho);
                // Só processa vizinhos ainda não visitados
                if (!visitado[v]) {
                    visitado[v] = true;      // Marca antes de enfileirar para evitar duplicatas
                    fila.add(v);              // Enfileira para processamento futuro
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

        // Percorre todos os vizinhos de u em ordem crescente
        for (Vertice vizinho : this.lista.get(u)) {
            int v = this.vertices.indexOf(vizinho);
            if (!visitado[v]) {
                // Chamada recursiva: aprofunda a busca no vizinho não visitado
                dfs(v, visitado, ordem);
            }
        }

        // Ao esgotar os vizinhos não visitados, retorna para a chamada anterior (backtracking)
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

        // Empilha o vértice inicial
        pilha.push(inicio);

        while (!pilha.isEmpty()) {
            // Retira o elemento do topo da pilha (LIFO)
            int u = pilha.pop();

            // Um vértice pode ter sido empilhado por caminhos alternativos; verifica se já foi visitado
            if (!visitado[u]) {
                visitado[u] = true;
                ordemVisitacao.add(u);

                System.out.println("\nVértice retirado do topo: " + this.vertices.get(u));

                List<Vertice> vizinhos = this.lista.get(u);
                // Inverte a lista de vizinhos para empilhar em ordem decrescente
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
        // Árvores clássicas não direcionadas são definidas sobre grafos não direcionados
        if (this.direcionado) return false;
        if (this.n == 0) return false;

        // Se |E| != |V| - 1 então retornar FALSO
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
        // 1. Para cada v em V: visitado[v] <- FALSO
        boolean[] visitado = new boolean[this.n];
        // 2. Criar fila Q; T <- vazio
        Queue<Integer> fila = new LinkedList<>();
        List<Aresta> t = new ArrayList<>();

        // 3. visitado[s] <- VERDADEIRO; enfileirar(Q, s)
        visitado[inicio] = true;
        fila.add(inicio);

        // 4. Enquanto Q não estiver vazia:
        while (!fila.isEmpty()) {
            int u = fila.poll(); // u <- desenfileirar(Q)
            for (Vertice vizinho : this.lista.get(u)) { // Para cada vizinho em Adj[u]
                int v = this.vertices.indexOf(vizinho);
                if (!visitado[v]) { // Se NÃO visitado[v]
                    visitado[v] = true;
                    // Aresta da árvore geradora registrada na descoberta do novo vértice
                    int peso = this.matriz[u][v];
                    t.add(new Aresta(this.vertices.get(u), vizinho, peso));
                    fila.add(v); // enfileirar(Q, v)
                }
            }
        }

        // 5. Retornar T
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
        // 1. Para cada v em V: visitado[v] <- FALSO
        boolean[] visitado = new boolean[this.n];
        // 2. Criar pilha P; T <- vazio
        Stack<int[]> pilha = new Stack<>();
        List<Aresta> t = new ArrayList<>();

        // 3. empilhar(P, (s, NULO)) onde NULO = -1
        pilha.push(new int[]{inicio, -1});

        // 4. Enquanto P não estiver vazia:
        while (!pilha.isEmpty()) {
            int[] par = pilha.pop(); // (u, pai) <- desempilhar(P)
            int u = par[0];
            int pai = par[1];

            if (!visitado[u]) { // Se NÃO visitado[u]
                visitado[u] = true;
                if (pai != -1) { // se pai != NULO
                    int peso = this.matriz[pai][u];
                    t.add(new Aresta(this.vertices.get(pai), this.vertices.get(u), peso)); // T <- T ∪ {(pai, u)}
                }

                // Para cada v em Adj[u] em ordem inversa
                List<Vertice> vizinhos = this.lista.get(u);
                for (int i = vizinhos.size() - 1; i >= 0; i--) {
                    Vertice vizinho = vizinhos.get(i);
                    int v = this.vertices.indexOf(vizinho);
                    if (!visitado[v]) {
                        pilha.push(new int[]{v, u}); // empilhar(P, (v, u))
                    }
                }
            }
        }

        // 5. Retornar T
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
        // 1. Marcar todos como NÃO visitados; A <- vazio
        boolean[] visitado = new boolean[this.n];
        Queue<Integer> fila = new LinkedList<>();
        List<Aresta> a = new ArrayList<>();

        // 2. visitado[r] <- VERDADEIRO; enfileirar(Q, r)
        visitado[raiz] = true;
        fila.add(raiz);

        // 3. Enquanto Q não estiver vazia:
        while (!fila.isEmpty()) {
            int u = fila.poll();
            // Examina somente os arcos que saem de u (u -> v)
            for (Vertice vizinho : this.lista.get(u)) {
                int v = this.vertices.indexOf(vizinho);
                if (!visitado[v]) {
                    visitado[v] = true;
                    int peso = this.matriz[u][v];
                    a.add(new Aresta(this.vertices.get(u), vizinho, peso)); // Registra arco da arborescência
                    fila.add(v);
                }
            }
        }

        // 4. Se existe vértice não visitado: FALHA (retorna null)
        for (int i = 0; i < this.n; i++) {
            if (!visitado[i]) {
                return null;
            }
        }

        // 5. Retornar A
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

}