import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        printTextFile("./logo.txt");
        System.out.println("Type ? for rules");

        Deck deck = new Deck();
        Scanner s = new Scanner(System.in);

        // Populate the 7 lanes and remove those cards from the main deck.
        ArrayList<Pile> lanes = deck.getLanes();
        Board board = new Board(deck, lanes);

        Pile drawnCards = null;

        enum GameState {ACTIVE, QUIT, WON}
        GameState gameState = GameState.ACTIVE;

        printAll(deck, board);

        while (gameState == GameState.ACTIVE) {
            System.out.println("\nMake a move:");
            char[] move = s.nextLine().toUpperCase().toCharArray();

            if (move.length == 2) {
                if(move[0] == 'P' && move[1] == 'R'){
                    printAll(deck, board);
                }
                else{
                    try{
                        board.moveCard(move[0], move[1]);
                    }
                    catch(IllegalMoveException ex){
                        System.out.println(ex);
                    }
                }
            } else {
                // this means the user wants to move multiple cards (provided <label1><label2><n_cards>
                if(move.length == 3){
                    try{
                        board.moveCard(move[0], move[1], move[2]);
                    }
                    catch(IllegalMoveException ex){
                        System.out.println(ex);
                    }

                }

                if(move[0] == '?'){
                    printTextFile("./how_to_play.txt");
                }

                // the only valid possible move available is 'D', to draw a card from the deck
                if (move[0] == 'D') {
                    try{
                        drawnCards = board.drawFromDeck();
                    }
                    catch(IllegalMoveException ex){
                        System.out.println(ex);
                    }

                    System.out.println("\nDrawn cards:");
                    for (Deck.Card c : drawnCards.getCards()) {
                        if (c != null) {
                            System.out.print(c.getRank() + " of " + c.getSuit() + " " + c.getIcon() + ", ");
                        }
                    }
                    System.out.println();
                } else if (move.length == 1 && move[0] == 'Q') {
                    System.out.println("Goodbye");
                    gameState = GameState.QUIT;
                } else {
                    System.out.println("Illegal move! That move is invalid.");
                }
            }


            // Print the board again after making a move
            printAll(deck, board);

        }
    }

    static void printAll(Deck d, Board b){
        d.printLanes();
        b.printFoundation();
    }

    static void printTextFile(String filePath){
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(new File(filePath));
        } catch (Exception e) {
            System.out.println("Error reading file: " + e);
        }

        if(fileReader != null){
            while (fileReader.hasNextLine()) {
                System.out.println(fileReader.nextLine());
            }
        }
    }
}