package agency.v3.components.model.helpers;


import io.reactivex.Observable;

/**
 * Can get current application state
 * */
public interface AppStateMonitor {

    Observable<State> monitor();

    enum State {
        FIRST_LAUNCH,
        ENTERS_FOREGROUND,
        ENTERS_BACKGROUND
    }
}
