package io.github.vatisteve.metadata.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author tinhnv
 * @since Dec 23, 2023
 */
public class MetadataState extends HashMap<MetadataState.Metadata<String>, List<MetadataState.State>> {

    private static final long serialVersionUID = -8907124938489670462L;

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
    public static class Metadata<T> {

        private final T root;
        private final T combo;

        public Metadata(T root) {
            this(root, null);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Metadata<?> metadata = (Metadata<?>) o;

            return new EqualsBuilder().append(root, metadata.root).append(combo, metadata.combo).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(root).append(combo).toHashCode();
        }

    }

    public enum State {
        CREATE, RENAME, MODIFY, DELETE
    }

}
