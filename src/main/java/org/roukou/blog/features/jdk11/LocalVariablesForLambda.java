package org.roukou.blog.features.jdk11;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class LocalVariablesForLambda
{
    public static void main( String... args )
    {
        //find the odd lendth words and transform them to capital
        System.out.println( "find the odd lendth words and transform them to capital - " );

        List.of( "alfa", "bravo", "charlie", "delta", "echo", "foxtrot" )
            .stream()
            .filter( word -> ( word.length() & 1) == 1  )
            .map( ( @NotNull  var word ) -> word.toUpperCase() )
            .collect( toList() ).forEach( System.out::println );
    }
}
