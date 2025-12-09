public class Tanque extends Campeao {
    public Tanque(String nome, int custo, double vida, double dano) {
        super(nome, "Tanque", custo, vida, dano, 1);
    }
    @Override
    public void usarUltimate(Campeao alvo) {
        double cura = this.vidaMax * 0.30;
        this.vida += cura;
        System.out.println(">>> " + this.nome + " (Tanque) usou FORTIFICAR! (+" + (int)cura + " vida)");
        alvo.receberDano(this.dano); 
        this.danoCausadoNaRodada += this.dano;
    }
}