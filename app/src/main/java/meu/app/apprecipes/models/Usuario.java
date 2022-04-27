package meu.app.apprecipes.models;

public class Usuario {
    private int id;
    private String nomeUsuario;
    private String nomeCompleto;
    private String email;
    private String senha;

    public Usuario(String nomeUsuario, String nomeCompleto, String email, String senha) {
        this.nomeUsuario = nomeUsuario;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(int id, String nomeUsuario, String nomeCompleto, String email, String senha) {
        this(nomeUsuario, nomeCompleto, email, senha);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String username) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String fullname) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String password) {
        this.senha = senha;
    }
}
