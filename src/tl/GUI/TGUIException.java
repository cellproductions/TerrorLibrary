package tl.GUI;

public class TGUIException extends RuntimeException
{
	private static final long serialVersionUID = 2261898731161280040L;
	
	public TGUIException()
	{
		super();
	}
	
	public TGUIException(String message)
	{
		super("id: " + message);
	}
}
