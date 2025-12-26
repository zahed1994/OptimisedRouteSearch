# Optimal Route Finder - Scala Edition

A powerful Scala application for finding optimal routes through graphs using multiple pathfinding algorithms.

## Features

- **Multiple Pathfinding Algorithms**:
  - **Dijkstra's Algorithm**: Finds the shortest path in weighted graphs
  - **Breadth-First Search (BFS)**: Efficient for unweighted or uniform-weight graphs
  - **A* Algorithm**: Heuristic-based pathfinding for optimal route discovery

- **Graph Representation**:
  - Directed and undirected graph support
  - Weighted edges with non-negative weights
  - Efficient adjacency list representation

- **Features**:
  - Find shortest paths between two nodes
  - Compute all paths from a source node
  - CSV import/export for graphs and routes
  - Clean CLI interface
  - Comprehensive error handling

## Project Structure

```
optimalroutefinder/
├── src/
│   ├── main/scala/
│   │   ├── Graph.scala           # Core data structures (Node, Edge, Graph, Route)
│   │   ├── Algorithms.scala      # Pathfinding implementations (Dijkstra, BFS, A*)
│   │   ├── RouteService.scala    # Service layer for route finding
│   │   ├── IOHandler.scala       # Input/output handling
│   │   └── main.scala            # CLI entry point
│   └── test/scala/
│       └── RouteFinderTests.scala # Unit tests
├── build.sbt                      # SBT build configuration
└── README.md                      # This file
```

## Core Components

### Graph.scala
Defines the fundamental data structures:
- **Node**: Represents a vertex in the graph
- **Edge**: Represents a weighted connection between two nodes
- **Route**: Represents a path and its total distance
- **Graph**: The main graph structure with nodes and edges

### Algorithms.scala
Implements three pathfinding algorithms:
- **DijkstraAlgorithm**: Uses priority queue for optimal pathfinding
- **BFSAlgorithm**: Simple breadth-first traversal
- **AStarAlgorithm**: Uses heuristic function for guided search

### RouteService.scala
High-level service providing:
- Unified interface for all algorithms
- Route finding with error handling
- All-paths computation from source node

### IOHandler.scala
Handles:
- CSV parsing and exporting
- File I/O operations
- Formatted output display
- Error message formatting

### main.scala
CLI interface with commands:
- `scala main.scala` - Welcome and demo
- `scala main.scala demo` - Interactive demonstration
- `scala main.scala help` - Show help
- `scala main.scala find <start> <end>` - Find route

## Usage Examples

### Running the Demo
```bash
scala main.scala demo
```

### Finding a Route
```bash
scala main.scala find A E
```

### Programmatic Usage
```scala
import optimalroutefinder.*

// Create a graph
val nodes = Set(Node("A"), Node("B"), Node("C"))
val edges = Set(
  Edge("A", "B", 4.0),
  Edge("B", "C", 2.0),
  Edge("A", "C", 5.0)
)
val graph = Graph(nodes, edges, directed = false)

// Find shortest path using Dijkstra
val result = RouteService.findRoute(graph, "A", "C", RouteService.Dijkstra)
result match {
  case Right(route) => println(s"Found route: ${route.nodes} with distance ${route.totalDistance}")
  case Left(error) => println(s"Error: $error")
}

// Find all paths from a node
val allPaths = RouteService.findAllPaths(graph, "A")
allPaths match {
  case Right(routes) => routes.foreach { case (nodeId, routeOpt) =>
    routeOpt match {
      case Some(route) => println(s"A → $nodeId: ${IOHandler.routeToString(route)}")
      case None => println(s"A → $nodeId: No path")
    }
  }
  case Left(error) => println(s"Error: $error")
}
```

## CSV Format

### Graph CSV Format
```
nodeId1,nodeId2,weight
A,B,4.0
B,C,2.0
A,C,5.0
```

### Route CSV Format
```
nodeId1,nodeId2,nodeId3,...,totalDistance
A,B,C,6.0
```

## Algorithm Complexity

### Dijkstra's Algorithm
- **Time Complexity**: O((V + E) log V) with binary heap
- **Space Complexity**: O(V)
- **Best for**: Dense graphs with non-negative weights

### Breadth-First Search
- **Time Complexity**: O(V + E)
- **Space Complexity**: O(V)
- **Best for**: Unweighted graphs or uniform weights

### A* Algorithm
- **Time Complexity**: O((V + E) log V) with good heuristic
- **Space Complexity**: O(V)
- **Best for**: Guided search with domain knowledge

## Testing

Run the test suite with:
```bash
sbt test
```

Test coverage includes:
- Graph creation and operations
- Algorithm correctness
- Edge cases and error handling
- CSV parsing and serialization
- Route service operations

## Building and Installation

### Prerequisites
- Scala 3.3.7
- SBT (Scala Build Tool)
- Java 11 or later

### Build
```bash
sbt clean compile
```

### Run
```bash
sbt "run demo"
sbt "run find A D"
```

### Package
```bash
sbt package
```

## Architecture Principles

1. **Separation of Concerns**: Clear division between algorithms, data structures, and I/O
2. **Extensibility**: Easy to add new algorithms by implementing the PathFinder trait
3. **Error Handling**: Comprehensive Either[Error, Result] pattern
4. **Testability**: Modular design with comprehensive test coverage
5. **Performance**: Efficient data structures and algorithms for various graph sizes

## Future Enhancements

- [ ] Interactive graph builder CLI
- [ ] Bidirectional search
- [ ] Floyd-Warshall all-pairs shortest path
- [ ] Graph visualization
- [ ] JSON support in addition to CSV
- [ ] Performance benchmarking suite
- [ ] Web API interface
- [ ] Distance matrix computation
- [ ] Cycle detection
- [ ] Connected components analysis

## License

MIT License

## Contributors

- Optimal Route Finder Development Team

