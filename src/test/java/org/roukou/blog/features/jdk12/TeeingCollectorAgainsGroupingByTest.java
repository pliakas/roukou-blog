package org.roukou.blog.features.jdk12;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Paths.get;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TeeingCollectorAgainsGroupingByTest
{
    private static final Pattern SPLIT_PATTERN = Pattern.compile("[- .:,]+");

    private BufferedReader reader;

    @BeforeEach
    void readFile() throws IOException, URISyntaxException
    {
        reader = Files.newBufferedReader(
            get( TeeingCollectorAgainsGroupingByTest.class.getClassLoader().getResource( "sample_test.txt" ).toURI() ),
            StandardCharsets.UTF_8 );
    }

    @AfterEach
    void tearDown() throws IOException
    {
        reader.close();
    }

    @Test
    @Order( 1 )
    @EnabledOnJre( JRE.JAVA_11 )
    @DisplayName( "Separate even and odd length words from a file - Using Java 11 with Immutable Pair (Apache Lang 3" )
    void findWordsWithOddAndEvenLength_Java11_withTuple()
    {
        Map<Boolean, List<String>> map = reader
            .lines()
            .flatMap( SPLIT_PATTERN::splitAsStream )
            .collect( partitioningBy(  word -> ( word.length() & 1 ) == 1 , toList() ) );

        var result = Pair.of( map.get( true ), map.get( false ) );

        assertEquals( 58, result.getLeft().size() );
        assertEquals( 49, result.getRight().size() );
    }

    @Test
    @Order( 2 )
    @EnabledOnJre( JRE.JAVA_12 )
    @DisplayName( "Separate even and odd length words from a file - Using Java 12 and an Immutable Tuple" )
    void findWordsWithOddAndEvenLength_Java12()
    {
        Pair<List<String>, List<String>> result = reader
            .lines()
            .flatMap( SPLIT_PATTERN::splitAsStream )
            .collect( teeing(
                filtering( word -> (  word.length() & 1 ) == 1, toList() ),
                filtering( word -> ( word.length() % 2 ) == 0, toList() ),
                Pair::of ) );

        assertEquals( 58, result.getLeft().size() );
        assertEquals( 49, result.getRight().size() );
    }

    @Test
    @Order( 3 )
    @EnabledOnJre( JRE.JAVA_12 )
    @DisplayName( "Separate even and odd length words from a file - Using Java 12 with Immutable list" )
    void findWordsWithOddAndEvenLength_Java12_withTuple()
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

    @Test
    @Order( 4 )
    @EnabledOnJre( JRE.JAVA_11 )
    @DisplayName( "Extracts all the superclasses of ArrayList and their implemented classes except AbstractClasses)" )
    void mapOfClassesAndInterfaces_withJava11()
    {
        Class<?> input = ArrayList.class;

        Stream<Class<?>> classesAndIterfaces = Stream
            .<Class<?>>iterate( input, Class::getSuperclass  )
            .takeWhile( Objects::nonNull )
            .flatMap( entry -> Stream.of( Stream.of(entry ) , Arrays.stream( entry.getInterfaces() ) ) )
            .flatMap( Function.identity() );

        Predicate<Class<?>> isInterface = Class::isInterface;
        Predicate<Class<?>> isConcrete = class_ -> !Modifier.isAbstract( class_.getModifiers() );

        Map<Boolean, Set<Class<?>>> result = classesAndIterfaces
            .filter( isInterface.or( isConcrete ) )
            .collect( partitioningBy( Class::isInterface, toSet() ) );

            Assert.assertEquals( Map.of(
            false, Set.of(ArrayList.class, Object.class),
            true,  Set.of(List.class, RandomAccess.class, Cloneable.class,
                Serializable.class, Collection.class)),
            result);
    }


    @Test
    @Order( 6 )
    /**
     * Write a method that extracts all the superclasses and
     * their implemented classes. Filter out the abstract classes, then
     * create a map with two boolean keys, true is associated to the interfaces
     * and false with the concrete classes. Do that for the provided classes, and
     * arrange the result in a Map<Class, ...> with those classes as the keys.
     */
    void findSuperclassesAndInterfaces_withJava11()
    {
        List<Class<?>> input = List.of( ArrayList.class, HashSet.class, LinkedHashSet.class );

        Function<Class<?>, Stream<Class<?>>> superClasses = clazz -> Stream.<Class<?>>iterate( clazz,
            Class::getSuperclass ).takeWhile( Objects::nonNull );

        Function<Stream<? extends Class<?>>, Stream<? extends Class<?>>> classAndInterfaces = stream -> stream
            .flatMap( clazz -> Stream.of( Stream.of( clazz ), Arrays.stream( clazz.getInterfaces() ) ) )
            .flatMap( Function.identity() );

        Function<Class<?>, Stream<? extends Class<?>>> superClassesAndInterfaces = superClasses
            .andThen( classAndInterfaces );

        Predicate<Class<?>> isConcrete = c -> !Modifier.isAbstract( c.getModifiers() );
        Predicate<Class<?>> isInterface = Class::isInterface;
        Predicate<Class<?>> isInterfaceOrConcreteClass = isInterface.or( isConcrete );

        Map<Boolean, Set<Class<?>>> unusedResult = input.stream().flatMap( superClassesAndInterfaces )
            .filter( isInterfaceOrConcreteClass )
            .collect( Collectors.partitioningBy( isInterface, Collectors.toSet() ) );

        // 2) Convert the processing to a collector
        Collector<Class<?>, ?, Map<Boolean, Set<Class<?>>>> collector = Collectors
            .flatMapping( superClassesAndInterfaces, Collectors.filtering( isInterfaceOrConcreteClass,
                partitioningBy( isInterface, Collectors.toSet() ) ) );

        // 3) use it as a downstream collector
        Map<Class<?>, Map<Boolean, Set<Class<?>>>> result = input
            .stream()
            .collect( groupingBy( Function.identity(), collector ) );

        Assert.assertEquals(
            Map.of(
                ArrayList.class,
                Map.of(false, Set.of(ArrayList.class, Object.class),
                    true,  Set.of(List.class, RandomAccess.class, Cloneable.class,
                        Serializable.class, Collection.class)),
                HashSet.class,
                Map.of(false, Set.of(HashSet.class, Object.class),
                    true,  Set.of(Set.class, Cloneable.class,
                        Serializable.class, Collection.class)),
                LinkedHashSet.class,
                Map.of(false, Set.of(LinkedHashSet.class, HashSet.class, Object.class),
                    true,  Set.of(Set.class, Cloneable.class,
                        Serializable.class, Collection.class))),
            result);
    }

    @Test
    @Order( 7 )
    @DisplayName( "" )
    void findSuperclassesAndInterfaces_withJava12()
    {

    }
}
