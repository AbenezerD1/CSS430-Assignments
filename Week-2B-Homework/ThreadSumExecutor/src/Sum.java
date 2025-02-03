public class Sum implements Runnable{
    private final long N;
    private volatile long sum;

    public Sum(int num){
        N = num;
        sum = 0;
    }

    public void run(){
        for(int i = 1; i <= N; i++){
            sum += i;
        }
        System.out.println(sum);
    }
}
