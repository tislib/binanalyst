package net.tislib.binanalyst.lib.calc.graph.decorator.optimizer.pattern;

import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.bit.OperationalBit;
import net.tislib.binanalyst.lib.bit.VarBit;
import net.tislib.binanalyst.lib.calc.graph.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public final class Pattern {
    PatternStructure structure;
    PatternStructure transform;

    public static Pattern compile(String source) {
        source = clean(source);
        String[] parts = source.split("=>");
        String leftPart = parts[0];
        String rightPart = parts[1];
        Pattern pattern = new Pattern();
        pattern.structure = parseStructure(leftPart);
        pattern.transform = parseStructure(rightPart);

        return pattern;
    }

    private static String clean(String source) {
        return source.replaceAll("!", "~");
    }

    private static PatternStructure parseStructure(String content) {
        char[] chars = content.toCharArray();

        Operation operation = null;
        int stackDeepness = 0;
        String captured = "";
        char capturedFlag = 0;
        List<PatternStructure> items = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (c == '(') {
                stackDeepness++;
            }

            if (c == ')') {
                stackDeepness--;
                captured += c;
                if (i < chars.length - 1) {
                    char flag = chars[i + 1];
                    if (isFlag(flag)) {
                        i++;
                        capturedFlag = flag;
                    }
                }
            }

            if (stackDeepness != 0) {
                captured += c;
            } else {
                if (!captured.equals("")) {

                    if (captured.startsWith("(") && captured.endsWith(")")) {
                        captured = captured.substring(1, captured.length() - 1);
                    }

                    PatternStructure structure = parseStructure(captured);
                    structure.flag = capturedFlag;
                    items.add(structure);
                    captured = "";
                    continue;
                }
                boolean isOperation = isOperationSign(c);
                if (isOperation) {
                    operation = Operation.fromSign(String.valueOf(c));
                } else {
                    PatternStructure var = new PatternStructure();
                    var.name = String.valueOf(c);
                    items.add(var);
                }
            }
        }

        if (items.size() == 1 && operation == null) {
            return items.get(0);
        }

        PatternStructure structure = new PatternStructure();
        structure.operation = operation;
        structure.items = items;
        return structure;
    }

    private static boolean isFlag(char flag) {
        List<CharSequence> flags = Arrays.asList(
                "e" // exact match
        );

        return flags.contains(String.valueOf(flag));
    }

    private static boolean isOperationSign(char c) {
        List<CharSequence> signs = Arrays.asList(
                Operation.OR.getSign(),
                Operation.AND.getSign(),
                Operation.XOR.getSign(),
                Operation.NOT.getSign(),
                Operation.COPY.getSign()
        );

        return signs.contains(String.valueOf(c));
    }

    public static class PatternStructure {
        public char flag;
        Operation operation;
        List<PatternStructure> items;
        String name;
    }
}
