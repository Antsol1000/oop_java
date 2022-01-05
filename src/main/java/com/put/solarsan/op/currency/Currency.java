package com.put.solarsan.op.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

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
