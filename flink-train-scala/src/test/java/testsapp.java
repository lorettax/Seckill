//import java.lang.Object.*

import java.util.HashMap;
import java.util.concurrent.*;

public class testsapp implements Cloneable{
    public static void main(String[] args) throws Throwable {

//        Executor fixedpool = Executors.newFixedThreadPool(5);
//
//        Executor chchepool = Executors.newCachedThreadPool();
//        ExecutorService schedluepool = Executors.newScheduledThreadPool(2);
//        ForkJoinPool forkjoin = new ForkJoinPool();
//        ExecutorService singlepool = Executors.newSingleThreadExecutor();
//        Executor workpool = Executors.newWorkStealingPool();
        testsapp obj1 = new testsapp();
        Object o2 = obj1.clone();
        obj1.finalize();
//        String
//        System.out.println(obj1.equals(o2));
//        System.out.println(obj1 ==o2);
//        HashMap<Integer,String> map = new HashMap<Integer,String >();
//        map.put(1,"jack");
////        System.out.println("hello java...");
    }
}
