package ru.makhach.springunzipper.executors;

import ru.makhach.springunzipper.models.CancelledTask;

import java.util.concurrent.*;

public class CancelledTaskExecutor extends ThreadPoolExecutor {
    public CancelledTaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof CancelledTask) {
            CancelledTask<T> cancelledTask = (CancelledTask<T>) callable;
            return new FutureTask<T>(callable) {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return super.cancel(mayInterruptIfRunning) && cancelledTask.cancel();
                }
            };
        }
        return super.newTaskFor(callable);
    }
}
