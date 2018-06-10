package me.organizi;

import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public class Balanco {

    private int referencia;
    private boolean saldo;
    private int receita;
    private int lucro;

    public int getReferencia() {
        return referencia;
    }

    public void setReferencia(int referencia) {
        this.referencia = referencia;
    }

    public boolean isSaldo() {
        return saldo;
    }

    public void setSaldo(boolean saldo) {
        this.saldo = saldo;
    }

    public int getReceita() {
        return receita;
    }

    public void setReceita(int receita) {
        this.receita = receita;
    }

    public int getLucro() {
        return lucro;
    }

    public void setLucro(int lucro) {
        this.lucro = lucro;
    }

    public Balanco() {
    }

}
