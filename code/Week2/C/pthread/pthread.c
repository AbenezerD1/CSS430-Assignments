/*
 * From Operating Systems Concepts - 10th Edition
 * Section 4.4, Figure 4.4.1
 * Refactored by S. Dame to add error checking
 * Winter 2025
 */
#include <pthread.h>
#include <stdio.h>
 
#include <stdlib.h>
 
int sum; /* this data is shared by the thread(s) */
void *runner(void *param); /* threads call this function */
 
int main(int argc, char *argv[])
{
  pthread_t tid;  /* the thread identifier */
  pthread_attr_t attr; /* set of thread attributes */

  if(argc != 2) {
    printf("usage: pthread <integer>\n");
    exit(EXIT_FAILURE);
  }
 
  /* set the default attributes of the thread */
  pthread_attr_init(&attr);
  /* create the thread */
  pthread_create(&tid, &attr, runner, argv[1]);
  /* wait for the thread to exit */
  pthread_join(tid,NULL);
 
  printf("sum = %d\n",sum);
}
 
/* The thread will execute in this function */
void *runner(void *param)
{
  int i, upper = atoi(param);
  sum = 0;
 
  for (i = 1; i <= upper; i++)
    sum += i;
 
  pthread_exit(0);
}