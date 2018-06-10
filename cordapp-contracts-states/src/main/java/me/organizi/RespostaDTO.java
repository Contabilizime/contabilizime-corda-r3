package me.organizi;

import net.corda.core.contracts.UniqueIdentifier;

public class RespostaDTO  {

    private boolean aprovacao;
    private Integer parcelas;
    private int valor;
    private String uniqueIdentifierPedido;

    public boolean isAprovacao() {
        return aprovacao;
    }

    public void setAprovacao(boolean aprovacao) {
        this.aprovacao = aprovacao;
    }

    public Integer getParcelas() {
        return parcelas;
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

    public String getUniqueIdentifierPedido() {
        return uniqueIdentifierPedido;
    }

    public void setUniqueIdentifierPedido(String uniqueIdentifierPedido) {
        this.uniqueIdentifierPedido = uniqueIdentifierPedido;
    }
}
