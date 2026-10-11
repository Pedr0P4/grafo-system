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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grafo grafo = criarGrafoPadrao();

        while (true) {
            limparTela();

            System.out.println("=======================================");
            System.out.println("        ALGORITMOS DE GRAFOS");
            System.out.println("=======================================");
            System.out.println("1 - Mostrar Matriz de Adjacência");
            System.out.println("2 - Mostrar Lista de Adjacência");
            System.out.println("3 - Executar BFS (Fila)");
            System.out.println("4 - Executar DFS Recursiva");
            System.out.println("5 - Executar DFS com Pilha (Iterativa)");
            System.out.println("6 - Adicionar aresta");
            System.out.println("7 - Criar novo grafo");
            System.out.println("0 - Sair");

            System.out.print("\nEscolha uma opção: ");
            String opcao = scanner.nextLine();

            if (opcao.equals("0")) {
                System.out.println("Encerrando programa...");
                break;
            } else if (opcao.equals("1")) {
                grafo.mostrarMatriz();
            } else if (opcao.equals("2")) {
                grafo.mostrarLista();
            } else if (opcao.equals("3")) {
                System.out.print("Vértice de início: ");
                int inicio = Integer.parseInt(scanner.nextLine());
                grafo.bfs(inicio);
            } else if (opcao.equals("4")) {
                System.out.print("Vértice de início: ");
                int inicio = Integer.parseInt(scanner.nextLine());
                grafo.dfsRecursiva(inicio);
            } else if (opcao.equals("5")) {
                System.out.print("Vértice de início: ");
                int inicio = Integer.parseInt(scanner.nextLine());
                grafo.dfsPilha(inicio);
            } else if (opcao.equals("6")) {
                System.out.print("Vértice U: ");
                int u = Integer.parseInt(scanner.nextLine());
                System.out.print("Vértice V: ");
                int v = Integer.parseInt(scanner.nextLine());
                grafo.adicionarAresta(u, v);
                System.out.println("Aresta adicionada com sucesso!");
            } else if (opcao.equals("7")) {
                System.out.print("Número de vértices: ");
                int n = Integer.parseInt(scanner.nextLine());
                grafo = new Grafo(n);
                
                System.out.print("Número de arestas: ");
                int m = Integer.parseInt(scanner.nextLine());
                
                for (int i = 0; i < m; i++) {
                    System.out.print("Vértice U: ");
                    int u = Integer.parseInt(scanner.nextLine());
                    System.out.print("Vértice V: ");
                    int v = Integer.parseInt(scanner.nextLine());
                    grafo.adicionarAresta(u, v);
                }
            } else {
                System.out.println("Opção inválida.");
            }

            System.out.print("\nPressione ENTER para continuar...");
            scanner.nextLine();
        }
        
        scanner.close();
    }
}