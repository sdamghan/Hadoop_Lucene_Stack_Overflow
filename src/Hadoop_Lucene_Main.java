import HDFS_pkg.JobBuilder;
import Lucene_StackOverflow_pkg.Indexer;
import Lucene_StackOverflow_pkg.Tester;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Hadoop_Lucene_Main {
	public static void main(String[] args) throws Exception{
		
		// TODO Auto-generated method stub
		// Log Hadoop and Indexing
		long startTime = System.currentTimeMillis();
		JobBuilder jb = new JobBuilder();
		
		if (args.length != 2)
		{
			System.out.println("*** [ERROR] Number of provided arguments must be two. Input CSV file and the output path of Hadoop.");
		}
		
		String[] arguments = {args[0], args[1]};
		jb.execute(arguments);
		
		long startTime2 = System.currentTimeMillis();
		Indexer idx = new Indexer();
		File HDFS_Output = new File (args[1]);
		
		for ( final File fileEntry : HDFS_Output.listFiles() )
		{
			if ( fileEntry.getName().startsWith("part-r-") )
		    	idx.createIndex(fileEntry.toString());
		}
		long endTime = System.currentTimeMillis();
		
		System.out.println("*** [INFO] INDEXING TIME WITH HADOOP \n\tTime : " + (endTime - startTime) + " ms" + "\n\n");
		System.out.println("*** [INFO] INDEXING TIME WITHOUT HADOOP\n\tTime : " + (endTime - startTime2) + " ms" + "\n\n");
		
		// Log Searching
		startTime = System.currentTimeMillis();
		Tester tester = new Tester( new BufferedWriter(new FileWriter( new File("./Outputs/RESULT.out") )) );
		tester.searchQuestionBodyByTerm("string");
		endTime = System.currentTimeMillis();
		
		System.out.println("*** [INFO] INDEXING TIME \n\tTime : " + (endTime - startTime) + " ms" + "\n\n");
	}

}
