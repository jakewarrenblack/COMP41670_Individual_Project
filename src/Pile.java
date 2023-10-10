import java.util.ArrayList;

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

    Deck.Card getCard(int i){
        return this.cards.get(i);
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


}