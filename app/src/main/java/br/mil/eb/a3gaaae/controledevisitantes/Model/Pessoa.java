package br.mil.eb.a3gaaae.controledevisitantes.Model;

public class Pessoa {
    private int id;
    private String nome, titulo, carroModelo, carroPlaca, identidade, cpf;
    private String entrada;

    public Pessoa(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getIdentidade() {
        return identidade;
    }

    public void setIdentidade(String identidade) {
        this.identidade = identidade;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCarroModelo() {
        return carroModelo;
    }

    public void setCarroModelo(String carroModelo) {
        this.carroModelo = carroModelo;
    }

    public String getCarroPlaca() {
        return carroPlaca;
    }

    public void setCarroPlaca(String carroPlaca) {
        this.carroPlaca = carroPlaca;
    }


    public void cadastrar(){

    }

    public void atualizar(){

    }

    public void registrarEntrada(Pessoa p){

    }

    public void rigistrarSaida(Pessoa p){

    }

    public String getEntrada() {
        return this.entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }
}
