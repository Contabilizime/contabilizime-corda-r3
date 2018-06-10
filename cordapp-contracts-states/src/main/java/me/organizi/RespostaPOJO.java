package me.organizi;


import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.Party;
import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public class RespostaPOJO {

    private boolean aprovacao;
    private Integer parcelas;
    private int valor;
    private UniqueIdentifier uniqueIdentifierPedido;
    private Party respondendoParty;


    public boolean isAprovacao() {
        return aprovacao;
    }

    public void setAprovacao(boolean aprovacao) {
        this.aprovacao = aprovacao;
    }

    public Integer getParcelas() {
        return parcelas;
    }

    public Party getRespondendoParty() {
        return respondendoParty;
    }

    public void setRespondendoParty(Party respondendoParty) {
        this.respondendoParty = respondendoParty;
    }

    public void setParcelas(Integer parcelas) {
        this.parcelas = parcelas;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }



    public UniqueIdentifier getUniqueIdentifierPedido() {
        return uniqueIdentifierPedido;
    }

    public void setUniqueIdentifierPedido(String identifierPedido, UniqueIdentifier uniqueIdentifierPedido) {
        this.uniqueIdentifierPedido = uniqueIdentifierPedido;
    }
}
