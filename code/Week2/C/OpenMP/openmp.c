/**
 * Example of openmp parallel region
 *
 * To compile, enter:
 *
 *	gcc -fopenmp openmp.c
 *
 * You should see the message "I am a parallel region" for each
 * processing core on your system.
 *
 * For those using a virtual machine, make sure you set the number of
 * processing cores > 1 to see parallel execution of the parallel region.
 */

#include <omp.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main(int argc, char *argv[]) {
  const int N = 16;
  int a[N], b[N], c[N];
  /* sequential code section */
  // Seed the random number generator.  Only call srand once.
  srand(time(NULL));
  // Initialize arrays a and b with random 16-bit integers
  for (int i = 0; i < N; i++) {
    a[i] =
        (rand() % (1 << 16)); // Generates random numbers between 0 and 2^16 - 1
    b[i] = (rand() % (1 << 16));
  }
  for (int i = 0; i < N; i++) {
    printf("a[%d] = %d, b[%d] = %d\n", i, a[i], i, b[i]);
  }
  // Initialize arrays a and b with random 16-bit integers
  for (int i = 0; i < N; i++) {
    a[i] = (rand() % (1 << 16));
    b[i] = (rand() % (1 << 16));
  }
#pragma omp parallel
  { printf("I am a parallel region\n"); }
#pragma omp parallel for
  printf("I am a parallel for loop\n");
  for (int i = 0; i < N; i++) {
    c[i] = a[i] + b[i];
  }
  for (int i = 0; i < N; i++) {
    printf("c[%d] = %d\n", i, c[i]);
  }
  /* sequential code section */
  return 0;
}