package org.beerenthusiasts.lucene;
import java.net.*;
import java.io.*;
import java.util.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;

public class IndexServerProtocol{
   private static final String KEY = "C89E158A0D01E37BZ89837119DD0D843";

   private static final int ACTION_INDEX    = 0;
   private static final int ACTION_QUERY    = 1;
   private static final int ACTION_SHUTDOWN = 2;

   public static void run(Socket socket, IndexSearcher indexSearcher, 
         IndexWriter indexWriter) throws Exception
   {
      PrintWriter out;
      BufferedReader in;
      StringBuffer doc;
      String line;
      String res;
      int action = -1;
      
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(
            new InputStreamReader(
               socket.getInputStream()));
      doc = new StringBuffer();

      out.println("BeerEnthusiasts.org Recipe Indexer");

      if(!in.readLine().equals(KEY))
      {
         socket.close();
         return;
      }

      line = in.readLine();
      if(line == null){
         System.err.println("error: input stream closed");
         socket.close();
         return;
      }else
         line = line.trim().toLowerCase();

      if(line.equals("index")){
         action = ACTION_INDEX;   
      }else if(line.equals("query")){
         action = ACTION_QUERY;
      }else if(line.equals("shutdown")){
         action = ACTION_SHUTDOWN;
      }else{
         out.println("error: unknown command");
         socket.close();
         return;
      }
      
      while((line = in.readLine()) != null){
         doc.append(line);
         doc.append('\n');
         System.err.println("{= " + line + "=}");
      }

      if(action == ACTION_INDEX)
         res = indexDoc(doc.toString());
      else if(action == ACTION_INDEX)
         res = issueQuery(doc.toString());
      else
         res = shutdown();

      out.println(res);
      System.out.println(res);
      out.flush();
      in.close();
      out.close();
      socket.close();

   }

   private static String indexDoc(String document)
   {
      return "indexDoc Recieved " + document;

   }

   private static String issueQuery(String query)
   {
      return "issueQuery Recieved " + query;
   }

   private static String shutdown()
   {
      return "Recieved Shutdown";
   }

}

