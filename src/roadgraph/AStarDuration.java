package roadgraph;

import geography.GeographicPoint;

public class AStarDuration implements Comparable<AStarDuration>{
	private GeographicPoint node;
	private double sourceTime;
	private double goalTime;
	
	AStarDuration(GeographicPoint point)
	{
		node = point;
		sourceTime = Double.MAX_VALUE;
		goalTime = Double.MAX_VALUE;
	}
	
	AStarDuration(GeographicPoint point, double t1, double t2)
	{
		node = point;
		sourceTime = t1;
		goalTime = t2;
	}
	
	public int compareTo(AStarDuration d)
	{
		if (getTotalTime() < d.getTotalTime())
			return -1;
		else if (getTotalTime() == d.getTotalTime())
			return 0;
		else
			return 1;
	}
	
	public double getSourceTime()
	{
		return sourceTime;
	}
	
	public double getGoalTime()
	{
		return goalTime;
	}
	
	public double getTotalTime()
	{
		return sourceTime + goalTime;
	}
	
	public GeographicPoint getPoint()
	{
		return node;
	}
	
	public void setSourceTime(double t)
	{
		sourceTime = t;
	}
	
	public void setGoalTime(double t)
	{
		goalTime = t;
	}

}
