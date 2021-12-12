package com.mrpekar.arg;

import java.util.ArrayList;
import java.util.List;

public class Lexer implements Tokenizer
{
    private String[] args;
    private int pointer = 0;
    private String file = null;

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

        else if (t.equals(Token.FILE))
            this.file = t.toString();

        return t;
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
    public boolean isAtEnd()
    {
        return this.pointer == this.args.length;
    }

    @Override
    public void reset()
    {
        this.pointer = 0;
    }

    public String getFile()
    {
        int current = this.pointer;
        all();

        String f = this.file;
        this.pointer = current;

        if (this.file == null)
            throw new RuntimeException("Missing file");

        return f;
    }
}
