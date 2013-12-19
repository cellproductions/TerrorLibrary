package tl.Util;

public class TAbstractMatrix
{
	static final byte SIZE = 9;
	float elements[];
	
	TAbstractMatrix()
	{
		elements = new float[]
				{
					1, 0, 0,
					0, 1, 0,
					0, 0, 1
				};
	}
	
	public void add(TAbstractMatrix matrix)
	{
		for (byte i = 0; i < SIZE; ++i)
			elements[i] += matrix.elements[i];
	}
	
	public void multiply(float scalar)
	{
		for (byte i = 0; i < SIZE; ++i)
			elements[i] *= scalar;
	}
	
	public void multiply(TAbstractMatrix matrix)
	{
		final byte DIV = SIZE / 2;
		//for (byte i = 0; i < DIV; ++i)
			//elements[i]
	}
}
