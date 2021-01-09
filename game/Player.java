package game;

import java.util.ArrayList;
import cards.Card;

public class Player
{

	private String name;
	private int avatar;
	private ArrayList<Card> handCards;
	private boolean unoSaid;
	private boolean won;
	private boolean passed;

	/**
	 * Creates a new player-object
	 * 
	 * @param _name   name of the player
	 * @param _avatar avatar which represents the player
	 */
	public Player(String _name, int _avatar)
	{
		name = _name;
		avatar = _avatar;
		handCards = new ArrayList<Card>();
		unoSaid = false;
	}

	public void addHandCards(ArrayList<Card> _cards)
	{
		handCards.addAll( _cards );
	}

	public void addHandCards(Card _card)
	{
		handCards.add( _card );
	}

	public ArrayList<Card> getHandCards()
	{
		return handCards;
	}

	public void discardHandCard(Card _card)
	{

		handCards.remove( _card );
	}

	public String getName()
	{
		return name;
	}

	public int getAvatar()
	{
		return avatar;
	}

	public void setUnoSaid(boolean _said)
	{
		unoSaid = _said;
	}

	public void setWon(boolean _won)
	{
		won = _won;
	}

	public void setPassed(boolean _passed)
	{
		passed = _passed;
	}

	public boolean getUnoSaid()
	{
		return unoSaid;
	}

	public boolean getWon()
	{
		return won;
	}

	public boolean getPassed()
	{
		return passed;
	}
}
