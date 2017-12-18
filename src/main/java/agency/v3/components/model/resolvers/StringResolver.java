package agency.v3.components.model.resolvers;


/**
 * An abstraction for Android string resource resolution
 * */
public interface StringResolver {
    String getString(int resId);
    String getString(int resId, Object... args);
}
