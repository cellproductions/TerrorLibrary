package tl.Slick;

import org.newdawn.slick.state.BasicGameState;

import tl.GUI.TGUIComponent;

public abstract class TGameState extends BasicGameState
{
	public TGUIComponent stateComponent;
	
	public void keyPressed(int key, char c) 
	{
		stateComponent.keyPressed(key, c);
	}

	
	public void keyReleased(int key, char c) 
	{
		stateComponent.keyReleased(key, c);
	}

	
	public void mouseMoved(int oldx, int oldy, int newx, int newy) 
	{
		//stateComponent.mouse
	}

	
	public void mouseDragged(int oldx, int oldy, int newx, int newy) 
	{
		//stateComponent.mou
	}

	
	public void mouseClicked(int button, int x, int y, int clickCount) 
	{
		//stateComponent
	}
	
	
	public void mousePressed(int button, int x, int y) 
	{
		stateComponent.mousePressed(button, x, y);
	}

	
	public void mouseReleased(int button, int x, int y) 
	{
		stateComponent.mouseReleased(button, x, y);
	}
	
	public void mouseWheelMoved(int newValue) 
	{
		stateComponent.mouseWheelMoved(newValue);
	}
}
