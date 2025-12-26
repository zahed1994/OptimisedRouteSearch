# Getting Started with Optimal Route Finder

## Quick Start

### 1. Build the Project
```bash
cd C:\Users\asus\IdeaProjects\optimalroutefinder
sbt clean compile
```

### 2. Run the Demo
```bash
sbt "run demo"
```

This will:
- Create a sample graph with 5 nodes (A-E)
- Find routes using three different algorithms
- Display all paths from node A
- Show the graph in CSV format

### 3. Find a Specific Route
```bash
sbt "run find A E"
```

This finds the shortest path from node A to node E using Dijkstra's algorithm.

## Understanding the Output

When you run the demo, you'll see output like:

```
╔════════════════════════════════════════════════════╗
║ GRAPH INFORMATION                                  ║
╠════════════════════════════════════════════════════╣
║ Nodes: 5                                           ║
║ Edges: 7                                           ║
║ Type: Undirected                                   ║
╚════════════════════════════════════════════════════╝
```

This shows:
- **Nodes**: Number of vertices in the graph
- **Edges**: Number of connections
- **Type**: Whether edges are directed or undirected

### Route Output
```
╔════════════════════════════════════════════════════╗
║ ROUTE FOUND                                        ║
╠════════════════════════════════════════════════════╣
║ Path: A → C → D → E                               ║
║ Distance: 12.0                                     ║
║ Hops: 4                                            ║
╚════════════════════════════════════════════════════╝
```

This shows:
- **Path**: The sequence of nodes to traverse
- **Distance**: Total cost/weight of the route
- **Hops**: Number of nodes in the path

## Sample Graph

The demo uses this graph:

```
    A ---4--- B
    |    \    |
    2 1   \1  5
    |      \ |
    C ---8--- D ---2--- E
     \      /
      -10--/
```

Edges:
- A → B: 4
- A → C: 2
- B → C: 1
- B → D: 5
- C → D: 8
- C → E: 10
- D → E: 2

### Example: Route from A to E

**Dijkstra's Algorithm** finds: A → C → D → E = 2 + 8 + 2 = 12.0

## Algorithm Comparison

### Dijkstra's Algorithm
- ✅ Guarantees shortest path
- ✅ Works with weighted graphs
- ⚠️ No negative weights allowed
- 📊 Time: O((V+E) log V)

### BFS (Breadth-First Search)
- ✅ Simple and fast
- ✅ Good for unweighted graphs
- ⚠️ Doesn't always find shortest path in weighted graphs
- 📊 Time: O(V+E)

### A* Algorithm
- ✅ Faster with good heuristic
- ✅ Flexible with heuristic functions
- ⚠️ Requires domain knowledge
- 📊 Time: O((V+E) log V)

## Running Tests

```bash
sbt test
```

This runs all unit tests covering:
- Graph operations
- Algorithm correctness
- Error handling
- CSV parsing

## Next Steps

1. **Explore the code**: Check out `src/main/scala/` to understand the implementation
2. **Run the tests**: Execute `sbt test` to see the test suite
3. **Create your own graph**: Modify the demo to use different nodes/edges
4. **Implement a custom algorithm**: Add a new PathFinder implementation
5. **Read the README**: Check README.md for detailed documentation

## Troubleshooting

### Issue: "sbt not found"
**Solution**: Install SBT from https://www.scala-sbt.org/download.html

### Issue: Compilation errors
**Solution**: Ensure you're using Scala 3.3.7 and Java 11+

### Issue: No path found
**Cause**: The start and end nodes may not be connected
**Solution**: Check that your graph has a path connecting them

## API Examples

### Creating a Graph Programmatically
```scala
val nodes = Set(Node("A"), Node("B"), Node("C"))
val edges = Set(
  Edge("A", "B", 4.0),
  Edge("B", "C", 2.0),
  Edge("A", "C", 5.0)
)
val graph = Graph(nodes, edges, directed = false)
```

### Finding a Path
```scala
RouteService.findRoute(graph, "A", "C", RouteService.Dijkstra) match {
  case Right(route) => println(route.nodes)
  case Left(error) => println(s"Error: $error")
}
```

### Using CSV Files
```scala
val graph = IOHandler.readGraphFromFile("graph_sample.csv")
val csv = IOHandler.graphToCSV(graph.right.get)
```

## Additional Resources

- **Dijkstra's Algorithm**: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
- **BFS**: https://en.wikipedia.org/wiki/Breadth-first_search
- **A* Algorithm**: https://en.wikipedia.org/wiki/A*_search_algorithm
- **Scala Documentation**: https://docs.scala-lang.org/
- **SBT Guide**: https://www.scala-sbt.org/

## Support

For issues or questions:
1. Check the README.md for detailed documentation
2. Review the test suite in `src/test/scala/` for examples
3. Examine the source code comments for implementation details

