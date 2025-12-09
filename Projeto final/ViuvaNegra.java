public class ViuvaNegra extends Assassino {
    public ViuvaNegra() {
     
        super("Viuva Negra", 1, 500, 50); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Viúva Negra usa: FERRÃO ELÉTRICO! <<<");
        
       
        alvo.receberDano(this.dano * 2.0);
        alvo.receberDano(20);
    }
}