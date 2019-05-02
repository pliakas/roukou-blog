package org.roukou.blog.testing.junit5.testkit;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.file.Paths.get;
import static java.util.stream.Collectors.groupingBy;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder( MethodOrderer.OrderAnnotation.class )
public class EngineTestCase
{
    private static final Pattern SPLIT_PATTERN = Pattern.compile("[- .:,]+");

    private BufferedReader reader;

    @BeforeEach
    void readFile() throws IOException, URISyntaxException
    {
        reader = Files.newBufferedReader(
            get( EngineTestCase.class.getClassLoader().getResource( "sample_test.txt" ).toURI() ),
            StandardCharsets.UTF_8 );
    }

    @Test
    @Order( 1 )
    @DisplayName( "Find Longest line from the file" )
    void findLongestLine()
    {
        var result = reader
            .lines()
            .max( Comparator.comparingInt( String::length ) )
            .orElse( "" );

        assertEquals("Feed'st thy light's flame with self-substantial fuel,", result );
    }

    @Test
    @Order( 2 )
    @DisplayName( "Find longest line from the file" )
    void findTheLengthOfLongestLine()
    {
        var result = reader
            .lines()
            .mapToInt( String::length )
            .max()
            .orElse( -1 );

        assertEquals( 53, result );
    }

    @Test
    @Order( 3 )
    @DisplayName( "Find word frequencies in the file - failing for demo purposes" )
    void findWordFrequency()
    {
        var result = reader
            .lines()
            .flatMap( SPLIT_PATTERN::splitAsStream )
            .collect( groupingBy( Function.identity(), Collectors.counting() ) );

        assertTrue( result.containsKey( "lambda" ) );
    }

    @Test
    @Order( 4 )
    @Disabled( "Empty tests that it is disabled (demo purposes) ")
    void skippedTest()
    {
        assertTrue( true );
    }
}
