package com.mrpekar.arg;

import com.mrpekar.Executable;
import com.mrpekar.Value;
import com.mrpekar.run.Intermediate;
import com.mrpekar.run.Length;
import com.mrpekar.run.Query;
import com.mrpekar.run.QueryPlan;

import java.io.File;

public enum Token
{
    PLAN("plan", new QueryPlan()),
    QUERY("query", new Query()),
    LENGTH("length", new Length()),
    FILE("file", null),
    PATH("path", null),
    INTERMEDIATE("intermediate", new Intermediate()),
    DATA_OPT("--data", null),
    DATA_LOC_OPT("--loc", null),
    QUERY_OPT("--query", null);

    private String lexeme;
    private Executable<Value> exec;

    Token(String lexeme, Executable<Value> executable)
    {
        this.lexeme = lexeme;
        this.exec = executable;
    }

    @Override
    public String toString()
    {
        return this.lexeme;
    }

    public boolean equals(Token t)
    {
        return this.lexeme.equals(t.toString());
    }

    public static Token toToken(String t)
    {
        if (t.contains("."))
        {
            Token token = Token.FILE;
            token.lexeme = t;
            return token;
        }

        else if (t.contains("/"))
        {
            Token token = Token.PATH;
            token.lexeme = t;
            return token;
        }

        switch (t)
        {
            case "plan":
                return Token.PLAN;

            case "query":
                return Token.QUERY;

            case "length":
                return Token.LENGTH;

            case "intermediate":
                return Token.INTERMEDIATE;

            case "--data":
                return Token.DATA_OPT;

            case "--loc":
                return Token.DATA_LOC_OPT;

            case "--query":
                return Token.QUERY_OPT;

            default:
                return null;
        }
    }

    public Executable<Value> getExecutable()
    {
        if (this.exec == null)
            return (Object... args) -> Value.EMPTY();

        return this.exec;
    }
}
