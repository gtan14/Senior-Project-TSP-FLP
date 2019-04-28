import java.util.ArrayList;
import java.util.Random;
import javax.swing.SwingUtilities;
public class UIMain 
{
	private static UICanvas campusPanel;
	private static UIFrame mainUI;
	private static ArrayList <UINode> nodeList;
	public static void main(String[] args) 
	{
		nodeList = new ArrayList<>();
		nodeList.add(new UINode("A", "White", 515, 50));
		nodeList.add(new UINode("B", "White", 400, 100));
		nodeList.add(new UINode("C", "White", 380, 140));
		nodeList.add(new UINode("D", "White", 430, 150));
		nodeList.add(new UINode("E", "White", 330, 200));
		nodeList.add(new UINode("F", "White", 400, 210));
		nodeList.add(new UINode("G", "White", 450, 215));
		nodeList.add(new UINode("H", "White", 570, 210));
		nodeList.add(new UINode("I", "White", 290, 250));
		nodeList.add(new UINode("J", "White", 347, 247));
		nodeList.add(new UINode("K", "White", 385, 245));
		nodeList.add(new UINode("L", "White", 298, 310));
		nodeList.add(new UINode("M", "White", 400, 295));
		nodeList.add(new UINode("N", "White", 468, 270));
		nodeList.add(new UINode("O", "White", 510, 255));
		nodeList.add(new UINode("P", "White", 310, 350));
		nodeList.add(new UINode("Q", "White", 355, 370));
		nodeList.add(new UINode("R", "White", 420, 350));
		nodeList.add(new UINode("S", "White", 550, 330));
		nodeList.add(new UINode("T", "White", 460, 380));
		nodeList.add(new UINode("U", "White", 495, 400));
		
		System.out.println("It works");
		
		SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() 
            {
            	campusPanel = new UICanvas(nodeList);
            	mainUI = new UIFrame(campusPanel);
            }
		});
	}
}