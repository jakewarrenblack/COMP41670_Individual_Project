import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Deck deck = new Deck();
        Scanner s = new Scanner(System.in);

        // Populate the 7 lanes and remove those cards from the main deck.
        ArrayList<Pile> lanes = deck.getLanes();
        Board board = new Board(deck, lanes);

        Pile drawnCards;

        enum GameState {ACTIVE, QUIT, WON}

        GameState gameState = GameState.ACTIVE;


        printAll(deck, board);

        while(gameState == GameState.ACTIVE){
            System.out.println("\n\nMake a move:");
            char[] move = s.nextLine().toUpperCase().toCharArray();
            boolean legalMove = false;

            // TODO: It can also be 3, if the user provides a number of cards to move
            if(move.length == 2){
                legalMove = board.moveCard(move[0], move[1]);
            }
            else{
                // if only 1 character was provided as input, instead of 2 or 3,
                // the only valid possible move available is 'D', to draw a card from the deck
                if( move[0] == 'D'){
                    drawnCards = board.drawFromDeck();
                    System.out.println("Drawn cards:\n");
                    for(Deck.Card c: drawnCards.getCards()){
                        if(c != null){
                           System.out.print(c.getRank() + " of " + c.getSuit() + ", ");
                        }
                    }
                }
                else if(move.length == 1 && move[0] == 'Q'){
                    System.out.println("Goodbye");
                    gameState = GameState.QUIT;
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