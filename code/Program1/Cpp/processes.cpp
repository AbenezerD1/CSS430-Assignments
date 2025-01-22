#include <cstddef>
#include <iostream> // for cout
#include <stdio.h>     // for NULL, perror
#include <stdlib.h>    // for exit
#include <sys/types.h> // for fork, wait
#include <sys/wait.h>  // for wait
#include <unistd.h>    // for fork, pipe, dup, close

using namespace std;

int main(int argc, char **argv) {
  enum {RD,WR};
  int fds[2][2];
  int pid;

  if (argc != 2) {
    cerr << "Usage: processes command" << endl;
    exit(-1);
  }

  
  if ((pid = fork()) < 0) { // fork a child process
    perror("Error forking a child process");
  } else if (pid == 0) { 
    // I'm a child process

    if(pipe(fds[0]) < 0){
      perror("Error creating a pipe using fds[0]");
    }else if((pid = fork()) < 0){ // fork a grand-child
      perror("Error forking a grand child process from child process");
    }else if(pid == 0){
      //I'm a grand-child process

      if(pipe(fds[1]) < 0){ // create a pipe using fds[1]
        perror("Error creating a pipe using fds[1]");
      }else if((pid = fork()) < 0){ // fork a great-grand-child
        perror("Error forking a great grand child process from a grand child process");
      }else if(pid == 0){
        //I'm a great-grand-child

        close(fds[1][RD]);
        dup2(fds[1][WR], 1); // stdout --> great grand child stdout
        
        execlp("/bin/ps","ps","-A", NULL);
      }else{
        wait(NULL);
      }
      
      close(fds[1][WR]);
      dup2(fds[1][RD],0); // stdin --> great grand child stdout
      
      close(fds[0][RD]);
      dup2(fds[0][WR], 1); // stdout --> grand child stdout
      execlp("/bin/grep", "grep",argv[1],NULL);
    }else{
      wait(NULL);
    }

    close(fds[0][WR]);
    dup2(fds[0][RD], 0); // stdin --> grand child stdout

    execlp("/bin/wc", "wc","-l",NULL);
  } else {
    // I'm a parent
    wait(NULL);
    cout << "commands completed" << endl;
  }
  exit(EXIT_SUCCESS);
}
