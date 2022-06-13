package comp1110.ass2;

import java.util.*;

public class Metro {

    static String deck = "aacb ".repeat(4) + "cbaa ".repeat(4) + "acba ".repeat(4)
            + "baac ".repeat(4) + "aaaa ".repeat(4) + "cbcb ".repeat(3)
            + "bcbc ".repeat(3) + "cccc ".repeat(2) + "bbbb ".repeat(2)
            + "dacc ".repeat(2) + "cdac ".repeat(2) + "ccda ".repeat(2)
            + "accd ".repeat(2) + "dbba ".repeat(2) + "adbb ".repeat(2)
            + "badb ".repeat(2) + "bbad ".repeat(2) + "ddbc ".repeat(2)
            + "cddb ".repeat(2) + "bcdd ".repeat(2) + "dbcd ".repeat(2)
            + "adad ".repeat(2) + "dada ".repeat(2) + "dddd ".repeat(2);

    static int score = 0; // Used as a score counter for runTrain method.
    // columns and rows in game grid
    static int columns = 7;
    static int rows = 7;
    static String middleStationCoords = "33 34 43 44";

    /**
     * Task 2
     * Determine whether a piece placement is well-formed. For a piece
     * placement to be well-formed, it must:
     * - contain exactly six characters;
     * - have as its first, second, third and fourth characters letters between
     * 'a' and 'd' inclusive (tracks); and
     * - have as its fifth and sixth characters digits between 0 and 7 inclusive
     * (column and row respectively).
     *
     * @param piecePlacement A String representing the piece to be placed
     * @return True if this string is well-formed
     * @author Bharath Kulkarni.
     */
    public static boolean isPiecePlacementWellFormed(String piecePlacement) {
        boolean flag = true;
        if (piecePlacement.length() != 6) {
            flag = false;
        }
        for (int i = 0; i < 4; i++) {
            if (!flag) {
                break;
            } else {
                // if char is NOT one of (a,b,c,d)
                if (!"abcd".contains(String.valueOf(piecePlacement.charAt(i)))) {
                    flag = false;
                }
            }
        }

        for (int i = 4; i < 6; i++) {
            if (!flag) {
                break;
            } else {
                int int1 = Integer.parseInt(String.valueOf(piecePlacement.charAt(i)));
                // if char is NOT one of (0-7)
                if (int1 > 7 || int1 < 0) {
                    flag = false;
                }
            }
        }

        return flag;
    }




    /**
     * Task 3
     * Determine whether a placement sequence string is well-formed.
     * For a placement sequence to be well-formed, it must satisfy the
     * following criteria:
     * - It must be composed of well-formed tile placements.
     * - For any piece x, there can exist no more instances of x on the board
     * than instances of x in the deck.
     *
     * @param placement A String representing the placement of all tiles on the
     *                  board
     * @return true if this placement sequence is well-formed
     * @author Bharath Kulkarni.
     */

    public static boolean isPlacementSequenceWellFormed(String placement) {

        String tempDeck = deck;
        boolean flag = true;

        if (placement.length() % 6 != 0) {
            flag = false;
        } else {
            // piecePlaceArray contains list of strings of length 6.
            String[] piecePlaceArray = splitPlacement(placement, Piece.Pieces.pieceAndPlacement);
            // pieceArray contains list of strings of length 4.
            String[] pieceArray = splitPlacement(placement, Piece.Pieces.pieceOnly);

            for (String s : piecePlaceArray) {
                if (!isPiecePlacementWellFormed(s)) {
                    flag = false;
                    break;
                }
            }

            for (String s : pieceArray) {
                if (!flag) {
                    break;
                } else {
                    if (tempDeck.contains(s)) {
                        tempDeck = tempDeck.replaceFirst(s, "");
                    } else {
                        flag = false;
                    }
                }
            }
        }

        return flag;
    }

