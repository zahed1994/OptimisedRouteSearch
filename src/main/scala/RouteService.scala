package optimalroutefinder

/**
 * Service for route finding operations
 */
object RouteService {

  sealed trait AlgorithmType
  case object Dijkstra extends AlgorithmType
  case object BFS extends AlgorithmType
  case class AStar(heuristic: String => Double) extends AlgorithmType
  case object BidirectionalDijkstraV2 extends AlgorithmType

  /**
   * Find an optimal route using the specified algorithm
   * @param graph the graph to search
   * @param start starting node ID
   * @param end ending node ID
   * @param algorithm the algorithm to use
   * @return Either a Route if successful, or an error message
   */
  def findRoute(
    graph: Graph,
    start: String,
    end: String,
    algorithm: AlgorithmType = Dijkstra
  ): Either[String, Route] = {

    // Validate inputs
    if (!graph.containsNode(start)) {
      return Left(s"Start node '$start' not found in graph")
    }

    if (!graph.containsNode(end)) {
      return Left(s"End node '$end' not found in graph")
    }

    if (start == end) {
      return Right(Route(List(start), 0.0))
    }

    // Find path using selected algorithm
    val pathFinder: PathFinder = algorithm match {
      case Dijkstra => DijkstraAlgorithm
      case BFS => BFSAlgorithm
      case BidirectionalDijkstraV2 => optimalroutefinder.BidirectionalDijkstraV2
      case _ => DijkstraAlgorithm // Default
    }

    pathFinder.findPath(graph, start, end) match {
      case Some(route) => Right(route)
      case None => Left(s"No path found from '$start' to '$end'")
    }
  }

  /**
   * Find a route using A* algorithm
   * @param graph the graph to search
   * @param start starting node ID
   * @param end ending node ID
   * @param heuristic the heuristic function
   * @return Either a Route if successful, or an error message
   */
  def findRouteAStar(
    graph: Graph,
    start: String,
    end: String,
    heuristic: String => Double
  ): Either[String, Route] = {

    // Validate inputs
    if (!graph.containsNode(start)) {
      return Left(s"Start node '$start' not found in graph")
    }

    if (!graph.containsNode(end)) {
      return Left(s"End node '$end' not found in graph")
    }

    if (start == end) {
      return Right(Route(List(start), 0.0))
    }

    AStarAlgorithm.findPath(graph, start, end, heuristic) match {
      case Some(route) => Right(route)
      case None => Left(s"No path found from '$start' to '$end'")
    }
  }

  /**
   * Find all paths from a source to all other nodes
   * @param graph the graph to search
   * @param start starting node ID
   * @return Map of node IDs to their routes from start
   */
  def findAllPaths(graph: Graph, start: String): Either[String, Map[String, Option[Route]]] = {
    if (!graph.containsNode(start)) {
      return Left(s"Start node '$start' not found in graph")
    }

    val routes = scala.collection.mutable.Map[String, Option[Route]]()

    graph.nodes.foreach { node =>
      if (node.id == start) {
        routes.put(node.id, Some(Route(List(start), 0.0)))
      } else {
        routes.put(node.id, DijkstraAlgorithm.findPath(graph, start, node.id))
      }
    }

    Right(routes.toMap)
  }
}

