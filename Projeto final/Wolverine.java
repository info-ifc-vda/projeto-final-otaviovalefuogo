public class Wolverine extends Duelista {
    public Wolverine() {
        
        super("Wolverine", 2, 650, 55); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> SNIKT! Wolverine entra em FÚRIA SELVAGEM! <<<");
        
        
        alvo.receberDano(this.dano * 0.8);
        alvo.receberDano(this.dano * 0.8);
        alvo.receberDano(this.dano * 0.8);
    }
}