    /**
     * Bharath: Splits a given placement string into array of individual tiles.
     *
     * @param placement A String representing the placement of all tiles on the
     *                  board
     * @return Array of pieces with their placements.
     * @author Bharath Kulkarni.
     */

    public static String[] splitPlacement(String placement, Piece.Pieces piece) {

        int i = 0;
        int p;

        if (piece == Piece.Pieces.handPieces) {
            String[] piecesArray = new String[placement.length() / 4];
            for (p = 0; p < placement.length(); p += 4) {
                piecesArray[i] = placement.substring(p, p + 4);
                i++;
            }
            return piecesArray;

        } else {
            String[] piecesArray = new String[placement.length() / 6];
            // String array contains (placement length/6) elements.
            // p = starting index of each piece.
            if (piece == Piece.Pieces.pieceAndPlacement) {
                for (p = 0; p < placement.length(); p += 6) {
                    piecesArray[i] = placement.substring(p, p + 6);
                    i++;
                }
            } else if (piece == Piece.Pieces.pieceOnly) {
                for (p = 0; p < placement.length(); p += 6) {
                    piecesArray[i] = placement.substring(p, p + 4);
                    i++;
                }
            } else if (piece == Piece.Pieces.placementOnly) {
                for (p = 0; p < placement.length(); p += 6) {
                    piecesArray[i] = placement.substring(p + 4, p + 6);
                    i++;
                }
            }
            return piecesArray;
        }
    }

    /**
     * Task 5
     * Draw a random tile from the deck.
     *
     * @param placementSequence a String representing the sequence of tiles
     *                          that have already been played
     * @param totalHands        a String representing all tiles (if any) in
     *                          all players' hands
     * @return a random tile from the deck
     * @author Bharath Kulkarni.
     */
    public static String drawFromDeck(String placementSequence, String totalHands) {
        String tempDeck = deck;

        String[] pieceArray = splitPlacement(placementSequence, Piece.Pieces.pieceOnly);
        String[] handArray = splitPlacement(totalHands, Piece.Pieces.handPieces);

        for (String s : pieceArray) {
            if (tempDeck.contains(s)) {
                tempDeck = tempDeck.replaceFirst(s, "");
            }
        }
        for (String s : handArray) {
            if (tempDeck.contains(s)) {
                tempDeck = tempDeck.replaceFirst(s, "");
            }
        }

        tempDeck = tempDeck.replaceAll("\\s", "");
        Random random = new Random();
        int index = random.nextInt(tempDeck.length() / 4);

        return tempDeck.substring(index * 4, (index * 4) + 4);
    }

    /**
     * Determine the position of a placed piece .
     *
     * @param placementPiece A String representing the placement of one tile on the
     *                       board
     * @return "top_edge" - if the piece is placed close to the top edge
     * "bottom_edge" - if the piece is placed close to the bottom edge
     * "left_edge" - if the piece is placed close to the left edge
     * "right_edge" - if the piece is placed close to the right edge
     * "top_right_corner" - if the piece is placed at the top right corner
     * "top_left_corner" - if the piece is placed at the top left corner
     * "bottom_left_corner" - if the piece is placed at the bottom left corner
     * "bottom_right_corner" - if the piece is placed at the bottom right corner
     *
     * @author: Zihao Li
     */
    public static String CheckPiecePos(String placementPiece) {

        //Initialize the result variable and assign "none-edge" to it.

        String result = "none_edge";

        //According to different coordinates patterns, we assign the different tile names to the result variable.

        if (placementPiece.contains("00")) {
            result = "top_left_corner";
        } else if (placementPiece.contains("07")) {
            result = "top_right_corner";
        } else if (placementPiece.contains("70")) {
            result = "bottom_left_corner";
        } else if (placementPiece.contains("77")) {
            result = "bottom_right_corner";
        } else {
            if (placementPiece.charAt(0) == '0') {
                result = "top_edge";
            } else if (placementPiece.charAt(1) == '0') {
                result = "left_edge";
            } else if (placementPiece.charAt(0) == '7') {
                result = "bottom_edge";
            } else if (placementPiece.charAt(1) == '7') {
                result = "right_edge";
            }
        }
        return result;
    }

