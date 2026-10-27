import java.util.Objects;

/**
 * Representa um Vértice (ou Nó) em uma estrutura de Grafo.
 * 
 * Na Teoria dos Grafos formal G = (V, E), o vértice é um elemento de V,
 * possuindo identidade única (rótulo/identificador) e sem carregar custos
 * de transição ou pesos de conexão, que pertencem formalmente às arestas (E).
 */
public class Vertice implements Comparable<Vertice> {
    private String rotulo;

    public Vertice(String rotulo) {
        if (rotulo == null || rotulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O rótulo do vértice não pode ser nulo ou vazio.");
        }
        this.rotulo = rotulo.trim();
    }

    public Vertice(int id) {
        this(String.valueOf(id));
    }

    public String getRotulo() {
        return this.rotulo;
    }

    public void setRotulo(String rotulo) {
        if (rotulo == null || rotulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O rótulo do vértice não pode ser nulo ou vazio.");
        }
        this.rotulo = rotulo.trim();
    }

    @Override
    public String toString() {
        return this.rotulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertice vertice = (Vertice) o;
        return Objects.equals(this.rotulo, vertice.rotulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rotulo);
    }

    @Override
    public int compareTo(Vertice outro) {
        if (outro == null) return 1;
        try {
            int num1 = Integer.parseInt(this.rotulo);
            int num2 = Integer.parseInt(outro.rotulo);
            return Integer.compare(num1, num2);
        } catch (NumberFormatException e) {
            return this.rotulo.compareTo(outro.rotulo);
        }
    }
}
