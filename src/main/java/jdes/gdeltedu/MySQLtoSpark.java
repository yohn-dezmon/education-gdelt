package jdes.gdeltedu;

import java.util.Properties;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class MySQLtoSpark {

	public static void main(String[] args) {
		String output;
		if (args.length == 1) {
			output = args[0];
		} else {
			System.out.println("Error with commandline inputs!");
			return;
		}
		
		SparkSession spark = SparkSession.builder().master("local").appName("reading-from-MySQL").
				config("some config", "value").getOrCreate();
		

		/* (SELECT Actor1Name, COUNT(Actor1Name) as Count "
		+ "from freqused WHERE Actor1Name IS NOT NULL GROUP BY "
		+ "Actor1Name ORDER BY Count DESC LIMIT 10) as t
		*/
		Dataset<Row> inputdf = spark.read().format("jdbc").
				option("url", "jdbc:mysql://localhost:3306/gdelt").
				option("driver", "com.mysql.jdbc.Driver").
				option("dbtable", "freqused").
				option("user", "root").option("password", "").load();
		inputdf.createOrReplaceTempView("freqused");
		
//		Dataset<Row> gdeltFreqUsed = inputdf.sqlContext().sql("SELECT * FROM freqused LIMIT 10");
		
		inputdf.show(10);

	}

}
