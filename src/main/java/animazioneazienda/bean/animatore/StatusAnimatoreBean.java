package animazioneazienda.bean.animatore;

public class StatusAnimatoreBean {
    private int id;
    private int animatoreId;
    private int aziendaId;
    private String modelloAuto;
    private String dimensioneAuto;
    private String lavoriAccettati;
    private boolean haccp;
    private String stato;

    public StatusAnimatoreBean() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

    public boolean isHaccp() { return haccp; }
    public void setHaccp(boolean haccp) { this.haccp = haccp; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
}