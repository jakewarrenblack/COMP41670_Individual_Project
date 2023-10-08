public class Deck {
    Card[] spades = new Card[13];
    Card[] clubs = new Card[13];
    Card[] diamonds = new Card[13];
    Card[] hearts = new Card[13];

    Card[][] cards = new Card[4][13];

    enum Suit {SPADES, HEARTS, CLUBS, DIAMONDS};
    enum Rank {ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING}

    public static class Card{
        // suits: there are 13 of each type, 52 in total (spades, diamonds, hearts, clubs)
        // ranks: consists of ACE, 2-10, JACK, QUEEN, KING. only one of each of these per suit.

        private final Suit suit;
        private final Rank rank;

        Card(Suit s, Rank r){
            this.suit = s;
            this.rank = r;
        }

        Suit getSuit(){
            return this.suit;
        }

        Rank getRank(){
            return this.rank;
        }
    }

    Deck(){
        for(int i=0; i<13; i++){
            this.clubs[i] = new Card(Suit.CLUBS, Rank.values()[i]);
            this.spades[i] = new Card(Suit.SPADES, Rank.values()[i]);
            this.hearts[i] = new Card(Suit.HEARTS, Rank.values()[i]);
            this.diamonds[i] = new Card(Suit.DIAMONDS, Rank.values()[i]);
        }

        this.cards[0] = this.clubs;
        this.cards[1] = this.diamonds;
        this.cards[2] = this.hearts;
        this.cards[3] = this.spades;
    }

    void getDeck(){
        for(Card[] suit: this.cards){
            for(Card c: suit){
                System.out.println("Suit: " + c.getSuit() + " Rank: " + c.getRank());
            }
        }

    }
}
