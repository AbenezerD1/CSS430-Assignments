#include <unistd.h>     // for fork, pipe
#include <stdio.h>      // for perror   
#include <sys/wait.h>   // for wait

int main( void ) {
   enum {RD, WR}; // pipe fd index RD=0, WR=1
   int n, fd[2];
   pid_t pid;
   char buf[100];

   if( pipe(fd) < 0 ) // 1: pipe created
      perror("pipe error");
   else if ((pid = fork()) < 0) // 2: child forked
      perror("fork error");
   
   else if (pid == 0) {
      close(fd[WR]);// 4: child's fd[1] closed
      n = read(fd[RD], buf, 100);
      write(STDOUT_FILENO, buf, n);
   }
   
   else {
      close(fd[RD]); // 3: parent's fd[0] closed 
      write(fd[WR], "Hello my child\n", 15);
      wait(NULL);
   }
}