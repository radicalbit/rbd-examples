package radicalbit.io.java;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class QuickstartFirstJob {

    public static void main(String[] args) throws Exception {

        //Here we create the Streaming Execution environment...
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        //...and we set its parallelism to 1 to see the results printed in order
        env.setParallelism(1);

        //We first create a DataStream from a bunch of strings...
        DataStream<String> source = env.fromElements("welcome", "to", "the", "fast-data", "revolution.");

        //...and we turn them to uppercase.
        DataStream<String> processed = source.map(new MapFunction<String, String>() {

            @Override
            public String map(String s) throws Exception {
                return s.toUpperCase();

            }
        });

        //At last we print the result to console. This also implicitly calls the .execute() method.
        processed.print();

        //We call it explicitely anyway to give the Job an unique and recognizable name.
        env.execute("My First Job");
    }
}