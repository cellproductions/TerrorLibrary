package tl.GUI;

import org.newdawn.slick.Graphics;

public interface TGUIInterface
{
	public void update(Graphics g);
	public void mousePressed(int button, int x, int y);
	public void mouseReleased(int button, int x, int y);
	public void mouseWheelMoved(int change);
	public void keyPressed(int key, char c);
	public void keyReleased(int key, char c);
}