import java.util.ArrayList;
import java.util.Arrays;

public class Board extends Deck {
    // want this to have the attributes of the deck, and a bit more
    // should also inherit the deck itself (for the remaining 23 cards)
    // inherits the tableau (the 7 piles)

    // it needs to have 4 foundation piles too:

    Deck deck;
    ArrayList<ArrayList<Card>> foundation = new ArrayList<>(4);



    // these will be empty initially, unless there are aces in our initial tableau, since they should be placed in the foundation piles first anyway
    Board(Deck d){
        this.deck = d;
        // spades
        this.foundation.add(0, new ArrayList<>());
        // clubs
        this.foundation.add(1, new ArrayList<>());
        // diamonds
        this.foundation.add(2, new ArrayList<>());
        // hearts
        this.foundation.add(3, new ArrayList<>());

    }

    /*
    *  The game begins with 7 piles of cards (the tableau)
    *  Above that are 4 empty piles (the foundation piles)
    *  Straight away, if there are any ACE cards facing up in our tableau, they can be added to the foundation piles
    *  When a card is removed from the tableau, the one below it is flipped to face upwards. If THAT card is ALSO an ace, that can go straight to the foundation pile.
    *  Hence, the recursion.
    */
//    void handleTopCard(Deck deck, Card topCard){
//        // if there's a face-up ACE in the tableau, add it to the foundation piles
//        if(!topCard.isFaceDown() && topCard.getRank() == Rank.ACE){
//           // spades, clubs, diamonds, hearts (0,1,2,3)
//            switch(topCard.getSuit()){
//                case SPADES -> foundation.get(0).add(topCard);
//                case CLUBS -> foundation.get(1).add(topCard);
//                case DIAMONDS -> foundation.get(2).add(topCard);
//                case HEARTS -> foundation.get(3).add(topCard);
//            }
//
//            // this returns the card below the one we removed
//            Card nextCard = this.deck.getTableau().removeCard(topCard);
//        }
//    }

    void moveCard(Card card){
        // we've added an ace to our foundation pile, now we need to remove it from the tableau...
        for(Pile tableauPile: this.deck.getTableau()){
            Card tableauPileTopCard = tableauPile.getCard(tableauPile.getSize()-1);

            // this is the card we just added to our foundation piles. we want to drop it from the tableau.
            if(tableauPileTopCard == card){
                tableauPile.removeCard(card);
                // then flip the card below it
                Card nextCard = tableauPile.getCard(tableauPile.indexOf(tableauPileTopCard)-1);
                nextCard.setIsFaceDown(false);
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
