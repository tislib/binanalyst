package net.tislib.binanalyst.lib.analyse.reverseCalc;

import net.tislib.binanalyst.lib.bit.NamedBit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;

public class ReverseCalculator {
    private final BitOpsGraphCalculator innerCalculator;

    public class PendingReverseOperationalBit extends ReverseOperationalBit {
        private final String name;

        public PendingReverseOperationalBit(String name) {
            this.name = name;
        }
    }

    public ReverseCalculator(BitOpsGraphCalculator innerCalculator) {
        this.innerCalculator = innerCalculator;
    }

    public ReverseOperationalBit wrap(NamedBit bit) {
//        ReverseOperationalBit reverseOperationalBit = new ReverseOperationalBit();
//
//        reverseOperationalBit.truth = bit;
//        reverseOperationalBit.falsy = innerCalculator.not(bit);
//
//        reverseOperationalBit.truthU = bit;
//        reverseOperationalBit.falsyU = innerCalculator.not(bit);

        return new PendingReverseOperationalBit(bit.getName());
    }

    public ReverseOperationalBit and(ReverseOperationalBit a, ReverseOperationalBit b) {
        if (a == null) {
            return b;
        }

        ReverseOperationalBit reverseOperationalBit = new ReverseOperationalBit();

        reverseOperationalBit.truth = and(a.truth, b.truth);
        reverseOperationalBit.falsy = and(a.falsy, b.falsy);

//        reverseOperationalBit.truthU = innerCalculator.or(
//                innerCalculator.and(a.truth, b.truth),
//                innerCalculator.and(a.truthU, b.truth),
//                innerCalculator.and(a.truth, b.truthU)
//        );
//        reverseOperationalBit.falsyU = innerCalculator.or(
//                innerCalculator.and(a.falsy, b.falsy),
//                innerCalculator.and(a.falsyU, b.falsy),
//                innerCalculator.and(a.falsy, b.falsyU)
//        );

        return reverseOperationalBit;
    }

    public ReverseOperationalBit or(ReverseOperationalBit a, ReverseOperationalBit b) {
        ReverseOperationalBit reverseOperationalBit = new ReverseOperationalBit();

//        reverseOperationalBit.truth = innerCalculator.or(a.truth, b.truth);
//        reverseOperationalBit.falsy = innerCalculator.or(a.falsy, b.falsy);
//
//        reverseOperationalBit.truthU = innerCalculator.or(a.truth, b.truth, a.truthU, b.truthU);
//        reverseOperationalBit.falsyU = innerCalculator.or(a.falsy, b.falsy, a.falsyU, b.falsyU);

        return reverseOperationalBit;
    }

    public ReverseOperationalBit xor(ReverseOperationalBit a, ReverseOperationalBit b) {
        return or(and(a, not(b)), and(b, not(a)));
    }

    public ReverseOperationalBit not(ReverseOperationalBit a) {
        ReverseOperationalBit reverseOperationalBit = new ReverseOperationalBit();
        reverseOperationalBit.truth = a.falsy;
        reverseOperationalBit.truthU = a.falsyU;
        reverseOperationalBit.falsy = a.truth;
        reverseOperationalBit.falsyU = a.truthU;
        return reverseOperationalBit;
    }

    public ReverseOperationalBit reverseAnd(OperationalBit operationalBit, NamedBit otherBit) {
        ReverseOperationalBit reverseOperationalBit = new ReverseOperationalBit();

        reverseOperationalBit.truth = wrap(operationalBit);
        reverseOperationalBit.truthU = or(wrap(operationalBit), not(wrap(otherBit)));
        reverseOperationalBit.falsy = and(not(wrap(operationalBit)), wrap(otherBit));
        reverseOperationalBit.falsyU = not(wrap(operationalBit));

        return reverseOperationalBit;
    }

    public ReverseOperationalBit reverseOr(OperationalBit operationalBit, NamedBit otherBit) {
        ReverseOperationalBit reverseOperationalBit = new ReverseOperationalBit();

        reverseOperationalBit.truth = and(wrap(operationalBit), not(wrap(otherBit)));
        reverseOperationalBit.truthU = wrap(operationalBit);
        reverseOperationalBit.falsy = not(wrap(operationalBit));
        reverseOperationalBit.falsyU = or(not(wrap(operationalBit)), wrap(otherBit));

        return reverseOperationalBit;
    }

    public void resolve() {

    }
}
