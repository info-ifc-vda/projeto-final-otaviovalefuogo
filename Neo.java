public class Neo extends Atirador {
    public Neo() {

        super("Neo", 3, 650, 85); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> NEO ENTRA NA MATRIX (Bullet Time) <<<");
        

        alvo.receberDano(this.dano * 4.0);
    }
}