package me.organizi;

import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.Party;
import net.corda.core.serialization.CordaSerializable;

import java.util.Date;

@CordaSerializable
public class AceitePOJO {

    private boolean aceite;
    private UniqueIdentifier uniqueIdentifierResposta;
    private Party party;

    public boolean isAceite() {
        return aceite;
    }

    public void setAceite(boolean aceite) {
        this.aceite = aceite;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public AceitePOJO(boolean aceite, Party party) {
        this.aceite = aceite;
        this.party = party;
    }

    public AceitePOJO(boolean aceite, UniqueIdentifier uniqueIdentifierResposta) {
        this.aceite = aceite;
        this.uniqueIdentifierResposta = uniqueIdentifierResposta;
    }

    public UniqueIdentifier getUniqueIdentifierResposta() {
        return uniqueIdentifierResposta;
    }

    public void setUniqueIdentifierResposta(UniqueIdentifier uniqueIdentifierResposta) {
        this.uniqueIdentifierResposta = uniqueIdentifierResposta;
    }

    public AceitePOJO(){}
}
