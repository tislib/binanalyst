package net.tislib.binanalyst.test.reverser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.Expression;

public class TestData {

    public static Collection<Object[]> getSingleLineExpressions() {
        List<Object[]> data = new ArrayList<>();

        data.add(new Object[]{
                (Expression) (input, calculator) -> new Bit[]{
                        calculator.and(input[input.length - 1], input[input.length - 2]),
                        calculator.xor(input[input.length - 1], input[input.length - 3]),
                }
        });

        data.add(new Object[]{
                (Expression) (input, calculator) -> new Bit[]{
                        calculator.and(input[input.length - 1], input[input.length - 2]),
                        calculator.or(input[input.length - 1], input[input.length - 3]),
                }
        });

        data.add(new Object[]{
                (Expression) (input, calculator) -> new Bit[]{
                        calculator.or(input[input.length - 1], input[input.length - 2]),
                        calculator.xor(input[input.length - 1], input[input.length - 3]),
                }
        });

        data.add(new Object[]{
                (Expression) (input, calculator) -> new Bit[]{
                        calculator.xor(input[input.length - 1], input[input.length - 2]),
                        calculator.xor(input[input.length - 1], input[input.length - 3]),
                }
        });

        data.add(new Object[]{
                (Expression) (input, calculator) -> new Bit[]{
                        calculator.and(input[input.length - 1], input[input.length - 2]),
                        calculator.and(input[input.length - 1], input[input.length - 3]),
                }
        });

        data.add(new Object[]{
                (Expression) (input, calculator) -> new Bit[]{
                        calculator.and(input[input.length - 1], input[input.length - 2]),
                        calculator.or(input[input.length - 1], input[input.length - 3]),
                        calculator.xor(input[input.length - 1], input[input.length - 3]),
                }
        });

        return data;
    }
}
