package common;

import java.util.List;

import static java.lang.Math.random;

public class Random {

    public <T> T from(List<T> elements) {
        return elements.get(between(0, elements.size() - 1));
    }

    public int between(int low, int high) {
        return low + (int) (random() * ((high - low) + 1));
    }

}
