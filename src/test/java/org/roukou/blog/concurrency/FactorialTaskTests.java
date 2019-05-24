package org.roukou.blog.concurrency;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class FactorialTaskTests
{

    private ExecutorService pool;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp()
    {
        pool = Executors.newSingleThreadExecutor();
    }

    @After
    public void tearDown() throws Exception
    {
        pool.shutdown();
        while (!pool.awaitTermination(1, TimeUnit.SECONDS)) ;
    }

    @Test
    public void whenTaskSubmitted_ThenFutureResultObtained() throws ExecutionException, InterruptedException
    {
        FactorialTask task = new FactorialTask(5);


        Future<Integer> result = pool.submit( task );
        assertEquals( 120, result.get().intValue() );
    }

    /*
     Callable using an ExecutorService, the exceptions are collected in the Future object,
     which can be checked by making a call to the Future.get() method.
     This will throw an ExecutionException – which wraps the original exception.
     To get the original checked exceptionthe getCause() method on this exception object .
     */
    @Test
    public void whenException_ThenCallableThrowsIt() throws ExecutionException, InterruptedException
    {

        FactorialTask task = new FactorialTask(-5 );
        Future<Integer> future = pool.submit(task);
        expectedException.expect( ExecutionException.class );
        expectedException.expectMessage( "" );

        Integer result = future.get();
    }

    /*
    If you don’t make the call to the get() method of Future class – then the exception thrown by call() method will
    not be reported back, and the task will still be marked as completed:
     */
    @Test
    public void whenException_ThenCallableDoesntThrowsItIfGetIsNotCalled(){
        FactorialTask task = new FactorialTask(-5 );
        Future<Integer> future = pool.submit(task);

        assertFalse( future.isDone() );
    }

}
