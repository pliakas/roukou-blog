package org.roukou.blog.collections.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class SwapHashMapExample
{
    public static void main( String[] args )
    {
        Map<String, String> first = generateMapContent( "buildTool:maven", "lang:java", "db:mysql" );
        Map<String, String> second = generateMapContent( "buildTool:gradle", "lang:scala", "db:mongo" );

        swapMapContentUsingDefaultMethods( first, second );

        swapMapContentUsingLambdas( first, second );

        swapMapContent( first, second );


    }

    public static List<Map<String, String>> swapMapContentUsingDefaultMethods( Map<String, String> first,
                                                                               Map<String, String>
        second )
    {

        System.out.println( "\nMethod Name: swapMapContentUsingDefaultMethods" + "\n"
                                + "Before Swap" + "\n"
                                + "First " + "Map: " + first + "\n"
                                + "Second " + "Map: " + "" + second  );

        first.replaceAll( second::put );

        System.out.println( "After Swap" + "\n" + "First Map: " + first + "\n" + "Second Map: " + second  );

        List<Map<String, String>> result = new ArrayList<>();
        result.add( first );
        result.add( second );

        return result;

    }

    public static List<Map<String, String>> swapMapContentUsingLambdas( Map<String, String> first,
                                                                               Map<String, String>
        second )
    {

        System.out.println( "\nMethod Name: swapMapContentUsingDefaultMethods" + "\n"
                                + "Before Swap" + "\n"
                                + "First Map: " + first + "\n"
                                + "Second Map: " + second  );

        final BiFunction<String, String, String> function = ( key, value ) -> second.put( key, value );
        for( Map.Entry<String, String> entry : first.entrySet() )
        {
            entry.setValue( function.apply( entry.getKey() , entry.getValue() ) );
        }

        System.out.println( "After Swap" + "\n" + "First Map: " + first + "\n" + "Second Map: " + second  );

        List<Map<String, String>> result = new ArrayList<>();
        result.add( first );
        result.add( second );

        return result;

    }


    public static List<Map<String, String>> swapMapContent( Map<String, String> first,
                                                                               Map<String, String>
        second )
    {

        System.out.println( "Before Swap" + "\n" + "First Map: " + first + "\n" + "Second Map: " + second  );

        for( Map.Entry<String, String> entry : first.entrySet() )
        {
            entry.setValue( second.put( entry.getKey(), entry.getValue() ) );
        }

        System.out.println( "After Swap" + "\n" + "First Map: " + first + "\n" + "Second Map: " + second  );

        List<Map<String, String>> result = new ArrayList<>();
        result.add( first );
        result.add( second );

        return result;

    }

    private static Map<String, String> generateMapContent( String...values )
    {
        Map<String, String> map = new HashMap<>();
        for( String value : values )
        {
            String[] splitted = value.split( ":" );
            map.put( splitted[0], splitted[1] );
        }

        return map;
    }
}
