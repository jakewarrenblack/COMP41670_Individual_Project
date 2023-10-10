import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Deck deck = new Deck();
        // Populate the 7 lanes and remove those cards from the main deck.
        ArrayList<Pile> lanes = deck.getLanes();

        // Print out the lane values
        deck.printTableau();

        Board b = new Board(deck, lanes);
        b.getFoundation();

        Deck.Card test = new Deck.Card(Deck.Suit.SPADES, Deck.Rank.FIVE, false);
        b.moveCard(test, 'a', 'b');
    }
}