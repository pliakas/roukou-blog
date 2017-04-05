package org.roukou.blog.collections.map;

import java.util.*;
import java.util.stream.Collectors;

public class SortedMapExample
{
    private Map<Integer, String> sortedMapByKey( Map<Integer, String> input )
    {
        Map<Integer, String> sorted = new TreeMap<>( input );

        return sorted;
    }

    private Map<Integer, String> sortedMapByKeyReverseUsingComparator( Map<Integer, String> input )
    {
        Map<Integer, String> sorted = new TreeMap<>( new Comparator<Integer>()
        {
            @Override
            public int compare( Integer key1, Integer key2 )
            {
                return key2.compareTo( key1 );
            }
        } );
        sorted.putAll( input );

        return sorted;
    }

    private Map<Integer, String> sortedMapByValueUsingCustomComparaotr( Map<Integer, String> input )
    {

        Map<Integer, String> sorted = new TreeMap<>(new CustomComparatorByValue( input));
        sorted.putAll( input );

        return sorted;

    }

    private Map<Integer, String> sortedMapByValueUsingLambdas( Map<Integer, String> input )
    {

        Map<Integer, String > sorted = input.entrySet().stream()
            .sorted( Map.Entry.comparingByValue() )
            .collect( Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                ( firstEntry, secondEntry) -> firstEntry, LinkedHashMap::new ) );

        return sorted;
    }

    class CustomComparatorByValue
        implements Comparator<Integer>
    {
        private Map<Integer, String> map;

        public CustomComparatorByValue( Map<Integer, String> map )
        {
            this.map = map;
        }

        @Override
        public int compare( Integer key1, Integer key2 )
        {
            return map.get( key1 ).compareTo( map.get( key2 ) );
        }
    }

    public static void main( String[] args )
    {
        System.out.println( "<java.util.TreeMap different usages" );
        Map<Integer, String> inputMap = new HashMap<>();
        for( int i = 0; i < 10; i++ )
        {
            inputMap.put( (int) ( Math.random() * 100 ),
                          String.valueOf( (int)(  Math.random() * 100  ) ) );
        }

        SortedMapExample app = new SortedMapExample();

        System.out.println( "Unorder Map used as input: " + inputMap );
        Map<Integer, String> sortedMapByKey
            = app.sortedMapByKey( inputMap );
        System.out.println( "Sorted Map by Key: " + sortedMapByKey );

        Map<Integer, String> sortedMapByKeyReverseUsingComparator =
            app.sortedMapByKeyReverseUsingComparator( inputMap );
        System.out.println( "Sorted Map by Key Reverse using Comparator: " +
                                sortedMapByKeyReverseUsingComparator );

        Map<Integer, String> sortedMapByValueUsingCustomComparator =
            app.sortedMapByValueUsingCustomComparaotr( inputMap );
        System.out.println( "Sorted Map by Value using Custom Comparator: " +
                                sortedMapByValueUsingCustomComparator );


        Map<Integer, String> sortedMapByValueUsingLambdas =
            app.sortedMapByValueUsingLambdas( inputMap );
        System.out.println( "Sorted Map by Value using lambdas: " +
                                sortedMapByValueUsingLambdas );
    }
}
