package com.put.solarsan.op.asset;

import lombok.Value;

import java.sql.Timestamp;

@Value
public class Record {

    Float value;
    Timestamp timestamp;

}
