public class JohnWick extends Atirador {
    public JohnWick() {

        super("JohnWick", 2, 550, 70); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> John Wick foca... HEADSHOT DUPLO! <<<");
        
   
        alvo.receberDano(this.dano * 3.5);
    }
}
