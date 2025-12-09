public class Assassino extends Campeao {
    public Assassino(String nome, int custo, double vida, double dano) {
        super(nome, "Assassino", custo, vida, dano, 1);
    }
    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println(">>> " + this.nome + " (Assassino) usou LÂMINA SOMBRIA!");
        double total = this.dano * 3.5;
        alvo.receberDano(total);
        this.danoCausadoNaRodada += total;
    }
}