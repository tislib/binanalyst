package net.tislib.binanalyst.lib.bit;

import java.util.StringJoiner;

/**
 * Created by Taleh Ibrahimli on 2/10/18.
 * Email: me@talehibrahimli.com
 */
public class ReverseBit extends VarBit {

    private final AnomalOperation operation;
    private final NamedBit[] bits;

    public ReverseBit(AnomalOperation operation, NamedBit[] bits) {
        this.operation = operation;
        this.bits = bits;
    }

    @Override
    public String getType() {
        return "reverse";
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(" " + operation.getSign() + " ");
        for (NamedBit bit : bits) {
            joiner.add(bit.getName());
        }
        return getName() + " : " + joiner;
    }

    public NamedBit[] getBits() {
        return bits;
    }

    public AnomalOperation getOperation() {
        return operation;
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
}
