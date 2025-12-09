import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InterfaceJogo {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String WHITE = "\u001B[37m";

    private Scanner scanner;

    public InterfaceJogo() {
        this.scanner = new Scanner(System.in);
    }

    public void limparTela() {
        try { System.out.print("\033[H\033[2J"); System.out.flush(); } catch (Exception e) { for(int i=0;i<50;i++) System.out.println(); }
    }

    public void imprimirCabecalho(int round) {
        System.out.println(YELLOW + "\n========================================");
        System.out.println("              ROUND " + round);
        System.out.println("========================================" + RESET);
    }

    public void exibirPlacar(List<Jogador> jogadores) {
        System.out.println(PURPLE + "\n📊 PLACAR GERAL (VIDA)" + RESET);
        List<Jogador> ranking = new ArrayList<>(jogadores);
        ranking.sort((j1, j2) -> Integer.compare(j2.vida, j1.vida));

        for (Jogador j : ranking) {
            String cor = (j.vida <= 0 || j.vida < 30) ? RED : (j.vida < 60) ? YELLOW : GREEN;
            String status = (j.vida <= 0) ? "(ELIMINADO)" : j.vida + " HP";
            String tag = (j.isBot) ? "" : " (EU)";
            System.out.printf("   %s%-10s : %s %s%s\n", cor, j.nome, status, tag, RESET);
        }
        System.out.println("────────────────────────────────────────");
    }

    public void imprimirTabuleiro(Jogador jogador, Jogador inimigo) {
        String[][] grid = new String[8][7];
        for(int l=0; l<8; l++) for(int c=0; c<7; c++) grid[l][c] = " .  ";
        
        for(Campeao c : jogador.arena) 
            if (c.estaVivo() && c.getX() >= 0) grid[c.getY()][c.getX()] = GREEN + formatarNome(c) + RESET;
        
        if (inimigo != null) 
            for(Campeao c : inimigo.arena) 
                if (c.estaVivo() && c.getX() >= 0) grid[c.getY()][c.getX()] = RED + formatarNome(c).replace("[", "<").replace("]", ">") + RESET;

        System.out.println("\n   0     1     2     3     4     5     6   (Colunas)");
        System.out.println(" ╔═════════════════════════════════════════╗");
        int inicio = (inimigo == null) ? 4 : 0;
        for(int l = inicio; l < 8; l++) {
            System.out.print(l + "║");
            for(int c=0; c<7; c++) System.out.print(grid[l][c] + " ");
            System.out.print("║");
            if (l==0) System.out.print(" <-- INIMIGO");
            if (l==4) System.out.print(" <-- SUA ÁREA");
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

    public void exibirBuffsAtivos(Jogador j) {
        int qtdDuel = 0, qtdTanque = 0, qtdMago = 0, qtdAssas = 0, qtdAtir = 0;
        for (Campeao c : j.arena) { if (c instanceof Duelista) qtdDuel++; else if (c instanceof Tanque) qtdTanque++; else if (c instanceof Mago) qtdMago++; else if (c instanceof Assassino) qtdAssas++; else if (c instanceof Atirador) qtdAtir++; }
        
        System.out.println(CYAN + "\n💎 SINERGIAS ATIVAS:" + RESET);
        if (qtdTanque >= 2) System.out.println("   🛡️  TANQUE (" + qtdTanque + ")");
        if (qtdDuel >= 2) System.out.println("   ⚔️  DUELISTA (" + qtdDuel + ")");
        if (qtdAtir >= 2) System.out.println("   🏹 ATIRADOR (" + qtdAtir + ")");
        if (qtdAssas >= 2) System.out.println("   🗡️  ASSASSINO (" + qtdAssas + ")");
        if (qtdMago >= 2) System.out.println("   ✨ MAGO (" + qtdMago + ")");
        System.out.println("───────────────────────────────────────────");
    }

    public void imprimirStatusBatalha(Jogador humano, Jogador inimigo) {
        System.out.println(GREEN + "SEU TIME:" + RESET);
        for(Campeao c : humano.arena) if(c.estaVivo()) System.out.printf(" %-10s %s %d/%d\n", c.getNome(), c.getBarraVida(), (int)c.vida, (int)c.vidaMax);
        System.out.println(RED + "INIMIGO:" + RESET);
        for(Campeao c : inimigo.arena) if(c.estaVivo()) System.out.printf(" %-10s %s %d/%d\n", c.getNome(), c.getBarraVida(), (int)c.vida, (int)c.vidaMax);
    }

    private String formatarNome(Campeao c) {
        String curto = c.getNome().length() > 3 ? c.getNome().substring(0, 3) : c.getNome();
        return "[" + curto.toUpperCase() + " " + c.getNivelEstrela() + "]";
    }

    public String lerEntrada() { return scanner.next().toUpperCase(); }
    public int lerInt() { try { return scanner.nextInt(); } catch(Exception e) { scanner.next(); return -1; } }
}