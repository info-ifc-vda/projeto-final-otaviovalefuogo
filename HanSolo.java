public class HanSolo extends Atirador {
    public HanSolo() {
       
        super("HanSolo", 1, 450, 50); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> HAN SOLO ATIROU PRIMEIRO! (Crítico Surpresa) <<<");
        
        alvo.receberDano(this.dano * 2.5);
    }
}