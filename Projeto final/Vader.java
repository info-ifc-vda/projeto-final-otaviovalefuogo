public class Vader extends Duelista {
    public Vader() {
       
        super("Vader", 3, 800, 80); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Vader diz: SE VOCE NÃO ESTÁ COMIGO,ENTÃO VOCÊ É MEU INIMIGO!. (Golpe de Sabre) <<<");
        
     
        alvo.receberDano(this.dano * 3.5);
    }
}