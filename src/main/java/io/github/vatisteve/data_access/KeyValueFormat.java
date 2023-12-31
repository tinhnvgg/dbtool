package io.github.vatisteve.data_access;

/**
 * @author tinhnv
 * @since Oct 31, 2023
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface KeyValueFormat<K, V> {

    K getIndex();
    V getValue();

}
