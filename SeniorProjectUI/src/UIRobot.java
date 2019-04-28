import java.util.ArrayList;
public class UIRobot 
{
	private int node1X, node2X, node1Y, node2Y;
	private int currentX, currentY;
	private static int robotSpeed = 3;
	private boolean fillFirstOrder, fillSecondOrder;
	private boolean canExplode;
	private String firstOrderNode, secondOrderNode;
	private ArrayList<UINode> orderNodeList;
	public UIRobot(UITruck dispatchTruck, String order1Node, String order2Node, ArrayList<UINode> aNodeList)
	{
		currentX = dispatchTruck.getX();
		currentY = dispatchTruck.getY();
		firstOrderNode = order1Node;
		secondOrderNode = order2Node;
		
		for(int i = 0; i<aNodeList.size(); i++)
		{
			if(aNodeList.get(i).getNodeName() == order1Node)
			{
				node1X = aNodeList.get(i).getNodeX();
				node1Y = aNodeList.get(i).getNodeY();
			}
			if(aNodeList.get(i).getNodeName() == order2Node)
			{
				node2X = aNodeList.get(i).getNodeX();
				node2Y = aNodeList.get(i).getNodeY();
			}
		}
	}
	public void isOrder1Filled(boolean fillOrder) // boolean to know if the first order has been serviced
	{
		fillFirstOrder = fillOrder;
	}
	
	public void isOrder2Filled(boolean fillOrder) // boolean to know if the second order has been serviced
	{
		fillSecondOrder = fillOrder;
	}
	
	public boolean getOrder1Filled() // returns boolean to know if the first order has been serviced
	{
		return fillFirstOrder;
	}
	
	public boolean getOrder2Filled() // returns boolean to know if the first order has been serviced
	{
		return fillSecondOrder;
	}
	
	public String getCurrentOrder() // gets the String name of the node the robot need to service
	{
		if(getOrder1Filled())
			return firstOrderNode;
		else
			return secondOrderNode;
	}
	
	public void setCurrentX(int x) // sets the X coordinate of the robot
	{
		currentX = x;
	}
	
	public void setCurrentY(int y) // sets the Y coordinates of the robot
	{
		currentY = y;
	}
	
	public int getOrderX() // gets the X coordinate of the order the robot is currently handling
	{
		if(getOrder1Filled())
			return node1X;
		else
			return node2X;
	}
	
	public int getOrderY() // gets the Y coordinate of the order the robot is currently handling
	{
		if(getOrder1Filled())
			return node1Y;
		else
			return node2Y;
	}
	
	public int getSpeed() // gets the speed of the robot
	{
		return robotSpeed;
	}
	
	public boolean explodeRobot() // determines whether or not the delete the robot from an array
	{
		if(getOrder1Filled() && getOrder2Filled())
			return true;
		else
			return false;
	}
	public void moveRobot() // moves the robot. Changes the values only
	{
		UINode tempNode;
		boolean tag1 = false;
		boolean tag2 = false;
		for(int i = 0; i<orderNodeList.size(); i++)
		{
			if(getCurrentOrder() == orderNodeList.get(i).getNodeName())
				tempNode = orderNodeList.get(i);
		}
		
		if(Math.abs(tempNode.getNodeX() - getOrderX()) > 3 )
		{
			if(tempNode.getNodeX() > getOrderX())
				setCurrentX(getOrderX() + getSpeed());
			else if(tempNode.getNodeX() < getOrderX())
				setCurrentX(getOrderX() - getSpeed());
		}
		else
			tag1 = true;
		
		if(Math.abs(tempNode.getNodeY() - getOrderY()) > 3 )
		{
			if(tempNode.getNodeY() > getOrderY())
				setCurrentY(getOrderY() + getSpeed());
			else if(tempNode.getNodeY() < getOrderY())
				setCurrentY(getOrderY() - getSpeed());
		}
		else
			tag2 = true;
		
		if(tag1 && tag2)
		{
			if(getOrder1Filled() == false)
				isOrder1Filled(true);
			else if(getOrder1Filled())
			{
				isOrder2Filled(true);
			}
		}
	}
}