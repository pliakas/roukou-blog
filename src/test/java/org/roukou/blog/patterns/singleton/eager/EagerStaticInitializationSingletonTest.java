package org.roukou.blog.patterns.singleton.eager;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class EagerStaticInitializationSingletonTest
{
    private final Supplier<EagerStaticInitializationSingleton> singletonInstance
        = EagerStaticInitializationSingleton::getInstance;

    @Test
    public void testEagerStaticInitializationSingleton()
    {
        EagerStaticInitializationSingleton instance1 = singletonInstance.get();
        EagerStaticInitializationSingleton instance2 = singletonInstance.get();
        EagerStaticInitializationSingleton instance3 = singletonInstance.get();

        assertSame( instance1, instance2 );
        assertSame( instance2, instance3 );
        assertSame( instance3, instance1 );
    }

    @Test
    void testBreakingEagerStaticInitializationSingleton()
    {
        EagerStaticInitializationSingleton instance1 = singletonInstance.get();
        EagerStaticInitializationSingleton instance2 = null;

        try
        {
            Constructor[] constructors = EagerStaticInitializationSingleton.class.getDeclaredConstructors();
            for( Constructor constructor : constructors )
            {
                constructor.setAccessible( true );
                instance2 = (EagerStaticInitializationSingleton) constructor.newInstance();
            }
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }

        assertNotEquals( instance1.hashCode(), instance2.hashCode() );

    }
}
