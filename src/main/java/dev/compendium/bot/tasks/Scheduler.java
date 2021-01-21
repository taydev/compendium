package dev.compendium.bot.tasks;

import dev.compendium.bot.CompendiumBot;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Credit to WalshyDev/JBA Link: https://github.com/WalshyDev/JBA/blob/master/src/main/java/com/walshydev/jba/scheduler/Scheduler.java
 */

public class Scheduler {

    private static final ScheduledExecutorService timer = Executors.newScheduledThreadPool(10,
        r -> new Thread(r, "Compendium Scheduled Task"));
    private static final Map<String, ScheduledFuture<?>> tasks = new HashMap<>();

    public static boolean scheduleRepeating(Runnable task, String taskName, long delay, long interval) {
        if (tasks.containsKey(taskName)) {
            return false;
        }
        tasks.put(taskName,
            timer.scheduleAtFixedRate(() -> {
                try {
                    task.run();
                } catch (Exception e) {
                    CompendiumBot.getLogger().error("Error in " + taskName + " task scheduler!", e);
                }
            }, delay, interval, TimeUnit.MILLISECONDS));
        return true;
    }

    public static void delayTask(Runnable task, long delay) {
        timer.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    public static boolean cancelTask(String taskName) {
        Iterator<Map.Entry<String, ScheduledFuture<?>>> i = tasks.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, ScheduledFuture<?>> next = i.next();
            if (next.getKey().equals(taskName)) {
                next.getValue().cancel(false);
                i.remove();
                return true;
            }
        }
        return false;
    }
}
