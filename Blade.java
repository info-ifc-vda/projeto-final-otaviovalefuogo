public class Blade extends Assassino {
    public Blade() {
        
        super("Blade", 2, 600, 75); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println("\n>>> BLADE USA A ESPADA DE PRATA! <<<");
        
        double danoAplicado = this.dano * 2.5;
        alvo.receberDano(danoAplicado);
        

        this.vida += 50;
        System.out.println("Blade se curou com o sangue do inimigo.");
    }
}