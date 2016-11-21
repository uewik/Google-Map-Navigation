/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.Map;	
import java.util.HashMap;	
import java.util.ArrayList;		
import java.util.Queue;		
import java.util.LinkedList;	
import java.util.HashSet;		
import java.util.Collections;	
import java.util.PriorityQueue;
import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author Hao Zhang
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	// Member variable constructs Adjacency List. Each start GeographicPoint maps 
	// to a list (HashMap) of end GeographicPoints, each of which maps to road object including
	// road information
	private Map<GeographicPoint, Map<GeographicPoint, Road>> map;
	// For Dijkstra method
	//private Map<GeographicPoint, Distance> mapDistance;
	// For shortest time version of Dijkstra method
	private Map<GeographicPoint, Duration> mapDuration;
	// For AStarSearch method
	//private Map<GeographicPoint, AStarDistance> mapAStarDistance;
	// For shortest time version of AStarSearch method
	private Map<GeographicPoint, AStarDuration> mapAStarDuration;
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		map = new HashMap<GeographicPoint, Map<GeographicPoint, Road>>();
		//mapDistance = new HashMap<GeographicPoint, Distance>();
		mapDuration = new HashMap<GeographicPoint, Duration>();
		//mapAStarDistance = new HashMap<GeographicPoint, AStarDistance>();
		mapAStarDuration = new HashMap<GeographicPoint, AStarDuration>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		return map.size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		return map.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		int size = 0;
		for (Map.Entry<GeographicPoint, Map<GeographicPoint, Road>> entry : map.entrySet())
			size += entry.getValue().size();
		return size;
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		if (location == null)
			return false;
		for (GeographicPoint v : map.keySet())
		{
			if (v.distance(location) == 0)
				return false;
		}
		map.put(location, new HashMap<GeographicPoint, Road>());
		//mapDistance.put(location, new Distance(location));
		mapDuration.put(location, new Duration(location));
		//mapAStarDistance.put(location, new AStarDistance(location));
		mapAStarDuration.put(location, new AStarDuration(location));
		return true;
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {
		if (from == null || to == null)
			throw new IllegalArgumentException("GeographicPoints cannot be null.");
		if (length == 0)
			throw new IllegalArgumentException("length cannot be zero.");
		if (!map.containsKey(from) || !map.containsKey(to))
			throw new IllegalArgumentException("points have not already been addes as nodes to the graph.");
		map.get(from).put(to, new Road(from, to, roadName, roadType, length));
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		List<GeographicPoint> list = new ArrayList<GeographicPoint>();
		// If start or goal is not in graph, return null.
		if (!map.containsKey(start) || !map.containsKey(goal))	
			return null;
		Queue<GeographicPoint> queue = new LinkedList<GeographicPoint>();
		Set<GeographicPoint> visited = new HashSet<GeographicPoint>();
		Map<GeographicPoint, GeographicPoint> parent = new HashMap<GeographicPoint, GeographicPoint>();
		queue.add(start);
		visited.add(start);
		while (queue.size() != 0)
		{
			GeographicPoint curr = queue.remove();
			// Hook for visualization.
			nodeSearched.accept(curr);
			//if goal is found, add goal as the first GeographicPoint in the result list and break loop.
			if (curr.distance(goal) == 0)
			{
				list.add(curr);		
				break;
			}
			for (GeographicPoint point : map.get(curr).keySet())
			{
				if (!visited.contains(point))
				{
					visited.add(point);
					parent.put(point, curr);
					queue.add(point);
				}
			}
		}
		//If there is no path from start to goal, return null.
		if (list.size() == 0)	
			return null;
		GeographicPoint point = list.get(0);
		//Add GeographicPoints in the result path in reversal order.
		while (parent.containsKey(point))	
		{
			GeographicPoint curr = parent.get(point);
			list.add(curr);
			point = curr;
		}
		// reverse GeographicPoints in the result path to get correct order.
		Collections.reverse(list);
		return list;
	}
	

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	/*public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		List<GeographicPoint> list = new ArrayList<GeographicPoint>();
		if (!map.containsKey(start) || !map.containsKey(goal))	
			return null;
		PriorityQueue<Distance> pq = new PriorityQueue<Distance>();
		Set<GeographicPoint> visited = new HashSet<GeographicPoint>();
		Map<GeographicPoint, GeographicPoint> parent = new HashMap<GeographicPoint, GeographicPoint>();
		mapDistance.get(start).setDistance(0);
		pq.add(mapDistance.get(start));
		int count = 0;
		while (pq.size() != 0)
		{
			GeographicPoint curr = pq.poll().getPoint();
			count++;
			// Hook for visualization.
			nodeSearched.accept(curr);
			if (!visited.contains(curr))
			{
				visited.add(curr);
				if (curr.distance(goal) == 0)
				{
					list.add(curr);		
					break;
				}
				for (GeographicPoint point : map.get(curr).keySet())
				{
					if (!visited.contains(point))
					{
						double currDistance = mapDistance.get(curr).getDistance() + map.get(curr).get(point).getLength();
						if (currDistance < mapDistance.get(point).getDistance())
						{
							mapDistance.get(point).setDistance(currDistance);
							parent.put(point, curr);
							pq.add(new Distance(point, currDistance));
						}
					}
				}
			}
		}
		if (list.size() == 0)	
			return null;
		GeographicPoint point = list.get(0);
		while (parent.containsKey(point))	
		{
			GeographicPoint curr = parent.get(point);
			list.add(curr);
			point = curr;
		}
		Collections.reverse(list);
		System.out.println("Dijkstra: " + count);
		return list;
	}*/
	
	// Find the path with shortest trip duration from start to goal using Dijkstra's algorithm
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		List<GeographicPoint> list = new ArrayList<GeographicPoint>();
		if (!map.containsKey(start) || !map.containsKey(goal))	
			return null;
		PriorityQueue<Duration> pq = new PriorityQueue<Duration>();
		Set<GeographicPoint> visited = new HashSet<GeographicPoint>();
		Map<GeographicPoint, GeographicPoint> parent = new HashMap<GeographicPoint, GeographicPoint>();
		mapDuration.get(start).setTime(0);
		pq.add(mapDuration.get(start));
		int count = 0;
		while (pq.size() != 0)
		{
			GeographicPoint curr = pq.poll().getPoint();
			count++;
			// Hook for visualization.
			nodeSearched.accept(curr);
			if (!visited.contains(curr))
			{
				visited.add(curr);
				if (curr.distance(goal) == 0)
				{
					list.add(curr);		
					break;
				}
				for (GeographicPoint point : map.get(curr).keySet())
				{
					if (!visited.contains(point))
					{
						double currTime = mapDuration.get(curr).getTime() + map.get(curr).get(point).getTime();
						if (currTime < mapDuration.get(point).getTime())
						{
							mapDuration.get(point).setTime(currTime);
							parent.put(point, curr);
							pq.add(new Duration(point, currTime));
						}
					}
				}
			}
		}
		if (list.size() == 0)	
			return null;
		GeographicPoint point = list.get(0);
		while (parent.containsKey(point))	
		{
			GeographicPoint curr = parent.get(point);
			list.add(curr);
			point = curr;
		}
		Collections.reverse(list);
		System.out.println("Dijkstra: " + count);
		return list;
	}
	

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	/*public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		List<GeographicPoint> list = new ArrayList<GeographicPoint>();
		if (!map.containsKey(start) || !map.containsKey(goal))	
			return null;
		PriorityQueue<AStarDistance> pq = new PriorityQueue<AStarDistance>();
		Set<GeographicPoint> visited = new HashSet<GeographicPoint>();
		Map<GeographicPoint, GeographicPoint> parent = new HashMap<GeographicPoint, GeographicPoint>();
		mapAStarDistance.get(start).setSourceDistance(0);
		mapAStarDistance.get(start).setGoalDistance(0);
		pq.add(mapAStarDistance.get(start));
		int count = 0;
		while (pq.size() != 0)
		{
			GeographicPoint curr = pq.poll().getPoint();
			count++;
			// Hook for visualization.
			nodeSearched.accept(curr);
			if (!visited.contains(curr))
			{
				visited.add(curr);
				if (curr.distance(goal) == 0)
				{
					list.add(curr);		
					break;
				}
				for (GeographicPoint point : map.get(curr).keySet())
				{
					if (!visited.contains(point))
					{
						double currSourceDistance = mapAStarDistance.get(curr).getSourceDistance() + map.get(curr).get(point).getLength();
						double currGoalDistance = point.distance(goal);
						if (currSourceDistance + currGoalDistance < mapAStarDistance.get(point).getTotalDistance())
						{
							mapAStarDistance.get(point).setSourceDistance(currSourceDistance);
							mapAStarDistance.get(point).setGoalDistance(currGoalDistance);
							parent.put(point, curr);
							pq.add(new AStarDistance(point, currSourceDistance, currGoalDistance));
						}
					}
				}
			}
		}
		if (list.size() == 0)	
			return null;
		GeographicPoint point = list.get(0);
		while (parent.containsKey(point))	
		{
			GeographicPoint curr = parent.get(point);
			list.add(curr);
			point = curr;
		}
		Collections.reverse(list);
		System.out.println("AStarSearch: " + count);
		return list;
	}*/
	
	// shortest time version of A star search
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{	
		List<GeographicPoint> list = new ArrayList<GeographicPoint>();
		if (!map.containsKey(start) || !map.containsKey(goal))	
			return null;
		PriorityQueue<AStarDuration> pq = new PriorityQueue<AStarDuration>();
		Set<GeographicPoint> visited = new HashSet<GeographicPoint>();
		Map<GeographicPoint, GeographicPoint> parent = new HashMap<GeographicPoint, GeographicPoint>();
		mapAStarDuration.get(start).setSourceTime(0);
		mapAStarDuration.get(start).setGoalTime(0);
		pq.add(mapAStarDuration.get(start));
		int count = 0;
		while (pq.size() != 0)
		{
			GeographicPoint curr = pq.poll().getPoint();
			count++;
			nodeSearched.accept(curr);
			if (!visited.contains(curr))
			{
				visited.add(curr);
				if (curr.distance(goal) == 0)
				{
					list.add(curr);		
					break;
				}
				for (GeographicPoint point : map.get(curr).keySet())
				{
					if (!visited.contains(point))
					{
						double currSourceTime = mapAStarDuration.get(curr).getSourceTime() + map.get(curr).get(point).getTime();
						double currGoalTime = point.distance(goal) / 65;
						if (currSourceTime + currGoalTime < mapAStarDuration.get(point).getTotalTime())
						{
							mapAStarDuration.get(point).setSourceTime(currSourceTime);
							mapAStarDuration.get(point).setGoalTime(currGoalTime);
							parent.put(point, curr);
							pq.add(new AStarDuration(point, currSourceTime, currGoalTime));
						}
					}
				}
			}
		}
		if (list.size() == 0)	
			return null;
		GeographicPoint point = list.get(0);
		while (parent.containsKey(point))	
		{
			GeographicPoint curr = parent.get(point);
			list.add(curr);
			point = curr;
		}
		Collections.reverse(list);
		System.out.println("AStarSearch: " + count);
		return list;
	}	
	
	
				
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		/*MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		*/
	}
	
}
