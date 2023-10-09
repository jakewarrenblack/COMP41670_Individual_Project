import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class
Deck {
    Card[] spades = new Card[13];
    Card[] clubs = new Card[13];
    Card[] diamonds = new Card[13];
    Card[] hearts = new Card[13];
    ArrayList<Card> cards = new ArrayList<>();

    // contains our 7 piles

    private ArrayList<ArrayList<Card>> tableau;

    enum Suit {SPADES, HEARTS, CLUBS, DIAMONDS};
    enum Rank {ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING}

    public static class Card{
        // suits: there are 13 of each type, 52 in total (spades, diamonds, hearts, clubs)
        // ranks: consists of ACE, 2-10, JACK, QUEEN, KING. only one of each of these per suit.

        private final Suit suit;
        private final Rank rank;
        private boolean isFaceDown;


        // oddly (to me), java does not support default params in constructors, so we need to actually provide a value here. I'd like it to be false by default.
        Card(Suit s, Rank r, boolean fd){
            this.suit = s;
            this.rank = r;
            this.isFaceDown = fd;
        }

        Suit getSuit(){
            return this.suit;
        }

        Rank getRank(){
            return this.rank;
        }

        boolean isFaceDown(){
            return this.isFaceDown;
        }

        void setIsFaceDown(boolean isFaceDown){
            this.isFaceDown = isFaceDown;
        }
    }

    Deck(){
        for(int i=0; i<13; i++){
            // so 13 of each = 52 total cards
            this.clubs[i] = new Card(Suit.CLUBS, Rank.values()[i], true);
            this.spades[i] = new Card(Suit.SPADES, Rank.values()[i], true);
            this.hearts[i] = new Card(Suit.HEARTS, Rank.values()[i], true);
            this.diamonds[i] = new Card(Suit.DIAMONDS, Rank.values()[i], true);
        }

        // I now want to combine these suits in to 52 total cards
        ArrayList<Card> all = new ArrayList<Card>(Arrays.asList(this.clubs));
        all.addAll(Arrays.asList(this.spades));
        all.addAll(Arrays.asList(this.hearts));
        all.addAll(Arrays.asList(this.diamonds));

        // From an arraylist back into an array
        this.cards = all;
    }

    void getDeck(){
        // iterate through each suit of cards; spades, diamonds, etc
        for(Card card: this.cards){
            System.out.println("\n");
            // iterate through each card in each suit
            System.out.println("Suit: " + card.getSuit() + " Rank: " + card.getRank());
        }
    }

    void printTableau(){
        int idx = 0;
        for(ArrayList<Card> pile: this.tableau){
            System.out.println("\nPile " + Integer.toString(idx+1) + ":\n");
            for(Card c: pile){
                if(c.isFaceDown){
                    System.out.println("Unknown card");
                }
                else{
                    System.out.println(c.getRank() + " of " + c.getSuit());
                }

            }
            idx++;
        }
    }

    ArrayList<ArrayList<Card>> getTableau(){
        return this.tableau;
    }

    // we want to deal out 7 piles, pile 1 has a single card, pile 2 has 2, and so on
    ArrayList<ArrayList<Card>> deal(){
        Collections.shuffle(this.cards);

        // will be a list containing arrays of cards of varying size, our piles
        // should be 7 piles, so a list of lists
        ArrayList<ArrayList<Card>> piles = new ArrayList<>();


        for(int i=0; i<7; i++){
            piles.add(new ArrayList<Card>());
            // increment up until we've reached the size of i, so we end up with a pile of size 1, 2, up to 7
            for(int j=0; j<=i; j++){
                // this line will remove the card object at this index, but also return that object, so we can add to the pile and remove from the cards at the same time
                // this means we end up with this.cards having the value '24', all the cards we haven't used to build our piles
                piles.get(i).add(this.cards.remove(this.cards.size() - 1)); // Remove cards from overall cards and add to the piles
            }
        }

        // the last card in the pile is face up
        for(ArrayList<Card> pile: piles){
            Card lastCard = pile.get(pile.size() -1);
            lastCard.setIsFaceDown(false);
        }

        this.tableau = piles;

        return piles;
    }
}
