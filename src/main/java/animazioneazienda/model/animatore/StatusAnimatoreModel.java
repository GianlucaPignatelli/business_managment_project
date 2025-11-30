package animazioneazienda.model.animatore;

import animazioneazienda.bean.animatore.StatusAnimatoreBean;

public class StatusAnimatoreModel {
    private final StatusAnimatoreBean bean;

    public StatusAnimatoreModel(StatusAnimatoreBean bean) { this.bean = bean; }

    public boolean isDisponibile() { return "Disponibile".equalsIgnoreCase(bean.getStato()); }

    public boolean isAbilitatoCarretti() {
        return bean.getLavoriAccettati().contains("Operatore carretti") && bean.isHaccp();
    }

    public String getSummary() {
        return "Auto: " + bean.getModelloAuto() +
                ", Ruoli: " + bean.getLavoriAccettati() +
                ", Stato: " + bean.getStato() +
                (bean.isHaccp() ? " (HACCP OK)" : "");
    }
}