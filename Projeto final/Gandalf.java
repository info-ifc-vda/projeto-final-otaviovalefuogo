public class Gandalf extends Mago {
    public Gandalf() {
        super("Gandalf", 2, 600, 55); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Gandalf bate o cajado: YOU SHALL NOT PASS! <<<");
        
  
        alvo.receberDano(this.dano * 2.8);
    }
}