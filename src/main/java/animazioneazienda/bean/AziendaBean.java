package animazioneazienda.bean;

public class AziendaBean {
    private int id;
    private String nome;
    private int numDipendenti;

    public AziendaBean() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getNumDipendenti() { return numDipendenti; }
    public void setNumDipendenti(int numDipendenti) { this.numDipendenti = numDipendenti; }
}