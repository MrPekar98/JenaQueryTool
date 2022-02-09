package com.mrpekar.run;

import com.mrpekar.Executable;
import com.mrpekar.Value;
import com.mrpekar.util.iterator.QueryIteratorCopy;
import org.apache.jena.query.*;
import org.apache.jena.query.Query;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.algebra.AlgebraGenerator;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.Op0;
import org.apache.jena.sparql.algebra.op.Op1;
import org.apache.jena.sparql.algebra.op.Op2;
import org.apache.jena.sparql.algebra.op.OpN;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.main.OpExecutor;
import org.apache.jena.tdb.base.file.Location;
import org.apache.jena.tdb.setup.DatasetBuilderStd;

import java.util.List;

public class Intermediate implements Executable<Value>
{
    @Override
    public Value exec(Object ... args)
    {
        if (args.length < 2)
            throw new RuntimeException("Expects a query file and an RDF data file or database storage path");

        String queryFile = (String) args[0], data = (String) args[1];
        Query query = QueryFactory.read(queryFile);
        Op op = (new AlgebraGenerator()).compile(query);
        ExecutionContext context;

        if (data.split("/")[data.split("/").length - 1].contains("."))
            context = new ExecutionContext(DatasetFactory.create(RDFDataMgr.loadModel(data)).asDatasetGraph());

        else
            context = new ExecutionContext(DatasetBuilderStd.create(Location.create(data)));

        OpExecutor executor = OpExecutor.stdFactory.create(context);
        return new Value("Query Plan Intermediate Result Set Sizes", intermediateResultSizesStr(context, executor, op));
    }

    private static String intermediateResultSizesStr(ExecutionContext ctx, OpExecutor executor, Op op)
    {
        QueryIteratorCopy copyIter = new QueryIteratorCopy(OpExecutor.createRootQueryIterator(ctx));
        return op.getName() + " (" + resultSize(executor.executeOp(op, copyIter.copy())) + ")" +
                intermediateResultSizesStrTail(ctx, executor, op, "", 1);
    }

    private static String intermediateResultSizesStrTail(ExecutionContext ctx, OpExecutor exec, Op op, String str, int layers)
    {
        QueryIteratorCopy copyIter = new QueryIteratorCopy(OpExecutor.createRootQueryIterator(ctx));

        if (op instanceof Op0)
            return str;

        else if (op instanceof Op1)
        {
            Op subOp = ((Op1) op).getSubOp();
            str = enterScope(str, layers) + subOp.getName() +
                    " (" + resultSize(exec.executeOp(subOp, copyIter.copy())) + ")";

            return intermediateResultSizesStrTail(ctx, exec, subOp, str, layers + 1);
        }

        else if (op instanceof Op2)
        {
            Op leftChild = ((Op2) op).getLeft(), rightChild = ((Op2) op).getRight();
            String leftScope = enterScope(str, layers) + leftChild.getName() +
                    " (" + resultSize(exec.executeOp(leftChild, copyIter.copy())) + ")";
            String leftBranch = intermediateResultSizesStrTail(ctx, exec, leftChild, leftScope, layers + 1);
            String rightScope = enterScope(leftBranch, layers) + rightChild.getName() +
                    " (" + resultSize(exec.executeOp(rightChild, copyIter.copy())) + ")";

            return intermediateResultSizesStrTail(ctx, exec, rightChild, rightScope, layers + 1);
        }

        else if (op instanceof OpN)
        {
            List<Op> ops = ((OpN) op).getElements();

            for (Op subOp : ops)
            {
                String scope = enterScope(str, layers) + subOp.getName() +
                        " (" + resultSize(exec.executeOp(subOp, copyIter.copy())) + ")";
                str += intermediateResultSizesStrTail(ctx, exec, subOp, scope, layers + 1);
            }

            return str;
        }

        return "";
    }

    private static String enterScope(String str, int layers)
    {
        String newStr = str;
        newStr += "\n";

        for (int i = 0; i < layers; i++)
        {
            newStr += "  ";
        }

        return newStr;
    }

    private static int resultSize(QueryIterator iter)
    {
        int count = 0;

        while (iter.hasNext())
        {
            count++;
            iter.next();
        }

        return count;
    }
}
