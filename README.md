# JenaQueryTool
A tool to analyze SPARQL queries in Apache Jena ARQ.

## Guide
Run the jar file with any of the following parameters using JDK 11:

- `query`: print the input query
- `plan`: print the physical query execution plan
- `length`: print the height of the query execution plan tree
- `intermediate`: print intermediate result set sizes

A query file is mandatory. Run the executable with `--query` followed by the query file name containing the SPARQL query.
To run the `intermediate` command, a data file is necessary. Add the flag `--data` followed by the RDF data file name.
All of the above commands can be passed at once to print all results.

The tool can be extended. Just add a new class in the `run` package that implements the `Executable<Value>` class. Instantiate the new class in the `Token` enum.