    /**
     * To check if the previous tile and current non-edge tile is related or not
     *
     * @param pre A String of previous placement position of the tile
     *            cur A String of current placement position of the non-edge tile
     * @return true - if there is previous tile next to the current non-edge tile
     * false - if there is no tiles next to the current non-edge tile
     *
     * @author: Zihao Li
     */

    public static boolean CheckSurround(String pre, String cur) {
        boolean result = false;

        //Creating two coordinate object to two indicators

        Coordinate c1 = Coordinate.GetCord(cur);
        Coordinate p1 = Coordinate.GetCord(pre);

        if (c1.getX() > 7 || c1.getY() > 7 || p1.getX() > 7 || p1.getY() > 7) {
            result = false;
        }

        //The statement below is used to check whether the given two points are next to each other.

        if ((p1.getX() == (c1.getX() - 1) && p1.getY() == c1.getY()) || (p1.getY() == (c1.getY() + 1) && p1.getX() == c1.getX()) || (p1.getX() == (c1.getX() + 1) && p1.getY() == c1.getY()) || (p1.getY() == (c1.getY() - 1) && p1.getX() == c1.getX())) {

            result = true;
        }

        return result;
    }

    /**
     * To generate an ArrayList with type String, and it includes all the piece positions left
     *
     * @param position A String array that includes the position of each tile in the placementSequence
     * @return result - An ArrayList of String type which includes all the piece positions left
     *
     * @author: Zihao Li
     */

    public static ArrayList<String> PosLeft(String[] position) {

        ArrayList<String> result = new ArrayList<>();

        //To go through the half-end of placementSequence to see which tiles are left. All three possibilities are corner, edge and non-edge.

        for (int i = 0; i < position.length; i++) {
            if (position[i].contains("00") || position[i].contains("07") || position[i].contains("70") || position[i].contains("77")) {
                result.add("corner");
            } else if (position[i].charAt(0) == '0' || position[i].charAt(1) == '0' || position[i].charAt(0) == '7' || position[i].charAt(1) == '7') {
                result.add("edge");
            } else {
                result.add("non_edge");
            }
        }
        return result;
    }


