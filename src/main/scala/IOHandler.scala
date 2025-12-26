package optimalroutefinder

import scala.io.Source
import scala.util.{Try, Using}

/**
 * Handles input and output operations for graphs and routes
 */
object IOHandler {

  /**
   * Parse a graph from CSV format
   * Format: edges are comma-separated: nodeId1,nodeId2,weight
   * Nodes are created automatically from edges
   */
  def parseGraphFromCSV(content: String, directed: Boolean = true): Either[String, Graph] = {
    try {
      val lines = content.strip().split("\n").filter(_.nonEmpty)

      if (lines.isEmpty) {
        return Left("Empty graph definition")
      }

      val edges = scala.collection.mutable.Set[Edge]()
      val nodeIds = scala.collection.mutable.Set[String]()

      lines.foreach { line =>
        val parts = line.split(",").map(_.trim)

        if (parts.length != 3) {
          throw new IllegalArgumentException(s"Invalid edge format: $line (expected: nodeId1,nodeId2,weight)")
        }

        val from = parts(0)
        val to = parts(1)
        val weight = Try(parts(2).toDouble).getOrElse(
          throw new IllegalArgumentException(s"Invalid weight: ${parts(2)}")
        )

        edges.add(Edge(from, to, weight))
        nodeIds.add(from)
        nodeIds.add(to)
      }

      val nodes = nodeIds.map(id => Node(id, None)).toSet
      Right(Graph(nodes, edges.toSet, directed))

    } catch {
      case e: Exception => Left(s"Error parsing graph: ${e.getMessage}")
    }
  }

  /**
   * Read graph from a CSV file
   */
  def readGraphFromFile(filePath: String, directed: Boolean = true): Either[String, Graph] = {
    try {
      Using(Source.fromFile(filePath)) { source =>
        val content = source.mkString
        parseGraphFromCSV(content, directed)
      }.get
    } catch {
      case e: Exception => Left(s"Error reading file: ${e.getMessage}")
    }
  }

  /**
   * Export graph to CSV format
   */
  def graphToCSV(graph: Graph): String = {
    graph.edges
      .map(edge => s"${edge.from},${edge.to},${edge.weight}")
      .toList
      .sorted
      .mkString("\n")
  }

  /**
   * Export route to human-readable format
   */
  def routeToString(route: Route): String = {
    s"${route.nodes.mkString(" → ")} (Total Distance: ${route.totalDistance})"
  }

  /**
   * Export route to CSV format
   */
  def routeToCSV(route: Route): String = {
    val pathCSV = route.nodes.mkString(",")
    s"$pathCSV,${route.totalDistance}"
  }

  /**
   * Display route information
   */
  def displayRoute(route: Route): Unit = {
    println(s"╔════════════════════════════════════════════════════╗")
    println(s"║ ROUTE FOUND                                        ║")
    println(s"╠════════════════════════════════════════════════════╣")
    println(s"║ Path: ${padRight(route.nodes.mkString(" → "), 44)} ║")
    println(s"║ Distance: ${padRight(route.totalDistance.toString, 38)} ║")
    println(s"║ Hops: ${padRight(route.nodes.length.toString, 42)} ║")
    println(s"╚════════════════════════════════════════════════════╝")
  }

  /**
   * Display graph information
   */
  def displayGraph(graph: Graph): Unit = {
    println(s"╔════════════════════════════════════════════════════╗")
    println(s"║ GRAPH INFORMATION                                  ║")
    println(s"╠════════════════════════════════════════════════════╣")
    println(s"║ Nodes: ${padRight(graph.nodes.size.toString, 41)} ║")
    println(s"║ Edges: ${padRight(graph.edges.size.toString, 41)} ║")
    println(s"║ Type: ${padRight(if (graph.directed) "Directed" else "Undirected", 41)} ║")
    println(s"╚════════════════════════════════════════════════════╝")
  }

  /**
   * Display error message
   */
  def displayError(message: String): Unit = {
    println(s"╔════════════════════════════════════════════════════╗")
    println(s"║ ERROR                                              ║")
    println(s"╠════════════════════════════════════════════════════╣")
    println(s"║ ${padRight(message, 46)} ║")
    println(s"╚════════════════════════════════════════════════════╝")
  }

  private def padRight(str: String, width: Int): String = {
    str + (" " * (width - str.length)).take(width - str.length)
  }
}

