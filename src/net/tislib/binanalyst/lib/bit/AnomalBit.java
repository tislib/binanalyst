package net.tislib.binanalyst.lib.bit;

import net.tislib.binanalyst.lib.calc.graph.Operation;

import java.util.StringJoiner;

/**
 * Created by Taleh Ibrahimli on 2/10/18.
 * Email: me@talehibrahimli.com
 */
public class AnomalBit extends VarBit {

    private final AnomalOperation operation;
    private final NamedBit[] bits;

    public AnomalBit(AnomalOperation operation, NamedBit[] bits) {
        this.operation = operation;
        this.bits = bits;
    }

    public enum AnomalOperation {
        RAND("R&"), ROR("R|");

        private final String sign;

        AnomalOperation(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return sign;
        }
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(" " + operation.getSign() + " ");
        for (NamedBit bit : bits) {
            joiner.add(bit.getName());
        }
        return getName() + " : " + joiner;
    }

}
