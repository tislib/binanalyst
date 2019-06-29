package net.tislib.binanalyst.lib.bit;

public enum BinaryValue {
    TRUE, FALSE, UNKNOWN, UNSET;

    public BinaryValue reverse() {
        switch (this) {
            case TRUE:
                return FALSE;
            case FALSE:
                return TRUE;
        }
        return this;
    }

    public byte toByteValue() {
        switch (this) {
            case FALSE:
                return 0;
            case TRUE:
                return 1;
            case UNKNOWN:
                return -1;
        }
        return 0;
    }

    public boolean isTrue() {
        return this == TRUE;
    }

    public boolean isFalse() {
        return this == FALSE;
    }

    @Override
    public String toString() {
        switch (this) {
            case UNKNOWN:
                return "U";
            case UNSET:
                return "_";
            case TRUE:
                return "1";
            case FALSE:
                return "0";
            default:
                throw new RuntimeException("Unknown binary value: " + this.name());
        }
    }

    public boolean mayBe(BinaryValue otherBit) {
        if (this == UNKNOWN || otherBit == UNKNOWN) {
            return true;
        }
        return this == otherBit;
    }
}
