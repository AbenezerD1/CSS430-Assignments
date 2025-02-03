import java.util.*;

class Shell extends Thread {
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
      while(!cmdLine.equalsIgnoreCase("exit")){
         cmdLine = GetLine();

         if(cmdLine.isEmpty() || cmdLine.equalsIgnoreCase("exit")){
            continue;
         }

         execute(cmdLine);
      }
      
      SysLib.cout("Done!\n");
      SysLib.exit();
   }

   /**
    * given a string of commands splits them to concurrent and sequential commands and runs them according to order they were in
    * @param line
    */
    public void execute(String line){
      boolean seqCommand = line.contains(";");
      boolean concurrCommand = line.contains("&");

      if(!seqCommand && !concurrCommand){ // single command
         SequentialExec(line);
         counter++;
         return;
      } 
      if(seqCommand && ! concurrCommand){ // concurrent commands only
         SequentialExec(line);
         counter++;
         return;
      }
      if(!seqCommand && concurrCommand ){ // seqential commands only
         ConcurrExec(line);
         counter++;
         return;
      }
      // combination of concurrent and sequential commands

      // always split substring using ;

      // Test Cases
      // x & y ; z
      // x ; y & z
      // w & x ; y & z
      // w & x & y ; z
      // w ; x & y ; z
      // w ; x ; y & z
      
      String[] splitCmd = splitString(line, ";");
      
      for(String str: splitCmd){
         ConcurrExec(str);
      }
      counter++;
   }

   /**
    * print shell intro and request user to input command
    * @return user input as a string
    */
   public String GetLine(){
      input = new StringBuffer();

      SysLib.cout("shell["+counter+"]%: ");
      SysLib.cin(input);

      return input.toString();
   }

   /**
    * split string using delim, doesn't care about blank spaces
    * @param line
    * @param delim
    * @return array of strings seprated using the delim
    */
   public String[] splitString(String line, String delim){
      StringTokenizer tokens = new StringTokenizer(line,delim);
      String[] toRet = new String[tokens.countTokens()];

      for(int i = 0; i < toRet.length; i++){
         toRet[i] = tokens.nextToken();
      }
      return toRet;
   }

   /**
    * run commands concurrently 
    * @param line
    */
   public void ConcurrExec(String line){
      int failedProcessCounter = 0;
      String[] commands = splitString(line, "&");

      for(String cmd: commands){
         String[] args = SysLib.stringToArgs(cmd);
         
         if(SysLib.exec(args) < 0){
            failedProcessCounter++;
         }
      }

      for(int i = 0; i < (commands.length - failedProcessCounter); i++){
         SysLib.join();
      }
   }

   /**
    * runs commands sequentially
    * @param line
    */
   public void SequentialExec(String line){
      String[] commands = splitString(line, ";");

      for(String cmd: commands){
         String[] args = SysLib.stringToArgs(cmd);
         if(!(SysLib.exec(args) < 0)){
            SysLib.join();
         }
      }
   }
}