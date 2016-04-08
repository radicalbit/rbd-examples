package radicalbit.io;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.List;

public class QuickstartFirstJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        List<String> words =
                Arrays.asList("welcome", "to", "the", "fast-data", "revolution.");

        DataStream<String> source = env.fromCollection(words);

        DataStream<String> processed = source.map(new MapFunction<String, String>() {

            @Override
            public String map(String s) throws Exception {
                return s.toUpperCase();

            }
        });

        processed.print();

        env.execute("My First Job");
    }
}