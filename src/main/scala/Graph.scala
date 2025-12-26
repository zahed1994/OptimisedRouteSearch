package optimalroutefinder

/**
 * Represents a node in the graph
 * @param id unique identifier for the node
 * @param label optional label for the node
 */
case class Node(id: String, label: Option[String] = None) {
  override def toString: String = label.getOrElse(id)
}

/**
 * Represents an edge connecting two nodes with a weight
 * @param from source node
 * @param to destination node
 * @param weight the cost or distance of the edge
 */
case class Edge(from: String, to: String, weight: Double) {
  require(weight >= 0, "Edge weight must be non-negative")
  
  override def toString: String = s"$from -> $to (weight: $weight)"
}

/**
 * Represents a route through the graph
 * @param nodes ordered list of nodes in the route
 * @param totalDistance total distance of the route
 */
case class Route(nodes: List[String], totalDistance: Double) {
  require(nodes.nonEmpty, "Route must contain at least one node")
  
  override def toString: String = 
    s"Route: ${nodes.mkString(" -> ")} (Total Distance: $totalDistance)"
}

/**
 * Represents an undirected/directed graph
 * @param nodes set of all nodes in the graph
 * @param edges set of all edges in the graph
 * @param directed if true, edges are directed; if false, edges are bidirectional
 */
case class Graph(
  nodes: Set[Node],
  edges: Set[Edge],
  directed: Boolean = true
) {
  
  // Build adjacency list for efficient lookups
  private val adjacencyList: Map[String, Set[Edge]] = {
    val map = scala.collection.mutable.Map[String, Set[Edge]]()
    
    // Initialize with all nodes
    nodes.foreach(node => map.put(node.id, Set()))
    
    // Add edges
    edges.foreach { edge =>
      map.update(edge.from, map(edge.from) + edge)
      if (!directed) {
        map.update(edge.to, map(edge.to) + Edge(edge.to, edge.from, edge.weight))
      }
    }
    
    map.toMap
  }
  
  /**
   * Get all neighbors of a node
   */
  def getNeighbors(nodeId: String): Set[Edge] = 
    adjacencyList.getOrElse(nodeId, Set())
  
  /**
   * Get a node by ID
   */
  def getNode(nodeId: String): Option[Node] = 
    nodes.find(_.id == nodeId)
  
  /**
   * Check if a node exists in the graph
   */
  def containsNode(nodeId: String): Boolean = 
    nodes.exists(_.id == nodeId)
  
  /**
   * Get the weight of an edge between two nodes
   */
  def getEdgeWeight(from: String, to: String): Option[Double] = {
    adjacencyList.getOrElse(from, Set())
      .find(_.to == to)
      .map(_.weight)
  }
  
  override def toString: String = 
    s"Graph(nodes: ${nodes.size}, edges: ${edges.size}, directed: $directed)"
}

