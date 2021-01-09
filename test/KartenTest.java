package test;

import java.util.ArrayList;

import cards.Card;
import cards.CardAdmin;
import game.GameManager;

public class KartenTest
{

	public static void main(String[] args)
	{
		// playerTest();
		cardsTest();
	}

	public static void cardsTest()
	{
		CardAdmin.newPile( 1 );
		CardAdmin.mergeCards();
		ArrayList<Card> c = CardAdmin.drawCards( 108 );

		for( int i = 0; i < 50; i++ )
		{
			CardAdmin.discardCard( c.get( i ) );
			c.remove( i );
		}
		CardAdmin.mergeCards();
		CardAdmin.mergeCards();
	}

	public static void playerTest()
	{
		GameManager g = new GameManager();
		g.createPlayer( "Miau", 1 );
		g.createPlayer( "Peter", 1 );
		g.createPlayer( "Wuff", 1 );
		g.createPlayer( "Kr�tze", 1 );
		g.nextPlayer();
		g.reverseOrder();
		g.nextPlayer();
	}

}
