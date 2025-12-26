# Architecture and Design

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     CLI Interface (main.scala)              │
│                                                             │
│  Commands: demo | help | find <start> <end>               │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│               Route Service Layer (RouteService.scala)      │
│                                                             │
│  • findRoute()        - Main API for pathfinding           │
│  • findRouteAStar()   - A* specific method                 │
│  • findAllPaths()     - Multi-destination routes           │
└────────┬────────────────────────────────────────────────────┘
         │
    ┌────┴────┬──────────────┬─────────────────┐
    │          │              │                 │
    ▼          ▼              ▼                 ▼
┌────────┐ ┌────────┐  ┌────────┐  ┌──────────────┐
│Dijkstra│ │  BFS   │  │  A*    │  │ I/O Handler  │
│Algorithm│ │Algorithm│  │Algorithm│  │(IOHandler)  │
└────────┘ └────────┘  └────────┘  └──────────────┘
    │          │              │                 │
    └──────────┴──────────────┴─────────────────┘
                  │
                  ▼
    ┌──────────────────────────────────┐
    │   Data Structures (Graph.scala)  │
    │                                  │
    │  • Node                          │
    │  • Edge                          │
    │  • Route                         │
    │  • Graph (adjacency list)        │
    └──────────────────────────────────┘
```

## Component Responsibilities

### CLI Layer (main.scala)
- Parse command-line arguments
- Display welcome messages
- Handle demo mode
- Route user commands to appropriate handlers

### Route Service (RouteService.scala)
- High-level API for route finding
- Algorithm selection and invocation
- Input validation
- Error handling and reporting
- Caching (future enhancement)

### Algorithms (Algorithms.scala)
- **PathFinder Trait**: Common interface for all algorithms
- **DijkstraAlgorithm**: Optimal pathfinding with priority queue
- **BFSAlgorithm**: Breadth-first traversal
- **AStarAlgorithm**: Heuristic-based search

### Data Structures (Graph.scala)
- **Node**: Vertex representation
- **Edge**: Weighted connections
- **Route**: Path and distance
- **Graph**: Adjacency list implementation

### I/O Handler (IOHandler.scala)
- File operations
- CSV parsing and serialization
- Formatted output
- Error messages

### Configuration (Config.scala)
- Heuristic functions library
- Graph templates
- Helper utilities

## Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│ User Input (CLI or Programmatic)                            │
│ - Graph definition                                          │
│ - Start and end nodes                                       │
│ - Algorithm selection (optional heuristic)                  │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
        ┌────────────────────────────┐
        │  Input Validation          │
        │ • Node existence check     │
        │ • Consistency check        │
        └────────────────┬───────────┘
                         │
                    ┌────┴─────┐
                    │           │
            ✓ (Valid)        ✗ (Invalid)
                    │           │
                    ▼           ▼
        ┌─────────────────┐  ┌─────────────┐
        │ Algorithm       │  │ Error       │
        │ Execution       │  │ Message     │
        └────────┬────────┘  └─────────────┘
                 │                  │
                 ▼                  ▼
    ┌──────────────────────┐  ┌─────────────────────┐
    │ Path Found?          │  │ Return Left(error)  │
    └────┬─────────────────┘  └─────────────────────┘
         │
    ┌────┴────┐
    │          │
  Yes        No
    │          │
    ▼          ▼
┌────────┐ ┌───────────┐
│Route   │ │Left(error)│
│(Right) │ │           │
└────┬───┘ └───────────┘
     │
     ▼
┌────────────────────────────────────┐
│ Format and Display Output          │
│ - Path nodes                       │
│ - Total distance                   │
│ - Number of hops                   │
└────────────────────────────────────┘
```

## Algorithm Comparison Matrix

```
┌──────────────────────────────────────────────────────────────┐
│                  Algorithm Comparison                        │
├──────────────────────────────────────────────────────────────┤
│ Aspect          │ Dijkstra  │ BFS       │ A*               │
├──────────────────────────────────────────────────────────────┤
│ Best For        │ Weighted  │ Unweight  │ Guided           │
│                 │ graphs    │ ed graphs │ search           │
├──────────────────────────────────────────────────────────────┤
│ Time Complexity │ O((V+E)   │ O(V+E)    │ O((V+E)          │
│                 │ log V)    │           │ log V)           │
├──────────────────────────────────────────────────────────────┤
│ Space          │ O(V)      │ O(V)      │ O(V)             │
│ Complexity     │           │           │                  │
├──────────────────────────────────────────────────────────────┤
│ Guaranteed     │ Yes       │ In        │ Yes (with        │
│ Shortest       │ (non-neg) │ unweight  │ admissible       │
│                │           │ graphs    │ heuristic)       │
├──────────────────────────────────────────────────────────────┤
│ Requires       │ No        │ No        │ Yes              │
│ Heuristic      │           │           │                  │
├──────────────────────────────────────────────────────────────┤
│ Supports       │ Yes       │ Yes       │ Yes              │
│ Negative Wts   │ No        │ N/A       │ No               │
└──────────────────────────────────────────────────────────────┘
```

## Class Hierarchy

