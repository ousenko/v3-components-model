package agency.v3.components.model.helpers;

/**
 * Reference watcher abstraction
 */
public interface ReferenceWatcher {

    void watch(Object watchedReference);
    void watch(Object watchedReference, String referenceName);
}
