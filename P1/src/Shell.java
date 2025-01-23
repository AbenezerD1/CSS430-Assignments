import java.io.*;
import java.util.*;

class Shell extends Thread
{
   //command line string to contain the full command
   private String cmdLine;
   private int counter;
   private StringBuffer input;

   // constructor for shell
   public Shell( ) {
      cmdLine = "";
      counter = 1;
      input = new StringBuffer();
   }

   // required run method for this Shell Thread
   public void run( ) {

      // build a simple command that invokes PingPong 
      //cmdLine = "PingPong abc 100";
   
      while(!cmdLine.equalsIgnoreCase("exit")){
         cmdLine = ProcessLine();

         if(cmdLine.isEmpty()){
            continue;
         }

         if(cmdLine.contains("&")){
            
         }else if(cmdLine.contains(";")){

         }

         String[] args = SysLib.stringToArgs(cmdLine);
         counter++;
         
         if(!(SysLib.exec(args) < 0)){
            SysLib.join();
         }else{
            continue;
         }
      }
      // must have an array of arguments to pass to exec()
      //String[] args = SysLib.stringToArgs(cmdLine);
      //SysLib.cout("Testing PingPong\n");

      // run the command
      //int tid = SysLib.exec( args );
      //SysLib.cout("Started Thread tid=" + tid + "\n");

      // wait for completion then exit back to ThreadOS
      SysLib.cout("Done!\n");
      SysLib.exit();
   }

   public String ProcessLine(){
      input = new StringBuffer();
      SysLib.cout("shell["+counter+"]%: ");
      SysLib.cin(input);
      return input.toString();
   }
}