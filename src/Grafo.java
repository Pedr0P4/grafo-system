import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

class Aresta {
    private int u;
    private int v;

    public Aresta(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public int getU() {
        return this.u;
    }

    public int getV() {
        return this.v;
    }

    @Override
    public String toString() {
        return "(" + this.u + ", " + this.v + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aresta aresta = (Aresta) o;
        return this.u == aresta.u && this.v == aresta.v;
    }

    @Override
    public int hashCode() {
        return 31 * this.u + this.v;
    }
}

class Grafo {
    private int n;
    private boolean direcionado;
    private int[][] matriz;
    private List<List<Integer>> lista;

    public Grafo(int numVertices) {
        this(numVertices, false);
    }

    public Grafo(int numVertices, boolean direcionado) {
        this.n = numVertices;
        this.direcionado = direcionado;
        this.matriz = new int[n][n];
        this.lista = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            this.lista.add(new ArrayList<>());
        }
    }

    public int getNumVertices() {
        return this.n;
    }

    public boolean isDirecionado() {
        return this.direcionado;
    }

    public List<List<Integer>> getLista() {
        return this.lista;
    }

    public int getQuantidadeArestas() {
        int total = 0;
        for (int i = 0; i < this.n; i++) {
            total += this.lista.get(i).size();
        }
        return this.direcionado ? total : total / 2;
    }

    public void adicionarAresta(int u, int v) {
        this.matriz[u][v] = 1;
        if (!this.lista.get(u).contains(v)) {
            this.lista.get(u).add(v);
            Collections.sort(this.lista.get(u));
        }

        if (!this.direcionado) {
            this.matriz[v][u] = 1;
            if (!this.lista.get(v).contains(u)) {
                this.lista.get(v).add(u);
                Collections.sort(this.lista.get(v));
            }
        }
    }

    public void mostrarMatriz() {
        System.out.println("\n--- Matriz de Adjacência ---");
        for (int i = 0; i < this.n; i++) {
            System.out.print("[");
            for (int j = 0; j < this.n; j++) {
                System.out.print(this.matriz[i][j] + (j < this.n - 1 ? ", " : ""));
            }
            System.out.println("]");
        }
    }

    public void mostrarLista() {
        System.out.println("\n--- Lista de Adjacência ---");
        for (int u = 0; u < this.n; u++) {
            System.out.println(u + " -> " + this.lista.get(u));
        }
    }

    public void bfs(int inicio) {
        System.out.println("\n--- Busca em Largura (BFS) ---");
        boolean[] visitado = new boolean[this.n];
        Queue<Integer> fila = new LinkedList<>();
        
        visitado[inicio] = true;
        fila.add(inicio);
        List<Integer> ordemVisitacao = new ArrayList<>();

        while (!fila.isEmpty()) {
            int u = fila.poll();
            ordemVisitacao.add(u);

            System.out.println("\nVértice retirado da fila: " + u);

            List<Integer> vizinhosEncontrados = this.lista.get(u);
            List<Integer> novosAdicionados = new ArrayList<>();

            for (int v : vizinhosEncontrados) {
                if (!visitado[v]) {
                    visitado[v] = true;
                    fila.add(v);
                    novosAdicionados.add(v);
                }
            }

            System.out.println("Vizinhos encontrados: " + vizinhosEncontrados);
            System.out.println("Novos vértices adicionados: " + novosAdicionados);
            System.out.println("Conteúdo atual da fila: " + fila);
            System.out.println("Vértices já visitados (ordem): " + ordemVisitacao);
        }
    }

    public void dfsRecursiva(int inicio) {
        System.out.println("\n--- Busca em Profundidade (DFS Recursiva) ---");
        boolean[] visitado = new boolean[this.n];
        List<Integer> ordem = new ArrayList<>();

        dfs(inicio, visitado, ordem);
        
        System.out.println("Sequência final de vértices visitados: " + ordem);
    }

    private void dfs(int u, boolean[] visitado, List<Integer> ordem) {
        visitado[u] = true;
        ordem.add(u);
        System.out.println("Entrando no vértice... " + u);

        for (int v : this.lista.get(u)) {
            if (!visitado[v]) {
                dfs(v, visitado, ordem);
            }
        }

        System.out.println("Retornando do vértice... " + u);
    }

    public void dfsPilha(int inicio) {
        System.out.println("\n--- Busca em Profundidade (DFS com Pilha) ---");
        boolean[] visitado = new boolean[this.n];
        Stack<Integer> pilha = new Stack<>();
        List<Integer> ordemVisitacao = new ArrayList<>();

        pilha.push(inicio);

        while (!pilha.isEmpty()) {
            int u = pilha.pop();

            if (!visitado[u]) {
                visitado[u] = true;
                ordemVisitacao.add(u);

                System.out.println("\nVértice retirado do topo: " + u);

                List<Integer> vizinhos = this.lista.get(u);
                List<Integer> vizinhosReversos = new ArrayList<>(vizinhos);
                Collections.reverse(vizinhosReversos);
                
                List<Integer> empilhados = new ArrayList<>();

                for (int v : vizinhosReversos) {
                    if (!visitado[v]) {
                        pilha.push(v);
                        empilhados.add(v);
                    }
                }

                System.out.println("Vizinhos encontrados: " + vizinhos);
                System.out.println("Vértices empilhados (ordem de inserção): " + empilhados);
                System.out.println("Estado atual da pilha: " + pilha);
                System.out.println("Vértices já visitados: " + ordemVisitacao);
            }
        }
    }

