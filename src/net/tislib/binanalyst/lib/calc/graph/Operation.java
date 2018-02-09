package net.tislib.binanalyst.lib.calc.graph;

public enum Operation {
    XOR("^"), AND("&"), OR("|"), NOT("~");

    private CharSequence sign;


    Operation(CharSequence sign) {
        this.sign = sign;
    }

    public CharSequence getSign() {
        return sign;
    }
}