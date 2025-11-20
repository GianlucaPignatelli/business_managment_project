package animazioneazienda.bean;

public class Utente {
    public enum Ruolo { SUPERADMIN, AMMINISTRATORE, ANIMATORE }

    private int id;
    private String email;
    private String password;
    private Ruolo ruolo;
    private int aziendaId;
    private String aziendaNome;

    public Utente() {}

    public Utente(String email, String password, Ruolo ruolo, int aziendaId, String aziendaNome) {
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
        this.aziendaId = aziendaId;
        this.aziendaNome = aziendaNome;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Ruolo getRuolo() { return ruolo; }
    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }

    public int getAziendaId() { return aziendaId; }
    public void setAziendaId(int aziendaId) { this.aziendaId = aziendaId; }

    public String getAziendaNome() { return aziendaNome; }
    public void setAziendaNome(String aziendaNome) { this.aziendaNome = aziendaNome; }
}