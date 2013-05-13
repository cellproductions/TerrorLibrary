package tl.GUI;

public interface TGUIEvent
{
	public void execute(TGUI gui);
	public void execute(int button, int x, int y, TGUI gui);
}