import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

class Grafo {
    private int n;
    private int[][] matriz;
    private List<List<Integer>> lista;

    public Grafo(int numVertices) {
        this.n = numVertices;
        this.matriz = new int[n][n];
        this.lista = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            this.lista.add(new ArrayList<>());
        }
    }

    public void adicionarAresta(int u, int v) {
        this.matriz[u][v] = 1;
        this.matriz[v][u] = 1;

        if (!this.lista.get(u).contains(v)) {
            this.lista.get(u).add(v);
            Collections.sort(this.lista.get(u));
        }
        if (!this.lista.get(v).contains(u)) {
            this.lista.get(v).add(u);
            Collections.sort(this.lista.get(v));
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
}