package org.roukou.blog.patterns.singleton.eager;

public class EagerSingleton
{
    private static final EagerSingleton INSTANCE
        = new EagerSingleton();

    private EagerSingleton() {}

    public static EagerSingleton getInstance()
    {
        return INSTANCE;
    }
}
