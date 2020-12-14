import java.util.*;
import java.util.concurrent.*;

public class CurrentQuery {
    public static void main(String[] args) throws Exception {
        concurrentQueryDB();
    }

    private static void concurrentQueryDB() throws Exception{
        ExecutorService pool = Executors.newFixedThreadPool(10);
        Date start = new Date();
        CompletableFuture<List<Map<String, String>>> q1 = query1(pool);
        CompletableFuture<List<Map<String, String>>> q2 = query2(pool);
        CompletableFuture<List<Map<String, String>>> q3 = query3(pool);
        List<Map<String, String>> res1 = q1.get();
        List<Map<String, String>> res2 = q2.get();
        List<Map<String, String>> res3 = q3.get();
        System.out.println(res1.get(0).get("result"));
        System.out.println(res2.get(0).get("result"));
        System.out.println(res3.get(0).get("result"));
        Date end = new Date();
        System.out.println("总耗时: " + (end.getTime() - start.getTime()));
        pool.shutdown();
    }

    private static CompletableFuture<List<Map<String, String>>> query1(ExecutorService pool) throws Exception{
        CompletableFuture<List<Map<String, String>>> query = CompletableFuture.supplyAsync( () -> {
            List<Map<String, String>> list = new ArrayList<>();
            try {
                int time = new Random().nextInt(2000);
                System.out.println("QueryGraph1 " + time);
                Thread.sleep(time);
                Map<String, String> map = new HashMap<>();
                map.put("result", "res1");
                list.add(map);
                System.out.println("QueryGraph1 complete");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return list;
        }, pool);
        return query;
    }
    private static CompletableFuture<List<Map<String, String>>> query2(ExecutorService pool) throws Exception{
        CompletableFuture<List<Map<String, String>>> query = CompletableFuture.supplyAsync( () -> {
            List<Map<String, String>> list = new ArrayList<>();
            try {
                int time = new Random().nextInt(2000);
                System.out.println("QueryGraph2 " + time);
                Thread.sleep(time);
                Map<String, String> map = new HashMap<>();
                map.put("result", "res2");
                list.add(map);
                System.out.println("QueryGraph2 complete");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return list;
        }, pool);
        return query;
    }
    private static CompletableFuture<List<Map<String, String>>> query3(ExecutorService pool) throws Exception{
        CompletableFuture<List<Map<String, String>>> query = CompletableFuture.supplyAsync( () -> {
            List<Map<String, String>> list = new ArrayList<>();
            try {
                int time = new Random().nextInt(2000);
                System.out.println("QueryGraph3 " + time);
                Thread.sleep(time);
                Map<String, String> map = new HashMap<>();
                map.put("result", "res3");
                list.add(map);
                System.out.println("QueryGraph3 complete");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return list;
        }, pool);
        return query;
    }
}

