import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Board extends Deck {
    private final Pile deck;
    private final ArrayList<Pile> lanes;
    private final ArrayList<Pile> foundation = new ArrayList<>(4);

    private Card[] drawnFromDeck = new Card[3];

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

        // cards in tableau / lanes must be in order
        // cards in foundation piles must be in order A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K
        // needs to alternate red / black
        // cant have more than 13 cards in a pile
    }

    // get the rank of the card
    // find that rank in the order array
    // if there's a card preceding the card you're trying to place, check if A followed by B is valid
    // if there's no other card preceding it, if the card is being placed in the foundation array, it needs to be an ACE
    // if the card is being placed in one of the lanes/tableau, it needs to be a KING (reverse of the foundation order)



    Pile findPile(char label){
        if(label == 'P'){
            // it's the remaining card pile, then
            return this.deck;
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
    private static int lastAdded;


    // FIXME: Bug? Seems to replace 3rd, then 1st, then eventually 2nd, after a few attempts
    Card[] drawFromDeck(){
        final boolean noCardsNull = this.drawnFromDeck[0] != null && this.drawnFromDeck[1] != null && this.drawnFromDeck[2] != null;

        for(int i=0; i<this.drawnFromDeck.length; i++){
            if(noCardsNull){

                this.drawnFromDeck[lastAdded] = this.deck.removeCard(this.deck.getSize()-1);

                if(lastAdded == 2){
                    lastAdded = 0;
                }
                else{
                    lastAdded++;
                }

                break;
            }
            else{
                if(this.drawnFromDeck[i] == null){
                    this.drawnFromDeck[i] = this.deck.removeCard(this.deck.getSize()-1);
                    lastAdded = i;
                    break;
                }
            }
        }

    return this.drawnFromDeck;
    }

    /*
    * Return true if move is legal
     */
    // FIXME: Overcomplicating validation? Pass two destinations,
    // figure out the cards at those places,
    // see if they're compatible?
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
