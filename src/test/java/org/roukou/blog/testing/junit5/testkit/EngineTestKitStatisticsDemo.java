package org.roukou.blog.testing.junit5.testkit;

import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.EngineTestKit;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

class EngineTestKitStatisticsDemo
{

    @Test
    void verifyJupiterContainersStats()
    {
        EngineTestKit
            .engine( "junit-jupiter" )
            .selectors( selectClass( EngineTestCase.class) )
            .execute()
            .containers()
            .assertStatistics( statistics -> statistics.started( 2 ).succeeded( 2 ) );
    }

    @Test
    void verifyJupiterTestStats()
    {
        EngineTestKit
            .engine( "junit-jupiter" )
            .selectors( selectClass( EngineTestCase.class) )
            .execute()
            .tests()
            .assertStatistics( statistics -> statistics.skipped( 1 ).started( 3 ).aborted( 1 ).succeeded( 2 ).failed( 1 ) );
    }
}
