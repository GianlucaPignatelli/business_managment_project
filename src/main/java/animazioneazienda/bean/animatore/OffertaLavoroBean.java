package animazioneazienda.bean.animatore;

import java.time.LocalDate;
import java.time.LocalTime;

public class OffertaLavoroBean {
    private int id;
    private int aziendaId;
    private int animatoreId;
    private LocalDate dataEvento;
    private LocalTime orarioInizio;
    private LocalTime orarioFine;
    private String descrizione;
    private String stato; // inviato, accettato, rifiutato

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAziendaId() { return aziendaId; }
    public void setAziendaId(int aziendaId) { this.aziendaId = aziendaId; }
    public int getAnimatoreId() { return animatoreId; }
    public void setAnimatoreId(int animatoreId) { this.animatoreId = animatoreId; }
    public LocalDate getDataEvento() { return dataEvento; }
    public void setDataEvento(LocalDate dataEvento) { this.dataEvento = dataEvento; }
    public LocalTime getOrarioInizio() { return orarioInizio; }
    public void setOrarioInizio(LocalTime orarioInizio) { this.orarioInizio = orarioInizio; }
    public LocalTime getOrarioFine() { return orarioFine; }
    public void setOrarioFine(LocalTime orarioFine) { this.orarioFine = orarioFine; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
}