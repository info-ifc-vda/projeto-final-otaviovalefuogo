public class Duelista extends Campeao {
    public Duelista(String nome, int custo, double vida, double dano) {
        super(nome, "Duelista", custo, vida, dano, 1);
    }
    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println(">>> " + this.nome + " (Duelista) usou GOLPE RÁPIDO!");
        double total = this.dano * 2.5;
        alvo.receberDano(total);
        this.danoCausadoNaRodada += total;
    }
}