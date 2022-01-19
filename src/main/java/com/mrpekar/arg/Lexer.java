package com.mrpekar.arg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lexer implements Tokenizer
{
    private String[] args;
    private int pointer = 0;
    private String queryFile = null, dataFile = null;
    private boolean queryOptSeen = false, dataOptSeen = false;

    public Lexer(String[] args)
    {
        this.args = args;
    }

    @Override
    public Token next()
    {
        if (this.pointer == this.args.length)
            return null;

        Token t = Token.toToken(this.args[this.pointer++]);

        if (t == null)
            throw new UnrecognizedToken("Lexeme: " + this.args[this.pointer - 1]);

        else if (t.equals(Token.QUERY_OPT))
            this.queryOptSeen = true;

        else if (t.equals(Token.DATA_OPT))
            this.dataOptSeen = true;

        else if (t.equals(Token.FILE))
        {
            Token prev = Token.toToken(this.args[this.pointer - 2]);

            if (prev.equals(Token.QUERY_OPT))
                this.queryFile = t.toString();

            else if (prev.equals(Token.DATA_OPT))
                this.dataFile = t.toString();
        }

        return t;
    }

    @Override
    public boolean hasNext()
    {
        return this.pointer < this.args.length;
    }

    @Override
    public List<Token> all()
    {
        List<Token> tokens = new ArrayList<>();
        Token t;

        while ((t = next()) != null)
        {
            tokens.add(t);
        }

        return tokens;
    }

    @Override
    public Token prev()
    {
        return Token.toToken(this.args[this.pointer - 1]);
    }

    @Override
    public void reset()
    {
        this.pointer = 0;
    }

    public String getQueryFile()
    {
        int current = this.pointer;
        all();

        String f = this.queryFile;
        this.pointer = current;

        if (f == null)
            throw new RuntimeException("Missing query file");

        else if (!this.queryOptSeen)
            throw new RuntimeException("Missing '--query' flag");

        return f;
    }

    public String getDataFile()
    {
        int current = this.pointer;
        all();

        String f = this.dataFile;
        this.pointer = current;

        if (f == null)
            throw new RuntimeException("Missing data file");

        else if (!this.dataOptSeen)
            throw new RuntimeException("Missing '--data' flag");

        return f;
    }
}
