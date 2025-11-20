package animazioneazienda.bean;

public class Azienda {
    private int id;
    private String nome;
    // Puoi aggiungere altri campi come indirizzo, telefono, etc.

    public Azienda() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}