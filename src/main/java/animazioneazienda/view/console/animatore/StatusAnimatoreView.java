package animazioneazienda.view.console.animatore;

import java.util.*;
import animazioneazienda.bean.animatore.StatusAnimatoreBean;

public class StatusAnimatoreView {
    // Ruoli disponibili
    private final String[] ruoliDisponibili = {
            "Capoanimatore",
            "Aiutoanimatore",
            "Delivery",
            "Operatore carretti"
    };

    public StatusAnimatoreBean creaProfiloStatus() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleziona i ruoli desiderati (inserisci numeri separati da virgola):");
        for (int i = 0; i < ruoliDisponibili.length; i++) {
            System.out.printf("%d. %s%n", i + 1, ruoliDisponibili[i]);
        }
        System.out.print("Scelta: ");
        String input = scanner.nextLine();
        String[] scelte = input.split(",");
        List<String> ruoliSelezionati = new ArrayList<>();
        boolean haccp = false;

        for (String s : scelte) {
            try {
                int idx = Integer.parseInt(s.trim()) - 1;
                if (idx >= 0 && idx < ruoliDisponibili.length) {
                    String ruolo = ruoliDisponibili[idx];
                    ruoliSelezionati.add(ruolo);
                    if ("Operatore carretti".equals(ruolo)) {
                        System.out.print("Hai la certificazione HACCP? (s/n): ");
                        String val = scanner.nextLine().trim().toLowerCase();
                        haccp = val.equals("s");
                    }
                }
            } catch (NumberFormatException ex) {
                // ignora input errato
            }
        }

        String lavoriAccettati = String.join(",", ruoliSelezionati);

        // Qui crea il bean StatusAnimatore
        StatusAnimatoreBean status = new StatusAnimatoreBean();
        status.setLavoriAccettati(lavoriAccettati);
        status.setHaccp(haccp);

        System.out.println("Ruoli selezionati: " + lavoriAccettati);
        if (ruoliSelezionati.contains("Operatore carretti"))
            System.out.println("Certificazione HACCP: " + (haccp ? "SÌ" : "NO"));

        return status;
    }
}