// Author: Dominik Assauer
package cards;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class CardAdmin
{
	private static ArrayList<Card> drawPile = new ArrayList<Card>();
	private static Stack<Card> discardPile = new Stack<Card>();

	/**
	 * Creates a new empty drawPile Array and a discardPile Stack with all classical UNO-Cards
	 * (Numbercards 0-9, drawcards 2 and 4, wildcards, skipcards and reversecards) in 4 different
	 * colors
	 * 
	 * @param amountPiles : Amount of piles using (Cards * 108)
	 */
	public static void newPile(int _amountPiles)
	{

		for( int n = 0; n < _amountPiles; n++ )
		{

			for( int i = 1; i <= 4; i++ )
			{
				drawPile.add( new Card( 0, i ) );

				for( int j = 1; j <= 9; j++ )
				{
					drawPile.add( new Card( j, i ) );
					drawPile.add( new Card( j, i ) );
				}
			}

			for( int i = 1; i <= 4; i++ )
			{
				drawPile.add( new DrawCard( i ) );
				drawPile.add( new DrawCard( i ) );
				drawPile.add( new SkipCard( i ) );
				drawPile.add( new SkipCard( i ) );
				drawPile.add( new ReverseCard( i ) );
				drawPile.add( new ReverseCard( i ) );
				drawPile.add( new WildDrawCard() );
				drawPile.add( new WildCard() );
			}
		}
	}

	/**
	 * Merges drawPile and discardPile and use Bogosort to shuffle the Cards in drawPile. The
	 * discardPile will be cleared.
	 */
	public static void mergeCards()
	{
		int amount = countCards(); // Size for cards array set to size of drawPile
		Card topCard = null; // will contain the top card of the discard pile

		if( discardPile.size() != 0 )
		{
			topCard = discardPile.pop();
			amount += discardPile.size(); // Increase the size for the cards array about the size of
											// discardPile.
			drawPile.addAll( discardPile ); // Merges drawPile and discardPile
			discardPile.clear();
		}
		Card[] cards = new Card[amount];
		System.out.println( cards.length );
		drawPile.toArray( cards );
		Random r = new Random();

		for( int i = 0; i < 500; i++ )
		{
			int a = r.nextInt( cards.length );
			int b = r.nextInt( cards.length );

			Card temp = cards[a];
			cards[a] = cards[b];
			cards[b] = temp;
		}
		drawPile.clear();

		// Adds the shuffled cards into drawPile
		for( Card c : cards )
		{

			// reset Color if it is a Wild card
			if( c instanceof WildCard || c instanceof WildDrawCard )
			{
				c.changeColor( 0 );
			}
			drawPile.add( c );
		}
		// add the top card to the discard pile again
		if( topCard != null ) discardPile.push( topCard );
	}

	/**
	 * @param _amount Amount of cards to draw
	 * @return returns _amount Cards from drawPile (begin with last index/top) and remove them from
	 *         Pile
	 */
	public static ArrayList<Card> drawCards(int _amount)
	{

		if( countCards() - _amount <= 0 )
		{
			if( discardPile.size() + countCards() - _amount <= 1 )
				newPile( 1 );
			mergeCards();
		}
		ArrayList<Card> handCards = new ArrayList<Card>();

		for( int i = 0; i < _amount; i++ )
		{
			handCards.add( drawPile.get( countCards() - 1 ) );
			drawPile.remove( countCards() - 1 );
		}
		return handCards;
	}

	/**
	 * Adds a card to discardPile
	 * 
	 * @param _card : added Card
	 */
	public static void discardCard(Card _card)
	{
		discardPile.add( _card );
	}

	/**
	 * 
	 * Adds Cards to discardPile
	 * 
	 * @param _list which contains the cards the player wants to discard
	 */

	public static void discardCards(ArrayList<Card> _list)
	{
		discardPile.addAll( _list );
	}

	/**
	 * 
	 * @return how many cards are left in drawPile
	 */
	public static int countCards()
	{
		return drawPile.size();
	}

	/**
	 * 
	 * @return top card of discardPile
	 */
	public static Card topCard()
	{
		if( discardPile.size() == 0 ) return null;
		return discardPile.peek();
	}
}