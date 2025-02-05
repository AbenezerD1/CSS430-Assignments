import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

public class Process {
    String name;
    private int n;
    private int k;

    public Process(String name, int n, int k){
        this.name = name;
        this.n = n;
        this.k = k;
    }

    //compute binomial value 
    public void criticalSection(ConcurrentHashMap<Integer, BigInteger> sharedCalculatedBinomials){
        sharedCalculatedBinomials.put(k, BinomialCoeffiecent.calculate(n, k)); // id speifies the k value of the binomial
    }
}
