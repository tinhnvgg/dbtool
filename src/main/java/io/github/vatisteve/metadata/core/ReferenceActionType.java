package io.github.vatisteve.metadata.core;

import io.github.vatisteve.common.EnumResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author tinhnv
 * @since Dec 19, 2023
 */
@Getter
@RequiredArgsConstructor
public enum ReferenceActionType implements EnumResponse {

    NO_ACTION("No action"),
    CASCADE("Cascade"),
    SET_NULL("Set NULL"),
    SET_DEFAULT("Set default");

    private final String label;

    @Override
    public String getValue() {
        return name();
    }

}
