package optimalroutefinder

import scala.math.Ordering
import scala.collection.mutable

/**
 * Trait for pathfinding algorithms
 */
trait PathFinder {
  /**
   * Find the shortest path between two nodes
   * @param graph the graph to search
   * @param start the starting node ID
   * @param end the ending node ID
   * @return Option containing the Route if a path exists, None otherwise
   */
  def findPath(graph: Graph, start: String, end: String): Option[Route]
}

/**
 * Dijkstra's algorithm for finding shortest paths
 */
object DijkstraAlgorithm extends PathFinder {

  def findPath(graph: Graph, start: String, end: String): Option[Route] = {
    // Validate nodes exist
    if (!graph.containsNode(start) || !graph.containsNode(end)) {
      return None
    }

    // Initialize distances and previous nodes
    val distances = scala.collection.mutable.Map[String, Double]()
    val previous = scala.collection.mutable.Map[String, Option[String]]()
    val visited = scala.collection.mutable.Set[String]()

    graph.nodes.foreach { node =>
      distances.put(node.id, Double.PositiveInfinity)
      previous.put(node.id, None)
    }

    distances.put(start, 0.0)

    // Priority queue: (distance, nodeId)
    implicit val ordering: Ordering[(Double, String)] =
      Ordering.by[(Double, String), Double](_._1).reverse
    val pq = mutable.PriorityQueue[(Double, String)]()
    pq.enqueue((0.0, start))

    while (pq.nonEmpty) {
      val (currentDist, currentNode) = pq.dequeue()

      if (!visited.contains(currentNode)) {
        visited.add(currentNode)

        if (currentNode == end) {
          // Reconstruct path
          return Some(reconstructPath(end, previous, distances))
        }

        // Relax edges
        graph.getNeighbors(currentNode).foreach { edge =>
          val neighbor = edge.to
          val newDist = currentDist + edge.weight

          if (newDist < distances(neighbor)) {
            distances.put(neighbor, newDist)
            previous.put(neighbor, Some(currentNode))
            pq.enqueue((newDist, neighbor))
          }
        }
      }
    }

    None // No path found
  }

  private def reconstructPath(
    end: String,
    previous: scala.collection.mutable.Map[String, Option[String]],
    distances: scala.collection.mutable.Map[String, Double]
  ): Route = {
    var path = List[String]()
    var current: Option[String] = Some(end)

    while (current.nonEmpty) {
      path = current.get :: path
      current match {
        case Some(node) => current = previous.getOrElse(node, None)
        case None => current = None
      }
    }

    Route(path, distances(end))
  }
}

/**
 * Breadth-First Search algorithm
 * Works best for unweighted graphs or graphs with uniform edge weights
 */
object BFSAlgorithm extends PathFinder {

  def findPath(graph: Graph, start: String, end: String): Option[Route] = {
    // Validate nodes exist
    if (!graph.containsNode(start) || !graph.containsNode(end)) {
      return None
    }

    val visited = scala.collection.mutable.Set[String]()
    val queue = scala.collection.mutable.Queue[(String, List[String], Double)]()

    queue.enqueue((start, List(start), 0.0))
    visited.add(start)

    while (queue.nonEmpty) {
      val (currentNode, path, distance) = queue.dequeue()

      if (currentNode == end) {
        return Some(Route(path, distance))
      }

      graph.getNeighbors(currentNode).foreach { edge =>
        val neighbor = edge.to
        if (!visited.contains(neighbor)) {
          visited.add(neighbor)
          queue.enqueue((neighbor, path :+ neighbor, distance + edge.weight))
        }
      }
    }

    None // No path found
  }
}

/**
 * A* algorithm for pathfinding with heuristic
 * Requires a heuristic function
 */
object AStarAlgorithm {

  /**
   * Find path using A* algorithm
   * @param graph the graph to search
   * @param start starting node ID
   * @param end ending node ID
   * @param heuristic function that estimates distance from a node to the goal
   */
  def findPath(
    graph: Graph,
    start: String,
    end: String,
    heuristic: String => Double
  ): Option[Route] = {

    // Validate nodes exist
    if (!graph.containsNode(start) || !graph.containsNode(end)) {
      return None
    }

    val gScore = scala.collection.mutable.Map[String, Double]()
    val fScore = scala.collection.mutable.Map[String, Double]()
    val previous = scala.collection.mutable.Map[String, Option[String]]()
    val openSet = scala.collection.mutable.Set[String]()
    val closedSet = scala.collection.mutable.Set[String]()

    graph.nodes.foreach { node =>
      gScore.put(node.id, Double.PositiveInfinity)
      fScore.put(node.id, Double.PositiveInfinity)
      previous.put(node.id, None)
    }

    gScore.put(start, 0.0)
    fScore.put(start, heuristic(start))
    openSet.add(start)

    while (openSet.nonEmpty) {
      // Find node in openSet with lowest fScore
      val current = openSet.minBy(fScore(_))

      if (current == end) {
        return Some(reconstructPath(end, previous, gScore))
      }

      openSet.remove(current)
      closedSet.add(current)

      graph.getNeighbors(current).foreach { edge =>
        val neighbor = edge.to
        if (!closedSet.contains(neighbor)) {
          val tentativeGScore = gScore(current) + edge.weight

          if (tentativeGScore < gScore(neighbor)) {
            previous.put(neighbor, Some(current))
            gScore.put(neighbor, tentativeGScore)
            fScore.put(neighbor, gScore(neighbor) + heuristic(neighbor))

            if (!openSet.contains(neighbor)) {
              openSet.add(neighbor)
            }
          }
        }
      }
    }

    None // No path found
  }

  private def reconstructPath(
    end: String,
    previous: scala.collection.mutable.Map[String, Option[String]],
    gScore: scala.collection.mutable.Map[String, Double]
  ): Route = {
    var path = List[String]()
    var current: Option[String] = Some(end)

    while (current.nonEmpty) {
      path = current.get :: path
      current match {
        case Some(node) => current = previous.getOrElse(node, None)
        case None => current = None
      }
    }

    Route(path, gScore(end))
  }
}

