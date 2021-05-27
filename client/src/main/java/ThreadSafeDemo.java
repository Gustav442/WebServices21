import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeDemo {


private static AtomicInteger anInt = new AtomicInteger();
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<?>[] futures = new Future[10];

        for (int i = 0; i <10; i++) {
         executorService.submit(()-> doSomeHeavyWork());
        }

        for (var f: futures) {
            try{
                f.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(anInt.get());
    }
    private static void doSomeHeavyWork(){
        for (int i = 0; i < 10000; i++)
           anInt.incrementAndGet();

        System.out.println(Thread.currentThread().getName() + ":" + anInt.get());
    }


}
