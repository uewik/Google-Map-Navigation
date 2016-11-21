package roadgraph;

import geography.GeographicPoint;


// For shortest time version of Dijkstra method in MapGraph.java. This class is a pair of node and the travel time from source to node.
public class Duration implements Comparable<Duration>{
	private GeographicPoint node;
	private double time;
	
	Duration(GeographicPoint point)
	{
		node = point;
		time = Double.MAX_VALUE;
	}
	
	Duration(GeographicPoint point, double t)
	{
		node = point;
		time = t;
	}
	
	public int compareTo(Duration d)
	{
		if (time < d.time)
			return -1;
		else if (time == d.time)
			return 0;
		else
			return 1;
	}
	
	public double getTime()
	{
		return time;
	}
	
	public GeographicPoint getPoint()
	{
		return node;
	}
	
	public void setTime(double t)
	{
		time = t;
	}

}
