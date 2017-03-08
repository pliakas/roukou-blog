package org.roukou.blog.streams.collectors;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomCollectorsTest
{
    @Test
    void testCustomCollectorToArray()
    {
        List<String> input = Arrays.asList( "alfa", "bravo", "charlie", "delta", "echo", "foxtrot" );

        String[] result = input.stream().filter( w -> ( w.length() & 1 ) == 1 ).map( String::toUpperCase )
                               .collect( CustomCollectors.toArray( String[]::new ) );

        assertEquals( Arrays.asList( "BRAVO", "CHARLIE", "DELTA", "FOXTROT" ), Arrays.asList( result ) );
    }
}
