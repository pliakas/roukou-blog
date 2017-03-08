package org.roukou.blog.patterns.singleton.eager;

public enum  EagerEnumSingleton
{
    INSTANCE;

    public static String doAction()
    {
        return "Make some action!!!";
    }
}
