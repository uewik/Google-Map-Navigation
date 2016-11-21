package roadgraph;

import geography.GeographicPoint;


// For Dijkstra method in MapGraph.java. This class is a pair of node and the distance from source to node.
public class Distance implements Comparable<Distance>{
	private GeographicPoint node;
	private double distance;
	
	Distance(GeographicPoint point)
	{
		node = point;
		distance = Double.MAX_VALUE;
	}
	
	Distance(GeographicPoint point, double d)
	{
		node = point;
		distance = d;
	}
	
	public int compareTo(Distance d)
	{
		if (distance < d.distance)
			return -1;
		else if (distance == d.distance)
			return 0;
		else
			return 1;
	}
	
	public double getDistance()
	{
		return distance;
	}
	
	public GeographicPoint getPoint()
	{
		return node;
	}
	
	public void setDistance(double d)
	{
		distance = d;
	}

}
