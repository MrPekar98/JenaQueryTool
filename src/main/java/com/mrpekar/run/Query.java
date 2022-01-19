package com.mrpekar.run;

import com.mrpekar.Executable;
import com.mrpekar.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Query implements Executable<Value>
{
    @Override
    public Value exec(Object... args)
    {
        if (args.length < 1)
            throw new RuntimeException("Expects a file");

        else if (!(args[0] instanceof String))
            throw new RuntimeException("Argument should be string");

        File f = new File((String) args[0]);

        try
        {
            return new Value<>("Query", getFileContent(new FileInputStream(f)));
        }

        catch (IOException exc)
        {
            throw new RuntimeException("Could not open file " + f.getName());
        }
    }

    private String getFileContent(FileInputStream input) throws IOException
    {
        byte[] bytes = input.readAllBytes();
        char[] chars = new char[bytes.length];
        int i = 0;

        for (byte b : bytes)
        {
            chars[i++] = (char) b;
        }

        return new String(chars);
    }
}
