# Quick Reference Guide

## 📋 Essential Commands

### Build & Compile
```bash
cd C:\Users\asus\IdeaProjects\optimalroutefinder
sbt clean compile    # Clean and compile
sbt compile          # Just compile
sbt run demo         # Run demo mode
sbt test             # Run all tests
sbt package          # Create JAR
```

### CLI Commands
```bash
sbt run                    # Welcome + demo
sbt "run demo"            # Interactive demo
sbt "run help"            # Show help
sbt "run find A E"        # Find route A→E
```

---

## 🎯 Core Concepts

### Graph Creation
```scala
// Create nodes
val nodes = Set(Node("A"), Node("B"), Node("C"))

// Create edges
val edges = Set(
  Edge("A", "B", 4.0),
  Edge("B", "C", 2.0),
  Edge("A", "C", 5.0)
)

// Create graph (undirected)
val graph = Graph(nodes, edges, directed = false)
```

### Finding Routes
```scala
// Using Dijkstra
RouteService.findRoute(graph, "A", "C", RouteService.Dijkstra)
// Result: Either[String, Route]

// Using BFS
RouteService.findRoute(graph, "A", "C", RouteService.BFS)

// Using A*
RouteService.findRouteAStar(graph, "A", "C", Config.Heuristics.euclidean)

// All paths from A
RouteService.findAllPaths(graph, "A")
```

### Handling Results
```scala
RouteService.findRoute(graph, start, end, algo) match {
  case Right(route) => 
    println(s"Path: ${route.nodes}")
    println(s"Distance: ${route.totalDistance}")
  case Left(error) => 
    println(s"Error: $error")
}
```

---

## 📊 Algorithm Quick Reference

| Algorithm | Time | Space | Use Case |
|-----------|------|-------|----------|
| **Dijkstra** | O((V+E) log V) | O(V) | Weighted graphs |
| **BFS** | O(V+E) | O(V) | Unweighted |
| **A*** | O((V+E) log V) | O(V) | With heuristic |

---

## 📁 File Organization

```
src/main/scala/
├── Graph.scala        → Data structures
├── Algorithms.scala   → Pathfinding algorithms
├── RouteService.scala → Service layer
├── IOHandler.scala    → I/O operations
├── Config.scala       → Utilities
├── main.scala         → CLI interface
└── package.scala      → Exports

src/test/scala/
└── RouteFinderTests.scala → Unit tests
```

---

## 📖 Documentation Map

| Need | File | Section |
|------|------|---------|
| **Start Here** | START_HERE.md | Quick overview |
| **Quick Start** | GETTING_STARTED.md | Build & run |
| **Full Docs** | README.md | Complete reference |
| **Architecture** | ARCHITECTURE.md | System design |
| **Implementation** | IMPLEMENTATION_SUMMARY.md | Features |
| **Files** | PROJECT_FILE_STRUCTURE.txt | Directory listing |
| **Checklist** | PROJECT_CHECKLIST.md | Completion status |

---

## 🛠️ Common Tasks

### Create a Simple Graph
```scala
val nodes = Set(Node("A"), Node("B"), Node("C"))
val edges = Set(Edge("A", "B", 1.0), Edge("B", "C", 2.0))
val graph = Graph(nodes, edges)
```

### Find Shortest Path
```scala
val route = RouteService.findRoute(graph, "A", "C", RouteService.Dijkstra)
route match {
  case Right(r) => IOHandler.displayRoute(r)
  case Left(e) => IOHandler.displayError(e)
}
```

### Load from CSV
```scala
val graph = IOHandler.readGraphFromFile("graph_sample.csv")
```

### Export to CSV
```scala
val csv = IOHandler.graphToCSV(graph.right.get)
println(csv)
```

### Use Different Heuristics
```scala
RouteService.findRouteAStar(graph, start, end, Config.Heuristics.euclidean)
RouteService.findRouteAStar(graph, start, end, Config.Heuristics.manhattan)
RouteService.findRouteAStar(graph, start, end, Config.Heuristics.chebyshev)
RouteService.findRouteAStar(graph, start, end, Config.Heuristics.none)
```

