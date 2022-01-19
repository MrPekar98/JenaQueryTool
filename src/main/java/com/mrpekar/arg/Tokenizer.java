package com.mrpekar.arg;

import java.util.Iterator;
import java.util.List;

public interface Tokenizer extends Iterator<Token>
{
    List<Token> all();
    Token next();
    Token prev();
    void reset();
}
