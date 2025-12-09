public class Groot extends Tanque {
    public Groot() {
       
        super("Groot", 2, 800, 40); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println(">>> EU... SOU... GROOT! (Raízes prendem o inimigo)");
        
        double absorcao = 250;

        alvo.receberDano(absorcao);
        this.vida += absorcao;
        
        System.out.println("Groot absorveu " + absorcao + " de vida do oponente.");
    }
}