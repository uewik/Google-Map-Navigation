package roadgraph;

import geography.GeographicPoint;

public class AStarDistance implements Comparable<AStarDistance>{
	private GeographicPoint node;
	private double sourceDistance;
	private double goalDistance;
	
	AStarDistance(GeographicPoint point)
	{
		node = point;
		sourceDistance = Double.MAX_VALUE;
		goalDistance = Double.MAX_VALUE;
	}
	
	AStarDistance(GeographicPoint point, double d1, double d2)
	{
		node = point;
		sourceDistance = d1;
		goalDistance = d2;
	}
	
	public int compareTo(AStarDistance d)
	{
		if (getTotalDistance() < d.getTotalDistance())
			return -1;
		else if (getTotalDistance() == d.getTotalDistance())
			return 0;
		else
			return 1;
	}
	
	public double getSourceDistance()
	{
		return sourceDistance;
	}
	
	public double getGoalDistance()
	{
		return goalDistance;
	}
	
	public double getTotalDistance()
	{
		return sourceDistance + goalDistance;
	}
	
	public GeographicPoint getPoint()
	{
		return node;
	}
	
	public void setSourceDistance(double d)
	{
		sourceDistance = d;
	}
	
	public void setGoalDistance(double d)
	{
		goalDistance = d;
	}

}
