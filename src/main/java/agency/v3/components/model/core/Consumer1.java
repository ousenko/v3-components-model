package agency.v3.components.model.core;

/**
 * 1-arg Consumer, without checked exception, replacement for RxJava 1's Action1
 */

public interface Consumer1<T> {
    void call(T value);
}
