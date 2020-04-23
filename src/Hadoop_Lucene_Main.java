import HDFS_pkg.JobBuilder;
import Lucene_StackOverflow_pkg.Indexer;
import Lucene_StackOverflow_pkg.Tester;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Hadoop_Lucene_Main {
	public static void main(String[] args) throws Exception{
		
		// TODO Auto-generated method stub
		
		JobBuilder jb = new JobBuilder();
		
		String[] arguments = {"/home/casauser/Lucene_Hadoop_Stack_Overflow/dataset/samplePosts.csv", "/home/casauser/Lucene_Hadoop_Stack_Overflow/Outputs/HDFS_Output"};
		jb.execute(arguments);
		
		Indexer idx = new Indexer();
		File HDFS_Output = new File ("/home/casauser/Lucene_Hadoop_Stack_Overflow/Outputs/HDFS_Output/");
		
		for ( final File fileEntry : HDFS_Output.listFiles() )
		{
			if ( fileEntry.getName().startsWith("part-r-") )
		    	idx.createIndex(fileEntry.toString());
		}
		
		Tester tester = new Tester( new BufferedWriter(new FileWriter( new File("/home/casauser/Lucene_Hadoop_Stack_Overflow/Outputs/RESULT.out") )) );
		tester.searchQuestionBodyByTerm("string");
	}

}
