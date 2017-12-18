package agency.v3.components.model.executors;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;


/**
 * Decorated {@link ThreadPoolExecutor}
 */
public class JobExecutor implements ExecutionThread {
    private static final int DEF_CORE_POOL_SIZE = 3;
    private static final int DEF_MAX_POOL_SIZE = 5;
    private static final int DEF_KEEP_ALIVE_TIME = 10;
    private static final TimeUnit DEF_KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final ThreadPoolExecutor threadPoolExecutor;

    /**
     * Constructs {@link JobExecutor} using default params and provided thread factory
     * */
    public JobExecutor(ThreadFactory threadFactory) {
        this.threadPoolExecutor = new ThreadPoolExecutor(
                DEF_CORE_POOL_SIZE,
                DEF_MAX_POOL_SIZE,
                DEF_KEEP_ALIVE_TIME,
                DEF_KEEP_ALIVE_TIME_UNIT,
                new LinkedBlockingQueue<>(),
                threadFactory
        );
    }

    /**
     * Constructs {@link JobExecutor} using provided thread pool executor
     * */
    public JobExecutor(ThreadPoolExecutor poolExecutor) {
        this.threadPoolExecutor = poolExecutor;
    }

    @Override
    public Scheduler getScheduler() {
        return Schedulers.from(threadPoolExecutor);
    }

}
