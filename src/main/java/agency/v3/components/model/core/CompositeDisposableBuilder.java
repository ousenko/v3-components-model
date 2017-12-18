package agency.v3.components.model.core;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * A helper for building composite subscription in a fluent way
 * */
public class CompositeDisposableBuilder {


    private final CompositeDisposable disposable;

    public CompositeDisposableBuilder() {
        this.disposable = new CompositeDisposable();
    }

    /**
     * Add a {@link Disposable} to constructed composite disposable
     * */
    public CompositeDisposableBuilder add(Disposable subscription) {
        this.disposable.add(subscription);
        return this;
    }

    /**
     * Retrieves a constructed composite disposable
     * */
    public Disposable get() {
        return disposable;
    }

}
