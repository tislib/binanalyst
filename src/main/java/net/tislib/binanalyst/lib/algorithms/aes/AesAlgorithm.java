package net.tislib.binanalyst.lib.algorithms.aes;

import net.tislib.binanalyst.lib.bit.Bit;

public interface AesAlgorithm {
    Bit[][] encrypt(byte[] data);
}
