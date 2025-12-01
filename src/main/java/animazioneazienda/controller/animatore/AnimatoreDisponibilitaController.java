package animazioneazienda.controller.animatore;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.dao.animatore.disponibilita.DisponibilitaAnimatoreRepository;
import animazioneazienda.exception.DaoException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AnimatoreDisponibilitaController {
    private final DisponibilitaAnimatoreRepository disponibilitaRepo;
    private final UtenteBean animatore;

    public AnimatoreDisponibilitaController(DisponibilitaAnimatoreRepository disponibilitaRepo, UtenteBean animatore) {
        this.disponibilitaRepo = disponibilitaRepo;
        this.animatore = animatore;
    }

    public List<DisponibilitaAnimatoreBean> visualizzaDisponibilita() throws DaoException {
        return disponibilitaRepo.findByAnimatore(animatore.getAziendaId(), animatore.getId());
    }

    public boolean inserisciDisponibilita(LocalDate giorno, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) throws DaoException {
        if (giorno == null || giorno.isBefore(LocalDate.now())) return false;
        if (!tuttoIlGiorno && (inizio == null || fine == null || !fine.isAfter(inizio))) return false;

        // Sovrapposizione va gestita in repository o in un metodo di utilità, qui non gestiamo direttamente
        return disponibilitaRepo.inserisciDisponibilita(
                animatore.getAziendaId(),
                animatore.getId(),
                giorno,
                inizio,
                fine,
                tuttoIlGiorno
        );
    }

    public boolean modificaDisponibilita(DisponibilitaAnimatoreBean bean, LocalDate giorno, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) throws DaoException {
        if (giorno == null || giorno.isBefore(LocalDate.now())) return false;
        if (!tuttoIlGiorno && (inizio == null || fine == null || !fine.isAfter(inizio))) return false;

        bean.setData(giorno);
        bean.setTuttoIlGiorno(tuttoIlGiorno);
        bean.setOrarioInizio(inizio);
        bean.setOrarioFine(fine);

        return disponibilitaRepo.modificaDisponibilita(bean, giorno, inizio, fine, tuttoIlGiorno);
    }

    public boolean eliminaDisponibilita(DisponibilitaAnimatoreBean bean) throws DaoException {
        return disponibilitaRepo.eliminaDisponibilita(bean);
    }
}