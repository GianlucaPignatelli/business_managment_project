package animazioneazienda.model.animatore;

import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;

import java.time.LocalTime;

public class DisponibilitaAnimatoreModel {
    private final DisponibilitaAnimatoreBean bean;

    public DisponibilitaAnimatoreModel(DisponibilitaAnimatoreBean bean) { this.bean = bean; }

    public boolean isNelRange(LocalTime ora) {
        if (bean.isTuttoIlGiorno()) {
            return true;
        }
        return bean.getOrarioInizio() != null && bean.getOrarioFine() != null &&
                !ora.isBefore(bean.getOrarioInizio()) && !ora.isAfter(bean.getOrarioFine());
    }

    public String toDisplayString() {
        if (bean.isTuttoIlGiorno())
            return bean.getData() + " (Tutto il giorno)";
        else
            return bean.getData() + " (" +
                    bean.getOrarioInizio() + " - " + bean.getOrarioFine() + ")";
    }
}