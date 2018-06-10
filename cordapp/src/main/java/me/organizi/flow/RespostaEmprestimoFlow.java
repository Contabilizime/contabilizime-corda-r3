package me.organizi.flow;

import co.paralleluniverse.fibers.Suspendable;
import me.organizi.RespostaPOJO;
import me.organizi.corda.EmprestimoContract;
import me.organizi.corda.PedidoEmprestimoState;
import me.organizi.corda.RespostaEmprestimoState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.flows.*;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RespostaEmprestimoFlow {

    @InitiatingFlow
    @StartableByRPC
    public static class Initiator extends FlowLogic<SignedTransaction> {

        private RespostaPOJO respostaPOJO;
        public Initiator(RespostaPOJO resposta){
            this.respostaPOJO = resposta;
        }

        @Suspendable
        @Override public SignedTransaction call() throws FlowException {

            //chamar notary
            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

            List<UniqueIdentifier> uniqueIdentifierList = new ArrayList<>(  );
            uniqueIdentifierList.add( respostaPOJO.getUniqueIdentifierPedido());

            QueryCriteria.LinearStateQueryCriteria linearStateQueryCriteria =
                    new QueryCriteria.LinearStateQueryCriteria( null, uniqueIdentifierList,
                    Vault.StateStatus.UNCONSUMED, null );

            //pesquisar State
            Vault.Page<PedidoEmprestimoState> results =
                    getServiceHub().getVaultService().queryBy(PedidoEmprestimoState.class, linearStateQueryCriteria);


            Party responderPara = results.getStates().get(0).getState().getData().getPedido().getParty();

            //criar state

            List<AbstractParty> listaAbstractParties = new ArrayList<AbstractParty>() ;
            listaAbstractParties.add( responderPara.anonymise() );
            listaAbstractParties.add( getOurIdentity().anonymise() );


            RespostaEmprestimoState respostaEmprestimoState =
                    new RespostaEmprestimoState( respostaPOJO, listaAbstractParties, new UniqueIdentifier(  ) );


            //informar intencao ... CommandData resposta da solicitações
            Command<EmprestimoContract.Commands> cmd =
                    new Command<EmprestimoContract.Commands>(
                            new EmprestimoContract.Commands.RespostaAoPedido(),
                            listaAbstractParties.stream().map( p -> p.getOwningKey()).collect( Collectors.toList()));
//

            //criar transacao -- transactionBuilder

            TransactionBuilder transactionBuilder = new TransactionBuilder(notary);
            transactionBuilder.addCommand( cmd );
            transactionBuilder.addOutputState( respostaEmprestimoState, EmprestimoContract.class.getCanonicalName());
            transactionBuilder.verify( getServiceHub());

            //chamar subflow para assinatura do emissor do pedido
            SignedTransaction signedTransaction = getServiceHub().signInitialTransaction( transactionBuilder );

            List<FlowSession> flowSessionList = new ArrayList<>();
            flowSessionList.add( initiateFlow( responderPara ) );


            SignedTransaction fullSignedTransaction =
                    subFlow( new CollectSignaturesFlow( signedTransaction, flowSessionList ));


            //finality flow, mandar para notary
            return  subFlow( new FinalityFlow( fullSignedTransaction )  );

        }
    }

    @InitiatedBy(Initiator.class)
    public static class Responder extends FlowLogic<SignedTransaction> {
        private FlowSession counterpartySession;

        public Responder(FlowSession counterpartySession) {
            this.counterpartySession = counterpartySession;
        }

        /**
         * Define the acceptor's flow logic here.
         */
        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {


            return subFlow(new VerificadorAssinatura( counterpartySession,  SignTransactionFlow.tracker()  ));
        }

        public class VerificadorAssinatura extends  SignTransactionFlow{

            public VerificadorAssinatura(FlowSession otherSideSession, ProgressTracker progressTracker) {
                super( otherSideSession, progressTracker );
            }

            @Override
            protected void checkTransaction(SignedTransaction stx) throws FlowException {
                //validar infos do nó
                //
            }
        }

    }
}
