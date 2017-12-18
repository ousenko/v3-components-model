package agency.v3.components.model.entity;


import agency.v3.components.model.core.Consumer0;
import io.reactivex.annotations.Nullable;

/**
 * An class that encapsulates error details as well as options for recovering from it
 * */
public class ErrorWithRecovery {

    private final Throwable reasonThrowable;
    private final String reason;

    @Nullable
    private final Consumer0 recovery;

    @Nullable
    private final Consumer0 cancellation;

    ErrorWithRecovery(Throwable reasonThrowable, String reason, Consumer0 recovery, Consumer0 cancellation) {
        this.reasonThrowable = reasonThrowable;
        this.reason = reason;
        this.recovery = recovery;
        this.cancellation = cancellation;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private Throwable reasonThrowable;
        private String reason;

        @Nullable
        private Consumer0 recovery;

        @Nullable
        private Consumer0 cancellation;

        public Builder() {

        }

        /**
         * Actual {@link Throwable} this {@link ErrorWithRecovery} encapsulates
         * */
        public Builder reasonThrowable(Throwable throwable) {
            this.reasonThrowable = throwable;
            return this;
        }

        /**
         * Textual representation of a reason of an error
         * */
        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        /**
         * An action invoked when user decides to cancel failed operation
         * */
        public Builder cancellation(Consumer0 cancellation) {
            this.cancellation = cancellation;
            return this;
        }

        /**
         * An action invoked when user decides to recover from failed operation
         * */
        public Builder recovery(Consumer0 recovery) {
            this.recovery = recovery;
            return this;
        }


        public ErrorWithRecovery build() {
            if (reason == null || reasonThrowable == null) {
                throw new IllegalArgumentException("Should provide both reason and reason throwable");
            }
            return new ErrorWithRecovery(reasonThrowable, reason, recovery, cancellation);
        }

    }


    /**
     * Actual {@link Throwable} this {@link ErrorWithRecovery} encapsulates
     * */
    public Throwable reasonThrowable() {
        return reasonThrowable;
    }

    /**
     * Textual representation of a reason of an error
     * */
    public String reason() {
        return reason;
    }

    /**
     * An action invoked when user decides to recover from failed operation
     * */
    @Nullable
    public Consumer0 recovery() {
        return recovery;
    }

    /**
     * An action invoked when user decides to cancel failed operation
     * */
    @Nullable
    public Consumer0 cancellation() {
        return cancellation;
    }
}
