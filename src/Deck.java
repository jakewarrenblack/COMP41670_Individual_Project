import java.util.ArrayList;
import java.util.Collections;

public class
Deck {
    private final ArrayList<Card> deck = new ArrayList<>();

    private ArrayList<Pile> lanes;

    public enum Suit {SPADES, HEARTS, CLUBS, DIAMONDS}

    public enum Rank {ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING}

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

        String getIcon(){
            String icon = null;

            switch(this.suit){
                case SPADES -> icon = "\u2660";
                case HEARTS -> icon = "\u2665";
                case CLUBS -> icon = "\u2663";
                case DIAMONDS -> icon = "\u2666";
            }

            return icon;
        }
    }

   public Deck(){

       Pile spades = new Pile('S', new ArrayList<>(13));
       Pile clubs = new Pile('C', new ArrayList<>(13));
       Pile diamonds = new Pile('D', new ArrayList<>(13));
       Pile hearts = new Pile('H', new ArrayList<>(13));
       for(int i = 0; i<13; i++){
            // so 13 of each = 52 total cards
            clubs.addCard(new Card(Suit.CLUBS, Rank.values()[i], true));
            spades.addCard(new Card(Suit.SPADES, Rank.values()[i], true));
            hearts.addCard(new Card(Suit.HEARTS, Rank.values()[i], true));
            diamonds.addCard(new Card(Suit.DIAMONDS, Rank.values()[i], true));
        }

        // I now want to combine these suits in to 52 total cards
        this.deck.addAll(clubs.getCards());
        this.deck.addAll(spades.getCards());
        this.deck.addAll(hearts.getCards());
        this.deck.addAll(diamonds.getCards());

        this.createLanes();
    }

    public Deck(ArrayList<Pile> customLanes){
        this.lanes = customLanes;
    }

    Pile getDeck(){
        // more convenient if I convert to Pile, since that has a label associated with it, which I can use to determine where the user wants to move to/from
        return new Pile('P', this.deck);
    }

    public void printLanes(){
        for(Pile pile: this.lanes){
            System.out.println("\n" + pile.getLabel() + ":");
            for(Card c: pile.getCards()){
                if(c.isFaceDown){
                    continue;
                }
                if(pile.getCards().indexOf(c) == pile.getSize()-1){
                    System.out.print(c.getRank() + " of " + c.getSuit() + " " + c.getIcon());
                }
                else{
                    System.out.print(c.getRank() + " of " + c.getSuit() + " " + c.getIcon() + ", ");
                }

            }
            System.out.println();
        }
    }

    /*
    * Shuffles the cards, populates the 7 lanes, and removes those cards from the overall deck.
    */
   public void createLanes(){
        Collections.shuffle(this.deck);

        // will be a list containing arrays of cards of varying size, our piles
        // should be 7 piles, so a list of lists
        ArrayList<Pile> piles = new ArrayList<>();


        for(int i=0; i<7; i++){
            // label the foundation piles 1-7
            piles.add(new Pile(i+1, new ArrayList<>()));
            // increment up until we've reached the size of i, so we end up with a pile of size 1, 2, up to 7
            for(int j=0; j<=i; j++){

                // this line will remove the card object at this index, but also return that object, so we can add to the pile and remove from the cards at the same time
                // this means we end up with this.cards having the value '24', all the cards we haven't used to build our piles
                piles.get(i).addCard(this.deck.remove(this.deck.size() - 1)); // Remove cards from overall cards and add to the piles
            }
        }

        // the last card in the pile is face up
        for(Pile pile: piles){
            Card lastCard = pile.getCard(pile.getSize() -1);
            lastCard.setIsFaceDown(false);
        }


        this.lanes = piles;
    }

    public ArrayList<Pile> getLanes(){
        return this.lanes;
    }
}
