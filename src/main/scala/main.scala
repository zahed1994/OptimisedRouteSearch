
import optimalroutefinder.*

@main
def main(args: String*): Unit =
  if (args.isEmpty) {
    displayWelcome()
    demonstrateUsage()
  } else {
    args.head match {
      case "demo" => demonstrateUsage()
      case "help" => displayHelp()
      case "find" if args.length >= 3 =>
        val start = args(1)
        val end = args(2)
        findRouteFromArgs(start, end)
      case _ => displayHelp()
    }
  }

def displayWelcome(): Unit =
  println("""
    |╔════════════════════════════════════════════════════╗
    |║     OPTIMAL ROUTE FINDER - Scala Edition          ║
    |║                                                    ║
    |║     Find shortest paths using various algorithms  ║
    |╚════════════════════════════════════════════════════╝
    |""".stripMargin)

def displayHelp(): Unit =
  println("""
    |Usage:
    |  scala main.scala                    - Shows welcome and demo
    |  scala main.scala demo               - Run interactive demo
    |  scala main.scala help               - Show this help message
    |  scala main.scala find <start> <end> - Find route from start to end
    |
    |Example:
    |  scala main.scala find A D
    |""".stripMargin)

def demonstrateUsage(): Unit =
  println("\n📊 Creating a sample graph...")

  // Create sample graph
  val nodes = Set(
    Node("A"),
    Node("B"),
    Node("C"),
    Node("D"),
    Node("E")
  )

  val edges = Set(
    Edge("A", "B", 4.0),
    Edge("A", "C", 2.0),
    Edge("B", "C", 1.0),
    Edge("B", "D", 5.0),
    Edge("C", "D", 8.0),
    Edge("C", "E", 10.0),
    Edge("D", "E", 2.0)
  )

  val graph = Graph(nodes, edges, directed = false)

  IOHandler.displayGraph(graph)

  println("\n🔍 Finding routes using different algorithms...\n")

  // Test Dijkstra
  println("1️⃣  Dijkstra's Algorithm:")
  RouteService.findRoute(graph, "A", "E", RouteService.Dijkstra) match {
    case Right(route) => IOHandler.displayRoute(route)
    case Left(error) => IOHandler.displayError(error)
  }

  // Test BFS
  println("\n2️⃣  Breadth-First Search:")
  RouteService.findRoute(graph, "A", "E", RouteService.BFS) match {
    case Right(route) => IOHandler.displayRoute(route)
    case Left(error) => IOHandler.displayError(error)
  }

  // Test A*
  println("\n3️⃣  A* Algorithm (with simple heuristic):")
  val heuristic: String => Double = _ => 0.0 // Simple heuristic for demo
  RouteService.findRouteAStar(graph, "A", "E", heuristic) match {
    case Right(route) => IOHandler.displayRoute(route)
    case Left(error) => IOHandler.displayError(error)
  }

  // Test V2 Bidirectional Dijkstra
  println("\n4️⃣  V2 Optimized - Bidirectional Dijkstra with Pruning:")
  RouteService.findRoute(graph, "A", "E", RouteService.BidirectionalDijkstraV2) match {
    case Right(route) => IOHandler.displayRoute(route)
    case Left(error) => IOHandler.displayError(error)
  }

  // Test all paths from a node
  println("\n5️⃣  All paths from node 'A':")
  RouteService.findAllPaths(graph, "A") match {
    case Right(routes) =>
      routes.foreach { case (nodeId, routeOpt) =>
        routeOpt match {
          case Some(route) => println(s"   A → $nodeId: ${IOHandler.routeToString(route)}")
          case None => println(s"   A → $nodeId: No path found")
        }
      }
    case Left(error) => IOHandler.displayError(error)
  }

  // Display graph as CSV
  println("\n6️⃣  Graph in CSV format:")
  println(IOHandler.graphToCSV(graph))

def findRouteFromArgs(start: String, end: String): Unit =
  // Create sample graph (in production, would read from file)
  val nodes = Set(
    Node("A"), Node("B"), Node("C"), Node("D"), Node("E")
  )

  val edges = Set(
    Edge("A", "B", 4.0), Edge("A", "C", 2.0), Edge("B", "C", 1.0),
    Edge("B", "D", 5.0), Edge("C", "D", 8.0), Edge("C", "E", 10.0),
    Edge("D", "E", 2.0)
  )

  val graph = Graph(nodes, edges, directed = false)

  RouteService.findRoute(graph, start, end, RouteService.Dijkstra) match {
    case Right(route) => IOHandler.displayRoute(route)
    case Left(error) => IOHandler.displayError(error)
  }


