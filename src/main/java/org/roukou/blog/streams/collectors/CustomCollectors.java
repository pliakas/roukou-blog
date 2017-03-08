package org.roukou.blog.streams.collectors;

import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class CustomCollectors
{
    private CustomCollectors() {}

    public static <T> Collector<T, ?, T[]> toArray( IntFunction<T[]> convert )
    {
        return Collectors.collectingAndThen( Collectors.toList(),
                                             list -> list.toArray( convert.apply( list.size() ) ) );
    }
}
