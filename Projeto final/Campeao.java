import java.util.List;

public abstract class Campeao {
    protected String nome;
    protected String classe;
    protected int custo;
    protected int nivelEstrela;
    
    protected double vida;
    protected double vidaMax;
    protected double dano;
    protected double danoBase;
    protected double mana;
    
    protected int alcance; 
    protected int x;
    protected int y;
    
   
    protected int inicioX;
    protected int inicioY;
    
  
    protected double danoCausadoNaRodada = 0;

    public Campeao(String nome, String classe, int custo, double vida, double dano, int alcance) {
        this.nome = nome;
        this.classe = classe;
        this.custo = custo;
        this.vida = vida;
        this.dano = dano;
        this.alcance = alcance;
        
        this.vidaMax = vida;
        this.danoBase = dano;
        this.nivelEstrela = 1;
        this.mana = 0;
        
       
        this.x = -1; this.y = -1;
        this.inicioX = -1; this.inicioY = -1;
    }

  
    public void setCoordenadas(int x, int y) {
        this.x = x;
        this.y = y;
        this.inicioX = x; 
        this.inicioY = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public double getDanoCausado() { return danoCausadoNaRodada; }

   
    public String getBarraVida() {
        int tamanhoBarra = 10;
        double porcentagem = this.vida / this.vidaMax;
        if (porcentagem < 0) porcentagem = 0;
        if (porcentagem > 1) porcentagem = 1;
        int preenchido = (int) (porcentagem * tamanhoBarra);
        String barra = "";
        for (int i = 0; i < preenchido; i++) barra += "█"; 
        for (int i = preenchido; i < tamanhoBarra; i++) barra += "-"; 
        return "[" + barra + "]";
    }

    public void agir(Campeao alvo, List<Campeao> todosNaArena) {
        if (!estaVivo() || !alvo.estaVivo()) return;

        double distancia = Math.sqrt(Math.pow(this.x - alvo.x, 2) + Math.pow(this.y - alvo.y, 2));

        if (distancia <= this.alcance) {
            atacar(alvo);
        } else {
            moverEmDirecaoA(alvo, todosNaArena);
        }
    }

    public void moverEmDirecaoA(Campeao alvo, List<Campeao> todos) {
        int novoX = this.x;
        int novoY = this.y;

        if (this.x < alvo.x) novoX++;
        else if (this.x > alvo.x) novoX--;
        if (this.y < alvo.y) novoY++;
        else if (this.y > alvo.y) novoY--;

        if (!posicaoOcupada(novoX, novoY, todos)) {
            this.x = novoX;
            this.y = novoY;
        } else {
            if (!posicaoOcupada(novoX, this.y, todos)) this.x = novoX;
            else if (!posicaoOcupada(this.x, novoY, todos)) this.y = novoY;
        }
    }

    private boolean posicaoOcupada(int tx, int ty, List<Campeao> todos) {
        for(Campeao c : todos) {
            if (c.estaVivo() && c.getX() == tx && c.getY() == ty) return true;
        }
        return false;
    }

    public void atacar(Campeao alvo) {
        this.mana += 20;
        alvo.receberDano(this.dano);
        this.danoCausadoNaRodada += this.dano;
        
        if (this.mana >= 100) {
            usarUltimate(alvo);
            this.mana = 0;
        }
    }

    public void subirEstrela() {
        this.nivelEstrela++;
        this.vidaMax *= 1.8; this.danoBase *= 1.5;
        this.vida = this.vidaMax; this.dano = this.danoBase;
        System.out.println(">>> UPGRADE! " + this.nome + " subiu para " + this.nivelEstrela + " estrelas! <<<");
    }
    
   
    public void resetarStatus() { 
        this.vida = this.vidaMax; 
        this.dano = this.danoBase; 
        this.mana = 0; 
        this.danoCausadoNaRodada = 0;
        
    
        if (this.inicioX != -1 && this.inicioY != -1) {
            this.x = this.inicioX;
            this.y = this.inicioY;
        }
    }

    public void aplicarBuff(double v, double d) { this.vida += v; this.vidaMax += v; this.dano += d; }
    public boolean estaVivo() { return this.vida > 0; }
    public String getNome() { return nome; }
    public int getCusto() { return custo; }
    public int getNivelEstrela() { return nivelEstrela; }
    public String getClasse() { return classe; }
    public void receberDano(double valor) { this.vida -= valor; }
    
    public abstract void usarUltimate(Campeao alvo);
}