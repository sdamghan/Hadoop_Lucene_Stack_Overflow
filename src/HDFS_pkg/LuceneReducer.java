package HDFS_pkg;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import common.io.TextPair;

public class LuceneReducer extends Reducer<TextPair, Text, Text, Text> 
{
	
	public LuceneReducer() 
	{
		System.out.println("*** [INFO] LUCENE REDUCER IS RUNNING.");
	}

  @Override
  protected void reduce(TextPair key, Iterable<Text> values, Context context)
      throws IOException, InterruptedException 
  {

	Text txtValue;
    String value;
    String[] splittedValue;
    List<String> tagsArray = null;
    String id = null, creationDate = null, tags = null, body = null;
    Iterator<Text> iter = values.iterator();

    while (iter.hasNext())
    {
    	txtValue = iter.next();
    	value = txtValue.toString();
    	splittedValue = value.split(",");
    	
    	if (key.getSecond().toString().equalsIgnoreCase("samplePosts"))
    	{
    		id = key.getFirst().toString();
    		
    	    creationDate = splittedValue[0];
    	    tags  = splittedValue[1];
    	    tagsArray  = Arrays.asList(splittedValue[1].split(" "));
    	    body = splittedValue[2];
    	}
    }
    
    if (creationDate != null && tagsArray != null && body != null) 
    {
    	
    	for ( String tag : tagsArray ) 
    	{	
    		if ( tag.equalsIgnoreCase("java") ) 
    			context.write(new Text(id), new Text(creationDate + "," + tags + "," + body));
    	}
    }
  }
  
}