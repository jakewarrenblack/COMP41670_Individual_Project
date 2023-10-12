import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Board extends Deck {
    private final Pile deck;
    private final ArrayList<Pile> lanes;
    private final ArrayList<Pile> foundation = new ArrayList<>(4);

   // private Card[] drawnFromDeck = new Card[3];

    private final Pile drawnFromDeck = new Pile('P', new ArrayList<>(3));

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
            //return new Pile('P', new ArrayList<>(List.of(this.drawnFromDeck)));
            return this.drawnFromDeck;
        }
        else {
            int n = Character.getNumericValue(label);
            if(n > 0 && n < 10){
                // in this case, the label corresponds with one of the lane labels
                return this.lanes.get(n-1);
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
    private static int lastIndexReplaced = 0;

    // Draw cards from the leftover deck and replace them cyclically
    // Max of 3 drawn at one time
    // so, draw 1..2..3, then replace 1..2..3
    Pile drawFromDeck(){
        if(this.deck.getSize() == 0){
            System.out.println("Deck is empty!");
            return new Pile('P', new ArrayList<>());
        }

        if(this.drawnFromDeck.getSize() != 3){
            this.drawnFromDeck.addCard(this.drawnFromDeck.getSize(), this.deck.removeCard(this.deck.getSize()-1));
        }
        else{
            this.drawnFromDeck.removeCard(lastIndexReplaced);
            this.drawnFromDeck.addCard(lastIndexReplaced, this.deck.removeCard(this.deck.getSize()-1));

            if(lastIndexReplaced == 2){
                lastIndexReplaced = 0;
            }
            else{
                lastIndexReplaced++;
            }
        }

    return this.drawnFromDeck;
    }

    /*
    * Return true if move is legal
     */
    boolean moveCard(char startPosition, char endPosition){
        // e.g. move = '7D' -> move NINE of HEARTS out of lane 7 and into foundation pile D

        if(!validateMove(startPosition, endPosition)){
            System.out.println("Illegal move!");
            return false;
        }

        Pile startPile = findPile(startPosition);
        Pile endPile = findPile(endPosition);
        Card thisCard = null;

        // if no card present at this index (i.e, there's no cards in the pile, that's illegal)
        if((startPile.getSize()-1) >= 0){
            thisCard = startPile.getCard(startPile.getSize()-1); // always using the card on top of the pile
        }
        else{
            System.out.println("Illegal move! No cards in pile " + startPosition);
            return false;
        }


        // a copy of the destination pile, to test the player's move, without actually making it
        Pile tempPile = new Pile(endPile);

        // FIXME: A lot of these predicate functions should throw errors, and be called from a try/catch block
        if(!endPile.validateOrder(thisCard, tempPile)){
            System.out.println("Invalid move!");
            return false;
        }
        // move is legal, go ahead with flipping previous card, and adding card to pile


        // need to check if there IS a card before this one, i.e., thisCard is not the first card in the deck.
        if(startPile.indexOf(thisCard) != 0){
            startPile.getCard(startPile.getSize()-2).setIsFaceDown(false); // flip the next card
        }

        startPile.removeCard(thisCard);
        endPile.addCard(thisCard);

        return true;
    }

    ArrayList<Pile> getFoundation(){
        return this.foundation;
    }

    void printFoundation(){
        System.out.println("\nFoundation piles:\n");

        for(Pile p: this.foundation){
            System.out.println(p.getLabel() + "\n");
            for(Card card: p.getCards()){
                System.out.print(card.getRank() + " of " + card.getSuit() + ", ");
            }
        }
    }
}
