import java.util.Objects;

/**
 * Representa uma Aresta (u, v) de um grafo não direcionado ou um Arco (u, v) de um dígrafo.
 * Formalmente, em um grafo ponderado, uma aresta e = (u, v) pertencente ao conjunto de arestas E
 * possui uma função de peso associada w: E -> R (ou Z).
 * O peso expressa o custo, distância, latência ou capacidade da conexão entre dois vértices.
 */
public class Aresta {
    private Vertice origem;
    private Vertice destino;
    private int peso;

    /**
     * Construtor para aresta não ponderada (ou com peso padrão 1).
     */
    public Aresta(Vertice origem, Vertice destino) {
        this(origem, destino, 1);
    }

    /**
     * Construtor para aresta ponderada.
     */
    public Aresta(Vertice origem, Vertice destino, int peso) {
        if (origem == null || destino == null) {
            throw new IllegalArgumentException("Origem e destino de uma aresta não podem ser nulos.");
        }
        this.origem = origem;
        this.destino = destino;
        this.peso = peso;
    }

    public Vertice getOrigem() {
        return this.origem;
    }

    public void setOrigem(Vertice origem) {
        if (origem == null) {
            throw new IllegalArgumentException("Origem não pode ser nula.");
        }
        this.origem = origem;
    }

    public Vertice getDestino() {
        return this.destino;
    }

    public void setDestino(Vertice destino) {
        if (destino == null) {
            throw new IllegalArgumentException("Destino não pode ser nulo.");
        }
        this.destino = destino;
    }

    public int getPeso() {
        return this.peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    /**
     * Métodos auxiliares de conveniência u e v.
     */
    public Vertice getU() {
        return this.origem;
    }

    public Vertice getV() {
        return this.destino;
    }

    @Override
    public String toString() {
        if (this.peso == 1) {
            return "(" + this.origem + ", " + this.destino + ")";
        }
        return "(" + this.origem + ", " + this.destino + ", peso: " + this.peso + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aresta aresta = (Aresta) o;
        return this.peso == aresta.peso &&
               Objects.equals(this.origem, aresta.origem) &&
               Objects.equals(this.destino, aresta.destino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.origem, this.destino, this.peso);
    }
}
