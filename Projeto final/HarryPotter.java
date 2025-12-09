public class HarryPotter extends Mago {
    public HarryPotter() {
        super("HarryPotter", 1, 450, 40); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Harry Potter lança: SECTUMSEMPRA! <<<");
        
        alvo.receberDano(this.dano * 2.5);
    }
}