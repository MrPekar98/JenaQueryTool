package com.mrpekar.util.iterator;

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.iterator.QueryIteratorBase;
import org.apache.jena.sparql.engine.iterator.QueryIteratorWrapper;
import org.apache.jena.sparql.serializer.SerializationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryIteratorCopy extends QueryIteratorBase
{
    List<Binding> elements = new ArrayList<>();
    QueryIterator iterator, original;

    public QueryIteratorCopy(QueryIterator qIter)
    {
        while (qIter.hasNext())
        {
            this.elements.add(qIter.nextBinding());
        }

        qIter.close();
        this.iterator = copy();
        this.original = qIter;
    }

    @Override
    protected Binding moveToNextBinding()
    {
        return this.iterator.nextBinding();
    }

    @Override
    public void output(IndentedWriter out, SerializationContext sCxt)
    {
        out.print("QueryIteratorCopy");
        out.incIndent();
        this.original.output(out, sCxt);
        out.decIndent();
    }

    public List<Binding> elements()
    {
        return Collections.unmodifiableList(this.elements);
    }

    public QueryIterator copy()
    {
        return new QueryIterPlainWrapper(this.elements.iterator());
    }

    @Override
    public void closeIterator()
    {
        this.iterator.close();
    }

    @Override
    protected void requestCancel()
    {
        this.iterator.cancel();
    }

    @Override
    protected boolean hasNextBinding()
    {
        return this.iterator.hasNext();
    }
}
