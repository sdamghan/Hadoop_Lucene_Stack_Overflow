package HDFS_pkg;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import common.io.TextPair;

public class LuceneMapper extends Mapper<LongWritable, Text, TextPair, Text> {
	
  public LuceneMapper()
  {
		System.out.println("*** [INFO] LUCENE MAPPER IS RUNNING.");

  }

  @Override
  public void map(LongWritable key, Text value, Context context)  throws IOException, InterruptedException 
  {  	  
	  //tconst
	TextPair fKey 	  = new TextPair(value.toString().split(",")[0].trim(), "samplePosts");
	// creationDate, Tags, Body
	Text 	 contextValue = new Text(value.toString().split(",")[1].trim() + "," + value.toString().split(",")[2].trim() + "," + value.toString().split(",")[3].trim());
    
    context.write(fKey, contextValue);
  }

}
