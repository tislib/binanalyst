package net.tislib.binanalyst.lib.algorithms.sha.sha256;

import net.tislib.binanalyst.lib.bit.Bit;

public interface Sha256Algorithm {
    Bit[][] hash(byte[] bytes);

    Bit[][] hash(Bit[][] bits);

    void init();

    void update(Bit[][] words);
}
