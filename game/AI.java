// Author Dominik Assauer
package game;

import java.util.ArrayList;
import java.util.Random;
import cards.Card;
import cards.CardAdmin;
import cards.DrawCard;
import cards.ReverseCard;
import cards.SkipCard;
import cards.WildCard;
import cards.WildDrawCard;
import gui.Gui;
import javafx.application.Platform;

public class AI extends Player implements Runnable
{

	private Card top = CardAdmin.topCard();
	private Card selectedCard = null;
	private ArrayList<Card> playableCards = new ArrayList<Card>();
	private int difficulty; // 1-3
	private boolean multipleCards = false;
	private Random random = new Random();
	private boolean skip;

	// Creates a new AI-player
	public AI(String _name, int _avatar, int _difficulty)
	{
		super( _name, _avatar );
		difficulty = _difficulty;

		if( difficulty < 0 || difficulty > 3 )
		{
			difficulty = 1;
		}
	}

	/**
	 * Decides if AI has a playable card and discard it. If next player has 2 or less cards, the AI
	 * will search for a DrawCard, WildDrawCard, ReverseCard or SkipCard and set is as selected
	 * card. If no card with same color or value as at top of discard pile is on hand, the AI will
	 * search for a WildCard or a WildDrawCard and sets it as the selected card. If no card is
	 * pre-selected and any card is playable, the AI will set one card randomly as selected. If a
	 * card is selected, it will be set at discard pile and removed from hand. If the AI can't play
	 * a card, it will draw one. Then it will switch to next player.
	 */
	@Override
	public void run() // Threads added by David Fröse
	{
		System.out.println( "---" + getName() + "---" );
		// update GUI
		Platform.runLater( new Runnable()
		{
			@Override
			public void run()
			{
				Gui.updateGUI();
			}
		} );

		if( skip )
		{
			System.out.println( "" );
		}

		if( !skip )
		{

			// wait 0.5 seconds
			try
			{
				Thread.sleep( 500 );
			}
			catch( InterruptedException e )
			{
				e.printStackTrace();
			}

			setCountCards();
			System.out.println( "---" + getName() + "---" );

			if( getHandCards().size() == 0 )
			{
				return;
			}
			top = CardAdmin.topCard();
			selectedCard = null;
			playableCards = new ArrayList<Card>();
			setPlayableCards();

			// Testzweck anfang
			System.out.println( "Top: " + top );
			for( Card card : playableCards )
				System.out.println( "Playable: " + card );
			// Testzweck ende

			nextPlayerUno();
			selectWild();

			/**
			 * If no card is pre-selected and any card is playable, the AI will set one card
			 * randomly as selected
			 */
			if( selectedCard == null && playableCards.size() > 0 && !multipleCards )
			{
				int rnd = random.nextInt( playableCards.size() );
				selectedCard = playableCards.get( rnd );
			}
			playCard();
			// update GUI again
			Platform.runLater( new Runnable()
			{
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					Gui.updateGUI();
				}
			} );

			// wait 0.5 seconds
			try
			{
				Thread.sleep( 500 );
			}
			catch( InterruptedException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println( "---" + getName() + "---SKIPPED" );
		}
		skip = false;
		// AI say UNO with 50% (ANMERKUNG: wird durch Schwierigkeit changed)
		GameManager.nextPlayer();

		Platform.runLater( new Runnable()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				Gui.updateGUI();
			}
		} );
		return;
	}

	public void skipTurn()
	{
		skip = true;
	}

	/**
	 * If a card is selected, it will be set at discard pile and removed from hand, if not, the AI
	 * will draw a card. If AI is allowed to play multiple cards, it will search for cards with the
	 * same color as top card and place them all;
	 */
	private void playCard()
	{

		if( (selectedCard instanceof WildCard || selectedCard instanceof WildDrawCard)
			&& selectedCard.getColor() == 0 )
		{
			selectWild();
		}

		if( selectedCard != null )
		{
			// Discard card
			CardAdmin.discardCard( selectedCard );
			System.out.println( "-Selected: " + selectedCard ); // Testanzeige
			discardHandCard( selectedCard );
			if( getHandCards().size() == 1 && random.nextInt( 2 ) == 0 )
				setUnoSaid( true );
		}
		else if( selectedCard == null && multipleCards && playableCards.size() > 0 )
		{
			// Discard card
			CardAdmin.discardCards( playableCards );
			System.out.println( "Selected: PLAYABLE" ); // Test

			for( Card card : playableCards )
			{
				discardHandCard( card );
			}
			if( getHandCards().size() == 1 && random.nextInt( 2 ) == 0 )
				setUnoSaid( true );
		}
		else
		{
			System.out.println( "DRAW" ); // Testanzeige
			addHandCards( CardAdmin.drawCards( 1 ) );
		}
	}

	/**
	 * If next player has 2 or less cards, the AI will search for a DrawCard, WildDrawCard,
	 * ReverseCard or SkipCard and set is as selected card.
	 */
	private void nextPlayerUno()
	{

		if( GameManager.getPlayer( 1 ).getHandCards().size() <= 2 && difficulty > 1 )
		{

			for( Card card : getHandCards() )
			{

				if( (card instanceof DrawCard || card instanceof WildDrawCard
					|| card instanceof ReverseCard || card instanceof SkipCard)
					&& (card.getColor() == top.getColor() || card.getColor() == 0) )
				{
					selectedCard = card;
					break;
				}
			}
		}
	}

	/**
	 * If no card with same color or value as at top of discard pile is on hand, the AI will search
	 * for a WildCard or a WildDrawCard and sets it as the selected card.
	 */
	private void selectWild()
	{

		if( playableCards.size() == 0 )
		{

			for( Card card : getHandCards() )
			{

				if( (card.getColor() != top.getColor() || card.getValue() != top.getValue())
					&& (card instanceof WildCard || card instanceof WildDrawCard) )
				{

					if( difficulty > 2 )
					{
						card.changeColor( setCountCards() );
					}
					else
					{
						card.changeColor( random.nextInt( 3 ) + 1 );
					}
					selectedCard = card;
					break;
				}
			}
		}
	}

	private int setCountCards()
	{
		int colorMaxValue = 0; // Card color with highest amount
		int[] countCards = new int[5];

		for( Card card : getHandCards() )
		{
			countCards[card.getColor()]++;
		}

		for( int i = 1; i < countCards.length - 1; i++ )
		{

			if( countCards[i] > countCards[colorMaxValue] )
			{
				colorMaxValue = i;
			}
			else if( countCards[i] == countCards[colorMaxValue] )
			{

				if( random.nextBoolean() )
				{
					colorMaxValue = i;
				}
			}
		}
		return colorMaxValue;
	}

	/**
	 * Sets all cards which has the same color or value as top card in arraylist. Or with multiple
	 * card play allowed it will set multiple cards with same color as top card.
	 */
	private void setPlayableCards()
	{

		for( Card card : getHandCards() )
		{

			System.out.println( card ); // Test

			if( !multipleCards )
			{

				// Tests, if color or value of card is same as top card and non-special
				if( top.getColor() == 0 || (card.getClass().equals( Card.class )
					&& (card.getColor() == top.getColor() || (card.getValue() == top.getValue()
						&& !(top instanceof WildDrawCard || top instanceof WildCard)))) )
				{
					playableCards.add( card );
				}
			}
			else
			{

				// Tests, if color of card is same as top card and non-special or if
				// color is same as top card and special
				if( (card.getClass().equals( Card.class )
					&& (card.getColor() == top.getColor())) )
				{
					playableCards.add( card );
				}
			}
		}

		if( playableCards.size() <= 2 && difficulty > 1 )
		{

			for( Card card : getHandCards() )
			{

				if( !multipleCards )
				{

					// Tests, if color or value of card is same as top card and non-special or if
					// color is same as top card and special
					if( top.getColor() == 0 || ((card.getClass().equals( Card.class )
						&& (card.getColor() == top.getColor()
							|| card.getValue() == top.getValue()))
						|| card instanceof Card && (card.getColor() == top.getColor()
							&& !(top instanceof WildDrawCard || top instanceof WildCard))) )
					{
						playableCards.add( card );
					}
				}
				else
				{

					// Tests, if color of card is same as top card and non-special or if
					// color is same as top card and special
					if( top.getColor() == 0 || ((card.getClass().equals( Card.class )
						&& (card.getColor() == top.getColor()))
						|| card instanceof Card && (card.getColor() == top.getColor()
							&& !(top instanceof WildDrawCard || top instanceof WildCard))) )
					{
						playableCards.add( card );
					}
				}
			}
		}
	}

}
