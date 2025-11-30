package animazioneazienda.model;

import animazioneazienda.bean.AziendaBean;

public class AziendaModel {
    private final AziendaBean bean;

    public AziendaModel(AziendaBean bean) { this.bean = bean; }

    public boolean isLargeCompany() { return bean.getNumDipendenti() > 100; }

    public String getSummary() { return bean.getNome() + " (" + bean.getNumDipendenti() + " addetti)"; }
}