package HDFS_pkg;
import common.io.*;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.*;



public class JobBuilder extends Configured implements Tool {
  
  public static class KeyPartitioner extends Partitioner<TextPair, Text> {
    @Override
    public int getPartition(/*[*/TextPair key/*]*/, Text value, int numPartitions) {
      return (/*[*/key.getFirst().hashCode()/*]*/ & Integer.MAX_VALUE) % numPartitions;
    }
  }
  
  public int run(String[] args) throws Exception {
    
	Path inputPath = new Path(args[0]);
	Path outputPath = new Path(args[1]);
				
			/*
	 * firstJob does thing related to joining the names and roles tables
	 * then based on its output we will use another join for the output
	 * and the movies table
	 */
	Configuration configuration = new Configuration();
	configuration.set("mapreduce.output.textoutputformat.separator", ",");
	Job firstJob = Job.getInstance(configuration);
	firstJob.setJobName("Names and Roles Reduce Side Join");
	firstJob.setJarByClass(JobBuilder.class);
	
	//MAP
	firstJob.setMapOutputKeyClass(TextPair.class);	  
	firstJob.setMapOutputValueClass(Text.class);
	
	//JOB
	firstJob.setPartitionerClass(KeyPartitioner.class);
	firstJob.setGroupingComparatorClass(TextPair.FirstComparator.class);
	
	firstJob.setOutputKeyClass(Text.class);
	firstJob.setOutputValueClass(Text.class);
	firstJob.setInputFormatClass(TextInputFormat.class);
	firstJob.setOutputFormatClass(TextOutputFormat.class);
	
	firstJob.setReducerClass(LuceneReducer.class);
	
	//IN/OUT
    MultipleInputs.addInputPath(firstJob,
    							inputPath,
								TextInputFormat.class,
								LuceneMapper.class);
	
	FileOutputFormat.setOutputPath(firstJob, outputPath);
	
	
	return firstJob.waitForCompletion(true) ? 0 : 1;
  }
  
  
  public static void execute(String[] args) throws Exception {
	  
	  int exitCode = ToolRunner.run(new Configuration(), new JobBuilder(), args);
	  
	  if (0 == exitCode) 
	  {
	        System.out.println("Reduce Side Join Mapreduce example using Java Job Completed Successfully...");
	  } 
	  else 
	  {
	        System.out.println("Reduce Side Join Mapreduce example using Java Job Failed...");
	  }

  }
  
  /*
  public static void main(String[] args) throws Exception {
	
	  JobBuilder jb = new JobBuilder();
	  String[] arguments = {"/home/casauser/Lucene_Hadoop_Stack_Overflow/LuceneOutput/samplePosts.csv", "/home/casauser/Lucene_Hadoop_Stack_Overflow/HDFS_Output"};
	  jb.execute(arguments);
  }
  */

}