package com.antsol.op.asset;

import lombok.Value;

import java.sql.Timestamp;

/**
 * Record represents float, time immutable pair.
 */
@Value
public class Record {

    Float value;
    Timestamp timestamp;

}
