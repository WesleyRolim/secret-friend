package br.com.friendsecretandroid.wrstecnology.model;

public class Participantes {
    private String nome;
    private String telefone;
    private String sugestao;

    public Participantes (String nome, String telefone, String sugestao){
        this.nome = nome;
        this.telefone = telefone;
        this.sugestao = sugestao;
    }

    public Participantes (){
        this.nome = "";
        this.telefone = "";
        this.sugestao = "";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSugestao() {
        return sugestao;
    }

    public void setSugestao(String sugestao) {
        this.sugestao = sugestao;
    }
}
