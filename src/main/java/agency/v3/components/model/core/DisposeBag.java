package agency.v3.components.model.core;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;

/**
 * A helper to remember Subscriptions, Destroyable (by name) and dispose them (by name, all)
 * when needed.
 * <br/>
 * <p>
 * <b>This class is not thread safe. Please address to it from one thread only.</b>
 *
 * @author drew
 */

public class DisposeBag {

    private final HashMap<String, Disposable> namedDisposables;
    private final HashMap<String, Destroyable> namedDestroyables;

    public DisposeBag() {
        this.namedDisposables = new HashMap<>();
        this.namedDestroyables = new HashMap<>();
    }


    /**
     * This method allows to add named operation handle, so that it could be further
     * cancelled by name.
     * Adding subsequent subscription with the same name will cancel previous ongoing operation
     */
    public void addDisposable(String name, Disposable subscription) {
        cancel(name);
        this.namedDisposables.put(name, subscription);

    }

    /**
     * This method allows to add named operation handle, so that it could be further
     * cancelled by name.
     * Adding subsequent subscription with the same name will cancel previous ongoing operation
     */
    public void addDisposable(String name, Destroyable disposable) {
        cancel(name);
        this.namedDestroyables.put(name, disposable);
    }


    /**
     * Disposes previously added disposable by name with which it was added.
     */
    private void cancelDestroyableByName(String name) {
        Destroyable previous = this.namedDestroyables.remove(name);
        if (previous != null) {
            previous.destroy();
        }
    }


    /**
     * Disposes previously added disposable by name with which it was added.
     */
    private void cancelDisposableByName(String name) {
        Disposable previous = this.namedDisposables.remove(name);
        if (previous != null && !previous.isDisposed()) {
            previous.dispose();
        }
    }

    /**
     * Disposes previously added disposable by name with which it was added.
     */
    public void cancel(String name) {
        cancelDisposableByName(name);
        cancelDestroyableByName(name);
    }


    /**
     * Disposes all disposables contained in this container.
     */
    public void disposeAll() {

        //dispose from all actions
        for (Destroyable d : namedDestroyables.values()) {
            d.destroy();
        }
        namedDestroyables.clear();

        //unsubscribe from all subscriptions and get rid from them
        for (Disposable s : namedDisposables.values()) {
            if (!s.isDisposed()) {
                s.dispose();
            }
        }
        namedDisposables.clear();


    }

}
