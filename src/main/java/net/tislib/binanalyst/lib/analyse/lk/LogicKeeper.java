package net.tislib.binanalyst.lib.analyse.lk;

import net.tislib.binanalyst.lib.bit.NamedBit;

public interface LogicKeeper {

    void keep(NamedBit operationalBit);

    void showFormula(NamedBit namedBit);
}