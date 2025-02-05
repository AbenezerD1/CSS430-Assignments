import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

public class Worker implements Runnable{
    private SemaphoreNoBusyWait sem;
    private int id; // which term of the binomial coefficient its calculating
    private int n; // order of binomial
    private ConcurrentHashMap<Integer, BigInteger> sharedCalculatedBinomials; 

    public Worker(SemaphoreNoBusyWait sem, int n, int id, ConcurrentHashMap<Integer, BigInteger> binomialMap){
        this.sem = sem;
        this.id = id;
        this.n = n;
        this.sharedCalculatedBinomials = binomialMap;
    }

    public void run(){
        Process calculateBinomialProcess = new Process(""+id, n, id); // id tells for which index to calculate the value for
        
        //Entry section
        sem.P(calculateBinomialProcess); // trying to run process CS if resources are available

        //Critical section
        System.out.println("--> Critical Section Entered, Processing: Worker-"+id);
        calculateBinomialProcess.criticalSection(sharedCalculatedBinomials);
        //System.out.println("Calculating CS: was " + sharedCalculatedBinomials.get(id));

        sem.V();
        System.out.println("Process Released: Worker-"+id);
        //remainder section

    }
}
