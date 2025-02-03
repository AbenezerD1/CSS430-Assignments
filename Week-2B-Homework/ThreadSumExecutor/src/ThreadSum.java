import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadSum{
    public static void main(String[] args){
        if(args.length != 3){ 
            System.err.println("Not enough arguments. Provided "+args.length+" arguments but one argument required.");
            return;
        }

        ExecutorService pool = Executors.newCachedThreadPool();
        Sum task1 = new Sum(Integer.parseInt(args[0]));
        Sum task2 = new Sum(Integer.parseInt(args[1]));
        Sum task3 = new Sum(Integer.parseInt(args[2]));
        
        pool.execute(task1);
        pool.execute(task2);
        pool.execute(task3);

        pool.shutdown();
    }
}
