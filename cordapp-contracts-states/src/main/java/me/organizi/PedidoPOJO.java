package me.organizi;

import net.corda.core.identity.Party;
import net.corda.core.serialization.CordaSerializable;

import java.util.Date;
import java.util.List;

@CordaSerializable
public class PedidoPOJO {

    private String balanco;
    private int valorEmprestimo;
    private Party party;

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

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }


    public PedidoPOJO() {
    }
}
