package radicalbit.io.java;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.cassandra.CassandraSink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 *
 * 	Example assumes that there are in our environment: a Cassandra instance with KEYSPACE test, and a TABLE created as follow:
 *
 * 		CREATE TABLE test.cassandraconnectorexample(id varchar, text varchar, PRIMARY KEY(id));
 */
public class CassandraConnectorExample {

	private final static Collection<String> collection = new ArrayList<>(50);
	static {
		for (int i = 1; i <=  50; ++i) {
			collection.add("element " + i);
		}
	}

	public static void main(String[] args) throws Exception {

		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		DataStream<Tuple2<String,String>> dataStream =
				env
				.fromCollection(collection)
				.map(new MapFunction<String, Tuple2<String, String>>() {

					final String mapped = " mapped ";
					String[] splitted;

					@Override
					public Tuple2<String,String> map(String s) throws Exception {
						splitted = s.split("\\s+");
						return Tuple2.of(
								UUID.randomUUID().toString(),
								splitted[0] + mapped + splitted[1]
						);
					}
				});

		CassandraSink
				.addSink(dataStream)
				.setQuery("INSERT INTO test.cassandraconnectorexample(id, text) VALUES (?,?);")
				.setHost("127.0.0.1")
				.build();

		env.execute();
	}
}
