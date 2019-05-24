package org.roukou.blog.features.streams;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
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
        Map<Integer, Long> result = reader
            .lines() //1
            .flatMap( SPLIT_PATTERN::splitAsStream ) //2
            .collect( Collectors.groupingBy( String::length, Collectors.counting()) ); //3

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
                entry(11,  1L)),
            result) );

    }

    @Test
    @Order( 1 )
    @DisplayName( "Categorize the words (key length sorted), where key=String::length & value=count of the words for this length" )
    void categorizeWordsBasedOnLength_ComparingByValue_ReverseOrder()
    {
        Map<Integer, Long> result = reader
            .lines() //1
            .flatMap( SPLIT_PATTERN::splitAsStream ) //2
            .collect( Collectors.groupingBy( String::length, Collectors.counting()) ); //3

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
                entry(11,  1L)),
                result) );

    }
}
