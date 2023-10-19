import java.lang.reflect.Array;
import java.util.*;

public class Board extends Deck {
    private final Pile deck;
    private final ArrayList<Pile> lanes;
    private ArrayList<Pile> foundation = new ArrayList<>(4);
    private final Pile drawnFromDeck = new Pile('P', new ArrayList<>(3));

    // wrapper class needed here, this doesn't support `char`
    // the set allows us to use the concise .contains method, which is why I've opted for it
    private static final Set<Character> VALID_MOVES = Set.of('1', '2', '3', '4', '5', '6', '7', 'D', 'H', 'C', 'S', 'P');

    public Board(Deck d, ArrayList<Pile> lanes){
        // The remaining cards after dealing out the cards for the lanes (24 of them).
        this.deck = d.getDeck();
        this.lanes = lanes;

        System.out.println("☆*:.｡.  Cards have been dealt! There are " + this.deck.getSize() + " cards left over in the draw pile (P)   .｡.:*☆");
        // diamonds
        this.foundation.add(0, new Pile('D', new ArrayList<>()));
        // hearts
        this.foundation.add(1, new Pile('H', new ArrayList<>()));
        // clubs
        this.foundation.add(2, new Pile('C', new ArrayList<>()));
        // spades
        this.foundation.add(3, new Pile('S', new ArrayList<>()));
    }

    // Constructor for creating a custom deck for testing
    public Board(Deck d, ArrayList<Pile> lanes, ArrayList<Pile> customFoundation) {
        this.deck = d.getDeck();
        this.lanes = lanes;

        this.foundation = customFoundation;
    }

    private boolean validateMove(char startPosition, char endPosition){
        // Immediately make sure that both chars actually relate to one of our card piles
        return VALID_MOVES.contains(startPosition) && VALID_MOVES.contains(endPosition);
    }


    private Pile findPile(char label){
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
    public Pile drawFromDeck(){
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

    // i.e, instead of passing this value around as a HashSet every time, or keeping track in Main.java
    // this needs to be on a per-Pile basis,
    private boolean isUniqueCombination(ArrayList<Card> cards, Pile p){
        return !p.getUniqueCardCombinations().contains(cards);
    }

    /*
    * Return true if move is legal
     */
    public int moveCard(char...move){
        // e.g. move = '7D' -> move NINE of HEARTS out of lane 7 and into foundation pile D
        char startPosition = move[0];
        char endPosition = move[1];
        int nCards;
        int score = 0;

        // try/catch to allow for move[2] index to be nonexistent
        try{
            nCards = Character.getNumericValue(move[2]);
        }
        catch(IndexOutOfBoundsException e){
            nCards = 1;
        }

        if(!validateMove(startPosition, endPosition)){
            System.out.println("Illegal move!");
            return 0;
        }

        Pile startPile = findPile(startPosition);
        Pile endPile = findPile(endPosition);
        Card thisCard = null;
        ArrayList<Card> theseCards = new ArrayList<>();

        // if no card present at this index (i.e, there's no cards in the pile, that's illegal)
        // (making sure there are enough cards to accommodate the number of cards the user is trying to move)
        if((startPile.getSize()-1) >= nCards-1){
            if(nCards == 1){
                thisCard = startPile.getCard(startPile.getSize()-1); // always using the card on top of the pile
            }
            else{
                // If user is adding multiple cards, add them all on top of the destination pile
                theseCards.addAll(startPile.getCards());
            }

        }
        else{
            System.out.println("Illegal move! No cards in pile " + startPosition);
            return 0;
        }


        // a copy of the destination pile, to test the player's move, without actually making it
        Pile tempPile = new Pile(endPile);

        // validateOrder accepts an ArrayList of cards, so even if there's only one, I'll wrap it in an arraylist
        if(theseCards.isEmpty()){
            theseCards.add(thisCard);
        }

        for(Card c: theseCards){
            if(!endPile.validateOrder(c, tempPile)){
                return 0;
            }
            c.setIsFaceDown(false);
        }

        // if we reach this point...move is legal, go ahead with flipping previous card, and adding card to pile

        // need to check if there IS a card before this one, i.e., thisCard is not the first card in the deck, otherwise there's no 'next' card to flip over
        if(startPile.indexOf(theseCards.get(0)) != 0){
            startPile.getCard(startPile.getSize()-(nCards+1)).setIsFaceDown(false); // flip the next card
        }

        startPile.removeMultiple(theseCards);

        if(!endPile.addMultiple(theseCards)){
            return 0;
        }

        // at this point, determine the type of move made
        // can do this by comparing the start and destination piles
        List<Character> foundationLabels = Arrays.asList('D', 'H', 'C', 'S');
        int startLane = Character.getNumericValue(startPosition);

        //Pile (P) to suit (D/H/C/S) -> 10 points
        if(startPosition == 'P'){
            if(foundationLabels.contains(endPosition)){
                score += 10;
            }
        }
        else{
            // if startPosition character is between 1 and 9 inclusive, Character.getNumericValue() will return int 1-9
            // otherwise, the value returned will be 10 or higher
            if(startLane > 0 && startLane < 10){
                // Lane (1-7) to suit (D/H/C/S) -> 20 points
                if(foundationLabels.contains(endPosition)){
                    score += 20;
                }
                // Lane (1-7) to Lane (1-7) -> 5 points
                else if(Character.getNumericValue(endPosition) > 0 && Character.getNumericValue(endPosition) < 10){
                    score += 5;
                }
            }
        }

        // *Points are awarded if a specific combination of cards is moved to a pile for the first time*
        if(isUniqueCombination(theseCards, endPile)){
            endPile.addUniqueCardCombination(theseCards);
        }
        // if this combination has been seen before, don't increment the score
        else{
            System.out.println("You've created this combination already. No points for that!");
            return 0;
        }

        // If user moved 2/3 cards, double or triple their points
        return score * nCards;
    }


    public ArrayList<Pile> getFoundation(){
        return this.foundation;
    }

    public void printFoundation(){
        System.out.println("\nFoundation piles:\n");

        for(Pile p: this.foundation){
            System.out.println("\n" + p.getLabel() + ":");
            for(Card card: p.getCards()){
                if(p.getCards().indexOf(card) == p.getSize()-1){
                    System.out.print(card.getRank() + " of " + card.getSuit() + " " + card.getIcon());
                }
                else{
                    System.out.print(card.getRank() + " of " + card.getSuit() + " " + card.getIcon() + ", ");
                }
            }
            System.out.println();
        }
    }
}
