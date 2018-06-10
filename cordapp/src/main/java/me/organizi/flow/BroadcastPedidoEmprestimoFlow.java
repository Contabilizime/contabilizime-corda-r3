package me.organizi.flow;

import co.paralleluniverse.fibers.Suspendable;
import me.organizi.PedidoPOJO;
import me.organizi.corda.EmprestimoContract;
import me.organizi.corda.PedidoEmprestimoState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.node.NodeInfo;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BroadcastPedidoEmprestimoFlow {

    @InitiatingFlow
    @StartableByRPC
    public static class Initiator extends FlowLogic<SignedTransaction> {
        /**
         * Define the initiator's flow logic here.
         */

        private PedidoPOJO pedidoPOJO;
        public Initiator(PedidoPOJO pedido){
            this.pedidoPOJO = pedido;
        }

        @Suspendable
        @Override public SignedTransaction call() throws FlowException {

            //chamar notary
            //criar state
            //informar intencao ... CommandData com participantes envolvidos
            //criar transacao -- transactionBuilder
            //chamar subflow para assinatura de todos os participantes
            //finality flow, mandar para notary

            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

            List<Party> listParty = getServiceHub().getNetworkMapCache().getAllNodes().stream().filter( node ->
                    !(node.getLegalIdentities().contains( getOurIdentity()) || node.getLegalIdentities().contains( notary))
            ).map( NodeInfo::getLegalIdentities).flatMap( Collection::stream ).collect( Collectors.toList( ));

            List<Party> listPartyComigo =  new ArrayList<Party>(listParty);
            listPartyComigo.add( getOurIdentity() );

            PedidoEmprestimoState pedidoEmprestimoState = new PedidoEmprestimoState(pedidoPOJO,
                    listPartyComigo.stream().map(Party::anonymise).collect( Collectors.toList()), new UniqueIdentifier());


            Command<EmprestimoContract.Commands> cmd =
                    new Command<EmprestimoContract.Commands>(new EmprestimoContract.Commands.PedirEmprestimo(),
                            listPartyComigo.stream().map( p -> p.getOwningKey()).collect( Collectors.toList()));

            TransactionBuilder transactionBuilder = new TransactionBuilder( notary);
            transactionBuilder.addCommand( cmd );
            transactionBuilder.addOutputState( pedidoEmprestimoState, EmprestimoContract.class.getCanonicalName());
            transactionBuilder.verify( getServiceHub());

            SignedTransaction signedTransaction = getServiceHub().signInitialTransaction( transactionBuilder );

            List<FlowSession> flowSessionList =  listParty.stream().map(
                    p -> initiateFlow( p ) ).collect(  Collectors.toList());

            SignedTransaction fullSignedTransaction =
                    subFlow( new CollectSignaturesFlow( signedTransaction, flowSessionList ) );

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

            ProgressTracker pt = new ProgressTracker(  );


            return subFlow( new VerificadorAssinatura( counterpartySession, SignTransactionFlow.tracker()  ));
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
