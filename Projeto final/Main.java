public class Main {
    public static void main(String[] args) {
        Jogo jogo = new Jogo();
        jogo.iniciar();
    }

   
    public static void aplicarSinergias(Jogador jogador) {
        for (Campeao c : jogador.arena) c.resetarStatus();
        int qtdDuel = 0, qtdTanque = 0, qtdMago = 0, qtdAssas = 0, qtdAtir = 0;
        for (Campeao c : jogador.arena) { 
            if (c instanceof Duelista) qtdDuel++; 
            else if (c instanceof Tanque) qtdTanque++; 
            else if (c instanceof Mago) qtdMago++; 
            else if (c instanceof Assassino) qtdAssas++; 
            else if (c instanceof Atirador) qtdAtir++; 
        }
        
        if (qtdTanque >= 2) { double v=(qtdTanque>=5)?3000:(qtdTanque>=4)?1500:(qtdTanque>=3)?800:300; for(Campeao c:jogador.arena) if(c instanceof Tanque) c.aplicarBuff(v,0); }
        if (qtdDuel >= 2) { double d=(qtdDuel>=5)?150:(qtdDuel>=3)?40:15; double v=(qtdDuel>=5)?600:(qtdDuel>=3)?150:0; for(Campeao c:jogador.arena) if(c instanceof Duelista) c.aplicarBuff(v,d); }
        if (qtdAtir >= 2) { double d=(qtdAtir>=5)?250:(qtdAtir>=3)?70:30; for(Campeao c:jogador.arena) if(c instanceof Atirador) c.aplicarBuff(0,d); }
        if (qtdAssas >= 2) { double d=(qtdAssas>=5)?300:(qtdAssas>=3)?90:40; for(Campeao c:jogador.arena) if(c instanceof Assassino) c.aplicarBuff(0,d); }
        if (qtdMago >= 2) { double p=(qtdMago>=5)?200:(qtdMago>=3)?60:25; for(Campeao c:jogador.arena) if(c instanceof Mago) c.aplicarBuff(0,p); }
    }
}