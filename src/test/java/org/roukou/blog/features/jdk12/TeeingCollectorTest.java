package org.roukou.blog.features.jdk12;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Paths.get;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TeeingCollectorTest
{
    private static final Pattern SPLIT_PATTERN = Pattern.compile("[- .:,]+");

    private BufferedReader reader;

    @BeforeEach
    void readFile() throws IOException, URISyntaxException
    {
        reader = Files.newBufferedReader(
            get( TeeingCollectorTest.class.getClassLoader().getResource( "sample_test.txt" ).toURI() ),
            StandardCharsets.UTF_8 );
    }

    @AfterEach
    void tearDown() throws IOException
    {
        reader.close();
    }


    @Test
    @Order( 1 )
    @DisplayName( "Find the longest and the shortest line out of  a List of strings using java 11" )
    void calculatesMinMaxLines_Traditional()
    {
        List<String> input = List.of(
            "alfa", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel");

        int max = input.stream().mapToInt( String::length ).max().orElse( -1 );
        int min = input.stream().mapToInt( String::length ).min().orElse( -1 );

        var result =
            Pair.of(
                input.stream().filter( word -> word.length() == max ).collect( toList() ),
                input.stream().filter( word -> word.length() == min ).collect( toList() ) );

        assertAll( "Longest and Shortest Words",
            () -> assertEquals( List.of( "charlie", "foxtrot"), result.getLeft() ),
            () -> assertEquals( List.of( "alfa", "echo", "golf"), result.getRight() ) );
    }

    @Test
    @Order( 2 )
    @DisplayName( "Find the longest and the shortest line out of  a List of strings using Collectors::teeing from Java 12" )
    void calculatesMinMaxLines_New()
    {
        List<String> input = List.of(
            "alfa", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel");

        int max = input.stream().mapToInt( String::length ).max().orElse( -1 );
        int min = input.stream().mapToInt( String::length ).min().orElse( -1 );

        var result = input.stream().collect( teeing(
            Collectors.filtering( word -> word.length() == max, toList() ),
            Collectors.filtering( word -> word.length() ==  min, toList() ),
            Pair::of
        ) );

        assertAll( "Longest and Shortest Words",
            () -> assertEquals( List.of( "charlie", "foxtrot"), result.getLeft() ),
            () -> assertEquals( List.of( "alfa", "echo", "golf"), result.getRight() ) );
    }

    @Test
    @Order( 3 )
    @DisplayName( "Calculate the average of Stream of integers" )
    void calculateAverageOfIntegers()
    {
        List<Integer> input = List.of( 1, 4, 2 ,7, 4, 6, 5);

        var result = input.stream().collect(
            teeing(
                summingDouble( number -> number ),
                counting(),
                ( sum, count ) -> sum / count
            )
        );

        assertEquals( 4.142857142857143, result );
    }
}
