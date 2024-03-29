package net.tislib.binanalyst.lib.algorithms.sha.sha256;

import static net.tislib.binanalyst.lib.BinOps.copy;

import net.tislib.binanalyst.lib.WordOpsHelper;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.BitOpsCalculator;

public class Sha256AlgorithmImpl implements Sha256Algorithm {

    private static final int[] K = {0x428a2f98, 0x71374491, 0xb5c0fbcf,
            0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74,
            0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 0xe49b69c1, 0xefbe4786,
            0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc,
            0x76f988da, 0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7,
            0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967, 0x27b70a85,
            0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb,
            0x81c2c92e, 0x92722c85, 0xa2bfe8a1, 0xa81a664b, 0xc24b8b70,
            0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3,
            0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, 0x748f82ee, 0x78a5636f,
            0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7,
            0xc67178f2};

    private static final int[] STATE_0 = {0x6a09e667, 0xbb67ae85, 0x3c6ef372,
            0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19};
    private final WordOpsHelper wordOpsHelper;
    private int rounds = 64;

    public Sha256AlgorithmImpl(BitOpsCalculator calculator) {
        this.wordOpsHelper = new WordOpsHelper(calculator);
    }

    @Override
    public Bit[][] hash(byte[] bytes) {
        return hash(wordOpsHelper.toBitWordArray(wordOpsHelper.pad(bytes)));
    }

    private final Bit[][] state = new Bit[8][32];

    @Override
    public Bit[][] hash(Bit[][] words) {
        init();

        for (int i = 0, n = words.length / 16; i < n; ++i) {
            final Bit[][] W = new Bit[16][32];
            // initialize W from the block's words
            System.arraycopy(words, i * 16, W, 0, 16);
            update(W);
        }

        return state;
    }

    public void init() {
        // initialize all words
        for (int i = 0; i < STATE_0.length; i++) {
            state[i] = wordOpsHelper.wordToBits(STATE_0[i]);
        }
    }

    private void update(Bit[][] words) {
        final Bit[][] w = new Bit[64][8];

        System.arraycopy(words, 0, w, 0, 16);

        for (int t = 16; t < w.length; ++t) {
            w[t] = wordOpsHelper.add(smallSig1(w[t - 2]), w[t - 7], smallSig0(w[t - 15]), w[t - 16]);
        }

        final Bit[][] TEMP = copy(state);

        // operate on TEMP
        for (int t = 0; t < rounds; ++t) {
            Bit[] t1 = wordOpsHelper.add(TEMP[7], bigSig1(TEMP[4]), ch(TEMP[4], TEMP[5], TEMP[6]), wordOpsHelper.wordToBits(K[t]), w[t]);

            Bit[] t2 = wordOpsHelper.add(bigSig0(TEMP[0]), maj(TEMP[0], TEMP[1], TEMP[2]));
            System.arraycopy(TEMP, 0, TEMP, 1, TEMP.length - 1);
            TEMP[4] = wordOpsHelper.add(TEMP[4], t1);
            TEMP[0] = wordOpsHelper.add(t1, t2);
        }

        // add values in TEMP to values in H
        for (int t = 0; t < state.length; ++t) {
            state[t] = wordOpsHelper.add(state[t], TEMP[t]);
        }
    }

    private Bit[] ch(Bit[] x, Bit[] y, Bit[] z) {
        return wordOpsHelper.or(wordOpsHelper.and(x, y), wordOpsHelper.and((wordOpsHelper.not(x)), z));
    }

    private Bit[] maj(Bit[] x, Bit[] y, Bit[] z) {
        return wordOpsHelper.or(wordOpsHelper.and(x, y), wordOpsHelper.and(x, z), wordOpsHelper.and(y, z));
    }

    private Bit[] bigSig0(Bit[] x) {
        return wordOpsHelper.xor(wordOpsHelper.rotateRight(x, 2), wordOpsHelper.rotateRight(x, 13), wordOpsHelper.rotateRight(x, 22));
    }

    private Bit[] bigSig1(Bit[] x) {
        return wordOpsHelper.xor(wordOpsHelper.rotateRight(x, 6), wordOpsHelper.rotateRight(x, 11), wordOpsHelper.rotateRight(x, 25));
    }

    private Bit[] smallSig0(Bit[] x) {
        return wordOpsHelper.xor(
                wordOpsHelper.rotateRight(x, 7),
                wordOpsHelper.rotateRight(x, 18),
                wordOpsHelper.shru(x, 3)
        );
    }

    private Bit[] smallSig1(Bit[] x) {
        return wordOpsHelper.xor(
                wordOpsHelper.rotateRight(x, 17),
                wordOpsHelper.rotateRight(x, 19),
                wordOpsHelper.shru(x, 10)
        );
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }
}
