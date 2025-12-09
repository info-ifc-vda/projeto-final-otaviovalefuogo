import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fabrica {
    private static Random random = new Random();

    public static List<Campeao> gerarLoja() {
        List<Campeao> loja = new ArrayList<>();
        for (int i = 0; i < 5; i++) loja.add(gerarCampeaoAleatorio());
        return loja;
    }

    public static Campeao gerarCampeaoAleatorio() {
        int r = random.nextInt(100);
        if (r < 60) { 
            int pick = random.nextInt(5);
            if (pick == 0) return new Zorro(); else if (pick == 1) return new Capitao(); else if (pick == 2) return new HanSolo(); else if (pick == 3) return new Ghostface(); else return new HarryPotter();
        } else if (r < 90) { 
            int pick = random.nextInt(6);
            if (pick == 0) return new Beatrix(); else if (pick == 1) return new Groot(); else if (pick == 2) return new JohnWick(); else if (pick == 3) return new Predador(); else if (pick == 4) return new DavyJones(); else return new Gandalf();
        } else { 
            int pick = random.nextInt(5);
            if (pick == 0) return new Vader(); else if (pick == 1) return new T800(); else if (pick == 2) return new Neo(); else if (pick == 3) return new Agent47(); else return new DrEstranho();
        }
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