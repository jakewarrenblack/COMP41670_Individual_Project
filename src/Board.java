import java.util.ArrayList;
import java.util.Arrays;

public class Board extends Deck {
    // want this to have the attributes of the deck, and a bit more
    // should also inherit the deck itself (for the remaining 23 cards)
    // inherits the tableau (the 7 piles)

    // it needs to have 4 foundation piles too:
    ArrayList<ArrayList<Card>> foundation = new ArrayList<>(4);

    // these will be empty initially, unless there are aces in our initial tableau, since they should be placed in the foundation piles first anyway
    Board(Deck deck){
        this.foundation.add(0, new ArrayList<>());
        this.foundation.add(1, new ArrayList<>());
        this.foundation.add(2, new ArrayList<>());
        this.foundation.add(3, new ArrayList<>());

        // set up the foundation piles
        for(ArrayList<Card> pile: deck.getTableau()){
            Card topCard = pile.get(pile.size() - 1);

            // if there's a face-up ace in the tableau, add it to the foundation piles
            if(!topCard.isFaceDown() && topCard.getRank() == Rank.ACE){
                // add this card to whichever foundation pile has the least number of cards in it (so ascending order)
                int[] sizes = new int[4];
                for(int i=0; i<foundation.size(); i++){
                    sizes[i] = foundation.get(i).size();
                }

                int smallest = sizes[0];
                for(int i = 1; i < sizes.length; i++){
                    if(sizes[i] < smallest){
                        smallest = sizes[i];
                    }
                }

                ArrayList<Card> pileToAddTo = foundation.get(smallest);
                pileToAddTo.add(topCard);
            }
        }
    }

    void getFoundation(){
        System.out.println("\nFoundation piles:\n");
        for(int i=0; i<this.foundation.size(); i++){
            System.out.println("Pile " + i);
            for(int j=0; j<this.foundation.get(i).size(); j++){
                Card currentCard = this.foundation.get(i).get(j);
                System.out.println(currentCard.getRank() + " of " + currentCard.getSuit());
            }
        }
    }
}
