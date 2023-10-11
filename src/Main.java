import java.util.ArrayList;
import java.util.Scanner;

public class Main {



    public static void main(String[] args) {
        Deck deck = new Deck();
        Scanner s = new Scanner(System.in);

        // Populate the 7 lanes and remove those cards from the main deck.
        ArrayList<Pile> lanes = deck.getLanes();
        Board board = new Board(deck, lanes);

        printAll(deck, board);


        while(true){
            System.out.println("Make a move:");
            char[] move = s.nextLine().toCharArray();
            boolean legalMove = board.moveCard(move[0], move[1]);

            if(legalMove){
                // Print the board again after making a move
                printAll(deck, board);
            }
        }
    }

    static void printAll(Deck d, Board b){
        d.printLanes();
        // TODO: Modify this so the player may draw a new card from here
        // Otherwise, the cards in the remaining deck are unknown
        b.printFoundation();
    }
}