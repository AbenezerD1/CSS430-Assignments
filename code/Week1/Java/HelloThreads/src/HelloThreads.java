public class HelloThreads {

    public static void main(String[] args) {
        Thread myThread = new Thread(new MyRunnable());
        myThread.start();
        System.out.println("Main thread continues...");

        try {
            // Wait for the worker thread to finish
            myThread.join(); 
            System.out.println("Main thread finished after waiting for myThread.");
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted.");
        }
    }

    // Runnable implementation for the new thread
    private static class MyRunnable implements Runnable {

        @Override
        public void run() {
            System.out.println("Hello from myThread!");
            // Simulate some work
            try {
                for (int i = 5; i >= 0; i--) {
                    System.out.println(String.format("Working[%d]", i));
                    Thread.sleep(1000); // Sleep for 2 seconds
                }
            } catch (InterruptedException e) {
                System.err.println("myThread interrupted.");
            }
            System.out.println("myThread exiting.");
        }
    }
}