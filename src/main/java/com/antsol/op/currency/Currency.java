package com.antsol.op.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Real world currency.
 * Defined by name and list or regions where it is used.
 */
@Getter
@AllArgsConstructor
public class Currency {

    private final String name;
    private final List<String> regions;

    @Override
    public String toString() {
        return name;
    }

}
