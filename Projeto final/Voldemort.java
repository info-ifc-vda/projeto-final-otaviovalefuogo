public class Voldemort extends Mago {
    public Voldemort() {
     
        super("Voldemort", 3, 600, 85); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Voldemort sussurra: AVADA KEDAVRA! <<<");
        
     
        alvo.receberDano(this.dano * 4.0);
    }
}