### Use Pre-built Templates
```scala
val linear = Config.Templates.linearGraph()
val grid = Config.Templates.gridGraph()
val complete = Config.Templates.completeGraph(5)
val cities = Config.Templates.cityNetworkGraph()
```

---

## 🧪 Testing

### Run All Tests
```bash
sbt test
```

### Test Categories
- Graph operations
- Algorithm correctness
- Error handling
- CSV operations
- Route service

### Run Specific Test
```bash
sbt "test-only RouteFinderTests"
```

---

## ⚙️ Configuration

### build.sbt Essentials
```scala
ThisBuild / scalaVersion := "3.3.7"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % Test
```

### Dependencies
- Scala: 3.3.7
- ScalaTest: 3.2.18 (for testing)

---

## 📊 Performance Tips

1. **For large graphs**: Use Dijkstra
2. **For unweighted**: Use BFS (faster)
3. **With domain knowledge**: Use A* with good heuristic
4. **CSV files**: Use IOHandler for efficient parsing

---

## 🐛 Troubleshooting

### "sbt not found"
→ Install SBT from https://www.scala-sbt.org/download.html

### "Compilation errors"
→ Ensure Scala 3.3.7 and Java 11+ installed

### "No path found"
→ Check nodes exist and graph is connected

### "CSV parse error"
→ Verify format: `nodeId1,nodeId2,weight`

---

## 🔑 Key Classes

```scala
case class Node(id: String, label: Option[String] = None)
case class Edge(from: String, to: String, weight: Double)
case class Route(nodes: List[String], totalDistance: Double)
case class Graph(nodes: Set[Node], edges: Set[Edge], directed: Boolean = true)

trait PathFinder {
  def findPath(graph: Graph, start: String, end: String): Option[Route]
}

object DijkstraAlgorithm extends PathFinder
object BFSAlgorithm extends PathFinder
object AStarAlgorithm  // Special: uses heuristic function
object RouteService    // Main API
object IOHandler       // CSV and display
object Config          // Utilities and templates
```

---

## 🎓 Learning Path

1. **Read** START_HERE.md (this overview)
2. **Run** `sbt "run demo"` (see it in action)
3. **Review** README.md (full documentation)
4. **Explore** src/main/scala/ (implementation)
5. **Study** ARCHITECTURE.md (system design)
6. **Experiment** with your own graphs

---

## 💡 Examples

### Find Route from A to E
```bash
sbt "run find A E"
```

### Programmatic Example
```scala
import optimalroutefinder.*

val nodes = Set(Node("A"), Node("B"), Node("C"))
val edges = Set(Edge("A", "B", 1.0), Edge("B", "C", 2.0))
val graph = Graph(nodes, edges)

RouteService.findRoute(graph, "A", "C", RouteService.Dijkstra) match {
  case Right(route) => println(route.nodes.mkString(" → "))
  case Left(error) => println(s"Error: $error")
}
```

### With Heuristic
```scala
val route = RouteService.findRouteAStar(
  graph, 
  "A", 
  "C", 
  Config.Heuristics.manhattan
)
```

---

## 📚 Additional Resources

- **Dijkstra**: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
- **BFS**: https://en.wikipedia.org/wiki/Breadth-first_search
- **A***: https://en.wikipedia.org/wiki/A*_search_algorithm
- **Scala Docs**: https://docs.scala-lang.org/
- **SBT Guide**: https://www.scala-sbt.org/

---

## 🎯 Project Status

✅ Implementation Complete
✅ Testing Complete
✅ Documentation Complete
✅ Ready to Use

---

**Quick Links:**
- [START_HERE.md](START_HERE.md) - Overview
- [GETTING_STARTED.md](GETTING_STARTED.md) - Setup guide
- [README.md](README.md) - Full documentation
- [ARCHITECTURE.md](ARCHITECTURE.md) - System design

Happy pathfinding! 🗺️

