package game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import cards.Card;
import cards.CardAdmin;
import cards.DrawCard;
import cards.ReverseCard;
import cards.SkipCard;
import cards.WildCard;
import cards.WildDrawCard;
import gui.Gui;

public class GameManager
{
	private static Player[] player; // fixed size
	private static Player activePlayer;
	private static boolean isReversed = false;
	private static Card lastPlayed;
	private static boolean hasSkipped = false;

	public static String mainPlayerName;
	public static int mainPlayerAvatar;
	public static boolean online = false;
	public static int aiDifficulty = 1;
	public static int numberPlayers = 4;
	public static int numberComputerPlayers = 3;
	public static int numberCards = 7;
	public static boolean cardStacking = false;
	public static boolean rule0 = false;
	public static boolean rule7 = false;
	public static boolean discardIdenticalCards = true;
	public static boolean discardAfterDrawCard = true;

	public static Queue<Player> winnerQueue = new LinkedList<Player>();

	/**
	 * Starts a new Game with all given settings
	 * 
	 * Author: Dominik Assauer, David Fröse
	 * 
	 */
	public static void startGame()
	{
		// reset game
		player = new Player[numberPlayers];
		isReversed = false;

		// create main player and start new game
		createPlayer( mainPlayerName, mainPlayerAvatar );
		CardAdmin.newPile( 1 );
		CardAdmin.mergeCards();

		for( int i = 0; i < numberComputerPlayers; i++ )
		{
			createAI( "CPU " + (i + 1), 2 + i );
		}
		activePlayer = player[0];
		// draw one random card and put it onto the discard pile and make sure that the first card
		// is not a wild card!
		Card topCard;

		do
		{
			topCard = CardAdmin.drawCards( 1 ).get( 0 );
			CardAdmin.discardCard( topCard );
		}
		while( topCard instanceof WildCard || topCard instanceof WildDrawCard );
		lastPlayed = CardAdmin.topCard();
		// distribute Cards
		distributeCards( numberCards );
		player[0].addHandCards( new SkipCard( 2 ) );

		if( isAI( activePlayer ) != null )
		{
			Thread t1 = new Thread( isAI( activePlayer ) );
			t1.setDaemon( true );
			t1.start();
		}
	}

	/**
	 * Creates a new Player with the given name and avatar. This function should be called before
	 * the first round starts!
	 * 
	 * Author: David Fröse
	 * 
	 * @param name : Name of the Player
	 */
	public static void createPlayer(String _name, int _avatar)
	{
		int index = 0;

		while( player[index] != null ) // find first index with null
		{
			index++;
		}
		player[index] = new Player( _name, _avatar );
	}

	/**
	 * Creates a new AI with the given name and avatar.
	 * 
	 * Author: Dominik Assauer
	 * 
	 * @param name : Name of the Player
	 */
	public static void createAI(String _name, int _avatar)
	{
		int index = 0;

		while( player[index] != null ) // find first index with null
		{
			index++;
		}
		player[index] = new AI( _name, _avatar, aiDifficulty );
	}

	/**
	 * This function distributes _amount cards to each player in the player array. It should be
	 * called exactly one time when the first round begins!
	 * 
	 * Author: David Fröse
	 * 
	 * @param _amount : Amount of cards to draw
	 */
	public static void distributeCards(int _amount)
	{

		for( Player p : player )
		{
			if( p != null ) p.addHandCards( CardAdmin.drawCards( _amount ) );
		}
	}

