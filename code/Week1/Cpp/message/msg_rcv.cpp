///////////////////////////////////////////////////////////
////        File: msg_rcv.cpp
//// Description: Demonstrates how to use message queues 
////      Author: Stephen Dame
////        Date: 09-APR-2014
///////////////////////////////////////////////////////////
#include <stdlib.h>  // for exit
#include <stdio.h>   // for perror
#include <sys/ipc.h> // for IPC_CREAT
#include <sys/msg.h> // for msgget, msgrcv
#include <iostream>  // for cin, cout, cerr
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

   if(msgrcv(msqid, &mymsg, MSGSZ, 1, 0) < 0) {
      perror("msgrcv");
      exit(1);
   }
   cout << mymsg.mtext << endl;
}
