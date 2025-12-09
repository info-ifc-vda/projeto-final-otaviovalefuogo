public class Atirador extends Campeao {
    public Atirador(String nome, int custo, double vida, double dano) {
        super(nome, "Atirador", custo, vida, dano, 4);
    }
    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println(">>> " + this.nome + " (Atirador) disparou TIRO CERTEIRO!");
        double total = this.dano * 3.0;
        alvo.receberDano(total);
        this.danoCausadoNaRodada += total;
    }
}