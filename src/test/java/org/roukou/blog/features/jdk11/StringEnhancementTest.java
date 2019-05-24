package org.roukou.blog.features.jdk11;

import org.junit.jupiter.api.*;

import java.util.Map;

@TestMethodOrder( MethodOrderer.OrderAnnotation.class )
public class StringEnhancementTest
{

    @Test
    @Order(1)
    @DisplayName( "isBlank() - " )
    void stringIsBlank()
    {
        Map.of();
    }
}
