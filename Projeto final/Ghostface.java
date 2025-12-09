public class Ghostface extends Assassino {
    public Ghostface() {
    
        super("Ghostface", 1, 450, 55); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Ghostface pergunta: QUAL SEU FILME FAVORITO? (Facada) <<<");
        
 
        alvo.receberDano(this.dano * 2.5);
    }
}