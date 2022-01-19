package com.mrpekar.run;

import com.mrpekar.Executable;
import com.mrpekar.Value;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.Op1;
import org.apache.jena.sparql.algebra.op.Op2;
import org.apache.jena.sparql.algebra.op.OpN;

import java.util.List;

public class Length implements Executable<Value>
{
    @Override
    public Value exec(Object... args)
    {
        if (args.length < 1)
            throw new RuntimeException("Expects a file");

        else if (!(args[0] instanceof String))
            throw new RuntimeException("Argument should be string");

        String query = (String) new Query().exec(args).getValue();
        Op op = Algebra.compile(QueryFactory.create(query));
        return new Value("Query Plan Length", countHops(op));
    }

    private int countHops(Op root)
    {
        if (root instanceof Op1)
            return countHops(((Op1) root).getSubOp()) + 1;

        else if (root instanceof Op2)
        {
            int op1 = countHops(((Op2) root).getLeft()), op2 = countHops(((Op2) root).getRight());
            return Math.max(op1, op2) + 1;
        }

        else if (root instanceof OpN)
        {
            List<Op> ops = ((OpN) root).getElements();
            int highest = 0;

            for (Op op : ops)
            {
                int hops = countHops(op);

                if (hops > highest)
                    highest = hops;
            }

            return highest;
        }

        return 1;
    }
}
