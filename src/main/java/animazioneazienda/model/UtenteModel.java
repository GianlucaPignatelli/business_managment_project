package animazioneazienda.model;

import animazioneazienda.bean.UtenteBean;

public class UtenteModel {
    private final UtenteBean bean;
    public UtenteModel(UtenteBean bean) { this.bean = bean; }

    public boolean isAnimatore() { return bean.getRuolo() == UtenteBean.Ruolo.ANIMATORE; }
    public boolean isAdmin() { return bean.getRuolo() == UtenteBean.Ruolo.AMMINISTRATORE; }
    public boolean isSuperAdmin() { return bean.getRuolo() == UtenteBean.Ruolo.SUPERADMIN; }
    public boolean isActive() { return bean.isAttivo(); }
    public String getFullName() { return bean.getNome() + " " + bean.getCognome(); }
}