package io.github.vatisteve.metadata.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author tinhnv
 * @since Dec 19, 2023
 */
@Getter
@RequiredArgsConstructor
public enum ReferenceActionType {

    NO_ACTION("No action"),
    CASCADE("Cascade"),
    SET_NULL("Set NULL"),
    SET_DEFAULT("Set default");

    private final String label;

    public String getValue() {
        return name();
    }
}
