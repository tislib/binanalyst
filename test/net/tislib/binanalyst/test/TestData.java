package net.tislib.binanalyst.test;

import java.util.Arrays;
import java.util.List;

public class TestData {

    public static List<Object[]> testPairData() {
        return Arrays.asList(new Object[][]{
                {23234, 34345}, {455, 65632}, {3, 23443244}, {32932321, 1}, {2, 1}, {5, 6}, {2, 422},
                {23235, 34335}, {415, 65532}, {2, 23473244}, {32832322, 2}, {1, 209}, {5, 6}, {2, 422},
                {23236, 34345}, {425, 65032}, {1, 23483244}, {32732323, 3}, {2, 3}, {5, 6}, {2, 422},
                {23237, 34355}, {435, 65732}, {3, 23493244}, {32632324, 4}, {3, 4}, {5, 6}, {2, 422},
                {23238, 34365}, {445, 65832}, {5, 23403244}, {32532325, 5}, {32532325, 23403244}, {4, 5}, {5, 6}, {2, 422},
                {32732323, 32732322}, {65032, 65033}, {425, 424},
//                {Integer.MAX_VALUE - 100, Integer.MAX_VALUE - 200},
//                {0xFFFFFFFF, 0xFFFFFFFF},
//                {0xFFFFFFFF, 0xFFFFFFFD},
//                {0xFFFFFFFD, 0xFFFFFFFD},
//                {123789129, 0},
        });
    }
}
