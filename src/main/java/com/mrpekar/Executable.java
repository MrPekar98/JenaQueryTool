package com.mrpekar;

public interface Executable<E>
{
    E exec(Object... args);
}
