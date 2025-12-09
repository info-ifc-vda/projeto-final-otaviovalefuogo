public class DavyJones extends Mago {
    public DavyJones() {
       
        super("DavyJones", 2, 550, 60); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Davy Jones grita: VOCÊ SABE A DIFERNÇA ENTRE O FÃ E O FANBOY?! (Dava Jonas ataca) <<<");
        
        
        alvo.receberDano(this.dano * 3.0);
    }
}