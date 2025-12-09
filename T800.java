
public class T800 extends Tanque {
    public T800() {
       
        super("T800", 3, 1200, 60); 
    }

    @Override
    public void usarUltimate(Campeao alvo) {
        System.out.println(">>> T800 diz: HASTA LA VISTA, BABY.");
        

        alvo.receberDano(this.dano * 3.0);
        
      
        this.vida += 400;
        System.out.println("Sistemas reiniciados. Chassi reparado (+400 vida).");
    }
}