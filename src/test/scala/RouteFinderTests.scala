package optimalroutefinder

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GraphTests extends AnyFunSuite with Matchers {
  
  test("Create a simple graph") {
    val nodes = Set(Node("A"), Node("B"), Node("C"))
    val edges = Set(Edge("A", "B", 1.0), Edge("B", "C", 2.0))
    val graph = Graph(nodes, edges)
    
    graph.nodes.size should be(3)
    graph.edges.size should be(2)
    graph.directed should be(true)
  }
  
  test("Get neighbors of a node") {
    val nodes = Set(Node("A"), Node("B"), Node("C"))
    val edges = Set(
      Edge("A", "B", 1.0),
      Edge("A", "C", 2.0),
      Edge("B", "C", 3.0)
    )
    val graph = Graph(nodes, edges)
    
    val neighbors = graph.getNeighbors("A")
    neighbors.size should be(2)
  }
  
  test("Get edge weight") {
    val nodes = Set(Node("A"), Node("B"))
    val edges = Set(Edge("A", "B", 5.0))
    val graph = Graph(nodes, edges)
    
    graph.getEdgeWeight("A", "B") should be(Some(5.0))
    graph.getEdgeWeight("B", "A") should be(None)
  }
  
  test("Check if node exists") {
    val nodes = Set(Node("A"), Node("B"))
    val edges = Set(Edge("A", "B", 1.0))
    val graph = Graph(nodes, edges)
    
    graph.containsNode("A") should be(true)
    graph.containsNode("C") should be(false)
  }
  
  test("Undirected graph has bidirectional edges") {
    val nodes = Set(Node("A"), Node("B"))
    val edges = Set(Edge("A", "B", 1.0))
    val graph = Graph(nodes, edges, directed = false)
    
    graph.getEdgeWeight("A", "B") should be(Some(1.0))
    graph.getEdgeWeight("B", "A") should be(Some(1.0))
  }
  
  test("Negative edge weight should fail") {
    intercept[IllegalArgumentException] {
      Edge("A", "B", -1.0)
    }
  }
}

class DijkstraAlgorithmTests extends AnyFunSuite with Matchers {
  
  test("Find shortest path in simple graph") {
    val nodes = Set(
      Node("A"), Node("B"), Node("C"), Node("D")
    )
    val edges = Set(
      Edge("A", "B", 4.0),
      Edge("A", "C", 2.0),
      Edge("B", "C", 1.0),
      Edge("B", "D", 5.0),
      Edge("C", "D", 8.0)
    )
    val graph = Graph(nodes, edges, directed = true)
    
    val route = DijkstraAlgorithm.findPath(graph, "A", "D")
    route.isDefined should be(true)
    route.get.totalDistance should be(11.0)
    route.get.nodes should be(List("A", "B", "D"))
  }
  
  test("Return None when no path exists") {
    val nodes = Set(Node("A"), Node("B"), Node("C"))
    val edges = Set(Edge("A", "B", 1.0))
    val graph = Graph(nodes, edges, directed = true)
    
    val route = DijkstraAlgorithm.findPath(graph, "B", "C")
    route.isDefined should be(false)
  }
  
  test("Return None when start node does not exist") {
    val nodes = Set(Node("A"), Node("B"))
    val edges = Set(Edge("A", "B", 1.0))
    val graph = Graph(nodes, edges)
    
    val route = DijkstraAlgorithm.findPath(graph, "X", "B")
    route.isDefined should be(false)
  }
  
  test("Find path in undirected graph") {
    val nodes = Set(
      Node("A"), Node("B"), Node("C")
    )
    val edges = Set(
      Edge("A", "B", 1.0),
      Edge("B", "C", 2.0)
    )
    val graph = Graph(nodes, edges, directed = false)
    
    val route = DijkstraAlgorithm.findPath(graph, "C", "A")
    route.isDefined should be(true)
    route.get.nodes.head should be("C")
    route.get.nodes.last should be("A")
  }
}

class BFSAlgorithmTests extends AnyFunSuite with Matchers {
  
