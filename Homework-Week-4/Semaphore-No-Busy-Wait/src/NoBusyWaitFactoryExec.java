import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NoBusyWaitFactoryExec {
    private static final int DEGREE_OF_BINOMIAL = 100;
    public static void main(String[] args){
        SemaphoreNoBusyWait sem = new SemaphoreNoBusyWait(1);
        ConcurrentHashMap<Integer, BigInteger> sharedCalculatedBinomials = new ConcurrentHashMap<>();

        ExecutorService pool = Executors.newCachedThreadPool();

        for(int i = 0; i <= DEGREE_OF_BINOMIAL; i++){
            pool.submit(new Worker(sem, DEGREE_OF_BINOMIAL, i, sharedCalculatedBinomials));
        }
        
        try {
            pool.awaitTermination(50, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        
        System.out.print(sharedCalculatedBinomials);
    }
}
