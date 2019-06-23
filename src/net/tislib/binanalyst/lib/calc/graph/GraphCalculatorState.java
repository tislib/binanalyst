package net.tislib.binanalyst.lib.calc.graph;

import java.util.ArrayList;
import java.util.List;

public class GraphCalculatorState {

    private LayerInfo input = new LayerInfo();

    public LayerInfo getInput() {
        return input;
    }

    public static class BitInfo {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class LayerInfo {
        private List<BitInfo> bit = new ArrayList<>();

        public List<BitInfo> getBit() {
            return bit;
        }

        public void setBit(List<BitInfo> bit) {
            this.bit = bit;
        }
    }
}