package com.mrpekar.arg;

import com.mrpekar.Executable;
import com.mrpekar.Value;

import java.util.ArrayList;
import java.util.List;

public final class Plan implements Executable<Value>
{
    private Lexer l;

    public Plan(String[] args)
    {
        this(new Lexer(args));
    }

    public Plan(Lexer l)
    {
        this.l = l;
    }

    @Override
    public Value exec(Object... args)
    {
        List<Value> values = new ArrayList<>();
        String file = this.l.getFile();

        while (!this.l.isAtEnd())
        {
            values.add(this.l.next().getExecutable().exec(file));
        }

        return new Value("Analysis", values);
    }
}
