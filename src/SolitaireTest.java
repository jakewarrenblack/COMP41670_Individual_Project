import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

public class SolitaireTest {
    private Deck deck;
    private ArrayList<Pile> lanes;
    private Board board;

    private Main.GameState gameState;

    @BeforeEach
    public void setUpStandard() {
        deck = new Deck();
        lanes = deck.getLanes();
        board = new Board(deck, lanes);
    }

    @Test
    public void testStandardSetup() {
        // Expect these cards to be remaining after dealing the lanes and foundations
        assertEquals(24, deck.getDeck().getSize());

        assertEquals(7, lanes.size());

        for(int i=0; i<lanes.size(); i++){
            // 1 card, 2 cards, etc
            assertEquals(i+1, lanes.get(i).getSize());
        }
    }


    public void setUpWin(){
        deck = new Deck();
        lanes = deck.getLanes();
        board = new Board(deck, lanes, generateDummyFoundations());
        gameState = Main.GameState.ACTIVE;

    }

    @Test
    public void testWin(){
        setUpWin();
        if(gameWon(board)){
            gameState = Main.GameState.WON;
        }

        assertEquals(Main.GameState.WON, gameState);
    }

    static boolean gameWon(Board b){
        ArrayList<Pile> foundation = b.getFoundation();

        // If all 4 foundation piles contain 13 cards, the game has been won
        for(Pile p: foundation){
            if(p.getSize() != 13){
                return false;
            }
        }
        return true;
    }

     //This provides a specific card setup for testing/demonstrating the movement of multiple cards at once
     ArrayList<Pile> generateDummyLanes(){
        ArrayList<Pile> lanes = new ArrayList<>();

        ArrayList<Deck.Card> firstCard = new ArrayList<>();
        firstCard.add(new Deck.Card(Deck.Suit.HEARTS, Deck.Rank.ACE, false));

        ArrayList<Deck.Card> secondCard = new ArrayList<>();
        secondCard.add(new Deck.Card(Deck.Suit.HEARTS, Deck.Rank.FIVE, false));

        ArrayList<Deck.Card> thirdCard = new ArrayList<>();
        thirdCard.add(new Deck.Card(Deck.Suit.DIAMONDS, Deck.Rank.THREE, false));

        // testing move multiple cards, then move one card from the top to another pile
        ArrayList<Deck.Card> fourthCard = new ArrayList<>();
        fourthCard.add(new Deck.Card(Deck.Suit.CLUBS, Deck.Rank.FOUR, false));

        lanes.add(0, new Pile(1, firstCard));
        lanes.add(1, new Pile(2, secondCard));
        lanes.add(2, new Pile(3, thirdCard));
        lanes.add(3, new Pile(4, fourthCard));

        return lanes;
    }


    public ArrayList<Pile> generateDummyFoundations(){
        ArrayList<Pile> foundations = new ArrayList<>();

        for(int i=0; i<4; i++){
            ArrayList<Deck.Card> dummyCards = new ArrayList<>();
            for(int j=0; j<13; j++){
                // The cards are immaterial, we're testing a win condition whereby all 4 foundations contain 13 cards
                dummyCards.add(new Deck.Card(Deck.Suit.CLUBS, Deck.Rank.FIVE, false));
            }
            foundations.add(i, new Pile('D', dummyCards));
        }
        return foundations;
    }

    // Testing 3 things:
    // movement between lanes
    // movement of multiple cards
    // movement from lanes to suits
    @Test
    public void testCardMovement(){
        this.lanes = generateDummyLanes();
        board = new Board(deck, lanes);

        // Move FIVE of HEARTS to ACE of HEARTS. Not allowed, no points for this.
        assertEquals(0, board.moveCard('2', '1'));


        // Move ACE of HEARTS to foundation H, expect 20 points
        assertEquals(20, board.moveCard('1', 'H'));

        // Move THREE of DIAMONDS to FOUR of CLUBS, expect 5 points
        assertEquals(5, board.moveCard('3', '4'));

        // Now move (FOUR of CLUBS, THREE of DIAMONDS) to FIVE of HEARTS
        assertEquals(10, board.moveCard('4', '2', '2'));

    }
}
