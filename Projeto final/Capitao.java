public class Capitao extends Tanque {
    public Capitao() {
       
        super("Capitao", 1, 600, 35); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println(">>> Capitao levanta o ESCUDO DE VIBRANIUM!");
        
     
        double danoUlt = this.dano * 1.5;
        alvo.receberDano(danoUlt);
        
       
        this.vida += 200;
        System.out.println("Capitao bloqueou danos e recuperou 200 de vida.");
    }
}