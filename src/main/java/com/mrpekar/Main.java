package com.mrpekar;

import com.mrpekar.arg.Plan;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        Value value = new Plan(args).exec();
        List<Value> values = (List<Value>) value.getValue();
        System.out.println(value.getLabel() + "\n");

        for (Value<?> val : values)
        {
            if (val.isEmpty())
                continue;

            System.out.println(val.getLabel());
            System.out.println(val.getValue().toString() + "\n");
        }
    }
}
