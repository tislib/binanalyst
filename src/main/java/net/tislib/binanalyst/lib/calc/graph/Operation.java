package net.tislib.binanalyst.lib.calc.graph;

public enum Operation {
    XOR(SIGNS.XOR), AND(SIGNS.AND), OR(SIGNS.OR), NOT(SIGNS.NOT), COPY(SIGNS.COPY);

    private CharSequence sign;

    Operation(CharSequence sign) {
        this.sign = sign;
    }

    public static Operation fromSign(String value) {
        switch (value) {
            case SIGNS.XOR:
                return XOR;
            case SIGNS.AND:
                return AND;
            case SIGNS.OR:
                return OR;
            case SIGNS.COPY:
                return COPY;
            case SIGNS.NOT:
                return NOT;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public CharSequence getSign() {
        return sign;
    }

    public String getSignName() {
        return name();
    }

    public static class SIGNS {
        public static final String XOR = "^";
        public static final String AND = "&";
        public static final String OR = "|";
        public static final String NOT = "~";
        public static final String COPY = "@";
    }
}