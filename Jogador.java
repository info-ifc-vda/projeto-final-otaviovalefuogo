import java.util.ArrayList;
import java.util.List;

public class Jogador {
    public String nome;
    public int vida = 100;
    public int ouro = 10;
    public int xp = 0;
    public int nivel = 3;
    public boolean isBot;
    
    public List<Campeao> banco = new ArrayList<>();
    public List<Campeao> arena = new ArrayList<>();
    
    private int[] xpParaUpar = {0, 0, 0, 6, 10, 20, 36, 56, 80}; 

    public Jogador(String nome, boolean isBot) {
        this.nome = nome;
        this.isBot = isBot;
    }

    
    public void venderCampeao(int index, boolean daArena) {
        List<Campeao> lista = daArena ? arena : banco;
        
        if (index >= 0 && index < lista.size()) {
            Campeao c = lista.get(index);
            
        
            int valorVenda = c.getCusto(); 
            
            lista.remove(index);
            this.ouro += valorVenda;
            
            if(!isBot) System.out.println(">>> VENDIDO: " + c.getNome() + " (Recuperou " + valorVenda + " ouro)");
        } else {
            if(!isBot) System.out.println("Posição inválida para venda!");
        }
    }

    public void ganharXp(int quantidade) {
        this.xp += quantidade;
        while (nivel < 8 && xp >= getXpNecessario()) {
            xp -= getXpNecessario();
            nivel++;
            if(!isBot) System.out.println(Main.GREEN + ">>> " + nome + " SUBIU PARA O NÍVEL " + nivel + "! <<<" + Main.RESET);
        }
    }

    public void comprarXp() {
        if (ouro >= 4) {
            ouro -= 4;
            ganharXp(4); 
            if(!isBot) System.out.println(nome + " comprou XP! (" + xp + "/" + getXpNecessario() + ")");
        }
    }

    public int getXpNecessario() {
        if (nivel >= xpParaUpar.length - 1) return 9999;
        return xpParaUpar[nivel];
    }

    public void comprarCampeao(Campeao c) {
        if (ouro >= c.getCusto()) {
            if (banco.size() < 10) { 
                ouro -= c.getCusto();
                banco.add(c);
                if (!isBot) System.out.println(nome + " comprou " + c.getNome());
                verificarFusao(c.getNome(), c.getNivelEstrela());
            } else {
                if(!isBot) System.out.println("Banco cheio!");
            }
        }
    }

    private void verificarFusao(String nomeChamp, int nivelAtual) {
        List<Campeao> candidatos = new ArrayList<>();
        for(Campeao c : banco) if(c.getNome().equals(nomeChamp) && c.getNivelEstrela() == nivelAtual) candidatos.add(c);
        for(Campeao c : arena) if(c.getNome().equals(nomeChamp) && c.getNivelEstrela() == nivelAtual) candidatos.add(c);

        if (candidatos.size() >= 3) {
            if(!isBot) System.out.println("\n*** FUSÃO! 3x " + nomeChamp + " viraram um de Nível " + (nivelAtual+1) + "! ***");
            
            Campeao peca1 = candidatos.get(0);
            Campeao peca2 = candidatos.get(1);
            Campeao peca3 = candidatos.get(2);
            
            boolean vaiPraArena = false;
            int posX = -1, posY = -1;

            if (arena.contains(peca1)) { vaiPraArena = true; posX = peca1.getX(); posY = peca1.getY(); }
            else if (arena.contains(peca2)) { vaiPraArena = true; posX = peca2.getX(); posY = peca2.getY(); }
            else if (arena.contains(peca3)) { vaiPraArena = true; posX = peca3.getX(); posY = peca3.getY(); }

            removerPecaEspecifica(peca1);
            removerPecaEspecifica(peca2);
            removerPecaEspecifica(peca3);

            Campeao novo = Main.gerarCampeaoPeloNome(nomeChamp);
            for(int i=0; i < nivelAtual; i++) novo.subirEstrela();

            if (vaiPraArena) {
                novo.setCoordenadas(posX, posY);
                arena.add(novo);
                if(!isBot) System.out.println(">>> O novo " + nomeChamp + " surgiu na ARENA!");
            } else {
                banco.add(novo);
                if(!isBot) System.out.println(">>> O novo " + nomeChamp + " foi para o BANCO.");
            }
            verificarFusao(nomeChamp, nivelAtual + 1);
        }
    }

    private void removerPecaEspecifica(Campeao c) {
        if (banco.contains(c)) banco.remove(c);
        else if (arena.contains(c)) arena.remove(c);
    }
    
    public void autoDeploy() {
        while (arena.size() < nivel && !banco.isEmpty()) {
            Campeao c = banco.remove(0);
            arena.add(c);
            if (!isBot) System.out.println("[AUTO] " + c.getNome() + " subiu para a Arena.");
        }
    }

    public void trocarBancoArena(int indexBanco, int indexArena) {
        if (indexBanco < banco.size() && indexArena < arena.size()) {
            Campeao cBanco = banco.get(indexBanco);
            Campeao cArena = arena.get(indexArena);
            banco.set(indexBanco, cArena);
            arena.set(indexArena, cBanco);
            
            int xAntigo = cArena.getX();
            int yAntigo = cArena.getY();
            cArena.setCoordenadas(-1, -1); 
            cBanco.setCoordenadas(xAntigo, yAntigo); 
            
            System.out.println("Trocado: " + cBanco.getNome() + " entrou, " + cArena.getNome() + " saiu.");
        }
    }
    
    public int contarVivosArena() {
        int count = 0;
        for(Campeao c : arena) if(c.estaVivo()) count++;
        return count;
    }
}