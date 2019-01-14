package net.tislib.binanalyst.lib.neo;

import net.tislib.binanalyst.lib.bit.*;
import net.tislib.binanalyst.lib.calc.graph.GraphBitOpsCalculator;
import net.tislib.binanalyst.lib.calc.graph.expression.AnomalGraphExpression;
import net.tislib.binanalyst.lib.calc.graph.expression.GraphExpression;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;


/**
 * Created by Taleh Ibrahimli on 2/10/18.
 * Email: me@talehibrahimli.com
 */
public class NeoGraphExpressionRenderer implements AutoCloseable {

    private final Driver driver;
    private final Session session;
    private final String name;

    public NeoGraphExpressionRenderer(String name) {
        this.name = name;
        this.driver = GraphDatabase.driver("bolt://twl.tisserv.net:7687", AuthTokens.basic("neo4j", "d709ec6c241244683c23e6c8d7638c40"));
        this.session = driver.session();
    }

    public void render(GraphBitOpsCalculator calculator) {
        for (VarBit varBit : calculator.getInput()) {
            String query = "CREATE (n:" + name + ":Bit:VarBit:Input:Calculator { name: '" + varBit.getName() + "', type: 'middle' });";
//            System.out.println(query);
            session.run(query);
        }
        for (OperationalBit operationalBit : calculator.getMiddle()) {
            String query = "CREATE (n:" + name + ":Bit:VarBit:OperationalBit:Middle:Calculator { name: '" + operationalBit.getName() + "', type: 'input' });";
//            System.out.println(query);
            session.run(query);
            for (NamedBit namedBit : operationalBit.getBits()) {
                query = "MATCH (a:" + name + ":Bit:Calculator),(b:" + name + ":Bit:Calculator)\n" +
                        "WHERE a.name = '" + operationalBit.getName() + "' AND b.name = '" + namedBit.getName() + "'\n" +
                        "CREATE (a)-[r:" + operationalBit.getOperation().getSignName() + "]->(b)\n" +
                        "RETURN r;";
//                System.out.println(query);
                session.run(query);
            }
        }

        Integer i = 0;
        for (NamedBit namedBit : calculator.getOutput()) {
            String query = "CREATE (n:" + name + ":Bit:NamedBit:Output:Calculator { name: 'O[" + i + "]', type: 'output' });";
//            System.out.println(query);
            session.run(query);
            query = "MATCH (a:" + name + ":Bit:Calculator),(b:" + name + ":Bit:Calculator)\n" +
                    "WHERE a.name = 'O[" + i + "]' AND b.name = '" + namedBit.getName() + "'\n" +
                    "CREATE (a)-[r:OUTPUT]->(b)\n" +
                    "RETURN r;";
//            System.out.println(query);
            session.run(query);
            i++;
        }
    }

    @Override
    public void close() {
        session.close();
        driver.close();
    }

    public static void easyRender(GraphExpression graphExpression) {
        try (NeoGraphExpressionRenderer neoGraphExpressionRenderer = new NeoGraphExpressionRenderer("Bit3")) {
            neoGraphExpressionRenderer.render(graphExpression);
        }
    }

    public void render(GraphExpression graphExpression) {
        for (VarBit varBit : graphExpression.getRoot()) {
            String query = "CREATE (n:" + name + ":Bit:VarBit:Root:Expression { name: '" + varBit.getName() + "', type: 'root' });";
//            System.out.println(query);
            session.run(query);
        }
        for (OperationalBit operationalBit : graphExpression.getMiddle()) {
            String query = "CREATE (n:" + name + ":Bit:VarBit:OperationalBit:Middle:Expression { name: '" + operationalBit.getName() + "', type: 'middle' });";
//            System.out.println(query);
            session.run(query);
            for (NamedBit namedBit : operationalBit.getBits()) {
                query = "MATCH (a:" + name + ":Bit:Expression),(b:" + name + ":Bit:Expression)\n" +
                        "WHERE a.name = '" + operationalBit.getName() + "' AND b.name = '" + namedBit.getName() + "'\n" +
                        "CREATE (a)-[r:" + operationalBit.getOperation().getSignName() + "]->(b)\n" +
                        "RETURN r;";
//                System.out.println(query);
                session.run(query);
            }
        }
    }

    public void render(AnomalGraphExpression anomalGraphExpression) {
        for (VarBit varBit : anomalGraphExpression.getResolved()) {
            String query = "CREATE (n:" + name + ":Bit:VarBit:Root:Expression { name: '" + varBit.getName() + "', type: 'root' });";
//            System.out.println(query);
            session.run(query);
        }
        for (VarBit varBit : anomalGraphExpression.getResolved()) {
            if (varBit instanceof OperationalBit) {
                OperationalBit operationalBit = (OperationalBit) varBit;
                String query = "CREATE (n:" + name + ":Bit:VarBit:OperationalBit:Middle:Expression { name: '" + operationalBit.getName() + "', type: 'middle' });";
//                System.out.println(query);
                session.run(query);
                for (NamedBit namedBit : operationalBit.getBits()) {
                    query = "MATCH (a:" + name + ":Bit:Expression),(b:" + name + ":Bit:Expression)\n" +
                            "WHERE a.name = '" + operationalBit.getName() + "' AND b.name = '" + namedBit.getName() + "'\n" +
                            "CREATE (a)-[r:" + operationalBit.getOperation().getSignName() + "]->(b)\n" +
                            "RETURN r;";
//                    System.out.println(query);
                    session.run(query);
                }
            }
            if (varBit instanceof ReverseBit) {
                ReverseBit operationalBit = (ReverseBit) varBit;
                String query = "CREATE (n:" + name + ":Bit:VarBit:OperationalBit:Middle:Expression { name: '" + operationalBit.getName() + "', type: 'middle' });";
//                System.out.println(query);
                session.run(query);
                for (NamedBit namedBit : operationalBit.getBits()) {
                    query = "MATCH (a:" + name + ":Bit:Expression),(b:" + name + ":Bit:Expression)\n" +
                            "WHERE a.name = '" + operationalBit.getName() + "' AND b.name = '" + namedBit.getName() + "'\n" +
                            "CREATE (a)-[r:" + operationalBit.getOperation().toString() + "]->(b)\n" +
                            "RETURN r;";
//                    System.out.println(query);
                    session.run(query);
                }
            }
        }
    }
}
