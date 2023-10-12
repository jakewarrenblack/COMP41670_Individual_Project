import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

// to represent a pile of cards anywhere on the board, e.g. part of the foundation, the main deck pile, etc
public class Pile{
    private final char label;
    private final ArrayList<Deck.Card> cards;

    Pile(char l, ArrayList<Deck.Card> c){
        this.label = l;
        this.cards = c;
    }

    Pile(int l, ArrayList<Deck.Card> c){
        this.label = Character.forDigit(l, 10); // 10 represents numeral system, so use digital. it'd print an ASCII code if we tried to just cast via (char)i.
        this.cards = c;
    }

    ArrayList<Deck.Card> getCards(){
        return this.cards;
    }

    Deck.Card removeCard(Deck.Card c){
        this.cards.remove(c);
        return c;
    }

    void addCard(Deck.Card c){
        this.cards.add(c);
    }

    void addCard(int i, Deck.Card c){
        this.cards.add(i, c);
    }

    Deck.Card getCard(int i){
        try{
            return this.cards.get(i);
        }
        catch(Exception ex){
            return null;
        }

    }

    boolean isEmpty(){
        return this.cards.isEmpty();
    }

    // Returns the element which was removed from the list
    Deck.Card removeCard(int i){
        return this.cards.remove(i);
    }
    
    int getSize(){
        return this.cards.size();
    }

    public int indexOf(Deck.Card c) {
        return this.cards.indexOf(c);
    }

    char getLabel(){
        return this.label;
    }

    // Deep copy.
    // Shallow copy would not work, I need to make a completely new arraylist with the same values.
    // Doing this, so we can validate a move on a copied object before running it on the real thing.

    public Pile(Pile another){
        this.label = another.label;
        this.cards = new ArrayList<Deck.Card>(another.cards.size());
        this.cards.addAll(another.cards);
    }

    String[] redSuits = {"HEARTS", "DIAMONDS"};
    String[] blackSuits = {"SPADES", "CLUBS"};
    private final String[] laneOrder = {"KING", "QUEEN", "JACK", "TEN", "NINE", "EIGHT", "SEVEN", "SIX", "FIVE", "FOUR", "THREE", "TWO", "ACE"};
    private final String[] foundationOrder = {"ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"};

    private final Character[] foundationLabels = {'H', 'C', 'S', 'D'};
    private final Character[] laneLabels = {'1', '2', '3', '4', '5', '6', '7'};

    // Check if the pile would be valid with this card added to it
    // if there's a card preceding the card you're trying to place, check if A followed by B is valid
    // if there's no other card preceding it, if the card is being placed in the foundation array, it needs to be an ACE
    // if the card is being placed in one of the lanes/tableau, it needs to be a KING (reverse of the foundation order)
    boolean validateOrder(Deck.Card card, Pile destinationPile){
        String[] relevantOrder = Arrays.asList(foundationLabels).contains(destinationPile.label) ? foundationOrder : laneOrder;

        Deck.Rank rank = card.getRank();

        enum Color {RED, BLACK};
        Color color;

        if(Arrays.asList(redSuits).contains(card.getSuit().toString())){
            color = Color.RED;
        }
        else{
            color = Color.BLACK;
        }

        destinationPile.addCard(card);
        int cardIndex = destinationPile.indexOf(card);


        if(cardIndex != 0){
            Deck.Card precedingCard = destinationPile.getCard(cardIndex-1);

            for(int i=0; i<relevantOrder.length-1; i++){
                // we expect the card after this one to be the same rank as our relevant card
                // if it isn't, this move is invalid
                if(Objects.equals(relevantOrder[i], precedingCard.getRank().toString())){
                    if(!Objects.equals(relevantOrder[i + 1], rank.toString())){
                        System.out.println("Invalid move! Card of rank: " + relevantOrder[i] + " may not be followed by " + rank);
                        return false;
                    }
                    else{
                        // means the combination of (card before our card) + (our card) is valid for our destination pile.

                        // now need to check the colour combination
                        // a red card should be followed by a black card, and vice-versa

                        Color previousCardColor;

                        Deck.Suit previousSuit = precedingCard.getSuit();

                        // if this is true, previous card is red. otherwise, it's black.
                        if(Arrays.asList(redSuits).contains(previousSuit.toString())){
                            previousCardColor = Color.RED;
                        }
                        else{
                            previousCardColor = Color.BLACK;
                        }


                        if(previousCardColor == color){
                            System.out.println("Invalid move! A " + previousCardColor + " card must be followed by a " + color + " card!");
                            return false;
                        }

                        return true;
                    }
                }
            }
        }
        else{
            // means there were no cards in the destinationPile, and the card we're trying to place is appropriate for the first card in said pile
            // (an ACE if foundation pile, and a KING if lane pile)
            if(Objects.equals(rank.toString(), relevantOrder[0])){
                return true;
            }
            else{
                System.out.println("Invalid move! First card in this pile must be of rank " + relevantOrder[0]);
                return false;
            }

            // TODO: also, don't think this is an actual rule of patience/solitaire, but in our case, the foundation lanes are labelled D, H, C, S so make sure the suit corresponds to these (IF we're placing in the foundations)
        }
        return false;
    }
}