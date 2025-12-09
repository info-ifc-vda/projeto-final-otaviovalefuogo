public class Hulk extends Tanque {
    public Hulk() {
        
        super("Hulk", 2, 850, 50); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println(">>> HULK ESMAGAAAA!");
        
      
        alvo.receberDano(this.dano * 2.5);
        
       
        this.vida += 350;
        System.out.println("Hulk se regenerou com a raiva (+350 vida).");
    }
}