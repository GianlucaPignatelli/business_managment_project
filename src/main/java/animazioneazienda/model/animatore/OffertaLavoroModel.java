package animazioneazienda.model.animatore;

import animazioneazienda.bean.animatore.OffertaLavoroBean;

import java.time.LocalDate;
import java.time.LocalTime;

public class OffertaLavoroModel {
    private final OffertaLavoroBean bean;

    public OffertaLavoroModel(OffertaLavoroBean bean) { this.bean = bean; }

    /** True se offerta va ancora gestita dall'animatore */
    public boolean isDaRispondere() {
        return "inviato".equalsIgnoreCase(bean.getStato());
    }

    public boolean isAccettata() {
        return "accettato".equalsIgnoreCase(bean.getStato());
    }

    public boolean isRifiutata() {
        return "rifiutato".equalsIgnoreCase(bean.getStato());
    }

    /** True se l'offerta è per una data passata */
    public boolean isScaduta() {
        return bean.getDataEvento().isBefore(LocalDate.now());
    }

    public String getFasciaOraria() {
        if (bean.getOrarioInizio() == null || bean.getOrarioFine() == null) return "Intera giornata";
        return String.format("%02d:%02d-%02d:%02d",
                bean.getOrarioInizio().getHour(), bean.getOrarioInizio().getMinute(),
                bean.getOrarioFine().getHour(), bean.getOrarioFine().getMinute());
    }

    /** Stringa compatta per visualizzazione GUI/lista/console */
    public String getDisplayString() {
        return String.format(
                "[%s %s] %s - %s (%s)",
                bean.getDataEvento(),
                getFasciaOraria(),
                bean.getDescrizione(),
                bean.getStato(),
                bean.getAziendaId()
        );
    }

    /** True se offerta è per una data e orario attualmente in corso */
    public boolean isEventoInCorso() {
        LocalDate oggi = LocalDate.now();
        LocalTime now = LocalTime.now();
        if (!bean.getDataEvento().equals(oggi)) return false;
        if (bean.getOrarioInizio() == null || bean.getOrarioFine() == null) return true;
        return !now.isBefore(bean.getOrarioInizio()) && !now.isAfter(bean.getOrarioFine());
    }
}