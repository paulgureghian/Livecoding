package com.example.paul.livecoding.database;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = StreamsDataBase.VERSION)
public class StreamsDataBase {
    private StreamsDataBase(){}

    public static final int VERSION = 1;

    @Table(StreamsColumns.class) public static final String STREAMS = "streams";
}
