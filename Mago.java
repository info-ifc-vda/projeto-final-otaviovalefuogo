public class Mago extends Campeao {
    public Mago(String nome, int custo, double vida, double dano) {
        super(nome, "Mago", custo, vida, dano, 3);
    }
    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println(">>> " + this.nome + " (Mago) lançou EXPLOSÃO ARCANA!");
        double total = this.dano * 4.0;
        alvo.receberDano(total);
        this.danoCausadoNaRodada += total;
    }
}