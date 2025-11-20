package animazioneazienda.bean.animatore;

import java.time.LocalDate;
import java.time.LocalTime;

public class DisponibilitaAnimatore {
    private int id;
    private int aziendaId;
    private int animatoreId;
    private LocalDate data;
    private boolean tuttoIlGiorno;
    private LocalTime orarioInizio;
    private LocalTime orarioFine;

    public DisponibilitaAnimatore() {}

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAziendaId() { return aziendaId; }
    public void setAziendaId(int aziendaId) { this.aziendaId = aziendaId; }
    public int getAnimatoreId() { return animatoreId; }
    public void setAnimatoreId(int animatoreId) { this.animatoreId = animatoreId; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public boolean isTuttoIlGiorno() { return tuttoIlGiorno; }
    public void setTuttoIlGiorno(boolean tuttoIlGiorno) { this.tuttoIlGiorno = tuttoIlGiorno; }
    public LocalTime getOrarioInizio() { return orarioInizio; }
    public void setOrarioInizio(LocalTime orarioInizio) { this.orarioInizio = orarioInizio; }
    public LocalTime getOrarioFine() { return orarioFine; }
    public void setOrarioFine(LocalTime orarioFine) { this.orarioFine = orarioFine; }
}