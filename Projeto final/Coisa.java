public class Coisa extends Tanque {
    public Coisa() {
        super("Coisa", 1, 700, 30); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println(">>> O Coisa grita: TÁ NA HORA DO PAU!");
        
        
        alvo.receberDano(this.dano);
        
        double escudoPedra = this.vida * 0.20;
        this.vida += escudoPedra;
        System.out.println("A pele de pedra absorveu dano (+"+(int)escudoPedra+" vida).");
    }
}