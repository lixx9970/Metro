package comp1110.ass2;

import java.util.HashMap;
import java.util.Map;

/**
 * Task7Methods class contains methods used for task 7, and some are also used for task 9.
 * @author Bharath Kulkarni.
 */
public class Task7Methods {


    /**
     * @param station station number. (1,2,3,4,...,32)
     * @return coordinate just in front of the station. (eg. for station 1, return "07")
     * @author Bharath Kulkarni.
     */
    public static String coordsAtStation(int station) {
        String coords = null;
        if (station <= 8) {
            coords = "0" + (8 - station);
        } else if (station <= 16) {
            coords = (station - 9) + "0";
        } else if (station <= 24) {
            coords = "7" + (station - 17);
        } else if (station <= 32) {
            coords = (32 - station) + "7";
        }
        return coords;
    }

    /**
     * @param stationNumber station number
     * @return exit at the station. eg: if station is at the top (1-8), return "5",
     * as the starting position of the train is at position 5 of the station tile.
     * @author Bharath Kulkarni.
     */

    public static String startingExit(int stationNumber) {
        //station numbers of each side of the board
        int[] topStations = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] leftStations = {9, 10, 11, 12, 13, 14, 15, 16};
        int[] bottomStations = {17, 18, 19, 20, 21, 22, 23, 24};
        int[] rightStations = {25, 26, 27, 28, 29, 30, 31, 32};

        String startingExit = "";

