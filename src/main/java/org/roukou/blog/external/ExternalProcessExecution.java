package org.roukou.blog.external;

import java.io.IOException;

public class ExternalProcessExecution
{
    public static void main( String[] args )
    {
        try
        {
            Process process = Runtime.getRuntime().exec( new String[]{"/bin/bash", "-c", "ls -la &> skata.txt"} );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }
}
