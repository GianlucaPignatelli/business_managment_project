package animazioneazienda.bean;

import java.sql.Date;

public class UtenteBean {
    public enum Ruolo { AMMINISTRATORE, ANIMATORE, SUPERADMIN }

    private int id;
    private int aziendaId;
    private String aziendaNome;
    private String nome;
    private String cognome;
    private Ruolo ruolo;
    private boolean attivo;

    /** CAMPI MANCANTI - aggiunti: */
    private String email;
    private String password;
    private String sesso;
    private Date dataNascita;

    public UtenteBean() {}

    // Costruttori sovraccarichi richiamati dai vari file:
    public UtenteBean(String email, String password, Ruolo ruolo, int aziendaId, String aziendaNome, String nome, String cognome, String sesso, Date dataNascita) {
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
        this.aziendaId = aziendaId;
        this.aziendaNome = aziendaNome;
        this.nome = nome;
        this.cognome = cognome;
        this.sesso = sesso;
        this.dataNascita = dataNascita;
        this.attivo = true;
    }
    public UtenteBean(String email, String password, Ruolo ruolo, int aziendaId, String aziendaNome) {
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
        this.aziendaId = aziendaId;
        this.aziendaNome = aziendaNome;
        this.attivo = true;
    }

    // Getter & setter per TUTTI I CAMPI:
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAziendaId() { return aziendaId; }
    public void setAziendaId(int aziendaId) { this.aziendaId = aziendaId; }

    public String getAziendaNome() { return aziendaNome; }
    public void setAziendaNome(String aziendaNome) { this.aziendaNome = aziendaNome; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public Ruolo getRuolo() { return ruolo; }
    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }

    public boolean isAttivo() { return attivo; }
    public void setAttivo(boolean attivo) { this.attivo = attivo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSesso() { return sesso; }
    public void setSesso(String sesso) { this.sesso = sesso; }

    public Date getDataNascita() { return dataNascita; }
    public void setDataNascita(Date dataNascita) { this.dataNascita = dataNascita; }
}