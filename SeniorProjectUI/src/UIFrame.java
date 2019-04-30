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
	private JPanel mainPanel, controlPanel;
	private UICanvas mapPanel;
	private ArrayList<UINode> mapPanelList = new ArrayList<>();
	private boolean stillRun;
	private Timer timer;
	public UIFrame(UICanvas _mapCanvas)
	{ 
		stillRun = true;
		mainPanel = new JPanel();
		mapPanel = _mapCanvas;
		mapPanelList = _mapCanvas.getNodeList();
		createUIFrame();
	}
	
	public void createUIFrame()
	{
		// Retrieve the top-level content-pane from JFrame
	      mainPanel = new JPanel();
	 
	      // Content-pane sets layout
	      mainPanel.setLayout(new BorderLayout(1,1));
	      mainPanel.setBorder(new EmptyBorder(5,5,5,5));
	      
	      controlPanel = new JPanel();
	      
	      
	      if(stillRun = true)
		  {
			  runSimButton();
			  mapPanel.repaint();
		  }
	      
	      
	 
	      // Content-pane adds components
	      controlPanel.add(new JLabel("Kennesaw State University Marietta Campus Simulation"));
	      JButton runBtn = new JButton("Run");
	      controlPanel.add(runBtn);
	      runBtn.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent e)
	    	  {
	    		  if(runBtn.getText().equals("Run"))
	    		  {
	    			  timer.start();
	    			  runBtn.setText("Stop");
	    			  stillRun = false;
	    			  
	    		//	  runBtn.updateUI();
	    		//	  runSimButton();
	    		//	  repaint();
	    		  }
	    		  else if(runBtn.getText().equals("Stop"))
	    		  {
	    			  runBtn.setText("Run");
	    			  stillRun = true;
	    			  timer.stop();
	    		  }	  
	    	  }
	      });
	      
	      timer = new Timer(2, new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	            	if(stillRun = false)
	            	{
	            		
	            	}
	            	else if(stillRun = true)
	            	{
	            		runSimButton();
		            	repaint();
	            	}
	            }
	      });
	      timer.start();
	      
	      mainPanel.add(controlPanel, BorderLayout.SOUTH);
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
	//		mapPanel.moveEverything();
			mapPanel.updateList(mapPanelList);
			canvasPaint();
	//	 
		
		//  Original Test Code
		// Random Colors
		// Code below
			
		/*
		
		System.out.println("The Button works!!!!");
		ArrayList <UINode> tempList = mapPanelList;
		
		Random rand = new Random();
		int changeColor, colorNode;
		for(int i = 0; i<5000000; i++)
		{
			changeColor = rand.nextInt(5)+1;
			colorNode = rand.nextInt(mapPanelList.size());
			if(changeColor == 1)
				tempList.get(colorNode).setNodeColor(Color.RED);
			if(changeColor == 3)
				tempList.get(colorNode).setNodeColor(Color.YELLOW);
			if(changeColor == 5)
				tempList.get(colorNode).setNodeColor(Color.WHITE);
			
			mapPanel.setTruckALocation(tempList.get(colorNode).getNodeName());
			mapPanel.setTruckAX(tempList.get(colorNode).getNodeX());
			mapPanel.setTruckAY(tempList.get(colorNode).getNodeY());
			
			mapPanel.setTruckBLocation("B");
			mapPanel.setTruckBX(420);
			mapPanel.setTruckBY(350);
			
			mapPanel.moveEverything();
			
			canvasPaint();
			
		}  
		*/
	}
	
	public void updateNodeList(ArrayList<UINode> temp)
	{
		mapPanelList = temp;
	}
	
	public ArrayList<UINode> getNodeList()
	{
		return mapPanelList;
	}
	
	public void setRun(boolean setRunning)
	{
		stillRun = setRunning;
	}
	
	public void canvasPaint()
	{
		mapPanel.repaint();
	}
}