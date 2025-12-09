public class Legolas extends Atirador {
    public Legolas() {
        super("Legolas", 1, 400, 45);
        this.alcance = 5; 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Legolas dispara: UM! DOIS! TRÊS! (Triple Shot) <<<");
        
        alvo.receberDano(this.dano * 0.8);
        alvo.receberDano(this.dano * 0.8);
        alvo.receberDano(this.dano * 0.8);
    }
}