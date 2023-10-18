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

        Pile drawnCards;

        enum GameState {ACTIVE, QUIT, WON}

        GameState gameState = GameState.ACTIVE;


        printAll(deck, board);

        int score = 0;

        while (gameState == GameState.ACTIVE) {
            System.out.println("\nMake a move:");
            char[] move = s.nextLine().toUpperCase().toCharArray();
            boolean scoreIncreased = false;

            if (move.length == 2) {
                if(move[0] == 'P' && move[1] == 'R'){
                    printAll(deck, board);
                }
                else{
                    int attempt = board.moveCard(move[0], move[1]);
                    if(attempt != 0){
                        score += attempt;
                        scoreIncreased = true;
                    }
                }

            } else {
                // this means the user wants to move multiple cards (provided <label1><label2><n_cards>
                if(move.length == 3){
                    int attempt = board.moveCard(move[0], move[1], move[2]);
                    if(attempt != 0){
                        score += attempt;
                        scoreIncreased = true;
                    }
                }


                if(move[0] == '?'){
                    printTextFile("./how_to_play.txt");
                }

                // otherwise, if only 1 character was provided as input, instead of 2 or 3,
                // the only valid possible move available is 'D', to draw a card from the deck
                if (move[0] == 'D') {
                    drawnCards = board.drawFromDeck();
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

            if(scoreIncreased){
                // Print the deck again after a valid move, so the player can see their changes
                printAll(deck, board);
                System.out.println("\n\nSCORE: " + score);
            }
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