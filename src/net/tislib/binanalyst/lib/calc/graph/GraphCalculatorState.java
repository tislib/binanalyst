package net.tislib.binanalyst.lib.calc.graph;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@XmlRootElement
@XmlType(propOrder = {"input"})
@XmlAccessorType(XmlAccessType.FIELD)
public class GraphCalculatorState {

    private LayerInfo input = new LayerInfo();

    public LayerInfo getInput() {
        return input;
    }

    @XmlRootElement(name = "bitx")
    @XmlType(propOrder = {"name"})
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BitInfo {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @XmlRootElement
    @XmlType(propOrder = {"bit"})
    @XmlAccessorType(XmlAccessType.FIELD)
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