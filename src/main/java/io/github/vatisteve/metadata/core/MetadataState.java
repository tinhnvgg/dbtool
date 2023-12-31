package io.github.vatisteve.metadata.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author tinhnv
 * @since Dec 23, 2023
 */
public class MetadataState extends HashMap<MetadataState.Metadata<String>, List<MetadataState.State>> {

    public void put(Metadata<String> key, State... states) {
        List<State> existingStates = get(key);
        if (existingStates == null) {
            put(key, List.of(states));
        } else {
            List<State> newStates = new ArrayList<>(existingStates);
            newStates.addAll(List.of(states));
            put(key, newStates);
        }
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Metadata<T> {

        private final T root;
        private final T combo;

        public Metadata(T root) {
            this(root, null);
        }

    }

    public enum State {
        CREATE, RENAME, MODIFY, DELETE
    }

}
