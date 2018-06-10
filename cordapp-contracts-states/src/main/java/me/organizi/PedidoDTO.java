package me.organizi;

import net.corda.core.identity.Party;

import java.util.List;

public class PedidoDTO {

    private String balanco;
    private int valorEmprestimo;

    public String getBalanco() {
        return balanco;
    }

    public void setBalanco(String balanco) {
        this.balanco = balanco;
    }

    public int getValorEmprestimo() {
        return valorEmprestimo;
    }

    public void setValorEmprestimo(int valorEmprestimo) {
        this.valorEmprestimo = valorEmprestimo;
    }


    public PedidoDTO() {
    }
}
