package com.mrpekar.util.iterator;

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.atlas.iterator.Iter;
import org.apache.jena.atlas.lib.Lib;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.iterator.QueryIter;
import org.apache.jena.sparql.serializer.SerializationContext;
import org.apache.jena.util.iterator.NiceIterator;

import java.util.Iterator;

public class QueryIterPlainWrapper extends QueryIter
{
    Iterator<Binding> iterator = null;

    public QueryIterPlainWrapper(Iterator<Binding> iter)
    {
        this(iter, null);
    }

    public QueryIterPlainWrapper(Iterator<Binding> iter, ExecutionContext context)
    {
        super(context);
        iterator = iter;
    }

    /** Preferable to use a constructor - but sometimes that is inconvenient
     *  so pass null in the constructor and then call this before the iterator is
     *  used.
     */
    public void setIterator(Iterator<Binding> iterator)
    {
        this.iterator = iterator;
    }

    @Override
    protected boolean hasNextBinding()
    {
        return iterator.hasNext();
    }

    @Override
    protected Binding moveToNextBinding()
    {
        return iterator.next();
    }

    @Override
    protected void closeIterator()
    {
        if ( iterator != null )
        {
            NiceIterator.close(iterator);
            Iter.close(iterator);
            iterator = null;
        }
    }

    @Override
    protected void requestCancel()
    { }

    @Override
    public void output(IndentedWriter out, SerializationContext sCxt)
    {
        out.println(Lib.className(this));
    }

}
