// Created by Daniel Alvarez
// UINode version 1.0
// Write any changes here are well as next to the change
// Changes:
//
import java.lang.Math;
import java.awt.Color;
import java.awt.color.*;
public class UINode 
{
	private String nodeState; // variable for the color state of the node.
	private String locationName; // The name of the node (A, B, C, ... , U)
	private Color currentNodeColor; // The color of the node that represents a state
	private int nodeX, nodeY; // (X,Y) coordinates of the node assuming a 2D Map
	private boolean isBaseNode; // boolean that says if the current node is one of the base nodes
	private boolean needsService; // boolean that says if current node requests service or not
	private boolean isSphereIn; // boolean that says if the current node is part of the Sphere of Influence
	private boolean hasRobot; // boolean that says if the current node has a robot servicing it
	private boolean hasTruckA; // boolean that says if the current node has Truck A in it
	private boolean hasTruckB; // boolean that says if the current node has Truck B in it
	public UINode(String locName, String oNodeState, int oNodeX, int oNodeY) 
	// Default Constructor
	{
		locationName = locName;
		nodeState = oNodeState;
		nodeX = oNodeX;
		nodeY = oNodeY;
		currentNodeColor = Color.WHITE;
	}
	
	public String getNodeName() // returns the name of the node
	{
		return locationName;
	}
	
	public String getNodeState() // returns Red/Green/White to show the current state of node
	{
		return nodeState;
	}
	
	public int getNodeX() // returns the x coordinate of the node
	{
		return nodeX;
	}
	
	public int getNodeY() // returns the y coordinate of the node
	{
		return nodeY;
	}
	
	public Color getNodeColor() // returns the color a Node is supposed to be
	{
		return currentNodeColor;
	}
	
	public boolean getIsBaseNode() // returns boolean that says if the current node is one of the base nodes
	{
		return isBaseNode;
	}
	
	public boolean getNeedsService() // returns boolean that says if current node requests service or not
	{
		return needsService;
	}
	
	public boolean getIsSphereIn() // returns boolean that says if the current node is part of the Sphere of Influence
	{
		return isSphereIn;
	}
	
	public boolean getHasRobot() // returns boolean that says if the current node has a robot servicing it
	{
		return hasRobot;
	}
	
	public boolean getHasTruckA() // return boolean that says if the current node has Truck A in it
	{
		return hasTruckA;
	}
	
	public boolean getHasTruckB() // boolean that says if the current node has Truck B in it
	{
		return hasTruckB;
	}
	
	public void setNodeColor(Color c) // sets the Color of the node 
	{
		currentNodeColor = c;
	}
	
	public void setBaseNode(boolean setBase) // set a node as a base node, TRUE = is a base node, FALSE = isn't a base node
	{
		isBaseNode = setBase;
		if(isBaseNode = true)
			setNodeColor(Color.YELLOW);
		if(isBaseNode = false)
			setNodeColor(Color.WHITE);
	}
	
	public void setNeedsService(boolean setServe) // current node needs service, TRUE = needs service, FALSE = doesn't need service
	{
		needsService = setServe;
		if(needsService = true)
			setNodeColor(Color.RED);
		if(needsService = false)
			setNodeColor(Color.WHITE);
	}
	
	public void setSphereIn(boolean setSOH) // boolean that says if the current node is part of the Sphere of Influence
	{
		isSphereIn = setSOH;
	}
	
	public void setHasRobot(boolean setRobo) // boolean that says if the current node has a robot servicing it
	{
		hasRobot = setRobo;
	}
	public void setTruckA(boolean setA) // boolean that says if the current node has Truck A in it
	{
		hasTruckA = setA;
	}
	public void setTruckB(boolean setB) // boolean that says if the current node has Truck B in it
	{
		hasTruckB = setB;
	}
	
	public double distBetweenNodes(UINode iNode) // DEFUNCT How to calculate the edges
	{
		// Distance Formula: square root(nx + ny)
		// Or square root((first node x - second node x)^2 + (first node y - second node y)^2)
		double nx = Math.pow(this.nodeX - iNode.nodeX, 2);
		double ny = Math.pow(this.nodeY - iNode.nodeY, 2);
		double nodeDistance = Math.sqrt((nx+ny));
		return nodeDistance; // return is the direct distance between any two nodes on a xy-plane
	}
	
	public String toString() // UNUSED toString() override
	{
		return getNodeName();
	}
}