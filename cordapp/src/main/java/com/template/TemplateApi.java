package com.template;

import me.organizi.AceitePOJO;
import me.organizi.PedidoDTO;
import me.organizi.PedidoPOJO;
import me.organizi.RespostaPOJO;
import me.organizi.corda.PedidoEmprestimoState;
import me.organizi.corda.RespostaEmprestimoState;
import me.organizi.flow.AceitacaoEmprestimoFlow;
import me.organizi.flow.BroadcastPedidoEmprestimoFlow;
import me.organizi.flow.RespostaEmprestimoFlow;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.transactions.SignedTransaction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;

// This API is accessible from /api/template. The endpoint paths specified below are relative to it.
@Path("organizi")
public class TemplateApi {
    private final CordaRPCOps rpcOps;

    public TemplateApi(CordaRPCOps services) {
        this.rpcOps = services;
    }


    @GET
    @Path("templateGetEndpoint")
    @Produces(MediaType.APPLICATION_JSON)
    public Response templateGetEndpoint() {
        return Response.ok("Template GET endpoint.").build();
    }


    @GET
    @Path("pedidos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PedidoEmprestimoState> getPedidos(){
        return rpcOps.vaultQuery( PedidoEmprestimoState.class ).getStates().stream().map(
                vault -> vault.getState().getData()).collect( Collectors.toList() );
    }


    @GET
    @Path("respostas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RespostaEmprestimoState> getRespostas(){
        return rpcOps.vaultQuery( RespostaEmprestimoState.class ).getStates().stream().map(
                vault -> vault.getState().getData()).collect( Collectors.toList() );
    }

    @POST
    @Path("create-iou")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPedido(PedidoDTO pedido) throws InterruptedException, ExecutionException {

        PedidoPOJO pedidoPOJO = new PedidoPOJO();
        pedidoPOJO.setParty( rpcOps.nodeInfo().getLegalIdentities().get( 0 ) );
        pedidoPOJO.setValorEmprestimo( pedido.getValorEmprestimo() );
        pedidoPOJO.setBalanco(pedido.getBalanco());

        try {
            final SignedTransaction signedTx = rpcOps
                    .startTrackedFlowDynamic(BroadcastPedidoEmprestimoFlow.Initiator.class, pedidoPOJO)
                    .getReturnValue()
                    .get();

            final String msg = String.format("Transaction id %s committed to ledger.\n", signedTx.getId());
            return Response.status(CREATED).entity(msg).build();

        } catch (Throwable ex) {
            final String msg = ex.getMessage();
            return Response.status(BAD_REQUEST).entity(msg).build();
        }
    }

    @POST
    @Path("offer-iou")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reponderPedido(RespostaPOJO resposta) throws InterruptedException, ExecutionException {


        try {
            final SignedTransaction signedTx = rpcOps
                    .startTrackedFlowDynamic(RespostaEmprestimoFlow.Initiator.class, resposta)
                    .getReturnValue()
                    .get();

            final String msg = String.format("Transaction id %s committed to ledger.\n", signedTx.getId());
            return Response.status(CREATED).entity(msg).build();

        } catch (Throwable ex) {
            final String msg = ex.getMessage();
            return Response.status(BAD_REQUEST).entity(msg).build();
        }
    }


    @POST
    @Path("accept-offer-iou")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response aceitarProposta(AceitePOJO aceite) throws InterruptedException, ExecutionException {

        aceite.setParty( rpcOps.nodeInfo().getLegalIdentities().get( 0 ) );

        try {
            final SignedTransaction signedTx = rpcOps
                    .startTrackedFlowDynamic(AceitacaoEmprestimoFlow.Initiator.class, aceite)
                    .getReturnValue()
                    .get();

            final String msg = String.format("Transaction id %s committed to ledger.\n", signedTx.getId());
            return Response.status(CREATED).entity(msg).build();

        } catch (Throwable ex) {
            final String msg = ex.getMessage();
            return Response.status(BAD_REQUEST).entity(msg).build();
        }
    }



}