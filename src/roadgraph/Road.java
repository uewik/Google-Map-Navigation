package roadgraph;

import geography.GeographicPoint;


//A class which stores road information. May add methods as needed in the future.

public class Road {
	private String roadName;
	private String roadType;
	private double length;
	private GeographicPoint from;
	private GeographicPoint to;
	private double time;
	
	Road(GeographicPoint from, GeographicPoint to, String roadName, String roadType, double length)
	{
		this.from = from;
		this.to = to;
		this.roadName = roadName;
		this.roadType = roadType;
		this.length = length;
		SpeedLimit speed;
		time = length / SpeedLimit.valueOf(roadType).getSpeed();
	}
	
	public double getLength()
	{
		return length;
	}
	
	public double getTime()
	{
		return time;
	}
	
}
