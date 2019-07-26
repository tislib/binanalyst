package net.tislib.binanalyst.lib.calc.graph;

public enum Operation {
    XOR("^"), AND("&"), OR("|"), NOT("~"), COPY("");

    private CharSequence sign;


    Operation(CharSequence sign) {
        this.sign = sign;
    }

    public CharSequence getSign() {
        return sign;
    }

    public String getSignName() {
        switch (this) {
            case XOR:
                return "XOR";
            case NOT:
                return "NOT";
            case AND:
                return "AND";
            case OR:
                return "OR";
            default:
                throw new UnsupportedOperationException();
        }
    }
}