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

  // fork a child
  if ((pid = fork()) < 0) {
    perror("fork error");
  } else if (pid == 0) {
    // I'm a child

    // create a pipe using fds[0]
    if(pipe(fds[0]) < 0){
      perror("pipe error");
    }else if((pid = fork()) < 0){ // fork a grand-child
      perror("fork 2 error");
    }else if(pid == 0){
      // if I'm a grand-child

      if(pipe(fds[1]) < 0){ // create a pipe using fds[1]
        perror("pipe 2 error");
      }else if((pid = fork()) < 0){ // fork a great-grand-child
        perror("fork 3 error");
      }else if(pid == 0){
        // if I'm a great-grand-child
        // execute "ps"

        close(fds[1][RD]);
        dup2(fds[1][WR], 1); // stdout --> fds[1][1]
        
        execlp("/bin/ps","ps","-A", NULL);
      }else{
        wait(NULL);
      }
      // else if I'm a grand-child
      // execute "grep"
      close(fds[1][WR]);
      dup2(fds[1][RD],0); // stdin --> great grand child stdout
      
      execlp("/bin/grep", "grep",argv[1],NULL);
    }else{
      
      wait(NULL);
    }
    // else if I'm a child
    // execute "wc"

    // execlp("/bin/wc", "wc","-l",NULL);
  } else {
    // I'm a parent
    wait(NULL);
    cout << "commands completed" << endl;
  }
  exit(EXIT_SUCCESS);
}
