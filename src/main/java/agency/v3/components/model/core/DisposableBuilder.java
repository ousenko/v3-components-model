package agency.v3.components.model.core;

import io.reactivex.observers.DisposableObserver;

/**
 * A helper for building {@link DisposableObserver} in a fluent way
 */
public class DisposableBuilder<T> {
    private Consumer0 whenStart;
    private Consumer0 whenDone;
    private Consumer1<T> whenNext;
    private Consumer1<Throwable> whenError;
    private final boolean allowQuietDisposable;

    /**
     * @param allowQuietDisposable whether to allow all callbacks to be empty, or throw an
     * */
    public DisposableBuilder(boolean allowQuietDisposable) {
        this.allowQuietDisposable = allowQuietDisposable;
    }

    /**
     * Invoke this callback in {@link DisposableObserver#onNext(T)}
     * */
    public DisposableBuilder<T> whenNext(Consumer1<T> onNext) {
        this.whenNext = onNext;
        return this;
    }

    /**
     * Invoke this callback in {@link DisposableObserver#onStart()}
     * */
    public DisposableBuilder<T> whenStart(Consumer0 onStart) {
        this.whenStart = onStart;
        return this;
    }

    /**
     * Invoke this callback in {@link DisposableObserver#onError(Throwable)}
     * */
    public DisposableBuilder<T> whenError(Consumer1<Throwable> onError) {
        this.whenError = onError;
        return this;
    }

    /**
     * Invoke this callback in {@link DisposableObserver#onComplete()}
     * */
    public DisposableBuilder<T> whenDone(Consumer0 onDone) {
        this.whenDone = onDone;
        return this;
    }

    private void onCompleted() {
        if (whenDone != null) {
            whenDone.call();
        }
    }

    private void onError(Throwable e) {
        if (whenError != null) {
            whenError.call(e);
        }
    }

    private void onNext(T t) {
        if (whenNext != null) {
            whenNext.call(t);
        }
    }

    private void onStart() {
        if (whenStart != null) {
            whenStart.call();
        }
    }

    /**
     * Builds a {@link DisposableObserver}.
     * @throws IllegalArgumentException if no callbacks provided AND allowQuietDisposable param is false
     * */
    public DisposableObserver<T> build() {
        if (!allowQuietDisposable && whenStart == null && whenDone == null && whenError == null && whenNext == null) {
            throw new IllegalArgumentException("Disposable does not define any callback, and allowQuietDisposable param is FALSE");
        }
        return new DisposableObserver<T>() {

            @Override
            public void onComplete() {
                DisposableBuilder.this.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                DisposableBuilder.this.onError(e);
            }

            @Override
            public void onNext(T t) {
                DisposableBuilder.this.onNext(t);
            }

            @Override
            public void onStart() {
                DisposableBuilder.this.onStart();
            }
        };
    }

}
