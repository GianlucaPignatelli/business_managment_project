package animazioneazienda.bean.animatore;

public class StatusAnimatore {
    private int animatoreId;
    private int aziendaId;
    private String modelloAuto;
    private String dimensioneAuto;
    private String lavoriAccettati;
    private String stato;
    private boolean haccp;

    public StatusAnimatore() {}

    public int getAnimatoreId() { return animatoreId; }
    public void setAnimatoreId(int animatoreId) { this.animatoreId = animatoreId; }
    public int getAziendaId() { return aziendaId; }
    public void setAziendaId(int aziendaId) { this.aziendaId = aziendaId; }
    public String getModelloAuto() { return modelloAuto; }
    public void setModelloAuto(String modelloAuto) { this.modelloAuto = modelloAuto; }
    public String getDimensioneAuto() { return dimensioneAuto; }
    public void setDimensioneAuto(String dimensioneAuto) { this.dimensioneAuto = dimensioneAuto; }
    public String getLavoriAccettati() { return lavoriAccettati; }
    public void setLavoriAccettati(String lavoriAccettati) { this.lavoriAccettati = lavoriAccettati; }
    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
    public boolean isHaccp() { return haccp; }
    public void setHaccp(boolean haccp) { this.haccp = haccp; }
}