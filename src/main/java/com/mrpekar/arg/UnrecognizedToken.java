package com.mrpekar.arg;

public class UnrecognizedToken extends RuntimeException
{
    private String what;

    public UnrecognizedToken(String what)
    {
        this.what = what;
    }

    @Override
    public String getMessage()
    {
        return this.what;
    }

    @Override
    public String toString()
    {
        return this.what;
    }
}
