/**
 * Representa uma Aresta (u, v) de um grafo não direcionado ou um Arco (u, v) de um dígrafo.
 * Utilizada pelos algoritmos de Árvore Geradora e Arborescência.
 */
public class Aresta {
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