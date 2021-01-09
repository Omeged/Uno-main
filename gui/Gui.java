package gui;

import java.util.ArrayList;
import cards.Card;
import cards.CardAdmin;
import cards.DrawCard;
import cards.ReverseCard;
import cards.SkipCard;
import cards.WildCard;
import cards.WildDrawCard;
import game.GameManager;
import game.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Gui extends Application
{
	private static final int CARD_WIDTH = 112;
	private static final int CARD_HEIGHT = 175;
	private static final int CARD_WIDTH_SMALL = 90;
	private static final int CARD_HEIGHT_SMALL = 140;
	private static final int HAND_CARDS_WIDTH = 504;
	private static final int HAND_CARDS_WIDTH_SMALL = 264;
	private static Pane pane;
	private static StackPane panePlayer[];
	private static Pane panePlayerStatus[];
	private static Label[] playerCardNumber;
	private static ImageView discardPile;
	private static ImageView buttonConfirm;
	private static ImageView buttonUno;
	private static int avatarIndex;
	private static Button[] colorSelection;

	public static void main(String[] args)
	{
		launch( args );
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		pane = new Pane();
		Scene scene = new Scene( pane, 960, 720 );
		stage.setScene( scene );
		stage.setTitle( "UNO Prototype" );
		pane.setPrefWidth( 960 );
		pane.setPrefHeight( 720 );
		stage.setResizable( false );
		stage.getIcons().add( new Image( "file:images/uno_logo.png" ) );

		// add css
		pane.setId( "pane" );
		pane.getStylesheets().add( "file:style.css" );

		// go to title screen
		switchScene( 0 );

		stage.show();
	}

	/**
	 * Switches to a given scene and creates all nodes and panes accordingly
	 * 
	 * Author: David Fröse
	 * 
	 * @param index: index of the scene (0 = Title Screen, 1 = Settings, 2 = Main Game, 3 = Winner
	 *               Screen)
	 */
	public static void switchScene(int index)
	{
		pane.getChildren().clear(); // reset pane

		switch (index)
		{
		case 0 :
			// Title Screen
			// Author: Nick Azarafroz
			ImageView imageViewTitle;
			ImageView avatar;
			ImageView arrowRight;
			ImageView arrowLeft;
			ImageView avatarBg;

			imageViewTitle = new ImageView( new Image( "file:images/uno_title.png" ) );
			imageViewTitle.relocate( 0, 0 );

			Button btnHostGame = new Button( "Host Game" );
			btnHostGame.relocate( 343, 450 );
			btnHostGame.setPrefWidth( 130 );

			Button btnPlayOffline = new Button( "Play Offline" );
			btnPlayOffline.relocate( 343, 490 );
			btnPlayOffline.setPrefWidth( 275 );

			Button btnExitGame = new Button( "Exit Game" );
			btnExitGame.relocate( 343, 530 );
			btnExitGame.setPrefWidth( 275 );

			Button btnJoinGame = new Button( "Join Game" );
			btnJoinGame.relocate( 483, 450 );
			btnJoinGame.setPrefWidth( 133 );

			avatar = new ImageView( new Image( "file:images/avatar_" + avatarIndex + ".png" ) );
			avatar.relocate( 443, 300 );

			arrowLeft = new ImageView( new Image( "file:images/arrow_left.png" ) );
			arrowLeft.relocate( 408, 325 );

			arrowRight = new ImageView( new Image( "file:images/arrow_right.png" ) );
			arrowRight.relocate( 533, 325 );

			avatarBg = new ImageView( new Image( "file:images/avatar_bg.png" ) );
			avatarBg.relocate( 440, 301 );

			TextField txtName = new TextField();
			txtName.relocate( 343, 400 );
			txtName.setPrefWidth( 275 );
			txtName.setAlignment( Pos.CENTER );

			btnHostGame.setOnAction( a ->
			{

				if( txtName.getLength() > 12 || txtName.getLength() == 0 )
				{
					Alert errorName = new Alert( AlertType.ERROR );
					errorName.setHeaderText( "Error!" );
					errorName.setTitle( "Invalid Name" );
					errorName.setContentText(
						"Name contains invalid/no chars or is to long! Please don't use more than 12 characters!"
					);
					errorName.showAndWait();
				}
			} );

			btnPlayOffline.setOnAction( a ->
			{

				if( txtName.getLength() > 12 || txtName.getLength() == 0 )
				{
					Alert errorName = new Alert( AlertType.ERROR );
					errorName.setHeaderText( "Error!" );
					errorName.setTitle( "Invalid Name" );
					errorName.setContentText(
						"Name contains invalid/no chars or is to long! Please don't use more than 12 characters!"
					);
					errorName.showAndWait();
				}
				else
				{
					GameManager.mainPlayerName = txtName.getText();
					GameManager.mainPlayerAvatar = avatarIndex;
					switchScene( 1 );
				}
			} );

			btnJoinGame.setOnAction( a ->
			{

				if( txtName.getLength() > 12 || txtName.getLength() == 0 )
				{
					Alert errorName = new Alert( AlertType.ERROR );
					errorName.setHeaderText( "Error!" );
					errorName.setTitle( "Invalid Name" );
					errorName.setContentText(
						"Name contains invalid/no chars or is to long! Please don't use more than 12 characters!"
					);
					errorName.showAndWait();
				}
			} );

			btnExitGame.setOnAction( a ->
			{
				Platform.exit();
			} );

			arrowLeft.setOnMouseClicked( a ->
			{
				avatarIndex--;

				if( avatarIndex < 0 )
				{
					avatarIndex = 4;
				}
				avatar.setImage( new Image( "file:images/avatar_" + avatarIndex + ".png" ) );
			} );

			arrowRight.setOnMouseClicked( a ->
			{
				avatarIndex++;

				if( avatarIndex > 4 )
				{
					avatarIndex = 0;
				}
				avatar.setImage( new Image( "file:images/avatar_" + avatarIndex + ".png" ) );
			} );
			pane.getChildren().addAll(
				imageViewTitle, avatarBg, arrowLeft, arrowRight, txtName, avatar, btnHostGame,
				btnPlayOffline, btnExitGame, btnJoinGame
			);
			break;
		case 1 :
			// Settings
			// Author: Andreas Kiel, David Fröse

			// add rectangle and set background color
			Rectangle r = new Rectangle();
			r.setWidth( 840 );
			r.setHeight( 410 );
			r.setX( 60 );
			r.setY( 150 );
			r.setFill( Color.rgb( 0, 0, 0, 0.5 ) );
			r.setArcWidth( 20 );
			r.setArcHeight( 20 );
			r.setStroke( Color.WHITE );

			// add and relocate elements
			Label l1 = new Label( "Settings" );
			Label l2 = new Label( "Number of players" );
			Label l3 = new Label( "Number of computer players" );
			Label l4 = new Label( "Computer difficulty" );
			Label l5 = new Label( "Number of cards" );
			Label l6 = new Label( "Card Stacking" );
			Label l7 = new Label( "0 Rule" );
			Label l8 = new Label( "7 Rule" );
			Label l9 = new Label( "Allow discarding identical cards" );
			Label l10 = new Label( "Allow discarding after draw card(s)" );

			ComboBox<String> c1 = new ComboBox<>(
				FXCollections.observableArrayList( "2", "3", "4" )
			);
			ComboBox<String> c2 = new ComboBox<>(
				FXCollections.observableArrayList( "1", "2", "3" )
			);
			ComboBox<String> c3 = new ComboBox<>(
				FXCollections.observableArrayList( "1", "2", "3" )
			);
			ComboBox<String> c4 = new ComboBox<>(
				FXCollections.observableArrayList(
					"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"
				)
			);

			CheckBox cb1 = new CheckBox();
			CheckBox cb2 = new CheckBox();
			CheckBox cb3 = new CheckBox();
			CheckBox cb4 = new CheckBox();
			CheckBox cb5 = new CheckBox();

			Button btnStart = new Button( "Start Game!" );

			l1.relocate( 100, 170 );
			l2.relocate( 100, 235 );
			l3.relocate( 100, 270 );
			l4.relocate( 100, 305 );
			l5.relocate( 100, 340 );
			l6.relocate( 100, 375 );
			l7.relocate( 100, 410 );
			l8.relocate( 100, 445 );
			l9.relocate( 100, 480 );
			l10.relocate( 100, 515 );

			c1.relocate( 780, 235 );
			c2.relocate( 780, 270 );
			c3.relocate( 780, 305 );
			c4.relocate( 780, 340 );

			c1.getSelectionModel().selectFirst();
			c2.getSelectionModel().selectFirst();
			c3.getSelectionModel().selectFirst();
			c4.getSelectionModel().selectFirst();

			c1.setPrefWidth( 100 );
			c2.setPrefWidth( 100 );
			c3.setPrefWidth( 100 );
			c4.setPrefWidth( 100 );

			c1.getSelectionModel().select( GameManager.numberPlayers - 2 );
			c4.getSelectionModel().select( GameManager.numberCards - 1 );
			if( !GameManager.online )
			{
				c2.getSelectionModel().select( c1.getSelectionModel().getSelectedIndex() );
				c2.setDisable( true );
			}

			cb1.relocate( 780, 375 );
			cb2.relocate( 780, 410 );
			cb3.relocate( 780, 445 );
			cb4.relocate( 780, 480 );
			cb5.relocate( 780, 515 );

			cb4.setSelected( GameManager.discardIdenticalCards );
			cb5.setSelected( GameManager.discardAfterDrawCard );

			btnStart.relocate( 336, 600 );
			btnStart.setPrefWidth( 288 );

			// set text color
			l1.setTextFill( Color.WHITE );
			l2.setTextFill( Color.WHITE );
			l3.setTextFill( Color.WHITE );
			l4.setTextFill( Color.WHITE );
			l5.setTextFill( Color.WHITE );
			l6.setTextFill( Color.WHITE );
			l7.setTextFill( Color.WHITE );
			l8.setTextFill( Color.WHITE );
			l9.setTextFill( Color.WHITE );
			l10.setTextFill( Color.WHITE );

			// set font
			l1.setFont( Font.font( "Arial", FontWeight.BOLD, 45 ) );
			l2.setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );
			l3.setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );
			l4.setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );
			l5.setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );
			l6.setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );
			l7.setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );
			l8.setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );
			l9.setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );
			l10.setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );

			// set text shadow effect
			DropShadow shadow = new DropShadow();
			l1.setEffect( shadow );
			l2.setEffect( shadow );
			l3.setEffect( shadow );
			l4.setEffect( shadow );
			l5.setEffect( shadow );

			pane.getChildren().addAll(
				r, l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, c1, c2, c3, c4, cb1, cb2, cb3, cb4, cb5,
				btnStart
			);

			// functions
			c1.setOnAction( e ->
			{

				// if we play offline, then the computer players will be determined automatically
				if( !GameManager.online )
				{
					c2.getSelectionModel().select( c1.getSelectionModel().getSelectedIndex() );
				}
			} );

			btnStart.setOnAction( e ->
			{
				GameManager.numberPlayers = c1.getSelectionModel().getSelectedIndex() + 2;
				GameManager.numberComputerPlayers = c2.getSelectionModel().getSelectedIndex() + 1;
				GameManager.aiDifficulty = c3.getSelectionModel().getSelectedIndex() + 1;
				GameManager.numberCards = c4.getSelectionModel().getSelectedIndex() + 1;
				GameManager.cardStacking = cb1.isSelected();
				GameManager.rule0 = cb2.isSelected();
				GameManager.rule7 = cb3.isSelected();
				GameManager.discardIdenticalCards = cb4.isSelected();
				GameManager.discardAfterDrawCard = cb5.isSelected();

				switchScene( 2 );
			} );
			break;
		case 2 :
			// Main Game
			// Author: David Fröse

			GameManager.startGame();

			// create hand card pane array
			panePlayer = new StackPane[4];
			// create status pane array and card number label array
			panePlayerStatus = new Pane[4];
			playerCardNumber = new Label[4];

			// add player panes to main pane
			if( GameManager.getPlayer( 0 ) != null )
			{
				// Main Player
				// hand card pane
				panePlayer[0] = new StackPane();
				panePlayer[0].setPrefWidth( HAND_CARDS_WIDTH );
				panePlayer[0].setPrefHeight( CARD_HEIGHT );
				panePlayer[0].relocate( 238, 512 );

				// status pane
				panePlayerStatus[0] = new Pane();
				panePlayerStatus[0].relocate( 16, 563 );
				playerCardNumber[0] = new Label( "0" );
				playerCardNumber[0].relocate( 111, 52 );
				playerCardNumber[0].setPrefWidth( 24 );
				playerCardNumber[0].setPrefHeight( 26 );
				playerCardNumber[0].setAlignment( Pos.CENTER );
				panePlayerStatus[0].getStyleClass().add( "playerStatus" ); // set css-class for
																			// background image

				pane.getChildren().add( panePlayer[0] );
				pane.getChildren().add( panePlayerStatus[0] );
				panePlayerStatus[0].getChildren().add( playerCardNumber[0] );

				Label playerNameLabel1 = new Label( GameManager.getPlayer( 0 ).getName() );
				playerNameLabel1.relocate( 2, 80 );
				playerNameLabel1.setPrefWidth( 138 );
				playerNameLabel1.setPrefHeight( 31 );
				playerNameLabel1.setAlignment( Pos.CENTER );
				ImageView playerAvatar1 = new ImageView(
					new Image( "file:images/avatar_" + GameManager.getPlayer( 0 ).getAvatar() + ".png" )
				);
				playerAvatar1.relocate( 33, 2 );
				panePlayerStatus[0].getChildren().addAll( playerNameLabel1, playerAvatar1 );
			}

			if( GameManager.getPlayer( 1 ) != null )
			{
				// Player 2
				// hand card pane
				panePlayer[1] = new StackPane();
				panePlayer[1].setPrefWidth( HAND_CARDS_WIDTH_SMALL );
				panePlayer[1].setPrefHeight( CARD_HEIGHT_SMALL );
				panePlayer[1].relocate( 16, 264 );

				// status pane
				panePlayerStatus[1] = new Pane();
				panePlayerStatus[1].relocate( 16, 133 );
				playerCardNumber[1] = new Label( "0" );
				playerCardNumber[1].relocate( 111, 52 );
				playerCardNumber[1].setPrefWidth( 24 );
				playerCardNumber[1].setPrefHeight( 26 );
				playerCardNumber[1].setAlignment( Pos.CENTER );
				panePlayerStatus[1].getStyleClass().add( "playerStatus" );

				pane.getChildren().add( panePlayer[1] );
				pane.getChildren().add( panePlayerStatus[1] );
				panePlayerStatus[1].getChildren().add( playerCardNumber[1] );

				Label playerNameLabel2 = new Label( GameManager.getPlayer( 1 ).getName() );
				playerNameLabel2.relocate( 2, 80 );
				playerNameLabel2.setPrefWidth( 138 );
				playerNameLabel2.setPrefHeight( 31 );
				playerNameLabel2.setAlignment( Pos.CENTER );
				ImageView playerAvatar2 = new ImageView(
					new Image( "file:images/avatar_" + GameManager.getPlayer( 1 ).getAvatar() + ".png" )
				);
				playerAvatar2.relocate( 33, 2 );
				panePlayerStatus[1].getChildren().addAll( playerNameLabel2, playerAvatar2 );
			}

			if( GameManager.getPlayer( 2 ) != null )
			{
				// Player 3
				// hand card pane
				panePlayer[2] = new StackPane();
				panePlayer[2].setPrefWidth( HAND_CARDS_WIDTH_SMALL );
				panePlayer[2].setPrefHeight( CARD_HEIGHT_SMALL );
				panePlayer[2].relocate( 361, 40 );

				// status pane
				panePlayerStatus[2] = new Pane();
				panePlayerStatus[2].relocate( 628, 54 );
				playerCardNumber[2] = new Label( "0" );
				playerCardNumber[2].relocate( 111, 52 );
				playerCardNumber[2].setPrefWidth( 24 );
				playerCardNumber[2].setPrefHeight( 26 );
				playerCardNumber[2].setAlignment( Pos.CENTER );
				panePlayerStatus[2].getStyleClass().add( "playerStatus" );

				pane.getChildren().add( panePlayer[2] );
				pane.getChildren().add( panePlayerStatus[2] );
				panePlayerStatus[2].getChildren().add( playerCardNumber[2] );

				Label playerNameLabel3 = new Label( GameManager.getPlayer( 2 ).getName() );
				playerNameLabel3.relocate( 2, 80 );
				playerNameLabel3.setPrefWidth( 138 );
				playerNameLabel3.setPrefHeight( 31 );
				playerNameLabel3.setAlignment( Pos.CENTER );
				ImageView playerAvatar3 = new ImageView(
					new Image( "file:images/avatar_" + GameManager.getPlayer( 2 ).getAvatar() + ".png" )
				);
				playerAvatar3.relocate( 33, 2 );
				panePlayerStatus[2].getChildren().addAll( playerNameLabel3, playerAvatar3 );
			}

			if( GameManager.getPlayer( 3 ) != null )
			{
				// Player 4
				// hand card pane
				panePlayer[3] = new StackPane();
				panePlayer[3].setPrefWidth( HAND_CARDS_WIDTH_SMALL );
				panePlayer[3].setPrefHeight( CARD_HEIGHT_SMALL );
				panePlayer[3].relocate( 680, 264 );

				// status pane
				panePlayerStatus[3] = new Pane();
				panePlayerStatus[3].relocate( 800, 137 );
				playerCardNumber[3] = new Label( "0" );
				playerCardNumber[3].relocate( 111, 52 );
				playerCardNumber[3].setPrefWidth( 24 );
				playerCardNumber[3].setPrefHeight( 26 );
				playerCardNumber[3].setAlignment( Pos.CENTER );
				panePlayerStatus[3].getStyleClass().add( "playerStatus" );

				pane.getChildren().add( panePlayer[3] );
				pane.getChildren().add( panePlayerStatus[3] );
				panePlayerStatus[3].getChildren().add( playerCardNumber[3] );

				Label playerNameLabel4 = new Label( GameManager.getPlayer( 3 ).getName() );
				playerNameLabel4.relocate( 2, 80 );
				playerNameLabel4.setPrefWidth( 138 );
				playerNameLabel4.setPrefHeight( 31 );
				playerNameLabel4.setAlignment( Pos.CENTER );
				ImageView playerAvatar4 = new ImageView(
					new Image( "file:images/avatar_" + GameManager.getPlayer( 3 ).getAvatar() + ".png" )
				);
				playerAvatar4.relocate( 33, 2 );
				panePlayerStatus[3].getChildren().addAll( playerNameLabel4, playerAvatar4 );
			}
			updateGUI();

			// buttons
			buttonConfirm = new ImageView( new Image( "file:images/button_draw.png" ) );
			buttonConfirm.relocate( 766, 622 );
			buttonUno = new ImageView( new Image( "file:images/button_uno_disabled.png" ) );
			buttonUno.relocate( 846, 622 );

			pane.getChildren().addAll( buttonConfirm, buttonUno );

			// functions
			// press Confirm Button
			buttonConfirm.setOnMouseClicked( a ->
			{

				if( GameManager.getActivePlayerIndex() == 0 )
				{
					System.out.println( "---------" );
					buttonUno.setImage( new Image( "file:images/button_uno_disabled.png" ) );

					// if the player drew a card normally, he still has the chance to discard the
					// card
					if( GameManager.playerTurn( false ) )
					{
						buttonConfirm.setImage( new Image( "file:images/button_pass.png" ) );
						updateGUI();
					}
					else
					{
						buttonConfirm.setImage( new Image( "file:images/button_draw.png" ) );
						updateGUI();
						// GameManager.nextPlayer();
					}
				}
			} );
			// press uno button
			buttonUno.setOnMouseClicked( a ->
			{

				if( GameManager.getActivePlayerIndex() == 0 )
				{

					if( GameManager.getSelectedCards( 0 ).size() > 0 )
					{
						buttonUno.setImage( new Image( "file:images/button_uno_disabled.png" ) );
						buttonConfirm.setImage( new Image( "file:images/button_draw.png" ) );
						buttonUno.setDisable( true );

						GameManager.playerTurn( true );

						updateGUI();
						// GameManager.nextPlayer();
					}
				}
			} );
			break;
		case 3 :
			// Winner Screen
			// Settings
			// Author: Andreas Kiel, David Fröse

			// add rectangle and set background color
			r = new Rectangle();
			r.setWidth( 840 );
			r.setHeight( 500 );
			r.setX( 60 );
			r.setY( 100 );
			r.setFill( Color.rgb( 0, 0, 0, 0.5 ) );
			r.setArcWidth( 20 );
			r.setArcHeight( 20 );
			r.setStroke( Color.WHITE );

			// add and relocate elements
			Label labelTitle = new Label( "Winners are:" );
			Label[] labelWinner = { new Label( "" ), new Label( "" ), new Label( "" ),
				new Label( "" ) };
			Label[] labelWinnerNumber = { new Label( "" ), new Label( "" ), new Label( "" ),
				new Label( "" ) };
			ImageView[] imageViewWinner = { new ImageView(), new ImageView(), new ImageView(),
				new ImageView() };

			Button btnExit = new Button( "Restart Game!" );

			labelTitle.relocate( 100, 130 );

			labelWinner[0].relocate( 300, 230 );
			labelWinner[1].relocate( 300, 330 );
			labelWinner[2].relocate( 300, 430 );
			labelWinner[3].relocate( 300, 530 );

			labelWinnerNumber[0].relocate( 95, 220 );
			labelWinnerNumber[1].relocate( 95, 320 );
			labelWinnerNumber[2].relocate( 95, 420 );
			labelWinnerNumber[3].relocate( 95, 520 );

			imageViewWinner[0].relocate( 200, 205 );
			imageViewWinner[1].relocate( 200, 305 );
			imageViewWinner[2].relocate( 200, 405 );
			imageViewWinner[3].relocate( 200, 505 );

			// set label texts
			int count = 0;
			while( !GameManager.winnerQueue.isEmpty() )
			{
				Player winner = GameManager.winnerQueue.poll();
				imageViewWinner[count]
					.setImage( new Image( "file:images/avatar_" + winner.getAvatar() + ".png" ) );
				labelWinnerNumber[count].setText( "#" + (count + 1) );
				labelWinner[count].setText( winner.getName() );
				count++;
			}

			btnExit.relocate( 336, 650 );
			btnExit.setPrefWidth( 288 );

			// set text color
			labelTitle.setTextFill( Color.WHITE );
			labelWinnerNumber[0].setTextFill( Color.WHITE );
			labelWinnerNumber[1].setTextFill( Color.WHITE );
			labelWinnerNumber[2].setTextFill( Color.WHITE );
			labelWinnerNumber[3].setTextFill( Color.WHITE );
			labelWinner[0].setTextFill( Color.WHITE );
			labelWinner[1].setTextFill( Color.WHITE );
			labelWinner[2].setTextFill( Color.WHITE );
			labelWinner[3].setTextFill( Color.WHITE );

			// set font
			labelTitle.setFont( Font.font( "Arial", FontWeight.BOLD, 45 ) );
			labelWinnerNumber[0].setFont( Font.font( "Arial", FontWeight.BOLD, 45 ) );
			labelWinnerNumber[1].setFont( Font.font( "Arial", FontWeight.BOLD, 45 ) );
			labelWinnerNumber[2].setFont( Font.font( "Arial", FontWeight.BOLD, 45 ) );
			labelWinnerNumber[3].setFont( Font.font( "Arial", FontWeight.BOLD, 45 ) );
			labelWinner[0].setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );
			labelWinner[1].setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );
			labelWinner[2].setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );
			labelWinner[3].setFont( Font.font( "Arial", FontWeight.BOLD, 20 ) );

			// set text shadow effect
			shadow = new DropShadow();
			labelTitle.setEffect( shadow );
			labelWinner[0].setEffect( shadow );
			labelWinner[1].setEffect( shadow );
			labelWinner[2].setEffect( shadow );
			labelWinner[3].setEffect( shadow );

			pane.getChildren().addAll( r, labelTitle, btnExit );
			pane.getChildren().addAll( labelWinnerNumber );
			pane.getChildren().addAll( labelWinner );
			pane.getChildren().addAll( imageViewWinner );

			// functions
			btnExit.setOnAction( e ->
			{
				switchScene( 1 );
			} );
			break;
		}

	}

	/**
	 * Author: Dominik Assauer Opens a window to select the color of played card, if confirmButton
	 * was pressed, the cards will be played
	 * 
	 * @param cards         : ArrayList of selected cards
	 * @param buttonPressed : if confirm button was pressed
	 * @param unoButton     : if uno button was pressed
	 */
	public static void
		showColorSelection(ArrayList<Card> cards, boolean buttonPressed, boolean unoButton)
	{

		Pane p = new Pane();
		p.getStylesheets().add( "file:style.css" );

		Scene scene = new Scene( p, 360, 200 );
		Stage stage = new Stage();
		stage.setTitle( "ColorSelection" );
		stage.setScene( scene );

		colorSelection = new Button[4];

		for( int i = 0; i < colorSelection.length; i++ )
		{
			colorSelection[i] = new Button( "" );
		}
		int i = 0;

		for( Button button : colorSelection )
		{

			switch (i)
			{
			case 0 :
				button.setText( "Blue" );
				button.getStyleClass().add( "blueButton" );
				button.relocate( 20, 20 );
				break;
			case 1 :
				button.setText( "Red" );
				button.getStyleClass().add( "redButton" );
				button.relocate( 180, 20 );
				break;
			case 2 :
				button.setText( "Green" );
				button.getStyleClass().add( "greenButton" );
				button.relocate( 20, 120 );
				break;
			case 3 :
				button.setText( "Yellow" );
				button.getStyleClass().add( "yellowButton" );
				button.relocate( 180, 120 );
				break;
			}
			button.getStyleClass().add( "colorButton" );
			i++;
		}
		p.getChildren().addAll( colorSelection );
		stage.show();

		for( Card card : cards )
		{

			for( int j = 0; j < colorSelection.length; j++ )
			{
				final int cardColor = j + 1;
				colorSelection[j].setOnMouseClicked( a ->
				{
					card.changeColor( cardColor ); // Changes Color to blue
					card.setSelected( false );
					pane.setDisable( false );
					displayCards( 0 );

					if( buttonPressed )
					{
						card.setSelected( true );

						if( card == cards.get( cards.size() - 1 ) )
						{
							GameManager.playerTurn( unoButton );
						}
					}
					stage.hide();
				} );
			}
		}
	}

	/**
	 * Author: Dominik Assauer Opens a window to select the color of played card, if confirmButton
	 * was pressed, the cards will be played
	 * 
	 * @param card          : selected card
	 * @param buttonPressed : if confirm button was pressed
	 * @param unoButton     : if uno button was pressed
	 */
	public static void
		showColorSelection(Card card, boolean buttonPressed, boolean unoButton)
	{

		Pane p = new Pane();
		p.getStylesheets().add( "file:style.css" );

		Scene scene = new Scene( p, 360, 200 );
		Stage stage = new Stage();
		stage.setTitle( "ColorSelection" );
		stage.setScene( scene );

		colorSelection = new Button[4];

		for( int i = 0; i < colorSelection.length; i++ )
		{
			colorSelection[i] = new Button( "" );
		}
		int i = 0;

		for( Button button : colorSelection )
		{

			switch (i)
			{
			case 0 :
				button.setText( "Blue" );
				button.getStyleClass().add( "blueButton" );
				button.relocate( 20, 20 );
				break;
			case 1 :
				button.setText( "Red" );
				button.getStyleClass().add( "redButton" );
				button.relocate( 180, 20 );
				break;
			case 2 :
				button.setText( "Green" );
				button.getStyleClass().add( "greenButton" );
				button.relocate( 20, 120 );
				break;
			case 3 :
				button.setText( "Yellow" );
				button.getStyleClass().add( "yellowButton" );
				button.relocate( 180, 120 );
				break;
			}
			button.getStyleClass().add( "colorButton" );
			i++;
		}
		p.getChildren().addAll( colorSelection );
		stage.show();

		for( int j = 0; j < colorSelection.length; j++ )
		{
			final int cardColor = j + 1;
			colorSelection[j].setOnMouseClicked( a ->
			{
				card.changeColor( cardColor ); // Changes Color to blue
				card.setSelected( false );
				pane.setDisable( false );
				displayCards( 0 );

				if( buttonPressed )
				{
					card.setSelected( true );
					GameManager.playerTurn( unoButton );
				}
				stage.hide();
			} );
		}
	}

	/**
	 * Displays the top card of the discard pile
	 * 
	 * Author: David Fröse
	 * 
	 */
	private static void displayDiscardPile()
	{
		// remove old top card if there is one
		if( discardPile != null ) pane.getChildren().remove( discardPile );
		// get top card, create image and display it!
		Card card = CardAdmin.topCard();

		if( card != null )
		{
			discardPile = new ImageView( new Image( getSourceToCard( card ) ) );
			discardPile.relocate( 434, 255 );
			pane.getChildren().add( discardPile );
		}
	}

	/**
	 * Displays the cards of one of the players
	 * 
	 * Author: David Fröse
	 * 
	 * @param value : number of the player (0 = Main Player, 1 = Player 2, 2 = Player 3, 3 = Player
	 *              4)
	 */
	private static void displayCards(int _value)
	{
		Player player = GameManager.getPlayer( _value );
		if( player == null ) return; // if there is no player, don't display the cards!
		ArrayList<Card> cards = player.getHandCards();

		// Remove all current Card Images, before drawing new ones
		panePlayer[_value].getChildren().clear();

		// update number display for this player
		playerCardNumber[_value].setText( "" + cards.size() );

		int cardWidth = CARD_WIDTH;
		int handCardsWidth = HAND_CARDS_WIDTH;

		if( _value != 0 ) // if it isn't the Main Player we need smaller cards!
		{
			cardWidth = CARD_WIDTH_SMALL;
			handCardsWidth = HAND_CARDS_WIDTH_SMALL;
		}

		for( int i = 0; i < cards.size(); i++ )
		{
			Card card = cards.get( i );
			String source = getSourceToCard( card );

			if( _value != 0 )
			{
				source = "file:images/card_0.png"; // if it isn't the Main Player show the back of
													// the card
			}
			ImageView imageCard = new ImageView( new Image( source ) );
			imageCard.setManaged( false ); // StackPane does not determine the position of the card
			panePlayer[_value].getChildren().add( imageCard );

			if( cards.size() == 1 )
			{
				// just center the one card
				double x = handCardsWidth / 2 - cardWidth / 2;
				imageCard.setLayoutX( (int) x );
			}
			else if( handCardsWidth / cards.size() >= cardWidth )
			{
				// next to each other
				double x = ((handCardsWidth / cards.size()) * i
					+ (handCardsWidth / cards.size()) / 2) - cardWidth / 2;
				imageCard.setLayoutX( (int) x );
			}
			else
			{
				// overlapping
				double x = (handCardsWidth / cards.size()) * i
					- ((cardWidth - (((double) handCardsWidth / cards.size()))) / cards.size()) * i;
				imageCard.setLayoutX( (int) x );
			}

			// if a card is selected, decrease its Y position by -30 so that it's higher in the GUI
			// and that the player gets notified that a card is selected
			if( card.isSelected() )
			{
				imageCard.setLayoutY( -30 );
				imageCard.setStyle(
					"-fx-effect: dropshadow(three-pass-box, rgba(255,255,0,1), 15, 0.5, 0, 0)"
				);
			}
		}

		for( int i = 0; i < panePlayer[0].getChildren().size(); i++ )
		{

			ImageView currentCardImage = (ImageView) panePlayer[0].getChildren().get( i );
			Card currentCard = GameManager.getPlayerHandCards( 0 ).get( i );

			// when the player hovers over a card, the card's vertical position decreases by 30 so
			// that it moves up on the GUI
			currentCardImage.setOnMouseMoved( e ->
			{

				if( GameManager.getActivePlayerIndex() == 0 )
				{
					if( e.getSceneY() < panePlayer[0].getLayoutY() + CARD_HEIGHT - 30 )
						currentCardImage.setLayoutY( -30 );
				}
			} );
			// if a player clicks on a card while hovering over it with the mouse, the card will
			// stay in its position to let the player know that the card is selected
			currentCardImage.setOnMousePressed( f ->
			{

				if( GameManager.getActivePlayerIndex() == 0 )
				{

					if( !currentCard.isSelected() )
					{

						if( f.isSecondaryButtonDown() && (currentCard instanceof WildCard
							|| currentCard instanceof WildDrawCard) )
						{
							showColorSelection( currentCard, false, false );
							pane.setDisable( true );
						}
						currentCard.setSelected( true );
						currentCardImage.setStyle(
							"-fx-effect: dropshadow(three-pass-box, rgba(255,255,0,1), 15, 0.5, 0, 0)"
						);
						currentCardImage.setLayoutY( -30 );
						buttonUno.setDisable( false );
						buttonUno.setImage( new Image( "file:images/button_uno.png" ) );
						buttonConfirm.setImage( new Image( "file:images/button_confirm.png" ) );
					}
					else
					{

						// if( currentCard instanceof WildCard || currentCard instanceof
						// WildDrawCard )
						// {
						// currentCard.changeColor( 0 );
						// }
						currentCard.setSelected( false );
						currentCardImage.setStyle( "-fx-effect: none" );

						if( GameManager.countSelectedCards( 0 ) == 0 )
						{
							buttonUno.setDisable( true );
							buttonUno
								.setImage( new Image( "file:images/button_uno_disabled.png" ) );

							if( GameManager.getPlayer( 0 ).getPassed() )
							{
								buttonConfirm
									.setImage( new Image( "file:images/button_pass.png" ) );
							}
							else
							{
								buttonConfirm
									.setImage( new Image( "file:images/button_draw.png" ) );
							}
						}
					}
				}
			} );

			// on mouse exit, the card's vertical position goes back to 0 if it is not selected
			currentCardImage.setOnMouseExited( e ->
			{

				if( !currentCard.isSelected() )
				{
					currentCardImage.setLayoutY( 0 );
				}
			} );
		}
	}

	/**
	 * Updates whole GUI
	 * 
	 * Author: Dominik Assauer
	 * 
	 */
	public static void updateGUI()
	{
		// update all card displays
		for( int i = 0; i < GameManager.getPlayerAmount(); i++ )
			displayCards( i );
		// update discardPile
		displayDiscardPile();

		// display active player highlighted
		// reset all styles
		for( int i = 0; i < panePlayerStatus.length; i++ )
		{
			if( panePlayerStatus[i] != null ) panePlayerStatus[i].setStyle( "-fx-effect: none" );
		}
		// get index of active player
		panePlayerStatus[GameManager.getActivePlayerIndex()]
			.setStyle( "-fx-effect: dropshadow(three-pass-box, rgba(255,255,0,1), 15, 0.5, 0, 0)" );
	}

	/**
	 * Returns the correct path of the image to a given Card Object
	 * 
	 * Author: David Fröse
	 * 
	 */
	private static String getSourceToCard(Card _card)
	{
		String cardColor = String.valueOf( _card.getColor() );
		String cardValue = String.valueOf( _card.getValue() );
		if( _card instanceof SkipCard ) cardValue = "x"; // x represents skip card in image name
		if( _card instanceof ReverseCard ) cardValue = "y"; // y represents reverse card in image
															// name
		if( _card instanceof DrawCard ) cardValue = "z"; // z represents draw card in image name
		if( _card instanceof WildCard ) cardValue = "wildCard"; // wildCard represents wild card in
																// image name
		if( _card instanceof WildDrawCard ) cardValue = "wildCardDraw"; // wildCardDraw represents
																		// draw wild card in image
																		// name
		return "file:images/card_" + cardColor + "_" + cardValue + ".png";
	}
}
