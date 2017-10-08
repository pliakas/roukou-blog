package org.roukou.blog.pattern.builder;

public class GenericBuilderSample
{
    public static void main( String[] args )
    {
        Person person = GenericBuilder.of( Person::new )
            .with( Person::setFirstName, "Nefeli" )
            .build();

        System.out.println( person.toString() );
    }
}
