package agency.v3.components.model.mvp;

import agency.v3.components.model.executors.ExecutionThread;
import agency.v3.components.model.executors.PostExecutionThread;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

/**
 * An interactor base class
 * */
public abstract class Interactor {

    protected final Scheduler worker;
    protected final Scheduler notifier;

    /**
     * Constructs {@link Interactor} using provided executor abstractions
     * */
    protected Interactor(ExecutionThread executor, PostExecutionThread postExecutionThread) {
        this(executor.getScheduler(), postExecutionThread.getScheduler());
    }


    /**
     * Constructs {@link Interactor} using provided schedulers
     * */
    protected Interactor(Scheduler executor, Scheduler postExecutionThread) {
        this.worker = executor;
        this.notifier = postExecutionThread;
    }

    /**
     * applies this {@link Interactor}'s schedulers to transform source Observable and subscribes {@link DisposableObserver} to transformed {@link Observable}
     * */
    protected <T> DisposableObserver<T> applySchedulers(Observable<T> source, DisposableObserver<T> subscriber) {
        return source.compose(o -> o.subscribeOn(worker).observeOn(notifier)).subscribeWith(subscriber);
    }
}
