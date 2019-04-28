import java.util.ArrayList;
public class UITruck 
{
	private String currentTruckNode, destinationNode;
	private ArrayList<UINode> parrallelList;
	private static int truckSpeed = 1;
	private int truckX, truckY;
	public UITruck(ArrayList <UINode> nodeList)
	{
		parrallelList = nodeList;
	}
	
	public void setTruckNode(String nodeName) // sets the current node the Truck is at or supposed to be at
	{
		for(int i = 0; i<parrallelList.size(); i++)
		{
			if(parrallelList.get(i).getNodeName() == nodeName)
			{
				currentTruckNode = nodeName;
			}
		}
	}
	public void setDestTruck(String moveNode) // sets the destination node of the truck
	{
		destinationNode = moveNode;
	}
	public void setX(int _x) // sets the X coordinate of the truck
	{
		truckX = _x;
	}
	public void setY(int _y) // sets the Y coordinate of the map
	{
		truckY = _y;
	}
	
	public String getCurrentNode() // gets the current node the truck is at or supposed to be at
	{
		return currentTruckNode;
	}
	public String getDestTruck() // gets the node the truck is supposed to be going to
	{
		return destinationNode;
	}
	
	public int getSpeed() // gets the speed of the truck
	{
		return truckSpeed;
	}
	
	public int getX() // gets the X coordinate of the truck
	{
		return truckX;
	}
	public int getY() // gets the Y coordinates of the truck
	{
		return truckY;
	}
	
	public void moveTruck() // Moves the Truck to destination node. Only changes values 
	{
		UINode tempNode = new UINode(" ", " ", 0, 0);
		for(int i = 0; i<parrallelList.size(); i++)
		{
			if(getDestTruck() == parrallelList.get(i).getNodeName())
				tempNode = parrallelList.get(i);
		}
		
		if(Math.abs(tempNode.getNodeX() - getX()) > 3 )
		{
			if(tempNode.getNodeX() > getX())
				setX(getX() + getSpeed());
			else if(tempNode.getNodeX() < getX())
				setX(getX() - getSpeed());
		}
		
		if(Math.abs(tempNode.getNodeY() - getY()) > 3 )
		{
			if(tempNode.getNodeY() > getY())
				setY(getY() + getSpeed());
			else if(tempNode.getNodeY() < getY())
				setY(getY() - getSpeed());
		}
	}
	
	public String toString() // toString() override
	{
		return getCurrentNode().toString();
	}
}