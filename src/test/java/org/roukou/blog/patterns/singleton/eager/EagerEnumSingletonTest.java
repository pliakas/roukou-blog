package org.roukou.blog.patterns.singleton.eager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EagerEnumSingletonTest
{
    @Test
    void doAction()
    {
        String expected = "Make some action!!!";
        String result = EagerEnumSingleton.INSTANCE.doAction();

        assertEquals( expected, result );
    }

}