	/**
	 * Switch the active player to index 0. All players indices decrease about 1, player at index 0
	 * will be shifted to last
	 * 
	 * Author: Dominik Assauer
	 * 
	 */
	public static void nextPlayer()
	{

		// add player to winnerQueue if he won and he is not in the queue yet
		if( getActivePlayer().getHandCards().size() <= 0
			&& !winnerQueue.contains( getActivePlayer() ) )
		{
			winnerQueue.add( getActivePlayer() );
		}

		// if player has won and he plays offline or if there is only 1 player in the game end the
		// game
		if( (!online && getActivePlayerIndex() == 0 && player[0].getHandCards().size() <= 0) ||
			countPlayersInGame() <= 1 )
		{
			Gui.switchScene( 3 );
			return;
		}
		if( CardAdmin.topCard() instanceof ReverseCard && lastPlayed != CardAdmin.topCard() )
			reverseOrder();
		else if( !hasSkipped && CardAdmin.topCard() instanceof SkipCard
			&& lastPlayed != CardAdmin.topCard() )
		{

			if( player[getNextPlayerIndex()] instanceof AI )
			{
				lastPlayed = CardAdmin.topCard();
				AI next = (AI) player[getNextPlayerIndex()];
				next.skipTurn();
				hasSkipped = true;
			}
		}
		else if( CardAdmin.topCard() instanceof DrawCard && lastPlayed != CardAdmin.topCard() )
		{

			// TODO: Implement list which stores how many DrawCards have been laid on the card pile
			// to implement the extra rule: Stack DrawCards
			if( CardAdmin.topCard() instanceof WildDrawCard )
			{

				if( isReversed )
				{
					System.out.println(
						player[getPreviousPlayerIndex()].getName()
							+ " has drawn 4 cards during reversed order."
					);
					player[getPreviousPlayerIndex()].addHandCards( CardAdmin.drawCards( 4 ) );
				}
				else
				{
					System.out
						.println( player[getNextPlayerIndex()].getName() + " has drawn 4 cards." );
					player[getNextPlayerIndex()].addHandCards( CardAdmin.drawCards( 4 ) );
				}
			}
			else
			{

				if( isReversed )
				{
					System.out.println(
						player[getPreviousPlayerIndex()].getName()
							+ " has drawn 2 cards during reversed order."
					);
					player[getPreviousPlayerIndex()].addHandCards( CardAdmin.drawCards( 2 ) );
				}
				else
				{
					System.out
						.println( player[getNextPlayerIndex()].getName() + " has drawn 2 cards." );
					player[getNextPlayerIndex()].addHandCards( CardAdmin.drawCards( 2 ) );
				}
			}
		}

		/*
		 * if( CardAdmin.topCard() instanceof SkipCard && lastPlayed != CardAdmin.topCard() ) {
		 * lastPlayed = CardAdmin.topCard(); nextPlayer(); }
		 */

		if( !isReversed )
		{

			for( int i = 0; i < player.length; i++ )
			{

				if( player[i] == activePlayer )
				{

					if( i < player.length - 1 )
					{
						activePlayer = player[i + 1];
						break;
					}
					else
					{
						activePlayer = player[0];
						break;
					}
				}
			}
		}
		else
		{

			for( int i = 0; i < player.length; i++ )
			{

				if( player[i] == activePlayer )
				{

					if( i > 0 )
					{
						activePlayer = player[i - 1];
						break;
					}
					else
					{
						activePlayer = player[player.length - 1];
						break;
					}
				}
			}
		}

		if( activePlayer == null || activePlayer.getHandCards().size() == 0 )
		{
			nextPlayer();
			return;
		}

		if( (!hasSkipped && CardAdmin.topCard() instanceof SkipCard) )
		{
			System.out.println( "LastPlayed: " + lastPlayed );
			lastPlayed = null;
			hasSkipped = true;
			nextPlayer();
			return;
		}
		else if( hasSkipped && !(CardAdmin.topCard() instanceof SkipCard) )
		{
			hasSkipped = false;
		}
		lastPlayed = CardAdmin.topCard();

		activePlayer.setUnoSaid( false ); // reset unoSaid

		if( isAI( activePlayer ) != null )
		{
			Thread t1 = new Thread( isAI( activePlayer ) );
			t1.setDaemon( true );
			t1.start();
		}
	}

