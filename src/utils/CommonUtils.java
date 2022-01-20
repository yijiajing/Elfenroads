package utils;

import java.util.Collections;
import java.util.List;

public final class CommonUtils {

    private CommonUtils() {}

    public static <T extends Comparable<T>> boolean listEqualsIgnoreOrder(List<T> l1, List<T> l2) {
        if (l1 == null || l2 == null || l1.size() != l2.size()) {
            return false;
        }
        Collections.sort(l1);
        Collections.sort(l2);
        return l1.equals(l2);
    }
}
