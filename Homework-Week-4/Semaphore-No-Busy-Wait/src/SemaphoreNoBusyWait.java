import java.util.LinkedList;
import java.util.Queue;

public class SemaphoreNoBusyWait {
    private int semVal;
    private Queue<Process> pList; // list of waiting processes

    public SemaphoreNoBusyWait(int initPermits){
        this.semVal = initPermits;
        pList = new LinkedList<Process>();
    }

    public synchronized void P(Process process){
        semVal--;
        System.out.println("Process Queued: Worker-"+process.name);
        if(semVal < 0){ // if can't aquire the semaphore, it has to wait
            pList.add(process);
            try{
                wait();
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void V(){
        semVal++; // allow another process to start its critical section.
        if(semVal <= 0){ // if there are processes in the waiting queue
            Process temp = pList.poll();
            if(temp != null){
                try{
                   notify(); 
                }catch(IllegalMonitorStateException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