	/**
	 * 
	 * This function performs the main player turn. If player has selected any WildCard or
	 * WildDrawCard without color it will open a window to select them and then they will be played.
	 * 
	 * Author: Nick Azarafroz, Dominik Assauer, David Fröse
	 * 
	 * @param unoButton : did the player hit the confirm button or the uno button
	 * @return did the player hit the draw button?
	 */
	public static boolean playerTurn(boolean unoButton)
	{
		ArrayList<Card> selectedCards = getSelectedCards( 0 );
		deselectCards( selectedCards );

		ArrayList<Card> selectedWildCards = new ArrayList<Card>();

		for( Card c : selectedCards )
		{

			if( (c instanceof WildDrawCard || c instanceof WildCard) && c.getColor() == 0 )
			{
				selectedWildCards.add( c );
			}
		}

		// Stops the funktion if the player has selected any WildCard or WildDrawCard without color
		if( selectedWildCards.size() > 0 )
		{
			Gui.showColorSelection( selectedWildCards.get( 0 ), true, unoButton );
			getActivePlayer().setPassed( false );
			return false;
		}

		if( unoButton )
		{

			if( getPlayerHandCards( 0 ).size() - selectedCards.size() > 1 )
			{
				getPlayerHandCards( 0 ).addAll( CardAdmin.drawCards( 1 ) );
			}
			else if( !validTurn( selectedCards ) )
			{
				getPlayerHandCards( 0 ).addAll( CardAdmin.drawCards( 1 ) );
			}
			else
			{
				CardAdmin.discardCards( selectedCards );
				removeCards( 0, selectedCards );
				player[0].setUnoSaid( true );
				System.out.println( "UNO said!" );
			}
		}
		else
		{

			// in case the player doesn't select any cards at all, he/she will draw a card from the
			// pile
			if( selectedCards.size() == 0 )
			{

				if( getActivePlayer().getPassed() )
				{
					// player passed again, so end the turn
					System.out.println( "---------" );
					System.out.println( "Passed!" );
					getActivePlayer().setPassed( false );
				}
				else
				{
					// no cards are selected, so the player passed and has to draw one card
					getPlayerHandCards( 0 ).addAll( CardAdmin.drawCards( 1 ) );
					getActivePlayer().setPassed( true );
					nextPlayer();
					return true;
				}
			}
			else if( !validTurn( selectedCards ) )
			{
				// turn is not valid, so draw one card
				getPlayerHandCards( 0 ).addAll( CardAdmin.drawCards( 1 ) );
				getActivePlayer().setPassed( false );
				System.out.println( "Turn is not valid!" );
				System.out.println( "---------" );
			}
			else
			{
				// turn is valid, discard all cards
				CardAdmin.discardCards( selectedCards );

				for( Card card : selectedCards )
				{
					System.out.println( "PLAYED: " + card );
				}
				removeCards( 0, selectedCards );
				getActivePlayer().setPassed( false );
				System.out.println( "---------" );
				nextPlayer();
			}
		}
		return false;
	}

	/**
	 * Turns the order in array. The player at last index will be at first and the player at first
	 * will be at last and so on
	 * 
	 * Author: Dominik Assauer
	 */
	public static void reverseOrder()
	{

		if( !isReversed )
		{
			isReversed = true;
		}
		else
		{
			isReversed = false;
		}
	}

	/**
	 * Checks if a player is an AI and returns it in this case
	 * 
	 * Author: Dominik Assauer
	 * 
	 */
	public static AI isAI(Player _player)
	{

		if( _player instanceof AI )
		{
			AI ai = (AI) _player;
			return ai;
		}
		return null;
	}

	/**
	 * Author: Dominik Assauer
	 * 
	 * @return active player (index 0)
	 */
	public static Player getActivePlayer()
	{
		return activePlayer;
	}

