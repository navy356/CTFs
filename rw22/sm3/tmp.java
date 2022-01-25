import java.io.BufferedReader;
import java.io.InputStreamReader;
public class Main {
  public static void main(String[] args) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            System.out.println("Ran");
          }
        });
    thread.start();
    try {
      thread.join();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Ran");
  }
}