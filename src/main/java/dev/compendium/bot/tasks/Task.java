package dev.compendium.bot.tasks;

/**
 * Credit to WalshyDev/JBA Link: https://github.com/WalshyDev/JBA/blob/master/src/main/java/com/walshydev/jba/scheduler/JBATask.java
 */
public abstract class Task implements Runnable {

    private final String taskName;

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public Task() {
        this.taskName = "JBATask-" + System.currentTimeMillis();
    }

    public void delay(long delay) {
        Scheduler.delayTask(this, delay);
    }

    public boolean repeat(long delay, long interval) {
        return Scheduler.scheduleRepeating(this, taskName, delay, interval);
    }

    public boolean cancel() {
        return Scheduler.cancelTask(taskName);
    }
}
