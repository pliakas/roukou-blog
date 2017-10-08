package org.roukou.blog.patterns.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GenericBuilder<T>
{
    private final Supplier<T> instance;

    private List<Consumer<T>> modifiers = new ArrayList<>();

    public GenericBuilder( Supplier<T> instance )
    {
        this.instance = instance;
    }

    public static <T> GenericBuilder<T> of( Supplier<T> instantiator )
    {
        return new GenericBuilder<T>( instantiator );
    }

    public <U> GenericBuilder<T> with( BiConsumer<T, U> consumer, U value )
    {
        Consumer<T> c = instance -> consumer.accept( instance, value );
        modifiers.add( c );
        return this;
    }

    public T build()
    {
        T value = instance.get();
        modifiers.forEach( modifier -> modifier.accept( value ) );
        modifiers.clear();

        return value;
    }
}