    /**
     * Task 6
     * Determine if a given placement sequence follows the rules of the game.
     * These rules are:
     * - No tile can overlap another tile, or any of the central stations.
     * - A tile cannot be placed next to one of the central squares unless it
     * continues or completes an existing track.
     * - If a tile is on an edge of the board, it cannot contain a track that
     * results in a station looping back to itself, UNLESS that tile could not
     * have been placed elsewhere.
     * - If a tile is on a corner of the board, it cannot contain a track that
     * links the two stations adjacent to that corner, UNLESS that tile could
     * not have been placed elsewhere.
     *
     * @param placementSequence A sequence of placements on the board.
     * @return Whether this placement string is valid.
     *
     * @author: Zihao Li
     */
    public static boolean isPlacementSequenceValid(String placementSequence) {

        //Initialize all the variables here

        boolean well_formed;

        boolean around, valid = true;

        boolean duplicate = false;

        ArrayList<String> total = new ArrayList<>();

        ArrayList<String> Left;

        String[] before;

        String[] pos_left;

        //First to check if the placementSequence is Well-formed

        well_formed = isPlacementSequenceWellFormed(placementSequence);

        //To split the placementSequence to the two parts, placement and piece.
        String[] pos = splitPlacement(placementSequence, Piece.Pieces.placementOnly);

        String[] pis = splitPlacement(placementSequence, Piece.Pieces.pieceOnly);

        //The first nested for loop to check the condition of overlapping to other tiles or central stations
        for (int i = 0; i < pos.length; i++) {
            if (pos[i].equals("33") || pos[i].equals("34") || pos[i].equals("43") || pos[i].equals("44")) {
                valid = false;
            }
            for (int j = i + 1; j < pos.length; j++) {
                if (pos[i].equals(pos[j])) {
                    duplicate = true;
                    break;
                }
            }
        }

        //Once all the conditions from above are met, we can process each tile separately
        if (well_formed && valid && !duplicate) {

            for (int i = 0; i < pis.length; i++) {

                //An indicator of the targeting tile
                String temp = CheckPiecePos(pos[i]);

                String first = CheckPiecePos(pos[0]);

                //The position of each tile left from the current tile.
                pos_left = Arrays.copyOfRange(pos, i, pos.length);

                Left = PosLeft(pos_left);

                //Then, the first tile on edge is unavoidable so we skip it, and when the inner square are all occupied by the tiles. That's also unavoidable

                if ((i == 0) && (first.equals("top_edge") || first.equals("left_edge") || first.equals("right_edge") || first.equals("bottom_edge"))) {
                    continue;
                } else if (i > 31 && (!Left.contains("non_edge"))) {
                    continue;
                } else {

                    //The below are all testing statements to each tile placement

                    if (temp.equals("top_left_corner")) {

                        if (pis[i].charAt(0) == 'c' || pis[i].charAt(0) == 'd') {
                            valid = false;
                        } else if (pis[i].charAt(3) == 'b' || pis[i].charAt(3) == 'd') {
                            valid = false;
                        }

                    } else if (temp.equals("top_right_corner")) {

                        if (pis[i].charAt(0) == 'b' || pis[i].charAt(0) == 'd') {
                            valid = false;
                        } else if (pis[i].charAt(1) == 'c' || pis[i].charAt(1) == 'd') {
                            valid = false;
                        }

                    } else if (temp.equals("bottom_left_corner")) {

                        if (pis[i].charAt(2) == 'b' || pis[i].charAt(2) == 'd') {
                            valid = false;
                        } else if (pis[i].charAt(3) == 'c' || pis[i].charAt(3) == 'd') {
                            valid = false;
                        }

                    } else if (temp.equals("bottom_right_corner")) {

                        if (pis[i].charAt(1) == 'b' || pis[i].charAt(1) == 'd') {
                            valid = false;
                        } else if (pis[i].charAt(2) == 'c' || pis[i].charAt(2) == 'd') {
                            valid = false;
                        }

                    } else if (temp.equals("top_edge")) {

                        if (pis[i].charAt(0) == 'd') {
                            valid = false;
                        }

                    } else if (temp.equals("right_edge")) {

                        if (pis[i].charAt(1) == 'd') {
                            valid = false;
                        }

                    } else if (temp.equals("bottom_edge")) {

                        if (pis[i].charAt(2) == 'd') {
                            valid = false;
                        }

                    } else if (temp.equals("left_edge")) {

                        if (pis[i].charAt(3) == 'd') {
                            valid = false;
                        }

                    } else if (temp.equals("none_edge")) {

                        ArrayList<String> adj = new ArrayList<>();

                        int z = i;
                        before = Arrays.copyOfRange(pos, 0, z);
                        for (int x = 0; x < before.length; x++) {
                            around = CheckSurround(before[x], pos[z]);
                            if (!around) {
                                adj.add("false");
                            } else {
                                adj.add("true");
                            }
                        }
                        if (adj.contains("true")) {
                            total.add("true");
                        } else {
                            total.add("false");
                        }
                        if (total.contains("false")) {
                            valid = false;
                        } else {
                            valid = true;
                        }
                    } //end for loop of non-edge condition
                } //end big if statement
            }//end big for loop
        } else {
            valid = false;
        }

        return valid;
    }