  test("Find path using BFS") {
    val nodes = Set(
      Node("A"), Node("B"), Node("C"), Node("D")
    )
    val edges = Set(
      Edge("A", "B", 1.0),
      Edge("A", "C", 1.0),
      Edge("B", "D", 1.0),
      Edge("C", "D", 1.0)
    )
    val graph = Graph(nodes, edges, directed = true)
    
    val route = BFSAlgorithm.findPath(graph, "A", "D")
    route.isDefined should be(true)
    route.get.nodes.length should be(3) // A -> B -> D or A -> C -> D
  }
  
  test("Return None when no path exists in BFS") {
    val nodes = Set(Node("A"), Node("B"), Node("C"))
    val edges = Set(Edge("A", "B", 1.0))
    val graph = Graph(nodes, edges, directed = true)
    
    val route = BFSAlgorithm.findPath(graph, "B", "C")
    route.isDefined should be(false)
  }
}

class RouteServiceTests extends AnyFunSuite with Matchers {
  
  test("Find route with Dijkstra algorithm") {
    val nodes = Set(Node("A"), Node("B"), Node("C"))
    val edges = Set(
      Edge("A", "B", 1.0),
      Edge("B", "C", 2.0)
    )
    val graph = Graph(nodes, edges)
    
    val result = RouteService.findRoute(graph, "A", "C", RouteService.Dijkstra)
    result.isRight should be(true)
    result.right.get.nodes should be(List("A", "B", "C"))
  }
  
  test("Find route with BFS algorithm") {
    val nodes = Set(Node("A"), Node("B"), Node("C"))
    val edges = Set(
      Edge("A", "B", 1.0),
      Edge("B", "C", 2.0)
    )
    val graph = Graph(nodes, edges)
    
    val result = RouteService.findRoute(graph, "A", "C", RouteService.BFS)
    result.isRight should be(true)
  }
  
  test("Return error when start node not found") {
    val nodes = Set(Node("A"), Node("B"))
    val edges = Set(Edge("A", "B", 1.0))
    val graph = Graph(nodes, edges)
    
    val result = RouteService.findRoute(graph, "X", "B")
    result.isLeft should be(true)
  }
  
  test("Return zero distance when start equals end") {
    val nodes = Set(Node("A"), Node("B"))
    val edges = Set(Edge("A", "B", 1.0))
    val graph = Graph(nodes, edges)
    
    val result = RouteService.findRoute(graph, "A", "A")
    result.isRight should be(true)
    result.right.get.totalDistance should be(0.0)
  }
  
  test("Find all paths from a source") {
    val nodes = Set(Node("A"), Node("B"), Node("C"))
    val edges = Set(
      Edge("A", "B", 1.0),
      Edge("B", "C", 2.0)
    )
    val graph = Graph(nodes, edges)
    
    val result = RouteService.findAllPaths(graph, "A")
    result.isRight should be(true)
    result.right.get.size should be(3)
  }
}

class IOHandlerTests extends AnyFunSuite with Matchers {
  
  test("Parse graph from CSV") {
    val csv = """A,B,1.0
                 |B,C,2.0
                 |A,C,3.0""".stripMargin
    
    val result = IOHandler.parseGraphFromCSV(csv)
    result.isRight should be(true)
    result.right.get.nodes.size should be(3)
    result.right.get.edges.size should be(3)
  }
  
  test("Parse CSV returns error on invalid format") {
    val csv = "A,B,invalid"
    val result = IOHandler.parseGraphFromCSV(csv)
    result.isLeft should be(true)
  }
  
  test("Convert graph to CSV") {
    val nodes = Set(Node("A"), Node("B"))
    val edges = Set(Edge("A", "B", 5.0))
    val graph = Graph(nodes, edges)
    
    val csv = IOHandler.graphToCSV(graph)
    csv should include("A,B,5.0")
  }
  
  test("Convert route to string") {
    val route = Route(List("A", "B", "C"), 10.0)
    val str = IOHandler.routeToString(route)
    
    str should include("A → B → C")
    str should include("10.0")
  }
}

