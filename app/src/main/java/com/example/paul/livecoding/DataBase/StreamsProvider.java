package com.example.paul.livecoding.DataBase;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = StreamsProvider.AUTHORITY, database = StreamsDataBase.class)
public class StreamsProvider {

    public static final String AUTHORITY = "com.example.paul.livecoding.DataBase.StreamsProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String STREAMS = "streams";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = StreamsDataBase.STREAMS)
    public static class Streams {

        @ContentUri(
                path = Path.STREAMS,
                type = "vnd.android.cursor.dir/stream"
        )
        public static final Uri CONTENT_URI = buildUri(Path.STREAMS);

        @InexactContentUri(
                path = StreamsProvider.Path.STREAMS + "/#",
                name = "STREAMS_ID",
                type = "vnd.android.cursor.item/streams",
                whereColumn = StreamsColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {

            return Uri.withAppendedPath(BASE_CONTENT_URI, String.valueOf(id));
        }
    }
}
