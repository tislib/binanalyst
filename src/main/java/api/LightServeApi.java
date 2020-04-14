package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD;
import net.tislib.binanalyst.lib.calc.graph.BitOpsGraphCalculator;
import net.tislib.binanalyst.lib.calc.graph.tools.GraphCalculatorTools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class LightServeApi {

    public static void serveGraphCalculator(Supplier<Map<String, BitOpsGraphCalculator>> calculatorMapSupplier) throws IOException {
        NanoHTTPD nanoHTTPD = new NanoHTTPD(15555) {
            @Override
            public Response serve(IHTTPSession session) {
                try {
                    Map<String, BitOpsGraphCalculator> calculatorMap = calculatorMapSupplier.get();
                    Map<String, GraphCalculatorTools.GraphCalculatorSerializedData> data = new HashMap<>();

                    calculatorMap.forEach((name, calculator) -> {
                        try {
                            GraphCalculatorTools.GraphCalculatorSerializedData serializedData = GraphCalculatorTools.serializeCalculator(calculator, false);
                            data.put(name, serializedData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    ObjectMapper objectMapper = new ObjectMapper();
                    String dataStr = null;
                    try {
                        dataStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    if (session.getMethod() == Method.GET && session.getUri().equals("/api/data")) {
                        return newFixedLengthResponse(dataStr);
                    } else {
                        return super.serve(session);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        nanoHTTPD.start();

        try {
            Thread.sleep(1000 * 3600 * 24 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
