public class Predador extends Assassino {
    public Predador() {
        
        super("Predador", 2, 650, 70); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> O PREDADOR MIRA COM O LASER... (Boom!) <<<");
        
        
        alvo.receberDano(this.dano * 3.0);
    }
}