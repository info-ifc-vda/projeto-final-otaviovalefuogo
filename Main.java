import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String WHITE = "\u001B[37m";

    public static void main(String[] args) {
        System.out.println(CYAN + ">>> INICIANDO O JOGO JAVA <<<" + RESET);

        List<Jogador> jogadores = new ArrayList<>();
        Jogador humano = new Jogador("VOCÊ", false);
        jogadores.add(humano);
        for (int i = 1; i <= 4; i++) jogadores.add(new Jogador("Bot " + i, true));

        
        for (Jogador j : jogadores) {
            Campeao inicial = gerarCampeaoAleatorio();
            while (inicial.getCusto() > 1) inicial = gerarCampeaoAleatorio();
            j.banco.add(inicial);
            if (j.isBot) j.autoDeploy();
        }

        int round = 1;

        
        while (humano.vida > 0 && contarVivos(jogadores) > 1) {
          
            
        
            faseDePreparacao(humano, jogadores, round);
            
            System.out.println("\n... Os bots estão se preparando ...");
            simularTurnoDosBots(jogadores);
            for (Jogador j : jogadores) j.autoDeploy();
            for(Jogador j : jogadores) if(j.arena.size() > 0) aplicarSinergias(j);

            
            gerenciarRodadaDeBatalhas(jogadores);

            resolverEconomia(jogadores);
            if (humano.vida <= 0) break;
            round++;
        }
        
        limparTela();
        if (humano.vida > 0) {
            System.out.println(GREEN + "\n🏆 PARABÉNS! VOCÊ É O CAMPEÃO! 🏆" + RESET);
        } else {
            System.out.println(RED + "\n☠️ GAME OVER! Você foi eliminado. ☠️" + RESET);
            for(Jogador j : jogadores) {
                if(j.vida > 0 && j.isBot) System.out.println("O vencedor foi: " + j.nome);
            }
        }
        scanner.close();
    }

    public static int contarVivos(List<Jogador> jogadores) {
        int vivos = 0;
        for(Jogador j : jogadores) if(j.vida > 0) vivos++;
        return vivos;
    }

    
    public static void exibirPlacar(List<Jogador> jogadores) {
        System.out.println(PURPLE + "\n📊 PLACAR GERAL (VIDA)" + RESET);
        
        List<Jogador> ranking = new ArrayList<>(jogadores);
        ranking.sort((j1, j2) -> Integer.compare(j2.vida, j1.vida));

        for (Jogador j : ranking) {
            String cor;
            if (j.vida <= 0) cor = RED; 
            else if (j.vida < 30) cor = RED; 
            else if (j.vida < 60) cor = YELLOW; 
            else cor = GREEN; 

            String status = (j.vida <= 0) ? "(ELIMINADO)" : j.vida + " HP";
            String tag = (j.isBot) ? "" : " (EU)";

            System.out.printf("   %s%-10s : %s %s%s\n", cor, j.nome, status, tag, RESET);
        }
        System.out.println("────────────────────────────────────────");
    }

    public static void gerenciarRodadaDeBatalhas(List<Jogador> todosJogadores) {
        List<Jogador> vivos = new ArrayList<>();
        for(Jogador j : todosJogadores) if(j.vida > 0) vivos.add(j);
        Collections.shuffle(vivos);

        for (int i = 0; i < vivos.size() - 1; i += 2) {
            Jogador p1 = vivos.get(i);
            Jogador p2 = vivos.get(i+1);

            if (!p1.isBot || !p2.isBot) {
                batalhaVisual(p1.isBot ? p2 : p1, p1.isBot ? p1 : p2);
            } else {
                batalhaSilenciosa(p1, p2);
            }
        }
        
        System.out.println("\nPressione ENTER para continuar...");
        try { System.in.read(); } catch(Exception e) {}

        for(Jogador j : todosJogadores) for(Campeao c : j.arena) c.resetarStatus();
    }

    public static void batalhaVisual(Jogador humano, Jogador inimigo) {
        int botY = 3; int botX = 0;
        for(Campeao c : inimigo.arena) { c.setCoordenadas(botX, botY); botX++; if (botX > 6) { botX = 0; botY--; } }
        int meuX = 0;
        for(Campeao c : humano.arena) { if(c.getX() == -1 || c.getY() == -1) { c.setCoordenadas(meuX, 7); meuX++; } }

        int turno = 1;
        while(humano.contarVivosArena() > 0 && inimigo.contarVivosArena() > 0) {
            limparTela();
            System.out.println(RED + "\n>>> COMBATE: " + humano.nome + " ("+humano.vida+" HP) VS " + inimigo.nome + " ("+inimigo.vida+" HP) <<<" + RESET);
            imprimirTabuleiro(humano, inimigo);
            imprimirStatusBatalha(humano, inimigo);
            try { Thread.sleep(1000); } catch(Exception e) {}

            List<Campeao> todos = new ArrayList<>();
            todos.addAll(humano.arena);
            todos.addAll(inimigo.arena);

            executarTurnoTime(humano.arena, inimigo.arena, todos);
            if(inimigo.contarVivosArena() > 0) executarTurnoTime(inimigo.arena, humano.arena, todos);
            turno++;
            if(turno > 50) break;
        }

        limparTela();
        imprimirTabuleiro(humano, inimigo);
        
        if (humano.contarVivosArena() > 0) {
            System.out.println(GREEN + "\n>>> VITÓRIA! <<<" + RESET);
            int dano = 2 * humano.contarVivosArena();
            inimigo.vida -= dano;
            System.out.println("Você tirou " + dano + " de vida do " + inimigo.nome);
        } else {
            System.out.println(RED + "\n>>> DERROTA... <<<" + RESET);
            int dano = 2 * inimigo.contarVivosArena();
            humano.vida -= dano;
            System.out.println("Você perdeu " + dano + " de vida.");
        }
        System.out.println(YELLOW + "\n📊 ESTATÍSTICAS (Dano) 📊" + RESET);
        for(Campeao c : humano.arena) {
            String barra = ""; for(int i=0; i < c.getDanoCausado()/50; i++) barra += "█";
            System.out.printf(" %-15s: %4d %s\n", c.getNome(), (int)c.getDanoCausado(), RED + barra + RESET);
        }
    }

    public static void batalhaSilenciosa(Jogador bot1, Jogador bot2) {
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(new OutputStream() { public void write(int b) {} }));
        try {
            int y = 3; int x = 0;
            for(Campeao c : bot2.arena) { c.setCoordenadas(x, y); x++; if(x>6){x=0;y--;} }
            x = 0; for(Campeao c : bot1.arena) { c.setCoordenadas(x, 7); x++; }
            int turno = 0;
            while(bot1.contarVivosArena() > 0 && bot2.contarVivosArena() > 0 && turno < 50) {
                List<Campeao> todos = new ArrayList<>();
                todos.addAll(bot1.arena); todos.addAll(bot2.arena);
                executarTurnoTime(bot1.arena, bot2.arena, todos);
                if(bot2.contarVivosArena() > 0) executarTurnoTime(bot2.arena, bot1.arena, todos);
                turno++;
            }
        } catch (Exception e) {} finally { System.setOut(originalOut); }

        if (bot1.contarVivosArena() > 0) {
            int dano = 2 * bot1.contarVivosArena();
            bot2.vida -= dano;
            System.out.println(CYAN + "[RESULTADO] " + bot1.nome + " venceu " + bot2.nome + " (Dano: " + dano + ")" + RESET);
        } else {
            int dano = 2 * bot2.contarVivosArena();
            bot1.vida -= dano;
            System.out.println(CYAN + "[RESULTADO] " + bot2.nome + " venceu " + bot1.nome + " (Dano: " + dano + ")" + RESET);
        }
    }

    private static void executarTurnoTime(List<Campeao> atacantes, List<Campeao> defensores, List<Campeao> todos) {
        for(Campeao atacante : atacantes) {
            if(atacante.estaVivo()) {
                Campeao alvo = null;
                double menorDist = 999.0;
                for(Campeao defensor : defensores) {
                    if (defensor.estaVivo()) {
                        double d = Math.sqrt(Math.pow(atacante.getX() - defensor.getX(), 2) + Math.pow(atacante.getY() - defensor.getY(), 2));
                        if (d < menorDist) { menorDist = d; alvo = defensor; }
                    }
                }
                if (alvo != null) atacante.agir(alvo, todos);
            }
        }
    }

    
    public static void faseDePreparacao(Jogador jogador, List<Jogador> todosJogadores, int round) {
        List<Campeao> lojaAtual = gerarLoja();
        boolean terminouTurno = false;
        while (!terminouTurno) {
            limparTela();
           
            System.out.println(YELLOW + "================= ROUND " + round + " =================" + RESET);
            
            exibirPlacar(todosJogadores);
            
            System.out.println(BLUE + "--- SEU STATUS ---" + RESET);
            System.out.println("Vida: " + jogador.vida + " | Ouro: " + YELLOW + jogador.ouro + RESET + " | Nível: " + jogador.nivel + " (XP: " + jogador.xp + "/" + jogador.getXpNecessario() + ")");
            imprimirTabuleiro(jogador, null);
            exibirBuffsAtivos(jogador);

            System.out.println("\n[LOJA]:");
            for (int i = 0; i < 5; i++) {
                Campeao c = lojaAtual.get(i);
                if (c == null) System.out.print("[" + (i+1) + ": --] ");
                else {
                    String cor = (c.getCusto() == 1) ? WHITE : (c.getCusto() == 2) ? GREEN : BLUE;
                    System.out.print("[" + (i+1) + ": " + cor + c.getNome() + " $" + c.getCusto() + RESET + "] ");
                }
            }

            System.out.println("\n\nOPÇÕES: (1-5) Comprar | (R) Atualizar ($2) | (X) XP ($4) | (M) Mover | (T) Trocar | (V) Vender | (P) Lutar");
            System.out.print("Escolha: ");
            String escolha = scanner.next().toUpperCase();

            if (escolha.equals("P")) {
                if (jogador.arena.isEmpty()) {
                    System.out.println(RED + "\n>>> PERIGO: Arena vazia! Vai perder vida. Continuar? (S/N)" + RESET);
                    if (scanner.next().equalsIgnoreCase("S")) terminouTurno = true;
                } else { terminouTurno = true; }
            }
            else if (escolha.equals("R")) { if (jogador.ouro >= 2) { jogador.ouro -= 2; lojaAtual = gerarLoja(); } }
            else if (escolha.equals("X")) jogador.comprarXp();
            else if (escolha.equals("M")) {
                System.out.print("Index Banco: ");
                try {
                    int slot = scanner.nextInt();
                    System.out.println("Destino (Linha 4-7 | Coluna 0-6): ");
                    int l = scanner.nextInt(); int c = scanner.nextInt();
                    if (l >= 4 && l <= 7 && c >= 0 && c <= 6) {
                        boolean ocupado = false;
                        for(Campeao ch : jogador.arena) if(ch.getX() == c && ch.getY() == l) ocupado = true;
                        if(!ocupado && slot >= 0 && slot < jogador.banco.size() && jogador.arena.size() < jogador.nivel) {
                            Campeao champ = jogador.banco.remove(slot);
                            champ.setCoordenadas(c, l);
                            jogador.arena.add(champ);
                        } else System.out.println(RED + "Erro!" + RESET);
                    }
                } catch(Exception e) {}
            }
            else if (escolha.equals("T")) { try { System.out.println("Index Banco e Arena:"); int b = scanner.nextInt(); int a = scanner.nextInt(); jogador.trocarBancoArena(b, a); } catch(Exception e) {} }
            else if (escolha.equals("V")) {
                System.out.println("Vender do (B)anco ou (A)rena?");
                String onde = scanner.next().toUpperCase();
                System.out.print("Qual index? ");
                try {
                    int idx = scanner.nextInt();
                    if(onde.equals("B")) jogador.venderCampeao(idx, false);
                    else if(onde.equals("A")) jogador.venderCampeao(idx, true);
                } catch(Exception e) {}
            }
            else {
                try {
                    int slot = Integer.parseInt(escolha) - 1;
                    if (slot >= 0 && slot < 5) {
                        jogador.comprarCampeao(lojaAtual.get(slot));
                        lojaAtual.set(slot, gerarCampeaoAleatorio());
                    }
                } catch (Exception e) {}
            }
        }
    }

    public static void imprimirStatusBatalha(Jogador humano, Jogador inimigo) {
        System.out.println("\nSTATUS DOS CAMPEÕES:");
        System.out.println("-----------------------------------------------------");
        System.out.println(GREEN + "SEU TIME:" + RESET);
        for(Campeao c : humano.arena) if(c.estaVivo()) System.out.printf(" %-10s %s %d/%d\n", c.getNome(), c.getBarraVida(), (int)c.vida, (int)c.vidaMax);
        System.out.println(RED + "INIMIGO:" + RESET);
        for(Campeao c : inimigo.arena) if(c.estaVivo()) System.out.printf(" %-10s %s %d/%d\n", c.getNome(), c.getBarraVida(), (int)c.vida, (int)c.vidaMax);
        System.out.println("-----------------------------------------------------");
    }

    public static void imprimirTabuleiro(Jogador jogador, Jogador inimigo) {
        String[][] grid = new String[8][7];
        for(int l=0; l<8; l++) for(int c=0; c<7; c++) grid[l][c] = " .  ";
        for(Campeao c : jogador.arena) if (c.estaVivo() && c.getX() >= 0 && c.getY() >= 0) grid[c.getY()][c.getX()] = GREEN + formatarNome(c) + RESET;
        if (inimigo != null) for(Campeao c : inimigo.arena) if (c.estaVivo() && c.getX() >= 0 && c.getY() >= 0) grid[c.getY()][c.getX()] = RED + formatarNome(c).replace("[", "<").replace("]", ">") + RESET;
        System.out.println("\n   0     1     2     3     4     5     6   (Colunas)");
        System.out.println(" ╔═════════════════════════════════════════╗");
        int inicio = (inimigo == null) ? 4 : 0;
        for(int l = inicio; l < 8; l++) {
            System.out.print(l + "║");
            for(int c=0; c<7; c++) System.out.print(grid[l][c] + " ");
            System.out.print("║");
            if (l == 0) System.out.print(" <-- INIMIGO");
            if (l == 4) System.out.print(" <-- SUA ÁREA");
            System.out.println();
        }
        System.out.println(" ╚═════════════════════════════════════════╝");
        System.out.println(YELLOW + " 👇 SEU BANCO DE RESERVAS 👇" + RESET);
        if (jogador.banco.isEmpty()) System.out.println("    (Vazio)");
        else {
            for(int i = 0; i < jogador.banco.size(); i++) {
                System.out.print(i + ":" + YELLOW + formatarNome(jogador.banco.get(i)) + RESET + " | ");
                if ((i+1)%5==0) System.out.println();
            }
            System.out.println();
        }
    }

    public static void exibirBuffsAtivos(Jogador j) {
        int qtdDuel = 0, qtdTanque = 0, qtdMago = 0, qtdAssas = 0, qtdAtir = 0;
        for (Campeao c : j.arena) { if (c instanceof Duelista) qtdDuel++; else if (c instanceof Tanque) qtdTanque++; else if (c instanceof Mago) qtdMago++; else if (c instanceof Assassino) qtdAssas++; else if (c instanceof Atirador) qtdAtir++; }
        System.out.println(CYAN + "\n💎 SINERGIAS ATIVAS:" + RESET);
        if (qtdTanque >= 2) { String bonus=(qtdTanque>=5)?"+3000 Vida":(qtdTanque>=4)?"+1500 Vida":(qtdTanque>=3)?"+800 Vida":"+300 Vida"; System.out.println("   🛡️  TANQUE (" + qtdTanque + "): "+GREEN+bonus+RESET); }
        if (qtdDuel >= 2) { String bonus=(qtdDuel>=5)?"+150 Dano/+600 Vida":(qtdDuel>=3)?"+40 Dano/+150 Vida":"+15 Dano"; System.out.println("   ⚔️  DUELISTA (" + qtdDuel + "): "+GREEN+bonus+RESET); }
        if (qtdAtir >= 2) { String bonus=(qtdAtir>=5)?"+250 Dano":(qtdAtir>=3)?"+70 Dano":"+30 Dano"; System.out.println("   🏹 ATIRADOR (" + qtdAtir + "): "+GREEN+bonus+RESET); }
        if (qtdAssas >= 2) { String bonus=(qtdAssas>=5)?"+300 Dano":(qtdAssas>=3)?"+90 Dano":"+40 Dano"; System.out.println("   🗡️  ASSASSINO (" + qtdAssas + "): "+GREEN+bonus+RESET); }
        if (qtdMago >= 2) { String bonus=(qtdMago>=5)?"+200 Poder":(qtdMago>=3)?"+60 Poder":"+25 Poder"; System.out.println("   ✨ MAGO (" + qtdMago + "): "+GREEN+bonus+RESET); }
        if (qtdTanque<2 && qtdDuel<2 && qtdAtir<2 && qtdAssas<2 && qtdMago<2) System.out.println("   (Nenhuma)");
        System.out.println("───────────────────────────────────────────");
    }

    public static void aplicarSinergias(Jogador jogador) {
        for (Campeao c : jogador.arena) c.resetarStatus();
        int qtdDuel = 0, qtdTanque = 0, qtdMago = 0, qtdAssas = 0, qtdAtir = 0;
        for (Campeao c : jogador.arena) { if (c instanceof Duelista) qtdDuel++; else if (c instanceof Tanque) qtdTanque++; else if (c instanceof Mago) qtdMago++; else if (c instanceof Assassino) qtdAssas++; else if (c instanceof Atirador) qtdAtir++; }
        if (qtdTanque >= 2) { double v=(qtdTanque>=5)?3000:(qtdTanque>=4)?1500:(qtdTanque>=3)?800:300; for(Campeao c:jogador.arena) if(c instanceof Tanque) c.aplicarBuff(v,0); }
        if (qtdDuel >= 2) { double d=(qtdDuel>=5)?150:(qtdDuel>=3)?40:15; double v=(qtdDuel>=5)?600:(qtdDuel>=3)?150:0; for(Campeao c:jogador.arena) if(c instanceof Duelista) c.aplicarBuff(v,d); }
        if (qtdAtir >= 2) { double d=(qtdAtir>=5)?250:(qtdAtir>=3)?70:30; for(Campeao c:jogador.arena) if(c instanceof Atirador) c.aplicarBuff(0,d); }
        if (qtdAssas >= 2) { double d=(qtdAssas>=5)?300:(qtdAssas>=3)?90:40; for(Campeao c:jogador.arena) if(c instanceof Assassino) c.aplicarBuff(0,d); }
        if (qtdMago >= 2) { double p=(qtdMago>=5)?200:(qtdMago>=3)?60:25; for(Campeao c:jogador.arena) if(c instanceof Mago) c.aplicarBuff(0,p); }
    }

    private static String formatarNome(Campeao c) {
         String curto = c.getNome().length() > 3 ? c.getNome().substring(0, 3) : c.getNome(); return "[" + curto.toUpperCase() + " " + c.getNivelEstrela() + "]"; 
        }
    public static void limparTela() {
         try {
             System.out.print("\033[H\033[2J"); System.out.flush(); 
            } 
            catch (Exception e) { for(int i=0;i<50;i++) System.out.println(); }
        }
    public static void simularTurnoDosBots(List<Jogador> jogadores) {
         for (Jogador bot : jogadores) {
             if (bot.isBot && bot.vida > 0) {
                if (bot.ouro > 30) bot.comprarXp(); List<Campeao> lojaBot = gerarLoja();
                for (Campeao c : lojaBot) 
                if (bot.ouro >= c.getCusto()) bot.comprarCampeao(c); 
                } 
            } 
        }
    public static void resolverEconomia(List<Jogador> jogadores) {
        jogadores.removeIf(j -> j.vida <= 0 && j.isBot); 
        for (Jogador j : jogadores) {
             j.ouro += Math.min(j.nivel, 5) + 2; j.ganharXp(2); 
            } 
        }
    public static List<Campeao> gerarLoja() {
         List<Campeao> loja = new ArrayList<>();
        for (int i = 0; i < 5; i++) 
        loja.add(gerarCampeaoAleatorio()); return loja; 
    }
    
    public static Campeao gerarCampeaoAleatorio() {
        int r = random.nextInt(100);
        if (r < 60) { int pick = random.nextInt(5); if (pick == 0) return new Zorro(); else if (pick == 1) return new Capitao(); else if (pick == 2) return new HanSolo(); else if (pick == 3) return new Ghostface(); else return new HarryPotter(); } 
        else if (r < 90) { int pick = random.nextInt(6); if (pick == 0) return new Beatrix(); else if (pick == 1) return new Groot(); else if (pick == 2) return new JohnWick(); else if (pick == 3) return new Predador(); else if (pick == 4) return new DavyJones(); else return new Gandalf(); } 
        else { int pick = random.nextInt(5); if (pick == 0) return new Vader(); else if (pick == 1) return new T800(); else if (pick == 2) return new Neo(); else if (pick == 3) return new Agent47(); else return new DrEstranho(); }
    }
    public static Campeao gerarCampeaoPeloNome(String nome) {
        switch (nome) {
            case "Zorro": return new Zorro(); case "Maximus": return new Maximus(); case "Beatrix": return new Beatrix(); case "Wolverine": return new Wolverine(); case "Vader": return new Vader();
            case "Capitao": return new Capitao(); case "Coisa": return new Coisa(); case "Hulk": return new Hulk(); case "Groot": return new Groot(); case "T800": return new T800();
            case "HanSolo": return new HanSolo(); case "Legolas": return new Legolas(); case "Rambo": return new Rambo(); case "JohnWick": return new JohnWick(); case "Neo": return new Neo();
            case "Ghostface": return new Ghostface(); case "ViuvaNegra": return new ViuvaNegra(); case "Predador": return new Predador(); case "Blade": return new Blade(); case "Agent47": return new Agent47();
            case "DavyJones": return new DavyJones(); case "HarryPotter": return new HarryPotter(); case "Gandalf": return new Gandalf(); case "DrEstranho": return new DrEstranho(); case "Voldemort": return new Voldemort();
            default: return new Zorro();
        }
    }
}