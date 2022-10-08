package com.mrpekar.run;

import com.mrpekar.Executable;
import com.mrpekar.Value;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.AlgebraGenerator;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.*;

import java.util.List;

public class QueryPlan implements Executable<Value>
{
    @Override
    public Value exec(Object ... args)
    {
        if (args.length < 1)
            throw new RuntimeException("Expects a query file");

        Query query = QueryFactory.read((String) args[0]);
        Op op = (new AlgebraGenerator()).compile(query);
        return new Value("Query Physical Plan", genASTString(op));
    }

    private static String genASTString(Op op)
    {
        return op.getName() + genASTStringTail(op, "", 1);
    }

    private static String genASTStringTail(Op op, String str, int layers)
    {
        if (op instanceof OpBGP)
        {
            OpBGP bgp = (OpBGP) op;
            String bgpStr = str;

            for (Triple triple : bgp.getPattern().getList())
            {
                bgpStr = enterScope(bgpStr, layers + 1) + '[' +  triple + ']';
            }

            return bgpStr ;
        }

        else if (op instanceof Op0)
            return str;

        else if (op instanceof Op1)
        {
            Op subOp = ((Op1) op).getSubOp();
            str = enterScope(str, layers);
            return genASTStringTail(subOp, str + subOp.getName(), layers + 1);
        }

        else if (op instanceof Op2)
        {
            Op leftChild = ((Op2) op).getLeft(), rightChild = ((Op2) op).getRight();
            String leftScope = enterScope(str, layers);
            String leftBranch = genASTStringTail(leftChild, leftScope + leftChild.getName(), layers + 1);
            String rightScope = enterScope(leftBranch, layers);
            return genASTStringTail(rightChild, rightScope + rightChild.getName(), layers + 1);
        }

        else if (op instanceof OpN)
        {
            List<Op> ops = ((OpN) op).getElements();

            for (Op operator : ops)
            {
                String scope = enterScope(str, layers);
                str += genASTStringTail(operator, scope + operator.getName(), layers + 1);
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
}