	/**
	 * This function returns the index of the active player
	 * 
	 * Author: David Fröse
	 * 
	 * @return the index of the active player
	 */
	public static int getActivePlayerIndex()
	{

		for( int i = 0; i < player.length; i++ )
		{

			if( player[i] == activePlayer )
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * This function returns the index of the player that was active before the active player
	 * 
	 * Author: David Hohmann
	 * 
	 * @return the index of the previous player
	 */
	public static int getPreviousPlayerIndex()
	{

		if( !isReversed )
		{
			int previousPlayer = getActivePlayerIndex() - 1;
			boolean isPreviousPlayer = false;

			// in case some players could already have won, and therefore have no cards on their
			// hands
			// anymore,
			// we need to check who comes before the active player.
			while( !isPreviousPlayer )
			{

				// there are no players in the minus range, go back to the end of the player array.
				if( previousPlayer < 0 )
				{
					previousPlayer = player.length - 1;
				}

				// as the player array always has 4 players in it,
				// check if player is not null to avoid null pointer exceptions
				if( player[previousPlayer] != null )
				{

					if( player[previousPlayer].getHandCards().size() > 0 )
					{
						// Hohmann: Haha, the previous player has finally been found! Gotcha, you
						// little
						// knave!
						// Now return the player.
						return previousPlayer;
					}
				}
				previousPlayer--;
			}
		}
		else
		{
			int previousPlayer = getActivePlayerIndex() + 1;
			boolean isPreviousPlayer = false;

			// in case some players could already have won, and therefore have no cards on their
			// hands
			// anymore,
			// we need to check who comes before the active player.
			while( !isPreviousPlayer )
			{

				// there are no players in the minus range, go back to the end of the player array.
				if( previousPlayer > player.length - 1 )
				{
					previousPlayer = 0;
				}

				// as the player array always has 4 players in it,
				// check if player is not null to avoid null pointer exceptions
				if( player[previousPlayer] != null )
				{

					if( player[previousPlayer].getHandCards().size() > 0 )
					{
						// Hohmann: Haha, the previous player has finally been found! Gotcha, you
						// little
						// knave!
						// Now return the player.
						return previousPlayer;
					}
				}
				previousPlayer++;
			}
		}
		return -1;
	}

	/**
	 * This function returns the index of the player that was active before the active player
	 * 
	 * Author: David Hohmann Edited(with: isReversed): Dominik Assauer
	 * 
	 * @return the index of the previous player
	 */
	public static int getNextPlayerIndex()
	{

		if( !isReversed )
		{
			int nextPlayer = getActivePlayerIndex() + 1;
			boolean isNextPlayer = false;

			// in case some players could already have won, and therefore have no cards on their
			// hands
			// anymore,
			// we need to check who comes after the active player.
			while( !isNextPlayer )
			{

				// there are no more players than 4, go back to start of the player array.
				if( nextPlayer > player.length - 1 )
				{
					nextPlayer = 0;
				}

				// as the player array always has 4 players in it,
				// check if player is not null to avoid null pointer exceptions
				if( player[nextPlayer] != null )
				{

					if( player[nextPlayer].getHandCards().size() > 0 )
					{
						// next Player has been found, return the player.
						return nextPlayer;
					}
				}
				nextPlayer++;
			}
		}
		else
		{
			int nextPlayer = getActivePlayerIndex() - 1;
			boolean isNextPlayer = false;

			// in case some players could already have won, and therefore have no cards on their
			// hands
			// anymore,
			// we need to check who comes after the active player.
			while( !isNextPlayer )
			{

				// there are no more players than 4, go back to start of the player array.
				if( nextPlayer < 0 )
				{
					nextPlayer = player.length - 1;
				}

				// as the player array always has 4 players in it,
				// check if player is not null to avoid null pointer exceptions
				if( player[nextPlayer] != null )
				{

					if( player[nextPlayer].getHandCards().size() > 0 )
					{
						// next Player has been found, return the player.
						return nextPlayer;
					}
				}
				nextPlayer--;
			}
		}

		return -1;
	}

	/**
	 * Returns a player with a given number
	 * 
	 * Author: David Fröse
	 * 
	 * @param value : number of the player (0 = Main Player, 1 = Player 2, 2 = Player 3, 3 = Player
	 *              4)
	 */
	public static Player getPlayer(int _value)
	{
		if( _value > player.length - 1 )
			return null;
		return player[_value];
	}

	/**
	 * Returns the number of players in the game
	 * 
	 * Author: ???
	 * 
	 */
	public static int getPlayerAmount()
	{
		return player.length;
	}

	/**
	 * Returns the cards of a given player
	 * 
	 * Author: David Fröse
	 * 
	 */
	public static ArrayList<Card> getPlayerHandCards(int _value)
	{
		return player[_value].getHandCards();
	}

	/**
	 * Returns the amount of the selected Cards the player has selected
	 * 
	 * Author: Nick Azarafroz
	 * 
	 * @return amount of selected Cards
	 * 
	 */
	public static int countSelectedCards(int _value)
	{
		int count = 0;
		ArrayList<Card> cardList = player[_value].getHandCards();

		for( Card card : cardList )
		{

			if( card.isSelected() )
			{
				count++;
			}
		}
		return count;
	}

	/**
	 * Returns a list of the selected cards
	 * 
	 * Author: Nick Azarafroz
	 * 
	 * @return the card list
	 * 
	 */
	public static ArrayList<Card> getSelectedCards(int _value)
	{
		ArrayList<Card> cardList = new ArrayList<Card>();

		for( Card card : player[_value].getHandCards() )
		{

			if( card.isSelected() )
			{
				cardList.add( card );
			}
		}
		return cardList;
	}

	/**
	 * checks if the turn is valid
	 * 
	 * Author: Nick Azarafroz, David Fröse
	 * 
	 * @param cards : ArrayList of selected cards
	 * 
	 */

	public static boolean validTurn(ArrayList<Card> _cards)
	{

		for( Card card : _cards )
		{

			// if all cards are not identical or there is one wildDrawCard, the turn is invalid
			if( _cards.size() > 1 && (card instanceof WildDrawCard
				|| !card.getClass().equals( _cards.get( 0 ).getClass() )
				|| card.getValue() != _cards.get( 0 ).getValue()
				|| card.getColor() != _cards.get( 0 ).getColor()) )
			{
				return false;
			}

			// if the topCard is a WildCard, the color must fit!
			if( !(card instanceof WildCard) && !(card instanceof WildDrawCard)
				&& (CardAdmin.topCard() instanceof WildCard
					|| CardAdmin.topCard() instanceof WildDrawCard)
				&& card.getColor() != CardAdmin.topCard().getColor() )
			{
				return false;
			}

			// if both, the value and the color, do not fit and it is not a special card the turn is
			// invalid
			if( !(card instanceof WildCard) && !(card instanceof WildDrawCard)
				&& card.getValue() != CardAdmin.topCard().getValue()
				&& card.getColor() != CardAdmin.topCard().getColor() )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * deselects all cards given in the ArrayList
	 * 
	 * Author: Nick Azarafroz
	 * 
	 * @param cards : ArrayList of selected cards
	 * 
	 */
	public static void deselectCards(ArrayList<Card> _cards)
	{

		for( Card card : _cards )
		{
			card.setSelected( false );
		}
	}

	/**
	 * removes all cards from the player's hand given in the list
	 * 
	 * Author: Nick Azarafroz
	 * 
	 * @param _value : player id
	 * @param _cards : ArrayList of cards to remove
	 * 
	 */
	public static void removeCards(int _value, ArrayList<Card> _list)
	{
		player[_value].getHandCards().removeAll( _list );
	}

	/**
	 * sets the player's won variable to true if he won
	 * 
	 * Author: Andreas Kiel
	 * 
	 * @param _value : player id
	 * @return boolean (did player win?)
	 * 
	 */
	public static boolean playerSetWon(int _value)
	{
		ArrayList<Card> handCards = player[_value].getHandCards();

		if( handCards.size() <= 0 )
		{
			player[_value].setWon( true );
			return true;
		}
		else
		{
			player[_value].setWon( false );
			return false;
		}
	}

	public static int countPlayersInGame()
	{
		int count = 0;

		for( int i = 0; i < player.length; i++ )
		{

			if( player[i] != null && player[i].getHandCards().size() > 0 )
			{
				count++;
			}
		}
		return count;

	}
}
