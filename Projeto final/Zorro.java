public class Zorro extends Duelista {
    public Zorro() {
    
        super("Zorro", 1, 500, 45); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Zorro risca o 'Z' no peito do inimigo! <<<");
        
        double danoTotal = this.dano * 2.0;
        alvo.receberDano(danoTotal);
    }
}