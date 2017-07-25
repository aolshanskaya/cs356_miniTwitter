package cs356_a2;

import java.awt.*;
import javax.swing.*;


public abstract class UI extends JFrame{
	
	private Toolkit tk = Toolkit.getDefaultToolkit();
	private Dimension screen = tk.getScreenSize();
	
	private int xPos = (screen.width / 4) - (this.getWidth() / 2);
	private int yPos = (screen.height / 4) - (this.getHeight() / 2);
	
	public int getXPos(){
		return xPos;
	}
	
	public int getYPos(){
		return yPos;
	}
	
	public Dimension getDim(){
		return screen;
	}
}
