package agency.v3.components.model.helpers;


import io.reactivex.annotations.Nullable;

public class TxtUtils {
    public static boolean isEmpty(@Nullable CharSequence value) {
        return value == null || value.length() == 0;
    }
}
