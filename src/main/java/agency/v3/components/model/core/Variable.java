package agency.v3.components.model.core;


import java.util.Objects;

import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;

/**
 * A variable that holds value and notifies it's consumer about value changes. Only one consumer is supported
 */
public class Variable<T> {

    @Nullable
    private volatile T value = null;

    @Nullable
    private Consumer1<T> consumer = null;

    private final boolean shouldClearValueAfterConsumption;


    private Variable(T value, boolean shouldClearValueAfterConsumption) {
        this.value = value;
        this.shouldClearValueAfterConsumption = shouldClearValueAfterConsumption;
    }


    /**
     * Set value of this {@link Variable} and notify registered consumers
     * */
    public void setValue(T value) {
        synchronized (this) {
            boolean isDistinct = setValueInternal(value);
            if (isDistinct) {
                maybeConsume();
            }
        }
    }

    private void maybeConsume() {
        if (value != null) {
            int consumeCount = 0;
//            for (Consumer1<T> c : consumers) {
//                c.call(value);
//                consumeCount++;
//            }
            if (consumer != null) {
                consumeCount++;
                consumer.call(value);
            }
            maybeClearValue(consumeCount != 0);
        }

    }


    /**
     * @return whether distinct value was set
     */
    private boolean setValueInternal(T value) {
        boolean isDistinct = !Objects.equals(this.value, value);
        this.value = value;
        return isDistinct;
    }

    private void maybeClearValue(boolean wasConsumed) {
        if (wasConsumed && shouldClearValueAfterConsumption) {
            this.value = null;
        }
    }

    /**
     * Connect observer to that variable. Observer will be notified of only distinct value changes
     * @throws IllegalStateException if you try to resubscribe already subscribed observer, or if this {@link Variable} is already subscribed to by some observer
     */
    public Disposable observe(Consumer1<T> observer) {
        synchronized (this) {
            if (consumer != null) {
                if (Objects.equals(observer, consumer)) {
                    throw new IllegalStateException("Attempt to RE-SUBSCRIBE the same observer");
                } else {
                    throw new IllegalStateException("Attempt to SUBSCRIBE observer while Variable already has observer");
                }
            } else {
                this.consumer = observer;
                maybeConsume();
            }
        }

        return new Disposable() {
            //TODO: atomic reference, compare and set?
            private volatile boolean isDisposed = false;

            @Override
            public void dispose() {
                synchronized (this) {
                    consumer = null;
                    isDisposed = true;
                }
            }

            @Override
            public boolean isDisposed() {
                return isDisposed;
            }
        };
    }


    /**
     * Constructs an empty Variable that can keep some value
     */
    public static <T> Variable<T> empty() {
        return new Variable<>(null, /*keep value*/false);
    }

    /**
     * Constructs a Variable that keeps value that's been set into it.
     */
    public static <T> Variable<T> value(T value) {
        return new Variable<>(value, /*keep value*/false);
    }

    /**
     * Constructs a Variable that keeps value only until it is consumed; after it is consumed, Variable does not hold value
     */
    public static <T> Variable<T> signal() {
        return new Variable<>(null, /*clear value after it's been consumed */true);
    }
}
