#include <stdio.h>    // for printf
#include <stdlib.h>   // for exit 
#include <unistd.h>   // for fork, execlp
#include <sys/wait.h> // for wait

int main(int argc, char *argv[])
{
   int pid; // process ID
   // fork another process 
   pid = fork();
   if (pid < 0) { // error occurred
      fprintf(stderr, "Fork Failed");
      exit(EXIT_FAILURE);
   }
   // ---------- CHILD SECTION -----------   
   else if (pid == 0) {
      sleep(2);
      execlp("/bin/ls","ls","-l",NULL);
   }
   // ---------- PARENT SECTION ----------
   else {
      // parent will wait for the child to complete
      wait(NULL);
      sleep(5);
      printf("Child Complete\n");
      exit(EXIT_SUCCESS);
   }
}
