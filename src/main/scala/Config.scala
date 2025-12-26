package optimalroutefinder

/**
 * Configuration and utility objects for the Optimal Route Finder
 */
object Config {
  
  /**
   * Heuristic functions for A* algorithm
   */
  object Heuristics {
    
    /**
     * Zero heuristic - essentially becomes Dijkstra
     */
    def none: String => Double = _ => 0.0
    
    /**
     * Simple straight-line distance heuristic for coordinate-based nodes
     * Assumes node IDs contain coordinates in format "x,y"
     */
    def euclidean: String => Double = { nodeId =>
      try {
        val parts = nodeId.split(",")
        if (parts.length >= 2) {
          val x = parts(0).toDouble
          val y = parts(1).toDouble
          math.sqrt(x * x + y * y)
        } else 0.0
      } catch {
        case _: Exception => 0.0
      }
    }
    
    /**
     * Manhattan distance heuristic for grid-based nodes
     */
    def manhattan: String => Double = { nodeId =>
      try {
        val parts = nodeId.split(",")
        if (parts.length >= 2) {
          val x = parts(0).toDouble.abs
          val y = parts(1).toDouble.abs
          x + y
        } else 0.0
      } catch {
        case _: Exception => 0.0
      }
    }
    
    /**
     * Chebyshev distance heuristic (max of absolute differences)
     */
    def chebyshev: String => Double = { nodeId =>
      try {
        val parts = nodeId.split(",")
        if (parts.length >= 2) {
          val x = parts(0).toDouble.abs
          val y = parts(1).toDouble.abs
          math.max(x, y)
        } else 0.0
      } catch {
        case _: Exception => 0.0
      }
    }
  }
  
  /**
   * Predefined graph templates
   */
  object Templates {
    
    /**
     * Create a simple linear graph A -> B -> C -> D
     */
    def linearGraph: Graph = {
      val nodes = Set(Node("A"), Node("B"), Node("C"), Node("D"))
      val edges = Set(
        Edge("A", "B", 1.0),
        Edge("B", "C", 1.0),
        Edge("C", "D", 1.0)
      )
      Graph(nodes, edges, directed = true)
    }
    
    /**
     * Create a grid graph (3x3)
     */
    def gridGraph: Graph = {
      val nodeIds = for {
        i <- 0 to 2
        j <- 0 to 2
      } yield s"($i,$j)"
      
      val nodes = nodeIds.map(id => Node(id)).toSet
      
      val edges = scala.collection.mutable.Set[Edge]()
      for {
        i <- 0 to 2
        j <- 0 to 2
      } {
        if (i < 2) edges += Edge(s"($i,$j)", s"(${i+1},$j)", 1.0)
        if (j < 2) edges += Edge(s"($i,$j)", s"($i,${j+1})", 1.0)
      }
      
      Graph(nodes, edges.toSet, directed = false)
    }
    
    /**
     * Create a complete graph (all nodes connected to all others)
     */
    def completeGraph(nodeCount: Int): Graph = {
      val nodeIds = (0 until nodeCount).map(i => s"N$i")
      val nodes = nodeIds.map(id => Node(id)).toSet
      
      val edges = scala.collection.mutable.Set[Edge]()
      for {
        i <- 0 until nodeCount
        j <- i + 1 until nodeCount
      } {
        edges += Edge(s"N$i", s"N$j", 1.0)
      }
      
      Graph(nodes, edges.toSet, directed = false)
    }
    
    /**
     * Create a simple city network graph
     */
    def cityNetworkGraph: Graph = {
      val nodes = Set(
        Node("New York"),
        Node("Boston"),
        Node("Philadelphia"),
        Node("Washington"),
        Node("Atlanta")
      )
      
      val edges = Set(
        Edge("New York", "Boston", 215.0),
        Edge("New York", "Philadelphia", 95.0),
        Edge("Philadelphia", "Washington", 140.0),
        Edge("Washington", "Atlanta", 640.0),
        Edge("New York", "Washington", 225.0),
        Edge("Boston", "Philadelphia", 305.0)
      )
      
      Graph(nodes, edges, directed = false)
    }
  }
}

/**
 * Helper functions for common operations
 */
object Helpers {
  
  /**
   * Validate a route is valid in a graph
   */
  def isValidRoute(graph: Graph, route: Route): Boolean = {
    if (route.nodes.isEmpty) return false
    
    // Check all nodes exist
    val allNodesExist = route.nodes.forall(graph.containsNode)
    if (!allNodesExist) return false
    
    // Check all consecutive nodes are connected
    val allEdgesExist = route.nodes.sliding(2).forall { pair =>
      graph.getEdgeWeight(pair(0), pair(1)).isDefined
    }
    
    allEdgesExist
  }
  
  /**
   * Calculate actual distance of a route in a graph
   */
  def calculateRouteDistance(graph: Graph, nodes: List[String]): Option[Double] = {
    if (nodes.isEmpty) return None
    
    var totalDistance = 0.0
    for (i <- 0 until nodes.length - 1) {
      graph.getEdgeWeight(nodes(i), nodes(i + 1)) match {
        case Some(weight) => totalDistance += weight
        case None => return None
      }
    }
    
    Some(totalDistance)
  }
  
  /**
   * Find the shortest edge in the graph
   */
  def findShortestEdge(graph: Graph): Option[Edge] = {
    if (graph.edges.isEmpty) None
    else Some(graph.edges.minBy(_.weight))
  }
  
  /**
   * Find the longest edge in the graph
   */
  def findLongestEdge(graph: Graph): Option[Edge] = {
    if (graph.edges.isEmpty) None
    else Some(graph.edges.maxBy(_.weight))
  }
  
  /**
   * Get average edge weight
   */
  def averageEdgeWeight(graph: Graph): Double = {
    if (graph.edges.isEmpty) 0.0
    else graph.edges.map(_.weight).sum / graph.edges.size
  }
  
  /**
   * Get node with highest degree (most connections)
   */
  def nodeDegree(graph: Graph, nodeId: String): Int = {
    graph.getNeighbors(nodeId).size
  }
  
  /**
   * Get the node with most connections
   */
  def getMostConnectedNode(graph: Graph): Option[String] = {
    if (graph.nodes.isEmpty) None
    else Some(graph.nodes.map(_.id).maxBy(nodeDegree(graph, _)))
  }
}