    public boolean ehArvore() {
        if (this.direcionado) {
            return false;
        }

        if (this.n == 0) {
            return false;
        }

        int m = getQuantidadeArestas();
        if (m != this.n - 1) {
            return false;
        }

        int s = 0;
        boolean[] visitado = new boolean[this.n];
        Queue<Integer> fila = new LinkedList<>();

        visitado[s] = true;
        fila.add(s);
        int totalVisitados = 0;

        while (!fila.isEmpty()) {
            int u = fila.poll();
            totalVisitados++;

            for (int v : this.lista.get(u)) {
                if (!visitado[v]) {
                    visitado[v] = true;
                    fila.add(v);
                }
            }
        }

        return totalVisitados == this.n;
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
                System.out.println("Motivo: O grafo possui |V| - 1 arestas, porém é desconexo (possui ciclo em alguma componente e vértices isolados).");
            }
            System.out.println("Resultado: O grafo NÃO É uma ÁRVORE.");
        }
    }

    public List<Aresta> arvoreGeradoraBFS(int inicio) {
        boolean[] visitado = new boolean[this.n];
        Queue<Integer> fila = new LinkedList<>();
        List<Aresta> t = new ArrayList<>();

        visitado[inicio] = true;
        fila.add(inicio);

        while (!fila.isEmpty()) {
            int u = fila.poll();
            for (int v : this.lista.get(u)) {
                if (!visitado[v]) {
                    visitado[v] = true;
                    t.add(new Aresta(u, v));
                    fila.add(v);
                }
            }
        }

        return t;
    }

    public void executarArvoreGeradoraBFS(int inicio) {
        System.out.println("\n--- Árvore Geradora por BFS ---");
        System.out.println("Vértice inicial (raiz): " + inicio);

        List<Aresta> t = arvoreGeradoraBFS(inicio);

        System.out.println("Arestas adicionadas a T (ordem de descoberta): " + t);
        
        boolean[] verticesAlcancados = new boolean[this.n];
        verticesAlcancados[inicio] = true;
        for (Aresta a : t) {
            verticesAlcancados[a.getU()] = true;
            verticesAlcancados[a.getV()] = true;
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
            System.out.println("Status: O grafo é desconexo; cobriu apenas a componente conexa do vértice " + inicio + ".");
        }
    }

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
                    t.add(new Aresta(pai, u));
                }

                List<Integer> vizinhos = this.lista.get(u);
                for (int i = vizinhos.size() - 1; i >= 0; i--) {
                    int v = vizinhos.get(i);
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
        System.out.println("Vértice inicial (raiz): " + inicio);

        List<Aresta> t = arvoreGeradoraDFS(inicio);

        System.out.println("Arestas adicionadas a T (ordem de inserção): " + t);

        boolean[] verticesAlcancados = new boolean[this.n];
        verticesAlcancados[inicio] = true;
        for (Aresta a : t) {
            verticesAlcancados[a.getU()] = true;
            verticesAlcancados[a.getV()] = true;
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
            System.out.println("Status: O grafo é desconexo; cobriu apenas a componente conexa do vértice " + inicio + ".");
        }
    }

    private int[] calcularNiveisArvore(List<Aresta> arestas, int raiz) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < this.n; i++) {
            adj.add(new ArrayList<>());
        }
        for (Aresta a : arestas) {
            adj.get(a.getU()).add(a.getV());
            adj.get(a.getV()).add(a.getU());
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

    public void compararBfsEDfs(int inicio) {
        System.out.println("\n========================================================");
        System.out.println("         COMPARAÇÃO ENTRE BFS E DFS (Raiz = " + inicio + ")");
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

    public List<Aresta> arborescenciaBFS(int raiz) {
        boolean[] visitado = new boolean[this.n];
        Queue<Integer> fila = new LinkedList<>();
        List<Aresta> a = new ArrayList<>();

        visitado[raiz] = true;
        fila.add(raiz);

        while (!fila.isEmpty()) {
            int u = fila.poll();
            for (int v : this.lista.get(u)) {
                if (!visitado[v]) {
                    visitado[v] = true;
                    a.add(new Aresta(u, v));
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
        System.out.println("Vértice raiz escolhido: " + raiz);
        if (!this.direcionado) {
            System.out.println("Aviso: O grafo atual é não direcionado. Arborescência é definida sobre grafos direcionados.");
        }

        List<Aresta> a = arborescenciaBFS(raiz);

        if (a == null) {
            System.out.println("Resultado: FALHA");
            System.out.println("Existem vértices inalcançáveis a partir da raiz " + raiz + ".");
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