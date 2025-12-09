public class DrEstranho extends Mago {
    public DrEstranho() {
        super("DrEstranho", 3, 650, 75); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Dr. Estranho abre os portais do multiverso! <<<");
        
  
        alvo.receberDano(this.dano * 3.5);
    }
}