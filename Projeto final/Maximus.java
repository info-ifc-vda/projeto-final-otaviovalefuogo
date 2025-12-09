public class Maximus extends Duelista {
    public Maximus() {
  
        super("Maximus", 1, 550, 40); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Maximus grita: VOCÊS NÃO ESTÃO ENTRETIDOS?! <<<");
        
        alvo.receberDano(this.dano * 2.2);
    }
}