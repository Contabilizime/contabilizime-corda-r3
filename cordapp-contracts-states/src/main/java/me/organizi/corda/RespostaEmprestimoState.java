package me.organizi.corda;


import me.organizi.AceitePOJO;
import me.organizi.RespostaPOJO;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.serialization.CordaSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@CordaSerializable
public class RespostaEmprestimoState implements LinearState {

    private UniqueIdentifier identifier;
    private UniqueIdentifier identifierPedidoEmprestimo;
    private RespostaPOJO resposta;
    private AceitePOJO aceite;
    private List<AbstractParty> participants;

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return participants;
    }

    @NotNull
    @Override
    public UniqueIdentifier getLinearId() {
        return identifier;
    }

    public RespostaEmprestimoState(){
        this.identifier = new UniqueIdentifier( );
    }

    public UniqueIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UniqueIdentifier identifier) {
        this.identifier = identifier;
    }

    public UniqueIdentifier getIdentifierPedidoEmprestimo() {
        return identifierPedidoEmprestimo;
    }

    public void setIdentifierPedidoEmprestimo(UniqueIdentifier identifierPedidoEmprestimo) {
        this.identifierPedidoEmprestimo = identifierPedidoEmprestimo;
    }

    public AceitePOJO getAceite() {
        return aceite;
    }

    public RespostaPOJO getResposta() {
        return resposta;
    }

    public void setResposta(RespostaPOJO resposta) {
        this.resposta = resposta;
    }

    public void setParticipants(List<AbstractParty> participants) {
        this.participants = participants;
    }

    public AceitePOJO getAceitacao() {
        return aceite;
    }

    public void setAceite(AceitePOJO aceite) {
        this.aceite = aceite;
    }

    public  RespostaEmprestimoState(RespostaPOJO resposta, List<AbstractParty> participants, UniqueIdentifier identifier){
        this.identifier = identifier;
        this.resposta = resposta;
        this.participants = participants;
    }
}
