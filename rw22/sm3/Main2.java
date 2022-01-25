import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Main2 {

    public static void main(String[] args) throws Exception {
        //Creating n threads as required
        ExecutorService exec = Executors.newFixedThreadPool(4);
        for(int i = 0; i < 4; i++){
            exec.execute(new ProcessRunnable());
        }

        Thread.sleep(10000);

        //whenever you want them to stop
        exec.shutdownNow();

    }   

}

class ProcessRunnable implements Runnable{
       @Override
       public void run(){
        do{
           Process p;
        try {
            p = Runtime.getRuntime().exec("ls");
            p.waitFor(); 
        } catch (Exception e) {
                    //Take appropriate steps
            e.printStackTrace();
        }

        }while(!Thread.interrupted());
       }
}