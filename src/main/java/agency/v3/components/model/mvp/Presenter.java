package agency.v3.components.model.mvp;

import java.util.HashMap;
import java.util.Map;

import agency.v3.components.model.core.CompositeDisposableBuilder;
import agency.v3.components.model.core.Destroyable;
import agency.v3.components.model.core.DisposableBuilder;
import agency.v3.components.model.core.DisposeBag;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Base presenter claass
 * */
public abstract class Presenter<View extends IView, PresenterState> implements Destroyable {

    protected final PresenterState state;

    /**
     * Stuff that gets cleared on complete activity destroy
     */
    private final DisposeBag onDestroyBag = new DisposeBag();

    /**
     * Maps View to it's properties' bindings
     */
    private HashMap<View, DisposeBag> connections = new HashMap<>();

    private boolean isInitialized = false;

    /**
     * Constructs Presenter, initializing it's internal state with provided state
     * */
    public Presenter(PresenterState state) {
        this.state = state;
    }

    /**
     * Performs a one time initialization - if ViewModel outlives Activity or Fragment, and new instance of Activity/Fragment calls init, initialization will not happen again
     */
    private void init() {
        if (!isInitialized) {

            onInit();
            isInitialized = true;
        }
    }

    /**
     * Invoked when {@link Presenter} is initialized first time. You may perform initial loading here
     */
    protected abstract void onInit();

    /**
     * Invoked each time when View is attached to presenter.
     *
     * @return Subscription of connection of view's properties to presenter
     */
    protected abstract Disposable onAttach(View view);

    /**
     * <p>Attaches to view. Subscribe to all view's properties here.</p>
     * <p>View and Presenter are connected functionally: presenter consumes view's outputs and notifies view's inputs</p>
     * View layer should manage the subscriptions in order not to leak memory
     */
    public final Disposable attach(View view) {
        init();
        return onAttach(view);
    }

    /**
     * Begins building {@link CompositeDisposable}
     * */
    protected CompositeDisposableBuilder bind() {
        return new CompositeDisposableBuilder();
    }

    /**
     * An empty binding in case your Presenter does not actually do anything
     * */
    protected Disposable emptyBinding() {
        return new CompositeDisposable();
    }

    /**
     * Start building a disposable of some type. Will detect if disposable callbacks are not provided and throw {@link IllegalArgumentException} if so
     *
     * @param type type of emission
     */
    protected  <T> DisposableBuilder<T> disposable(Class<T> type) {
        return new DisposableBuilder<>(false/*strict mode: detect if disposable is empty*/);
    }

    /**
     * Start building a disposable of some type.
     *
     * @param type           type of emission
     * @param allowQuietMode whether to detect quiet (no callback defined) disposables and throw {@link IllegalArgumentException} if so
     */
    public <T> DisposableBuilder<T> disposable(Class<T> type, boolean allowQuietMode) {
        return new DisposableBuilder<>(allowQuietMode);
    }


    /**
     * Destroy this presenter. Make sure you destroy presenter when Controller is destroyed (not when View is)
     */
    public void destroy() {
        for (Map.Entry<View, DisposeBag> e : connections.entrySet()) {
            DisposeBag bag = e.getValue();
            if (bag != null) {
                bag.disposeAll();
            }
        }
        connections.clear();

        onDestroyBag.disposeAll();
    }


    /**
     * Cleans a given subscription when {@link Presenter} is destroyed
     */
    protected void autoDestroy(String name, Disposable subscription) {
        onDestroyBag.addDisposable(name, subscription);
    }

}
