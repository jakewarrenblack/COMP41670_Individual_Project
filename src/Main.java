import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Deck deck = new Deck();
        Scanner s = new Scanner(System.in);

        // Populate the 7 lanes and remove those cards from the main deck.
        ArrayList<Pile> lanes = deck.getLanes();
        Board board = new Board(deck, lanes);

        Deck.Card[] drawnCards = new Deck.Card[3];


        printAll(deck, board);

        while(true){
            System.out.println("Make a move:");
            char[] move = s.nextLine().toCharArray();
            boolean legalMove = false;

            // keep track of the last index we replaced
            // e.g. if we replaced index 0 last time because it already had a value,
            // then replace index 1 this time, then index 2, then start again from 0
            int lastIndexReplaced = -1;

            // TODO: It can also be 3, if the user provides a number of cards to move
            if(move.length == 2){
                legalMove = board.moveCard(move[0], move[1]);
            }
            else{
                // if only 1 character was provided as input, instead of 2 or 3,
                // the only valid possible move available is 'D', to draw a card from the deck
                if(move[0] == 'D'){
                    drawnCards = board.drawFromDeck();
                    System.out.println("Drawn cards:\n");
                    for(Deck.Card c: drawnCards){
                        if(c != null){
                            System.out.println(c.getRank() + " of " + c.getSuit());
                        }
                    }
                }
                else{
                    System.out.println("Illegal move! That move is invalid.");
                }
            }

            if(legalMove){
                // Print the board again after making a move
                printAll(deck, board);
            }
        }
    }

    static void printAll(Deck d, Board b){

        d.printLanes();
        b.printFoundation();
    }
}