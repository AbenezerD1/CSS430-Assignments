import java.util.concurrent.Semaphore;

public class Deadlock {
    
    public Deadlock( ) {
        Semaphore mutex[] = new Semaphore[4];

        for ( int i = 0; i < 4; i++ )
            mutex[i] = new Semaphore(1);

        A threadA = new A( mutex );
        B threadB = new B( mutex );
        C threadC = new C( mutex );

        threadA.start( );
        threadB.start( );
        threadC.start( );
    }

    private class A extends Thread {
        private Semaphore[] resource;

        public A(Semaphore[] m) {
            resource = m;
        }

        public void run( ) {
            System.out.println( "A started" );
            synchronized ( resource[1] ) {
                System.out.println( "--A locks rsc 1" );
                synchronized ( resource[0] ) {
                    System.out.println( "--A locks rsc 0" );
                }
            }
            System.out.println( "A finished" );
        }
    }

    private class B extends Thread {
        private Semaphore[] resource;

        public B(Semaphore[] m) {
            resource = m;
        }

        public void run() {
            System.out.println("B started");
            synchronized (resource[3]) {
                System.out.println("--B locks rsc 3");
                synchronized (resource[0]) {
                    System.out.println("--B locks rsc 0");
                    synchronized (resource[2]) {
                        System.out.println("--B locks rsc 2");
                    }
                }
            }
            System.out.println("B finished");
        }
    }

    private class C extends Thread
    {
        private Semaphore[] resource;
        public C( Semaphore[] m ) {
            resource = m;
        }

        public void run( ) {
            System.out.println( "C started" );
            synchronized ( resource[2] ) {
                System.out.println( "--C locks rsc 2" );
                synchronized ( resource[1] ) {
                    System.out.println( "--C locks rsc 1" );
                }
            }
            System.out.println( "C finished" );
        }
    }
}