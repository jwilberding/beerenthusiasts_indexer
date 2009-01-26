package org.beerenthusiasts.lucene;
import java.net.*;
import java.io.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;

public class ServerThread implements Runnable{

   private IndexSearcher indexSearcher;
   private IndexWriter   indexWriter;
   private Socket        socket;

   public ServerThread(Socket socket, IndexSearcher indexSearcher, 
         IndexWriter indexWriter)
   {
      this.socket = socket;
      this.indexSearcher = indexSearcher;
      this.indexWriter = indexWriter;
   }

   public void run()
   {
      try{
         IndexServerProtocol.run(socket, indexSearcher, indexWriter);
      }catch(Exception e){
         e.printStackTrace();
      }
      
   }

}
