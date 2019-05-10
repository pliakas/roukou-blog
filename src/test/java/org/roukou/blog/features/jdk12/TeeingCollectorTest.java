package org.roukou.blog.features.jdk12;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.util.List;
import java.util.regex.Pattern;

import static java.nio.file.Paths.get;
import static java.util.stream.Collectors.*;
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
    @DisplayName( "Separate even and odd length words from a file " )
    void findWordsWithOddAndEvenLength()
    {
       var result = reader
           .lines()
           .flatMap( SPLIT_PATTERN::splitAsStream )
            .collect( teeing(
                filtering( word -> (  word.length() & 1 ) == 1, toList() ),
                filtering( word -> ( word.length() % 2 ) == 0, toList() ),
                ( List<String> odd, List<String> even) -> List.of( odd, even )) );

       assertEquals( 58, result.get( 0 ).size() );
       assertEquals( 49, result.get( 1 ).size() );
    }
}
