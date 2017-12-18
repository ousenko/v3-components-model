package agency.v3.components.model.entity;


import agency.v3.components.model.core.Consumer0;

/**
 * A no-op implementation of {@link Consumer0}
 * */
public enum NoAction implements Consumer0 {
    INSTANCE;

    @Override
    public void call() {

    }
}
