import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Batalha {
    private InterfaceJogo ui;

    public Batalha(InterfaceJogo ui) {
        this.ui = ui;
    }

    public void executarVisual(Jogador humano, Jogador inimigo) {
        
        int botY = 3; int botX = 0;
        for(Campeao c : inimigo.arena) { c.setCoordenadas(botX, botY); botX++; if (botX > 6) { botX = 0; botY--; } }
        int meuX = 0;
        for(Campeao c : humano.arena) { if(c.getX() == -1) { c.setCoordenadas(meuX, 7); meuX++; } }

        int turno = 1;
        while(humano.contarVivosArena() > 0 && inimigo.contarVivosArena() > 0) {
            ui.limparTela();
            System.out.println(InterfaceJogo.RED + "\n>>> COMBATE: " + humano.nome + " VS " + inimigo.nome + " <<<" + InterfaceJogo.RESET);
            ui.imprimirTabuleiro(humano, inimigo);
            ui.imprimirStatusBatalha(humano, inimigo);
            
            try { Thread.sleep(1000); } catch(Exception e) {}

            executarTurno(humano.arena, inimigo.arena);
            if(inimigo.contarVivosArena() > 0) executarTurno(inimigo.arena, humano.arena);
            
            turno++;
            if(turno > 50) break;
        }
        
        finalizarRound(humano, inimigo, true);
    }

    public void executarSilenciosa(Jogador bot1, Jogador bot2) {
        PrintStream original = System.out;
        System.setOut(new PrintStream(new OutputStream() { public void write(int b) {} }));
        
        try {
            int y = 3; int x = 0;
            for(Campeao c : bot2.arena) { c.setCoordenadas(x, y); x++; if(x>6){x=0;y--;} }
            x = 0; for(Campeao c : bot1.arena) { c.setCoordenadas(x, 7); x++; }

            int turno = 0;
            while(bot1.contarVivosArena() > 0 && bot2.contarVivosArena() > 0 && turno < 50) {
                executarTurno(bot1.arena, bot2.arena);
                if(bot2.contarVivosArena() > 0) executarTurno(bot2.arena, bot1.arena);
                turno++;
            }
        } catch(Exception e) {} finally { System.setOut(original); }

        finalizarRound(bot1, bot2, false);
    }

    private void executarTurno(List<Campeao> atacantes, List<Campeao> defensores) {
        List<Campeao> todos = new ArrayList<>();
        todos.addAll(atacantes); todos.addAll(defensores);

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

    private void finalizarRound(Jogador p1, Jogador p2, boolean mostrarMsg) {
        if (p1.contarVivosArena() > 0) {
            int dano = 2 * p1.contarVivosArena();
            p2.vida -= dano;
            if(mostrarMsg) System.out.println(InterfaceJogo.GREEN + ">>> VITÓRIA! Inimigo perdeu " + dano + " vida." + InterfaceJogo.RESET);
            else System.out.println(InterfaceJogo.CYAN + "[RESULTADO] " + p1.nome + " venceu " + p2.nome + " (Dano: " + dano + ")" + InterfaceJogo.RESET);
        } else {
            int dano = 2 * p2.contarVivosArena();
            p1.vida -= dano;
            if(mostrarMsg) System.out.println(InterfaceJogo.RED + ">>> DERROTA... Você perdeu " + dano + " vida." + InterfaceJogo.RESET);
            else System.out.println(InterfaceJogo.CYAN + "[RESULTADO] " + p2.nome + " venceu " + p1.nome + " (Dano: " + dano + ")" + InterfaceJogo.RESET);
        }
        
        if (mostrarMsg) {
            System.out.println("\n📊 RELATÓRIO DE DANO:");
            for(Campeao c : p1.arena) System.out.println(" " + c.getNome() + ": " + (int)c.getDanoCausado());
            System.out.println("\nPressione ENTER...");
            try { System.in.read(); } catch(Exception e) {}
        }
    }
}