```
┌──────────────────────────────────────────────────────────────┐
│ Data Classes                                                 │
├──────────────────────────────────────────────────────────────┤
│ Node(id: String, label: Option[String])                     │
│   - Represents a vertex in the graph                         │
│                                                              │
│ Edge(from: String, to: String, weight: Double)              │
│   - Represents a weighted directed edge                      │
│                                                              │
│ Route(nodes: List[String], totalDistance: Double)           │
│   - Represents a path through the graph                      │
│                                                              │
│ Graph(nodes: Set[Node], edges: Set[Edge],                   │
│        directed: Boolean)                                    │
│   - Main graph structure with adjacency list                │
│   - Methods: getNeighbors(), getNode(), etc.               │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│ Algorithm Trait & Implementations                           │
├──────────────────────────────────────────────────────────────┤
│ trait PathFinder                                             │
│   - findPath(graph, start, end): Option[Route]             │
│                                                              │
│ object DijkstraAlgorithm extends PathFinder                 │
│ object BFSAlgorithm extends PathFinder                      │
│ object AStarAlgorithm                                        │
│   - findPath(graph, start, end, heuristic): Option[Route] │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│ Service & Utility Objects                                    │
├──────────────────────────────────────────────────────────────┤
│ object RouteService                                          │
│   - findRoute(graph, start, end, algorithm)                │
│   - findRouteAStar(graph, start, end, heuristic)           │
│   - findAllPaths(graph, start)                             │
│                                                              │
│ object IOHandler                                             │
│   - parseGraphFromCSV()                                      │
│   - readGraphFromFile()                                      │
│   - graphToCSV()                                             │
│   - displayRoute()                                           │
│                                                              │
│ object Config                                                │
│   - Heuristics: euclidean, manhattan, chebyshev            │
│   - Templates: linearGraph, gridGraph, etc.               │
│                                                              │
│ object Helpers                                               │
│   - isValidRoute()                                           │
│   - calculateRouteDistance()                                 │
│   - findShortestEdge()                                       │
└──────────────────────────────────────────────────────────────┘
```

## Error Handling Strategy

```
┌───────────────────────────────┐
│ Operation Request             │
└───────────────┬───────────────┘
                │
                ▼
    ┌───────────────────────────┐
    │ Input Validation          │
    └───┬───────────────────┬───┘
        │                   │
        ▼                   ▼
    ✓ Valid            ✗ Invalid
        │                   │
        │                   ▼
        │           ┌─────────────────────┐
        │           │ Left(error_message) │
        │           └─────────────────────┘
        │                   │
        ▼                   │
    ┌──────────────┐        │
    │ Execute      │        │
    │ Algorithm    │        │
    └──┬─────────┬─┘        │
       │         │          │
    Path Found  No Path     │
       │         │          │
       ▼         ▼          │
   ┌────────┐ ┌──────────┐  │
   │Right   │ │Left(no   │  │
   │(route) │ │path err) │  │
   └────────┘ └──────────┘  │
       │         │          │
       └────┬────┴──────────┘
            │
            ▼
    ┌─────────────────────────┐
    │ Return Either Result    │
    │ to Caller               │
    └─────────────────────────┘
```

## Dependency Graph

```
main.scala
    │
    ├─→ RouteService
    │        │
    │        ├─→ Graph
    │        ├─→ Route
    │        ├─→ DijkstraAlgorithm
    │        ├─→ BFSAlgorithm
    │        └─→ AStarAlgorithm
    │
    ├─→ IOHandler
    │        │
    │        ├─→ Graph
    │        ├─→ Route
    │        └─→ Edge
    │
    ├─→ Node
    ├─→ Edge
    └─→ Graph

Tests (RouteFinderTests.scala)
    │
    ├─→ All core classes and objects
    ├─→ RouteService
    ├─→ All algorithms
    ├─→ IOHandler
    └─→ Graph operations
```

## Design Patterns Used

1. **Singleton Pattern**: Algorithm objects (DijkstraAlgorithm, BFSAlgorithm)
2. **Factory Pattern**: Graph creation, Node/Edge factory methods
3. **Strategy Pattern**: PathFinder trait with multiple implementations
4. **Either Monad Pattern**: Functional error handling
5. **Builder Pattern**: RouteService provides fluent interface
6. **Template Method Pattern**: Base algorithm structure in PathFinder

## Performance Characteristics

```
Operation                    Time Complexity    Space Complexity
─────────────────────────────────────────────────────────────────
Create Graph                 O(V + E)          O(V + E)
Get Neighbors                O(1) avg          O(1)
Dijkstra                     O((V+E) log V)    O(V)
BFS                          O(V + E)          O(V)
A*                           O((V+E) log V)    O(V)
Find All Paths               O(V * E log V)    O(V²)
CSV Parse                    O(E)              O(V + E)
─────────────────────────────────────────────────────────────────
```

## Extensibility Points

1. **Add New Algorithm**: Implement PathFinder trait
2. **Add Heuristics**: Add to Config.Heuristics object
3. **Graph Templates**: Add to Config.Templates object
4. **Helper Functions**: Add to Helpers object
5. **I/O Formats**: Extend IOHandler with JSON/XML support
6. **Edge Weights**: Add constraints/validation

---

This architecture provides a clean, modular, and extensible foundation for the Optimal Route Finder application.

