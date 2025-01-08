///////////////////////////////////////////////////////////
////        File: msg_snd.cpp
//// Description: Demonstrates how to use message queues 
////      Author: Stephen Dame
////        Date: 09-APR-2014
///////////////////////////////////////////////////////////
#include <stdlib.h>  // for exit
#include <stdio.h>   // for perror
#include <sys/ipc.h> // for IPC_CREAT
#include <sys/msg.h> // for msgget, msgrcv
#include <iostream>  // for cin, cout, cerr
#include <string.h>  // for strcpy, strlen
using namespace std;
#define MSGSZ 128

typedef struct {
   long    mtype;
   char    mtext[MSGSZ];
} message_buf;

int main() {
   int msqid;
   size_t msgsz;
   int msgflg = IPC_CREAT | 0666;
   message_buf mymsg;

   key_t key = 74563;
   if((msqid = msgget(key, msgflg)) < 0) {
      perror("msgget");
      exit(1);
   }

   cout << "Sending to Msg Queue:" << key << endl;

   strcpy( mymsg.mtext, "Hello Huskies!");
   msgsz = strlen(mymsg.mtext) + 1;
   mymsg.mtype = 1;
   if(msgsnd(msqid, &mymsg, msgsz, IPC_NOWAIT) < 0) {
      perror("msgsnd");
   }
}
