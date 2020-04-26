package Lucene_StackOverflow_pkg;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

//import static Lucene_StackOverflow.LuceneConstants.Q_A_INDEX_DIRECTORY;
//import static Lucene_StackOverflow.LuceneConstants.SAMPLE_INDEX_DIRECTORY;

/**
 * Created by "P.Khodaparast" on 10/18/2016.
 */
public class Indexer {
    public Indexer() throws IOException 
    {
		System.out.println("*** [INFO] INDEXER IS RUNNING.");
    }
    public Directory directory = new SimpleFSDirectory(new File ("/home/casauser/Lucene_Hadoop_Stack_Overflow/Outputs/LuceneOutput"));
    public Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
    public IndexWriter iWriter = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);

// 1. IndexBody -----------------------------------------------------------------------------------------------------------

    public void createIndex(String fileNameToBeIndex) throws IOException {

//        System.out.println("Indexing.......");
        BufferedReader br = new BufferedReader(new FileReader(fileNameToBeIndex));
        String sCurrentLine =  br.readLine();
        
        //Looping through a set and adding to index file
        int count = 0;
       do {
        	
//    	   System.out.println(count);
           Document doc = new Document();
           
        	/*
             * [0] ID
             * [1] Creation Date
             * [2] Tags
             * [3] Body
             * */
            String[] splittedLine = sCurrentLine.split(",");
            
            String id = splittedLine[0];
            String creationdate = splittedLine[1];
//            System.out.println("creation Date : " + creationdate);
            String tags = splittedLine[2];
            String body = splittedLine[3];
            
            doc.add(new Field("Id", id, Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("CreationDate", creationdate, Field.Store.YES, Field.Index.NOT_ANALYZED));

            doc.add(new Field("Tags", tags, Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("Body", body, Field.Store.YES, Field.Index.ANALYZED));
            
            // Adding doc to iWriter
            iWriter.addDocument(doc);
            count++;
            
        }  while ((sCurrentLine = br.readLine()) != null);
       
       
        System.out.println(count + " record indexed");
        //Closing iWriter
        iWriter.commit();
        iWriter.close();
        System.out.println("iwriter closed... done....");
    }

    // 2. Index Answers -----------------------------------------------------------------------------------------------------------
//Id,CreationDate,CreationDatesk,Score,OwnerUserId,LastEditorUserId,
// LastActivityDate,LastEditDate,CommentCount,CommunityOwnedDate,ClosedDate

    public void createIndexAnswer(String fileNameToBeIndex) throws IOException {
        System.out.println("Indexing.......");
        BufferedReader br = new BufferedReader(new FileReader(fileNameToBeIndex));
        String sCurrentLine = br.readLine();

        //Looping through a set and adding to index file
        int count = 0;
        while ((sCurrentLine = br.readLine()) != null) {
            System.out.println(count);
            Document doc = new Document();
            String[] newLine = sCurrentLine.split(",");

            String id = newLine[0];
            String Score = newLine[3];

            doc.add(new Field("Id", id, Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("Score", Score, Field.Store.YES, Field.Index.ANALYZED));

            // Adding doc to iWriter
            iWriter.addDocument(doc);
            count++;
        }
        System.out.println(count + " record indexed");
        //Closing iWriter
        iWriter.commit();
        iWriter.close();
        System.out.println("iwriter closed... done....");
    }

    // 3. Index Q_A -----------------------------------------------------------------------------------------------------------
    public void createQAIndex(String fileNameToBeIndex) throws IOException {
        System.out.println("Indexing... :" + fileNameToBeIndex);
        //fileNameTOBeIndex is something like this "body.txt" the file name and path that going to be indexed
        BufferedReader br = new BufferedReader(new FileReader(fileNameToBeIndex));
        String sCurrentLine = br.readLine();

        //Looping through a set and adding to index file
        int count = 0;
        while ((sCurrentLine = br.readLine()) != null) {
            Document doc = new Document();
            String[] newLine = sCurrentLine.split(",");

            String QId = newLine[0];
            String AId = newLine[1];
            String accepted = newLine[2];

            doc.add(new Field("QId", QId, Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("AId", AId, Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("accepted", accepted, Field.Store.YES, Field.Index.ANALYZED));

            // Adding doc to iWriter
            iWriter.addDocument(doc);
            count++;
            System.out.println(count);
            System.out.println("AId : " + AId + "     QId : " + QId + "   Accepted : " + accepted);


        }
        System.out.println(count + " record indexed");
        //Closing iWriter
        iWriter.commit();
        iWriter.close();
        System.out.println("iwriter closed... done....");

    }
   
    /*
    public static void main (String[] args) throws Exception {
    	
    	Indexer indexer = new Indexer();
    	indexer.createIndex("/home/casauser/Lucene_Hadoop_Stack_Overflow/src/Lucene_StackOverflow/samplePosts.txt");
    }
    */
}

