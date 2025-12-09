public class Agent47 extends Assassino {
    public Agent47() {
       
        super("Agent47", 3, 700, 90); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> Agente 47 executa o contrato. (Hitman) <<<");
        

        alvo.receberDano(this.dano * 4.0);
    }
}