    /**
     * Determine which edge stations belong to each player depends on the number of players in the game.
     *
     * @param numberOfPlayers A integer shows the number of players.
     * @return stationsForEach - an 2D array with first entry indicates the number of players, the second entry indicates his/her belonging stations.
     *
     * @auhtor: Zihao Li
     */
    public static int[][] StationsForPlayers(int numberOfPlayers) {
        int[][] stationsForEach = null;

        //Since there are no particular pattern on dividing the stations, we use case statement and 2D array to decide which player owns which stations.

        switch (numberOfPlayers) {
            case 2:
                stationsForEach = new int[][]{{1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31}, {2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32}};
                break;
            case 3:
                stationsForEach = new int[][]{{1, 4, 6, 11, 15, 20, 23, 25, 28, 31}, {2, 7, 9, 12, 14, 19, 22, 27, 29, 32}, {3, 5, 8, 10, 13, 18, 21, 24, 26, 30}};
                break;
            case 4:
                stationsForEach = new int[][]{{4, 7, 11, 16, 20, 23, 27, 32}, {3, 8, 12, 15, 19, 24, 28, 31}, {1, 6, 10, 13, 18, 21, 25, 30}, {2, 5, 9, 14, 17, 22, 26, 29}};
                break;
            case 5:
                stationsForEach = new int[][]{{1, 5, 10, 14, 22, 28}, {6, 12, 18, 23, 27, 32}, {3, 7, 15, 19, 25, 29}, {2, 9, 13, 21, 26, 30}, {4, 8, 11, 20, 24, 31}};
                break;
            case 6:
                stationsForEach = new int[][]{{1, 5, 10, 19, 27}, {2, 11, 18, 25, 29}, {4, 8, 14, 21, 26}, {6, 15, 20, 24, 31}, {3, 9, 13, 23, 30}, {7, 12, 22, 28, 32}};
                break;
        }
        return stationsForEach;
    }

    /**
     * Task 7
     * Determine the current score for the game.
     *
     * @param placementSequence a String representing the sequence of piece placements made so far in the game
     * @param numberOfPlayers   The number of players in the game
     * @return an array containing the scores for each player
     * @author Bharath Kulkarni.
     */
    public static int[] getScore(String placementSequence, int numberOfPlayers) {

        int[][] stations = StationsForPlayers(numberOfPlayers);
        // scores variable keeps track of each player's score. This is what is returned for this function.
        int[] scores = new int[numberOfPlayers];



        // 1st for loop iterates through players, adds total score per player to scores.
        for (int i = 0; i < numberOfPlayers; i++) {
            int playerScore = 0;
            // 2nd loop iterates through each station in player's list.
            for (int j = 0; j < stations[i].length; j++) {
                String currentCoords = Task7Methods.coordsAtStation(stations[i][j]);
                String currentExit = Task7Methods.startingExit(stations[i][j]); // first iteration of loop will use exit of station.

                //executes if there is a tile outside the current station.
                if (placementSequence.contains(currentCoords)) {
                    // gets score for the track originating from current station.
                    int stationScore = Task7Methods.runTrain(currentCoords, currentExit, placementSequence);
                    playerScore = playerScore + stationScore; // adds each station score to player's total score.
                }
            }
            // each player's score is added to the score list in the position corresponding to each player.
            scores[i] = playerScore;
        }
        return scores;
    }

    /**
     * Task 9
     * Given a placement sequence string, generate a valid next move.
     *
     * @param placementSequence a String representing the sequence of piece placements made so far in the game
     * @param piece             a four-character String representing the tile just drawn
     * @param numberOfPlayers   The number of players in the game
     * @return A valid placement of other the drawn tile or the tile from the player's hand (if it is not empty).
     * @author Bharath Kulkarni.
     */
    public static String generateMove(String placementSequence, String piece, int numberOfPlayers) {
        //creates a list of all valid coordinates
        ArrayList<String> possiblePlacements = Task9Methods.allPossiblePlacements(placementSequence,piece);

        Random random = new Random();
        int index = random.nextInt(possiblePlacements.size());

        return (piece + possiblePlacements.get(index));
    }


}
