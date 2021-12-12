# JenaQueryTool
A tool to analyze SPARQL queries in Apache Jena ARQ.

## Guide
Run the jar file with any of the following parameters:

- `query`: print the input query
- `plan`: print the query execution plan
- `length`: print the height of the query execution plan tree

Pass the name of the .txt file with the input SPARQL query.
All of the above commands can be passed at once to print all results.

The tool can be extended. Just add a new class in the run package that implements the `Executable<Value>` class. Instantiate the new class in the `Token` enum.
