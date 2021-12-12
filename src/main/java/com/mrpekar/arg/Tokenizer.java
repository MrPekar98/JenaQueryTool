package com.mrpekar.arg;

import java.util.List;

public interface Tokenizer
{
    List<Token> all();
    Token next();
    Token prev();
    boolean isAtEnd();
    void reset();
}
