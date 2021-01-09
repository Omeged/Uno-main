// Author: Dominik Assauer
package cards;

public class Card
{
	private int color;
	private final int value;
	private boolean selected;

	/**
	 * Creates a new Card-Object
	 * 
	 * @param _value Number of card (0-9 or draw amount)
	 * @param _color color of card (1 = blue, 2 = red, 3 = green, 4 = yellow, 0 = colorless)
	 */
	public Card(int _value, int _color)
	{
		color = _color;
		value = _value;
		selected = false;
	}

	public void changeColor(int _color)
	{
		color = _color;
	}

	public final int getColor()
	{
		return color;
	}

	public final int getValue()
	{
		return value;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean _selected)
	{
		selected = _selected;
	}

	public String toString()
	{
		String name = getClass().getName().substring( getClass().getName().indexOf( "." ) + 1 );
		return String.format( "%-12s|: F=%1d W=%1d", name, getColor(), getValue() );
	}

}
