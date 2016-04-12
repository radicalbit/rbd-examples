package radicalbit.io.java;

import io.radicalbit.flink.pmml.java.api.JPMMLEvaluationOperatorBuilder;
import io.radicalbit.flink.pmml.java.strategies.*;
import org.apache.commons.io.IOUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.FileInputStream;
import java.util.*;

public class FlinkJPMMLQuickstart {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        List<Map<String,Object>> dataWithHeader= new LinkedList<>();

        Random rand=new Random();
        for(int i=0;i<100;i++){
            Map<String,Object> row= new HashMap<>();
            row.put("petal_width", rand.nextDouble()*8);
            row.put("sepal_width", rand.nextDouble()*8);
            row.put("petal_length", rand.nextDouble()*8);
            row.put("sepal_length", rand.nextDouble()*8);

            dataWithHeader.add(row);
        }

        DataStreamSource<Map<String,Object>> input = env.fromCollection(dataWithHeader);

        String pmmlSource= IOUtils.toString(
                    new FileInputStream(
                            (new FlinkJPMMLQuickstart().getClass().getClassLoader()
                                    .getResource("single_iris_kmeans.xml"))
                                    .getFile()));

        MapFunction<Map<String,Object>,Map<String,Object>> operator=
                JPMMLEvaluationOperatorBuilder.create(pmmlSource)
                        .setExceptionHandlingStrategy(Strategies.LogExceptionStrategy())
                        .setResultExtractionStrategy(Strategies.ExtractTargetAndOutputFieldStrategy())
                        .buildMapOperator();

        input.map(operator).print();

        env.execute("Flink-JPMML prediction Job");
    }
}
