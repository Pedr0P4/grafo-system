public class Vertice {
    private int vertice;
    private int peso;
    
    public Vertice(int vertice) {
        this(vertice, 0);
    }

    public Vertice(int vertice, int peso) {
        this.vertice = vertice;
        this.peso = peso;
    }

    public int getVertice() {
        return this.vertice;
    }

    public void setVertice(int newVertice) {
        this.vertice = newVertice;
    }

    public int getPeso() {
        return this.peso;
    }

    public void setPeso(int newPeso) {
        this.peso = newPeso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertice vertice = (Vertice) o;
        return this.vertice == vertice.vertice;
    }

    @Override
    public int hashCode() {
        return 31 * this.vertice;
    }
}
