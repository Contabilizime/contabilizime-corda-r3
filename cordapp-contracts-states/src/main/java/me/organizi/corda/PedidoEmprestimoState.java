package me.organizi.corda;

import me.organizi.PedidoPOJO;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.serialization.CordaSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@CordaSerializable
public class PedidoEmprestimoState implements LinearState {

    private UniqueIdentifier identifier;
    private PedidoPOJO pedido;

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

    public PedidoEmprestimoState(){
        this.identifier = new UniqueIdentifier();
    }

    public  PedidoEmprestimoState(PedidoPOJO pedido, List<AbstractParty> participants, UniqueIdentifier identifier){
        this.identifier = identifier;
        this.pedido = pedido;

        this.participants = participants;

    }
    public void setIdentifier(UniqueIdentifier identifier) {
        this.identifier = identifier;
    }

    public void setPedido(PedidoPOJO pedido) {
        this.pedido = pedido;
    }

    public void setParticipants(List<AbstractParty> participants) {
        this.participants = participants;
    }

    public UniqueIdentifier getIdentifier() {
        return identifier;
    }

    public PedidoPOJO getPedido() {
        return pedido;
    }


}

