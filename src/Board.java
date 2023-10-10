import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Set;

public class Board extends Deck {
    Pile deck;
    ArrayList<Pile> lanes;
    ArrayList<Pile> foundation = new ArrayList<>(4);

    // wrapper class needed here, this doesn't support `char`
    // the set allows us to use the concise .contains method, which is why I've opted for it
    private static final Set<Character> VALID_MOVES = Set.of('1', '2', '3', '4', '5', '6', '7', 'D', 'H', 'C', 'S', 'P');

    // these will be empty initially, unless there are aces in our initial tableau, since they should be placed in the foundation piles first anyway
    Board(Deck d, ArrayList<Pile> lanes){
        // The remaining cards after dealing out the cards for the lanes (24 of them).
        this.deck = d.getDeck();
        this.lanes = lanes;

        System.out.println("\n" + this.deck.getSize() + " cards in the draw pile (P).");

        // diamonds
        this.foundation.add(0, new Pile('D', new ArrayList<>()));
        // hearts
        this.foundation.add(1, new Pile('H', new ArrayList<>()));
        // clubs
        this.foundation.add(2, new Pile('C', new ArrayList<>()));
        // spades
        this.foundation.add(3, new Pile('S', new ArrayList<>()));
    }

    boolean validateMove(char startPosition, char endPosition){
        // Immediately make sure that both chars actually relate to one of our card piles
        return VALID_MOVES.contains(startPosition) && VALID_MOVES.contains(endPosition);
    }

    Pile findPile(char label){
        if(label == 'P'){
            // it's the remaining card pile, then
            return this.deck;
        }
        else {
            int n = Character.getNumericValue(label);
            if(n > 0 && n < 10){
                // in this case, the label corresponds with one of the lane labels
                return this.lanes.get(n);
            }
            else{
                Pile returnPile = null;
                // finally, it's a foundation / 4 suit card then
                switch(label){
                    case('D') -> returnPile = this.foundation.get(0);
                    case('H') -> returnPile = this.foundation.get(1);
                    case('C') -> returnPile = this.foundation.get(2);
                    case('S') -> returnPile = this.foundation.get(3);
                }
                return returnPile;
            }
        }
        
    }

    void moveCard(char startPosition, char endPosition){
        // e.g. move = '7D' -> move NINE of HEARTS out of lane 7 and into foundation pile D

        if(!validateMove(startPosition, endPosition)){
            System.out.println("Illegal move!");
            return;
        }

        Pile startPile = findPile(startPosition);
        Card c = startPile.getCard(startPile.getSize()-1); // always using the card on top of the pile
        Pile endPile = findPile(endPosition);

        // startPile.remove(c)
        // endPile.add(c)

        //Card nextCard = tableauPile.getCard(tableauPile.indexOf(tableauPileTopCard) - 1);
        //nextCard.setIsFaceDown(false);
    }

    void getFoundation(){
        System.out.println("\nFoundation piles:\n");

        for(Pile p: this.foundation){
            System.out.println(p.getLabel() + "\n");
            for(Card card: p.getCards()){
                System.out.println(card.getRank() + " of " + card.getSuit());
            }
        }
    }
}