        for (int topStation : topStations) {
            if (topStation == stationNumber) {
                startingExit = "5";
                break;
            }
        }
        for (int leftStation : leftStations) {
            if (leftStation == stationNumber) {
                startingExit = "3";
                break;
            }
        }
        for (int bottomStation : bottomStations) {
            if (bottomStation == stationNumber) {
                startingExit = "1";
                break;
            }
        }
        for (int rightStation : rightStations) {
            if (rightStation == stationNumber) {
                startingExit = "7";
                break;
            }
        }
        return startingExit;
    }

    /**
     * @param position          starting position right outside each station.
     * @param exit              starting exit on station tile.
     * @param placementSequence original placement sequence, unchanged.
     * @return number of tiles the track has moved over. Score for the track.
     * @author Bharath Kulkarni.
     */
    public static int runTrain(String position, String exit, String placementSequence) {
        int multiplier = 2; // multiplier applied to score in special cases (connecting to mid station).

        for (int i = 0; i < placementSequence.length() / 6; i++) {
            String currentCoords = position;
            String currentExit = exit;

            // gets tile identity (eg: "aaaa", "aacb")
            String currentTile = placementSequence.substring(
                    placementSequence.indexOf(currentCoords) - 4,
                    placementSequence.indexOf(currentCoords));
            // gets an array of track exits for each track on tile. (eg: {"05","27","41","63"} for tile "aaaa")
            String[] currentTrackExits = trackExits(currentTile);
            // new exit on current tile. (ie. other end of track on current tile)
            String newExit = getNewExit(currentExit, currentTrackExits);
            // coordinates of position next to current tile where a track could connect.
            // (eg: if current track ends at position 2, new tile that can connect the track must be to the right.)
            String newCoords = getNewCoords(newExit, currentCoords);

            // checks if there is a tile on the board, next to the current position.
            if (placementSequence.contains(newCoords)) {
                position = newCoords;
                exit = newExit;
                Metro.score++;
            }

            //check if a tile will connect to a station.
            else if (connectsToStation(currentCoords, newExit)) {
                Metro.score++;
                break;
            }

            // check if the track attaches to a middle station.
            else if (connectsToMiddle(currentCoords, newExit)) {
                Metro.score = (Metro.score + 1) * multiplier; // score +1 for final tile, double the score, then return.
                break;
            }
            // if there is no tile at the new coordinates to continue the track, set score to 0 and return it.
            else {
                Metro.score = 0;
            }

        }
        int storedScore = Metro.score;
        Metro.score = 0; // reset score for use with next station.
        return storedScore;

    }

    /**
     * @param tile String with all tracks on a single tile
     * @return Array of Strings for each track containing exit points. eg: {"05","27","41","63"} for tile "aaaa"
     * @author Bharath Kulkarni.
     */
    public static String[] trackExits(String tile) {
        String[] exits = new String[tile.length()];
        for (int i = 0; i < tile.length(); i++) {
            // first position
            if (i == 0 && tile.charAt(i) == 'a') {
                exits[i] = "05";
            } else if (i == 0 && tile.charAt(i) == 'b') {
                exits[i] = "03";
            } else if (i == 0 && tile.charAt(i) == 'c') {
                exits[i] = "07";
            } else if (i == 0 && tile.charAt(i) == 'd') {
                exits[i] = "01";
            }
            // second position
            else if (i == 1 && tile.charAt(i) == 'a') {
                exits[i] = "27";
            } else if (i == 1 && tile.charAt(i) == 'b') {
                exits[i] = "25";
            } else if (i == 1 && tile.charAt(i) == 'c') {
                exits[i] = "21";
            } else if (i == 1 && tile.charAt(i) == 'd') {
                exits[i] = "23";
            }
            // third position
            else if (i == 2 && tile.charAt(i) == 'a') {
                exits[i] = "41";
            } else if (i == 2 && tile.charAt(i) == 'b') {
                exits[i] = "47";
            } else if (i == 2 && tile.charAt(i) == 'c') {
                exits[i] = "43";
            } else if (i == 2 && tile.charAt(i) == 'd') {
                exits[i] = "45";
            }
            // fourth position
            else if (i == 3 && tile.charAt(i) == 'a') {
                exits[i] = "63";
            } else if (i == 3 && tile.charAt(i) == 'b') {
                exits[i] = "61";
            } else if (i == 3 && tile.charAt(i) == 'c') {
                exits[i] = "65";
            } else if (i == 3 && tile.charAt(i) == 'd') {
                exits[i] = "67";
            }


        }
        return exits;
    }

    /**
     * @param currentExit exit at the start of the track on current tile.
     * @param trackExits  array of exits of each track.
     * @return new exit at the other end of track.
     * @author Bharath Kulkarni.
     */
    public static String getNewExit(String currentExit, String[] trackExits) {
        Map<String, String> coupledExits = new HashMap<>();
        coupledExits.put("0", "5");
        coupledExits.put("1", "4");
        coupledExits.put("2", "7");
        coupledExits.put("3", "6");
        coupledExits.put("4", "1");
        coupledExits.put("5", "0");
        coupledExits.put("6", "3");
        coupledExits.put("7", "2");

        String newExit = "";
        // gets new exit for track on current tile
        for (String trackExit : trackExits) {
            if (trackExit.contains(coupledExits.get(currentExit))) {
                // newExit is the other end of the track on the current tile.
                newExit = trackExit.replace(coupledExits.get(currentExit), "");

            }
        }
        return newExit;
    }


    /**
     * @param newExit       exit at the other end of track.
     * @param currentCoords current position
     * @return coordinates of new position, where the track can continue. eg: if newExit = 3, the new tile must be to the right.
     * @author Bharath Kulkarni.
     */

    public static String getNewCoords(String newExit, String currentCoords) {
        String newCoords = "";

        // following statements find position of new tile.
        // if new exit is at the top of the current tile
        if ("01".contains(newExit)) {
            newCoords = Character.toString(currentCoords.charAt(0) - 1) + currentCoords.charAt(1);
        }
        //new exit on the right of tile
        else if ("23".contains(newExit)) {
            newCoords = currentCoords.charAt(0) + Character.toString(currentCoords.charAt(1) + 1);
        }
        //new exit on the bottom of tile
        else if ("45".contains(newExit)) {
            newCoords = Character.toString(currentCoords.charAt(0) + 1) + currentCoords.charAt(1);
        }
        // new exit on the left of tile
        else if ("67".contains(newExit)) {
            newCoords = currentCoords.charAt(0) + Character.toString(currentCoords.charAt(1) - 1);
        }
        return newCoords;
    }


    /**
     * @param currentCoords coordinates at current position.
     * @param newExit       end of the track on current tile.
     * @return true if a the track connects to a station.
     * @author Bharath Kulkarni.
     */
    public static boolean connectsToStation(String currentCoords, String newExit) {
        boolean flag = false;

        if (currentCoords.charAt(0) == '0' && newExit.equals("1")) {
            flag = true;

        } else if (currentCoords.charAt(0) == '7' && newExit.equals("5")) {
            flag = true;

        } else if (currentCoords.charAt(1) == '7' && newExit.equals("3")) {
            flag = true;

        } else if (currentCoords.charAt(1) == '0' && newExit.equals("7")) {
            flag = true;
        }

        return flag;
    }

    /**
     * @param currentCoords coordinates at current position.
     * @param newExit       end of the track on current tile.
     * @return true if track connects to a middle station.
     * @author Bharath Kulkarni.
     */
    public static boolean connectsToMiddle(String currentCoords, String newExit) {
        boolean flag = false;

        switch (currentCoords) {
            case "23":
            case "24":
                if (newExit.equals("4") || newExit.equals("5")) {
                    flag = true;
                }
                break;

            case "35":
            case "45":
                if (newExit.equals("6") || newExit.equals("7")) {
                    flag = true;
                }
                break;

            case "53":
            case "54":
                if (newExit.equals("0") || newExit.equals("1")) {
                    flag = true;
                }
                break;

            case "42":
            case "32":
                if (newExit.equals("2") || newExit.equals("3")) {
                    flag = true;
                }
                break;
        }
        return flag;
    }

}
