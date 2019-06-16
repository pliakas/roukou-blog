package org.roukou.blog.features.streams;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.file.Paths.get;
import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortingMapTest
{

    private static final Pattern SPLIT_PATTERN = Pattern.compile("[- .:,]+");

    private BufferedReader reader;
    private Map<Integer, Long> wordFrequencyMap;

    @BeforeEach
    void readFile() throws IOException, URISyntaxException
    {
        reader = Files.newBufferedReader(
            get( SortingMapTest.class.getClassLoader().getResource( "sample_test.txt" ).toURI() ),
            StandardCharsets.UTF_8 );
    }

    @AfterEach
    void tearDown() throws IOException
    {
        reader.close();
    }

    @Test
    @Order( 1 )
    @DisplayName( "Categorize the words (key length sorted), where key=String::length & value=count of the words for this length" )
    void categorizeWordsBasedOnLength_ComparingByKey_NaturalOrder()
    {
        var result = reader
            .lines() //1
            .flatMap( SPLIT_PATTERN::splitAsStream ) //2
            .collect( Collectors.groupingBy( String::length,  HashMap::new, Collectors.counting()) ); //3

        System.out.println( result );

        assertAll(  "Sorted Map by key (String::length",
            () -> assertEquals( Map.ofEntries(
                entry( 1,  1L),
                entry( 2, 11L),
                entry( 3, 28L),
                entry( 4, 21L),
                entry( 5, 16L),
                entry( 6, 12L),
                entry( 7, 10L),
                entry( 8,  3L),
                entry( 9,  2L),
                entry(10,  2L),
                entry(11,  1L)).toString(),
            result.toString()) );
    }

    @Test
    @Order( 1 )
    @DisplayName( "Categorize the words (key length sorted), where key=String::length & value=count of the words for this length" )
    void categorizeWordsBasedOnLength_ComparingByKey_ReverseOrder()
    {
        //create a map based from the file
        Map<Integer, Long> result = reader
            .lines() //1
            .flatMap( SPLIT_PATTERN::splitAsStream ) //2
            .collect( Collectors.groupingBy( String::length, Collectors.counting()) ); //3

        //create a map in descending order
        Map<Integer, Long> reversed = new LinkedHashMap<>();
        result.entrySet().stream()
            .sorted( Map.Entry.comparingByKey( Comparator.reverseOrder() ) )
            .forEach( entry -> reversed.put( entry.getKey(), entry.getValue() ) );

        System.out.println( reversed );
        assertAll(  "Sorted Map by key (String::length",
            () -> assertEquals( Map.ofEntries(
                entry( 1,  1L),
                entry( 2, 11L),
                entry( 3, 28L),
                entry( 4, 21L),
                entry( 5, 16L),
                entry( 6, 12L),
                entry( 7, 10L),
                entry( 8,  3L),
                entry( 9,  2L),
                entry(10,  2L),
                entry(11,  1L)).toString(),

                reversed.toString()
            ) );

    }
}
