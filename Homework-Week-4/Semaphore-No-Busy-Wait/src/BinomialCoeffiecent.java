import java.math.BigInteger;

public class BinomialCoeffiecent {
    public static BigInteger calculate(int n, int k){
        //calculate n choose k
        assert !(k < 0 || k > n || n < 0): "Binomial coeffient can't have k or n being negative or k being of greater than n";

        if(k == 0 || k == n) return new BigInteger("1");

        return factorial(n).divide(factorial(k).multiply(factorial(n-k)));
    }

    public static BigInteger factorial(int num){
        assert num >= 0 : "To take factorial of num, it needs to be positive";

        BigInteger res = new BigInteger("1");

        for(int i = num; i >= 2; i--){
            res = res.multiply(BigInteger.valueOf(i));
        }

        return res;
    }
}
