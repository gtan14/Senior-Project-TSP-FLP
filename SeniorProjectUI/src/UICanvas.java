import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class UICanvas extends JPanel
{
	private ArrayList <UINode> theNodeList = new ArrayList<>();
	private UITruck truckA, truckB;
	private ArrayList <UIRobot> roboList = new ArrayList<>();
	int amount = 50;
	int actual = 0;
	public UICanvas(ArrayList <UINode> _theNodeList) // constructor
	{
		theNodeList = _theNodeList;
		truckA = new UITruck(theNodeList);
		truckB = new UITruck(theNodeList);
		
		
        Timer paintTimer = new Timer(2000, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent evt) 
            {
                repaint();
                actual ++;
                if (actual >= amount) 
                {
                    Timer t = (Timer) evt.getSource();
                    t.stop();
                }
            }
        });
        paintTimer.setRepeats(true);
        paintTimer.start();
	}
	
	public void setTruckALocation(String curNode) // Set the location of the truck
	{
		truckA.setTruckNode(curNode);
	}
	
	public void setTruckBLocation(String curNode) // Set the location of the truck
	{
		truckB.setTruckNode(curNode);
	}
	
	public void setTruckADest(String newNode) // Set the node Truck A is going to
	{
		truckA.setDestTruck(newNode);
	}
	
	public void setTruckBDest(String newNode) // Set the node Truck B is going to
	{
		truckB.setDestTruck(newNode);
	}
	
	public void setTruckAX(int _x) // Set X coordinate of Truck A
	{
		truckA.setX(_x);
	}
	
	public void setTruckBX(int _x) // Set X coordinate of Truck B
	{
		truckB.setX(_x);
	}
	
	public void setTruckAY(int _y) // Set the Y coordinate of Truck A
	{
		truckA.setY(_y);
	}
	
	public void setTruckBY(int _y) // Sets the Y coordinate of Truck B
	{
		truckB.setX(_y);
	}
	
	public void dispatchRobotA(String order1, String order2) // Creates new robot from Truck A with origin and two destinations
	{
		UIRobot roboDispatch = new UIRobot(truckA, order1, order2, theNodeList);
		roboList.add(roboDispatch);
	}
	
	public void dispatchRobotB(String order1, String order2) // Creates new robot from Truck B with origin and two destinations
	{
		UIRobot roboDispatch = new UIRobot(truckB, order1, order2, theNodeList);
		roboList.add(roboDispatch);
	}
	
	
	public ArrayList<UINode> getNodeList() // gets the node list
	{
		return theNodeList;
	}
	
	public void moveEverything() // calls the methods in Robot and trucks to move everything.
	{
		truckA.moveTruck();
		truckB.moveTruck();
		for(int i = 0; i<roboList.size(); i++)
		{
			if(roboList.get(i).explodeRobot())
				roboList.remove(i);
			else
				roboList.get(i).moveRobot();
		}
		repaint();
	}
	
	@Override
	protected void paintComponent (Graphics g) 
	{
		super.paintComponent(g);
		setBackground (Color.BLACK);
        setSize(800, 500);
        
        g.setColor(Color.WHITE); // Draws Color Key Box
        g.fillRect(20, 240, 150, 140);
        
        g.setColor(Color.BLACK); // WHITE ON ACTUAL UI Empty Node or Line
        g.drawRect(25, 250, 30, 15);
        
        // Creates Key with colors and Strings
        
        g.drawString("Empty Node/Line", 61, 262);
        g.drawString("Truck A", 61, 282);
        g.drawString("Truck B", 61, 302);
        g.drawString("Robot", 61, 322);
        g.drawString("Base Node", 61, 342);
        g.drawString("Service Request", 61, 362);
        
        g.setColor(Color.BLUE); // Truck A
        g.fillRect(25, 270, 30, 15);
        
        g.setColor(Color.ORANGE); // Truck B
        g.fillRect(25, 290, 30, 15);
        
        g.setColor(Color.GREEN); // Robots
        g.fillRect(25, 310, 30, 15);
        
        g.setColor(Color.YELLOW); // Base Node
        g.fillRect(25, 330, 30, 15);
        
        g.setColor(Color.RED); // Node needs service
        g.fillRect(25, 350, 30, 15);
        
        
        if(truckA.getCurrentNode() != truckA.getDestTruck()) // Draws graph line for Truck A
        {
        	int tempX, tempY;
        	for(int i = 0; i<theNodeList.size(); i++)
        	{
        		if(truckA.getDestTruck() == theNodeList.get(i).getNodeName())
        		{
        			tempX = theNodeList.get(i).getNodeX();
        			tempY = theNodeList.get(i).getNodeY();
        			g.setColor(Color.WHITE);
                	g.drawLine(truckA.getX(), truckA.getY(), tempX, tempY);
        		}
        	}
        }
        if(truckB.getCurrentNode() != truckB.getDestTruck()) // Draws graph line for Truck B
        {
        	int tempX, tempY;
        	for(int i = 0; i<theNodeList.size(); i++)
        	{
        		if(truckB.getDestTruck() == theNodeList.get(i).getNodeName())
        		{
        			tempX = theNodeList.get(i).getNodeX();
        			tempY = theNodeList.get(i).getNodeY();
        			g.setColor(Color.WHITE);
        			g.drawLine(truckB.getX(), truckB.getY(), tempX, tempY);
        		}
        	}
        }
         
		for(int i = 0; i<theNodeList.size(); i++) // Draws the nodes 
		{
			g.setColor(theNodeList.get(i).getNodeColor());
			g.fillOval(theNodeList.get(i).getNodeX(), theNodeList.get(i).getNodeY(), 16, 16);
		}
		
		// Draws Truck A
		g.setColor(Color.BLUE);
		g.drawRect(truckA.getX(), truckA.getY(), 20, 20);
		
		// Draws Truck B
		g.setColor(Color.ORANGE);
		g.drawRect(truckB.getX(), truckB.getY(), 20, 20);
		
		// Draws any dispatched robots
		for(int i = 0; i<roboList.size(); i++)
		{
			g.setColor(Color.GREEN);
			g.fillOval(roboList.get(i).getOrderX(), roboList.get(i).getOrderY(), 10, 10); 
		}
	}
}