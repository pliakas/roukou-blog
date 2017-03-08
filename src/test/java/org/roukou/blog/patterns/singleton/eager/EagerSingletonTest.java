package org.roukou.blog.patterns.singleton.eager;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class EagerSingletonTest
{
    private final Supplier<EagerSingleton> singletonInstance = EagerSingleton::getInstance;

    @Test
    public void testEagerSingletonGetInstance()
    {
        EagerSingleton instance1 = singletonInstance.get();
        EagerSingleton instance2 = singletonInstance.get();
        EagerSingleton instance3 = singletonInstance.get();

        assertSame( instance1, instance2 );
        assertSame( instance2, instance3 );
        assertSame( instance3, instance1 );
    }

    @Test
    void testBreakingEagerSingleton()
    {
        EagerSingleton instance1 = singletonInstance.get();
        EagerSingleton instance2 = null;

        try
        {
            Constructor[] constructors = EagerSingleton.class.getDeclaredConstructors();
            for( Constructor constructor : constructors )
            {
                constructor.setAccessible( true );
                instance2 = (EagerSingleton) constructor.newInstance();
            }
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }

        assertNotEquals( instance1.hashCode(), instance2.hashCode() );

    }
}
