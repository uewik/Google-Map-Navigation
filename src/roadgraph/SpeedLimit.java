package roadgraph;


enum SpeedLimit {
	// enum constatns are used lowercase to be consistent with road type in map data.
	secondary(40), secondary_link(40), tertiary(35), tertiary_link(35), motorway(65), motorway_link(65), primary(40), primary_link(40), residential(35), 
	unclassified(25), trunk(45), trunk_link(45);
	
	private int speed;
	
	SpeedLimit(int s)	{ speed = s; }
	
	int getSpeed()	{ return speed; }
}
