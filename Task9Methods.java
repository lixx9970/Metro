package comp1110.ass2;

import java.util.ArrayList;

/**
 * Task9Methods class contains methods used for task 9.
 * @author Bharath Kulkarni.
 */
public class Task9Methods {

    /**
     * @param outerCoordinates  list of coordinates outside of a station.
     * @param placementSequence
     * @param piece
     * @return a list of valid placement coordinates.
     * @author Bharath Kulkarni.
     */
    public static ArrayList<String> outerPlacements(ArrayList<String> outerCoordinates, String placementSequence, String piece) {
        ArrayList<String> possibleOuterPlacements = new ArrayList<>();

        for (String coords : outerCoordinates) {
            // if board is empty, check if loopback is unavoidable. if avoidable, don't add coords to possibleOuterPlacements.
            if (placementSequence.isEmpty()) {
                String newPlacementSequence = piece + coords;
                if (Metro.isPlacementSequenceValid(newPlacementSequence) && checkUnavoidableLoopback(newPlacementSequence)) {
                    // newPlacementSequence only contains one tile, so it can be passed to this method.
                    possibleOuterPlacements.add(coords); // add if all checks are passed.

                }
            }
            // if the board is almost full (59 tiles placed), find the empty coords and add to possibleOuterPlacements.
            else if (placementSequence.length() / 6 == 59) {
                if (!placementSequence.contains(coords)) {
                    possibleOuterPlacements.add(coords); // wherever the empty coords are, the last tile must be placed there. unavoidable.
                }
            }
            // if placementSequence is not empty.
            else {
                if (!placementSequence.contains(coords)) {
                    String newPlacementSequence = placementSequence + piece + coords;
                    // check if the new placementSequence is valid, then add to possibleOuterPlacements.
                    if (Metro.isPlacementSequenceValid(newPlacementSequence)) {
                        possibleOuterPlacements.add(coords);
                    }
                }
            }
        }
        return possibleOuterPlacements;
    }

    /**
     * @param tile tile type (eg: aaaa) and its position. (eg: aaaa00)
     * @return True if loopback is unavoidable. False if avoidable. (eg: dada01 is avoidable as it can be placed at dada10 to avoid a loopback).
     * @author Bharath Kulkarni.
     */

    public static boolean checkUnavoidableLoopback(String tile) {
        boolean test1 = true;
        boolean test2 = true;
        boolean test3 = true;
        boolean test4 = true;
        if (tile.equals("dddd")) {
            return true; // loopback is unavoidable.
        }
        if (tile.charAt(0) == 'd') {
            test1 = tile.charAt(4) != '0';
        }
        if (tile.charAt(1) == 'd') {
            test2 = tile.charAt(5) != '7';
        }
        if (tile.charAt(2) == 'd') {
            test3 = tile.charAt(4) != '7';
        }
        if (tile.charAt(3) == 'd') {
            test4 = tile.charAt(5) != '0';
        }
        return (test1 && test2 && test3 && test4);
    }

    /**
     * @param innerCoordinates  list of all coordinates excluding the coordinates at a station on the edge.
     * @param placementSequence
     * @param piece
     * @return a list of valid placement coordinates.
     * @author Bharath Kulkarni.
     */
    public static ArrayList<String> innerPlacements(ArrayList<String> innerCoordinates, String placementSequence, String piece) {
        ArrayList<String> possibleInnerPlacements = new ArrayList<>();

        for (String coords : innerCoordinates) {
            String newPlacementSequence = placementSequence + piece + coords;
            if (adjacentTilesExist(coords, placementSequence)
                    && !placementSequence.contains(coords)
                    && Metro.isPlacementSequenceValid(newPlacementSequence)) {

                possibleInnerPlacements.add(coords);
            }
        }
        return possibleInnerPlacements;
    }

    /**
     * @param currentCoords
     * @param placementSequence
     * @return true if there is a tile adjacent to current position.
     * @author Bharath Kulkarni.
     */
    public static boolean adjacentTilesExist(String currentCoords, String placementSequence) {
        String above = Task7Methods.getNewCoords("0", currentCoords);
        String below = Task7Methods.getNewCoords("4", currentCoords);
        String left = Task7Methods.getNewCoords("6", currentCoords);
        String right = Task7Methods.getNewCoords("2", currentCoords);

        return placementSequence.contains(above)
                || placementSequence.contains(below)
                || placementSequence.contains(left)
                || placementSequence.contains(right);
    }
    public static ArrayList<String> allCoordList(){
        ArrayList<String> coordinates = new ArrayList<>();
        for (int i = 0; i <= Metro.columns; i++) {
            for (int j = 0; j <= Metro.rows; j++) {
                coordinates.add(Integer.toString(i) + (j));
            }
        }
        return coordinates;
    }

    public static ArrayList<String> outerCoordList(){
        int[] stations = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32};
        ArrayList<String> outerCoordinates = new ArrayList<>();
        for (int station : stations) {
            String coords = Task7Methods.coordsAtStation(station);
            outerCoordinates.add(coords);
        }
        return outerCoordinates;
    }

    public static ArrayList<String> innerCoordList(ArrayList<String> outerCoordinates, ArrayList<String> allCoordinates) {
        ArrayList<String> innerCoordinates = new ArrayList<>();
        for (String coords : allCoordinates) {
            if (!outerCoordinates.contains(coords) && !Metro.middleStationCoords.contains(coords)) {
                innerCoordinates.add(coords);
            }
        }
        return innerCoordinates;
    }

    public static ArrayList<String> allPossiblePlacements(String placementSequence, String piece){
        ArrayList<String> possiblePlacements = new ArrayList<>();
        ArrayList<String> allCoordinates = allCoordList();
        // outer coords are coords right outside stations.
        ArrayList<String> outerCoordinates = outerCoordList();
        // inner coords are all coords except coords at a station.
        ArrayList<String> innerCoordinates = innerCoordList(outerCoordinates, allCoordinates);

        possiblePlacements.addAll(Task9Methods.outerPlacements(outerCoordinates, placementSequence, piece));
        possiblePlacements.addAll(Task9Methods.innerPlacements(innerCoordinates, placementSequence, piece));

        return possiblePlacements;
    }
}
