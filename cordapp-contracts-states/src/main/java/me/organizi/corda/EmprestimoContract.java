package me.organizi.corda;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.serialization.CordaSerializable;
import net.corda.core.transactions.LedgerTransaction;


public class EmprestimoContract implements Contract {


    @Override
    public void verify(LedgerTransaction tx) throws IllegalArgumentException {

    }

    public static class Commands implements CommandData {


        public static class PedirEmprestimo extends Commands {
            @Override
            public boolean equals(Object obj) {
                return obj instanceof PedirEmprestimo;
            }
        }

        public static class RespostaAoPedido extends Commands {
            @Override
            public boolean equals(Object obj) {
                return obj instanceof RespostaAoPedido;
            }
        }

        public static class TreplicaAoPedido extends Commands {
            @Override
            public boolean equals(Object obj) {
                return obj instanceof TreplicaAoPedido;
            }
        }
    }
}


