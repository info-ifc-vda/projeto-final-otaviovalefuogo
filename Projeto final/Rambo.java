public class Rambo extends Atirador {
    public Rambo() {
        super("Rambo", 2, 600, 60); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> RAMBO GRITA: AAAHHH!! (Rajada de M60) <<<");
        
       
        alvo.receberDano(this.dano * 3.0);
    }
}