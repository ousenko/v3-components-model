package agency.v3.components.model.executors;


import io.reactivex.Scheduler;

/**
 * Where job is actually done
 * */
public interface ExecutionThread {
    Scheduler getScheduler();
}
