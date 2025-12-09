import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jogo {
    private List<Jogador> jogadores;
    private InterfaceJogo ui;
    private Batalha sistemaBatalha;
    private int round;

    public Jogo() {
        this.jogadores = new ArrayList<>();
        this.ui = new InterfaceJogo();
        this.sistemaBatalha = new Batalha(ui);
        this.round = 1;
        inicializarJogadores();
    }

    private void inicializarJogadores() {
        jogadores.add(new Jogador("VOCÊ", false));
        for (int i = 1; i <= 4; i++) jogadores.add(new Jogador("Bot " + i, true));

        for (Jogador j : jogadores) {
            Campeao inicial = Fabrica.gerarCampeaoAleatorio();
            while (inicial.getCusto() > 1) inicial = Fabrica.gerarCampeaoAleatorio();
            j.banco.add(inicial);
            if (j.isBot) j.autoDeploy();
        }
    }

    public void iniciar() {
        System.out.println(InterfaceJogo.CYAN + ">>> INICIANDO O CINE TFT <<<" + InterfaceJogo.RESET);
        
        while (jogadores.get(0).vida > 0 && contarVivos() > 1) {
            fasePreparacao(jogadores.get(0));
            faseBots();
            aplicarTodosBuffs();
            faseBatalha();
            faseEconomia();
            round++;
        }
        
        ui.limparTela();
        if (jogadores.get(0).vida > 0) System.out.println("PARABÉNS! CAMPEÃO!");
        else System.out.println("GAME OVER");
    }

    private void fasePreparacao(Jogador humano) {
        List<Campeao> loja = Fabrica.gerarLoja();
        boolean fim = false;
        while(!fim) {
            ui.limparTela();
            ui.imprimirCabecalho(round);
            ui.exibirPlacar(jogadores);
            System.out.println(InterfaceJogo.BLUE + "--- SEU STATUS ---" + InterfaceJogo.RESET);
            System.out.println("Vida: " + humano.vida + " | Ouro: " + humano.ouro + " | Nível: " + humano.nivel + " (XP: " + humano.xp + "/" + humano.getXpNecessario() + ")");
            ui.imprimirTabuleiro(humano, null);
            ui.exibirBuffsAtivos(humano);
            
            System.out.println("\n[LOJA]:");
            for(int i=0; i<5; i++) {
                if(loja.get(i) == null) System.out.print("["+(i+1)+": --] ");
                else System.out.print("["+(i+1)+": "+loja.get(i).getNome()+" $"+loja.get(i).getCusto()+"] ");
            }
            
            System.out.println("\n\n(1-5) Comprar | (R)oll $2 | (X)P $4 | (M)over | (T)rocar | (V)ender | (P)ronto");
            String op = ui.lerEntrada();

            if (op.equals("P")) {
                if(humano.arena.isEmpty()) { 
                    System.out.println("Arena vazia! Tem certeza? (S/N)");
                    if(ui.lerEntrada().equals("S")) fim = true;
                } else fim = true;
            }
            else if (op.equals("R")) { if(humano.ouro >= 2) { humano.ouro -= 2; loja = Fabrica.gerarLoja(); } }
            else if (op.equals("X")) humano.comprarXp();
            else if (op.equals("M")) {
                System.out.print("Index Banco: "); int slot = ui.lerInt();
                System.out.print("Linha (4-7): "); int l = ui.lerInt();
                System.out.print("Col (0-6): "); int c = ui.lerInt();
                if(l>=4 && l<=7) {
                    if(slot >= 0 && slot < humano.banco.size() && humano.arena.size() < humano.nivel) {
                        Campeao champ = humano.banco.remove(slot);
                        champ.setCoordenadas(c, l);
                        humano.arena.add(champ);
                    }
                }
            }
            else if (op.equals("V")) {
                System.out.println("(B)anco ou (A)rena?");
                String onde = ui.lerEntrada();
                System.out.print("Index: ");
                int idx = ui.lerInt();
                humano.venderCampeao(idx, onde.equals("A"));
            }
            else if (op.equals("T")) {
                System.out.println("Banco e Arena:");
                humano.trocarBancoArena(ui.lerInt(), ui.lerInt());
            }
            else {
                try {
                    int idx = Integer.parseInt(op) - 1;
                    if(idx >= 0 && idx < 5 && loja.get(idx) != null) {
                        humano.comprarCampeao(loja.get(idx));
                        loja.set(idx, Fabrica.gerarCampeaoAleatorio());
                    }
                } catch(Exception e){}
            }
        }
    }

    private void faseBots() {
        System.out.println("... Bots jogando ...");
        for(Jogador bot : jogadores) {
            if(bot.isBot && bot.vida > 0) {
                if(bot.ouro > 30) bot.comprarXp();
                List<Campeao> loja = Fabrica.gerarLoja();
                for(Campeao c : loja) if(bot.ouro >= c.getCusto()) bot.comprarCampeao(c);
                bot.autoDeploy();
            }
        }
    }

    private void aplicarTodosBuffs() {
        for(Jogador j : jogadores) if(j.arena.size() > 0) Main.aplicarSinergias(j); 
    }

    private void faseBatalha() {
        List<Jogador> vivos = new ArrayList<>();
        for(Jogador j : jogadores) if(j.vida > 0) vivos.add(j);
        Collections.shuffle(vivos);

        for(int i=0; i < vivos.size()-1; i+=2) {
            Jogador p1 = vivos.get(i);
            Jogador p2 = vivos.get(i+1);
            if(!p1.isBot || !p2.isBot) sistemaBatalha.executarVisual(p1.isBot ? p2 : p1, p1.isBot ? p1 : p2);
            else sistemaBatalha.executarSilenciosa(p1, p2);
        }
        for(Jogador j : jogadores) for(Campeao c : j.arena) c.resetarStatus();
    }

    private void faseEconomia() {
        jogadores.removeIf(j -> j.vida <= 0 && j.isBot);
        for(Jogador j : jogadores) { j.ouro += Math.min(j.nivel, 5) + 2; j.ganharXp(2); }
    }

    private int contarVivos() { int count=0; for(Jogador j : jogadores) if(j.vida > 0) count++; return count; }
}