public class Beatrix extends Duelista {
    public Beatrix() {
        
        super("Beatrix", 2, 600, 65); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Beatrix usa a TÉCNICA DO CORAÇÃO EXPLOSIVO! <<<");
        
        
        alvo.receberDano(this.dano * 3.0);
    }
}