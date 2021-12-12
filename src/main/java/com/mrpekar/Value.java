package com.mrpekar;

public class Value<V>
{
    private V value;
    private String label;
    private boolean isEmpty = false;

    public Value(String label, V value)
    {
        this.label = label;
        this.value = value;
    }

    public static Value EMPTY()
    {
        Value<Object> v = new Value<>(null, null);
        v.isEmpty = true;
        return v;
    }

    public String getLabel()
    {
        return this.label;
    }

    public V getValue()
    {
        return this.value;
    }

    public boolean isEmpty()
    {
        return this.isEmpty;
    }
}
