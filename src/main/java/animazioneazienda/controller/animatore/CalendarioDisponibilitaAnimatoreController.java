package animazioneazienda.controller.animatore;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.dao.animatore.disponibilita.EliminaDisponibilitaDAO;
import animazioneazienda.dao.animatore.disponibilita.InserisciDisponibilitaDAO;
import animazioneazienda.dao.animatore.disponibilita.ModificaDisponibilitaDAO;
import animazioneazienda.dao.animatore.disponibilita.VisualizzaDisponibilitaDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CalendarioDisponibilitaAnimatoreController {
    private final VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO;
    private final InserisciDisponibilitaDAO inserisciDisponibilitaDAO;
    private final ModificaDisponibilitaDAO modificaDisponibilitaDAO;
    private final EliminaDisponibilitaDAO eliminaDisponibilitaDAO;
    private final UtenteBean animatore;

    public CalendarioDisponibilitaAnimatoreController(
            VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO,
            InserisciDisponibilitaDAO inserisciDisponibilitaDAO,
            ModificaDisponibilitaDAO modificaDisponibilitaDAO,
            EliminaDisponibilitaDAO eliminaDisponibilitaDAO,
            UtenteBean animatore
    ) {
        this.visualizzaDisponibilitaDAO = visualizzaDisponibilitaDAO;
        this.inserisciDisponibilitaDAO = inserisciDisponibilitaDAO;
        this.modificaDisponibilitaDAO = modificaDisponibilitaDAO;
        this.eliminaDisponibilitaDAO = eliminaDisponibilitaDAO;
        this.animatore = animatore;
    }

    public List<DisponibilitaAnimatoreBean> ottieniDisponibilita() {
        return visualizzaDisponibilitaDAO.trovaPerAnimatore(animatore.getAziendaId(), animatore.getId());
    }

    public boolean inserisciDisponibilita(LocalDate giorno, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) {
        if (giorno == null || giorno.isBefore(LocalDate.now())) return false;
        if (!tuttoIlGiorno && (inizio == null || fine == null || !fine.isAfter(inizio))) return false;
        if (visualizzaDisponibilitaDAO.esisteSovrapposizione(animatore.getAziendaId(), animatore.getId(), giorno, inizio, fine, tuttoIlGiorno)) return false;

        DisponibilitaAnimatoreBean bean = new DisponibilitaAnimatoreBean();
        bean.setAziendaId(animatore.getAziendaId());
        bean.setAnimatoreId(animatore.getId());
        bean.setData(giorno);
        bean.setTuttoIlGiorno(tuttoIlGiorno);
        bean.setOrarioInizio(inizio);
        bean.setOrarioFine(fine);

        return inserisciDisponibilitaDAO.inserisci(bean);
    }

    public boolean modificaDisponibilita(DisponibilitaAnimatoreBean bean, LocalDate giorno, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) {
        if (giorno == null || giorno.isBefore(LocalDate.now())) return false;
        if (!tuttoIlGiorno && (inizio == null || fine == null || !fine.isAfter(inizio))) return false;

        boolean cambiato = !bean.getData().equals(giorno) || bean.isTuttoIlGiorno() != tuttoIlGiorno ||
                (bean.getOrarioInizio() != null && !bean.getOrarioInizio().equals(inizio)) ||
                (bean.getOrarioFine() != null && !bean.getOrarioFine().equals(fine));

        if (cambiato && visualizzaDisponibilitaDAO.esisteSovrapposizione(animatore.getAziendaId(), animatore.getId(), giorno, inizio, fine, tuttoIlGiorno, bean.getId())) return false;

        bean.setData(giorno);
        bean.setTuttoIlGiorno(tuttoIlGiorno);
        bean.setOrarioInizio(inizio);
        bean.setOrarioFine(fine);

        return modificaDisponibilitaDAO.modifica(bean);
    }

    public boolean eliminaDisponibilita(DisponibilitaAnimatoreBean bean) {
        return eliminaDisponibilitaDAO.elimina(bean.getId(), bean.getAziendaId(), bean.getAnimatoreId());
    }
}