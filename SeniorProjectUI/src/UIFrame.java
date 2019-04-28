// Created by Daniel Alvarez
// UITruck version 2.0
import java.awt.*;       
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;
public class UIFrame extends JFrame
{
	private JPanel mainPanel, controlPanel, keyPanel;
	private UICanvas mapPanel;
	public UIFrame(UICanvas _mapCanvas)
	{
		createUIFrame(_mapCanvas);
	}
	
	public void createUIFrame(UICanvas _mapPanel)
	{
		// Retrieve the top-level content-pane from JFrame
	      mainPanel = new JPanel();
	 
	      // Content-pane sets layout
	      mainPanel.setLayout(new BorderLayout(1,1));
	      mainPanel.setBorder(new EmptyBorder(5,5,5,5));
	      
	      controlPanel = new JPanel();
	      
	 
	      // Content-pane adds components
	      controlPanel.add(new JLabel("Kennesaw State University Marietta Campus Simulation"));
	      JButton runBtn = new JButton("Run");
	      controlPanel.add(runBtn);
	      runBtn.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent e)
	    	  {
	    		  runSimButton();
	    	  }
	      });
	      
	      mainPanel.add(controlPanel, BorderLayout.SOUTH);
	      
	      mapPanel = _mapPanel;
	      mainPanel.add(mapPanel, BorderLayout.NORTH);
	      
	      
	 
	      // Source object adds listener
	      // .....
	 
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	         // Exit the program when the close-window button clicked
	      
	      setContentPane(mainPanel);
	      setBackground(Color.BLACK);
	      setTitle("Pamelo Simulation");  // "super" JFrame sets title
	      setSize(1000, 700);   // "super" JFrame sets initial size
	      setVisible(true);    // "super" JFrame shows
	}
	
	
	private void runSimButton() // TODO: Replace with TSP/FLP combo simulation
	{
		System.out.println("The Button works!!!!");
		ArrayList <UINode> tempList = mapPanel.getNodeList();
		
		Random rand = new Random();
		int changeColor, colorNode;
		for(int i = 0; i<5000000; i++)
		{
			changeColor = rand.nextInt(5)+1;
			colorNode = rand.nextInt(tempList.size());
			if(changeColor == 1)
				tempList.get(colorNode).setNodeColor(Color.RED);
			if(changeColor == 2)
				tempList.get(colorNode).setNodeColor(Color.BLUE);
			if(changeColor == 3)
				tempList.get(colorNode).setNodeColor(Color.YELLOW);
			if(changeColor == 4)
				tempList.get(colorNode).setNodeColor(Color.GREEN);
			if(changeColor == 5)
				tempList.get(colorNode).setNodeColor(Color.WHITE);
			
			mapPanel.setTruckALocation(tempList.get(colorNode).getNodeName());
			mapPanel.setTruckAX(tempList.get(colorNode).getNodeX());
			mapPanel.setTruckAY(tempList.get(colorNode).getNodeY());
			mapPanel.moveEverything();
			
			canvasPaint();
		}
	}
	
	public void canvasPaint()
	{
		mapPanel.repaint();
	}
}