# COMP41670_Individual_Project

## Java Solitaire.
![image](https://github.com/jakewarrenblack/COMP41670_Individual_Project/assets/47800618/e0c61ae2-b4a8-4b64-aee2-3c9a721be872)


#### Features:

- Displays cards on the console, receives input from the keyboard.
- Display lists the score and number of moves.
- Allows movement of single or multiple cards between piles, according to the rules of Solitaire.

#### Models solitaire board:

- 52 cards.
- 7 lanes, 4 foundations, and a draw pile.

---

**Q** = quit                                                                                                                

**D** = uncover a new card from the draw pile. You can draw up to 3 cards at once from this pile.                           

**PR** = print the deck                                                                                                     

### To move a single card:                                                                                                  
- `<Location 1><Location 2>` - For example, you might move "65", or "2D"                                                 

### To move multiple cards (assuming the move is valid)                                                                     
- `<Location 1><Location 2><Number of cards>` - For example, you could move "122", moving 2 cards from lane 1 to lane 2 

### The position labels are:                                                                                                
- Lanes: 1-7                                                                                                            
- Foundations: D, H, C, S                                                                                               
- Draw pile: P                                                                                                          

### Cards in the lanes must be in order:                                                                                    
- `KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO,  ACE`

### Cards in the foundations must be in order:
- `ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING`

The cards in each foundation must only contain cards matching the foundation's label. (E.g H = HEARTS).

- A black card (spades, clubs) may only be placed above a red card (diamonds, hearts), and vice-versa.
- The deck of 52 cards has been dealt into 7 piles. Pile 1 contains 1 card, pile 2 contains 2, and so on, up to pile 7.
- Only the card on top of each pile is visible at first.

### Points:
- 5 points are awarded for moving cards between lanes. E.g, from lane 1 to lane 2.
- 10 points are awarded for moving from the pile (P) to one of the foundations/suits.
- 20 points are awarded for moving cards from one of the lanes to one of the foundations/suits.

Points are awarded only if a specific combination of cards is moved to a pile for the first time.

Your goal is to create 4 piles of 13 cards (52 total) in the 4 foundation piles.       

