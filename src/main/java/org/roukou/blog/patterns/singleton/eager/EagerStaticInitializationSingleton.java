package org.roukou.blog.patterns.singleton.eager;

public class EagerStaticInitializationSingleton
{
    private static EagerStaticInitializationSingleton INSTANCE;

    private EagerStaticInitializationSingleton() {}

    static
    {
        try
        {
            INSTANCE = new EagerStaticInitializationSingleton();
        }
        catch( Exception ex )
        {
            throw new RuntimeException( "Some exception occurred!" );
        }
    }

    public static EagerStaticInitializationSingleton getInstance()
    {
        return INSTANCE;
    }
}
