package org.beerenthusiasts.lucene;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;


public class IndexServer{
   private static ExecutorService pool;
   private static Properties   props;
   private static boolean      listening;
   private static ServerSocket serverSocket;
   private static IndexSearcher  indexSearcher;
   private static IndexWriter  indexWriter;

   public static void main(String[] args) throws Exception{
      int port = 0;
      int timeout = 0;
      int maxthreads = 0;
      String indexDir = null;

      if(args.length != 1) {
         System.err.println("usage: java IndexServer properties");
         System.exit(-1);
      }

      props = new Properties();
      props.load(new FileInputStream(args[0]));

      try{
         port = Integer.parseInt(
               props.getProperty("server.port", "6969"));
         timeout = Integer.parseInt(
               props.getProperty("server.timeout","1000"));
         maxthreads = Integer.parseInt(
               props.getProperty("server.maxthreads","10"));
         indexDir = props.getProperty("server.index");
      }catch(Exception e){
         System.err.println("error: reading properties file");
         throw e;
      }

      if(indexDir == null)
      {
         System.err.println("error: index directory was not specified in the properties file");
         System.exit(-1);
      }

      pool = Executors.newFixedThreadPool(maxthreads);

      try{
         serverSocket = new ServerSocket(port);
         serverSocket.setSoTimeout(timeout);
      }catch(Exception e){
         System.err.println("Error creating server socket");
         throw e;
      }

      listening = true;
      System.err.println("Server Started");

      while(listening){
         try{
            pool.execute(new ServerThread(serverSocket.accept(), indexSearcher, indexWriter));
         }catch(Exception e){

         }
      }
      serverSocket.close();

      
   }

}
