package net.tislib.binanalyst.test.reverser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.tislib.binanalyst.lib.bit.Bit;
import net.tislib.binanalyst.lib.calc.Expression;
import net.tislib.binanalyst.lib.operator.BinAdd;
import net.tislib.binanalyst.lib.operator.BinMul;

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

    public static List<Object[]> getMultiLineExpressions() {
        List<Object[]> data = new ArrayList<>();

        data.add(new Object[]{
                (Expression) (input, calculator) -> new Bit[]{
                        calculator.or(
                                calculator.and(input[input.length - 1], input[input.length - 2]),
                                calculator.xor(input[input.length - 1], input[input.length - 2])
                        ),
                }
        });

        data.add(new Object[]{
                (Expression) (input, calculator) -> new Bit[]{
                        calculator.or(
                                calculator.and(input[input.length - 1], input[input.length - 2]),
                                calculator.xor(input[input.length - 1], input[input.length - 3])
                        ),
                        calculator.or(
                                calculator.or(input[input.length - 1], input[input.length - 2]),
                                calculator.xor(input[input.length - 1], input[input.length - 3])
                        ),
                        calculator.or(
                                calculator.and(input[input.length - 1], input[input.length - 2]),
                                calculator.or(input[input.length - 1], input[input.length - 3])
                        )
                }
        });

        data.add(new Object[]{
                (Expression) (input, calculator) -> new Bit[]{
                        calculator.or(
                                calculator.and(input[input.length - 1], input[input.length - 2]),
                                calculator.xor(input[input.length - 1], input[input.length - 2])
                        ),
                        calculator.or(
                                calculator.or(input[input.length - 1], input[input.length - 2]),
                                calculator.xor(input[input.length - 1], input[input.length - 2])
                        ),
                        calculator.or(
                                calculator.and(input[input.length - 1], input[input.length - 2]),
                                calculator.or(input[input.length - 1], input[input.length - 2])
                        )
                }
        });

        data.add(new Object[]{
                (Expression) (input, calculator) -> new Bit[]{
                        calculator.and(
                                calculator.or(
                                        calculator.or(input[input.length - 1], input[input.length - 2]),
                                        calculator.xor(input[input.length - 1], input[input.length - 2])
                                ),
                                calculator.or(
                                        calculator.or(input[input.length - 1], input[input.length - 2]),
                                        calculator.and(input[input.length - 1], input[input.length - 2])
                                )
                        )
                }
        });

//        data.add(new Object[]{
//                (Expression) (input, calculator) ->
//                        BinAdd.add(calculator, new Bit[]{
//                               input[0],
//                               input[1],
//                        }, new Bit[]{
//                                input[2],
//                                input[3],
//                        })
//        });

//        data.add(new Object[]{
//                (Expression) (input, calculator) ->
//                        BinMul.multiply(calculator, new Bit[]{
//                               input[input.length - 2],
//                               input[input.length - 1],
//                        }, new Bit[]{
//                                input[input.length - 4],
//                                input[input.length - 3],
//                        })
//        });

        return data;
    }
}
