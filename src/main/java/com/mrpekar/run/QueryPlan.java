package com.mrpekar.run;

import com.mrpekar.Executable;
import com.mrpekar.Value;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;

public class QueryPlan implements Executable<Value>
{
    private String file;

    public void setFile(String file)
    {
        this.file = file;
    }

    @Override
    public Value exec(Object... args)
    {
        if (args.length != 1)
            throw new RuntimeException("Expects a file");

        else if (!(args[0] instanceof String))
            throw new RuntimeException("Argument should be string");

        String query = (String) new Query().exec(args).getValue();
        Op op = Algebra.compile(QueryFactory.create(query));
        return new Value<>("Query Plan", op.toString());
    }
}
