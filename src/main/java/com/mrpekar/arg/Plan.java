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
        String queryFile = this.l.getQueryFile(), dataFile = null;

        while (this.l.hasNext())
        {
            Token next = this.l.next();

            if (next.equals(Token.DATA_OPT) && (next = this.l.next()).equals(Token.FILE))
                dataFile = this.l.getDataFile();

            if (dataFile != null)
                values.add(next.getExecutable().exec(queryFile, dataFile));

            else
                values.add(next.getExecutable().exec(queryFile));
        }

        return new Value("Analysis", values);
    }
}
