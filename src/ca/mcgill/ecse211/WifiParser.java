package ca.mcgill.ecse211;

import static ca.mcgill.ecse211.Resources.*;

/**
 * Parses wifi parameter from the server in order to deal with edge cases. This class is made in order to make the rest simply.
 */
public class WifiParser {
    /**
     * Our corner.
     */
    public static int ourCorner;
    
    /**
     * Our Tunnel
     */
    public static Region ourTunnel = null; // this shouldnt matter just so compiler doesnt scream at me
    
    /**
     * Sets our corner number based on our team number from wifi parameter
     */
    public static void setOurCorner() {
        if (greenTeam == 15) {
            ourCorner = greenCorner;
        }
        if (redTeam == 15) {
            ourCorner = redCorner;
        }
    }
    
    /**
     * Returns our corner number
     *
     * @return: corner number
     */
    public static int getOurCorner() {
        setOurCorner();
        return ourCorner;
    }
    
    /**
     * Generates Localization points based on the corner. This is will be used to set the odometer after localization.
     * [0] is X
     * [1] is Y
     * [2] is Theta
     *
     * @return: Array of localization parameter
     */
    public static int[] getLocalizeStartingPoint() {
        int[] location = new int[3];
        
        int corner = getOurCorner();
        if (corner == 0) {
            location[0] = 1;
            location[1] = 1;
            location[2] = 0;
        }
        if (corner == 1) {
            location[0] = 14;
            location[1] = 1;
            location[2] = 270;
        }
        if (corner == 2) {
            location[0] = 14;
            location[1] = 8;
            location[2] = 180;
        }
        if (corner == 3) {
            location[0] = 1;
            location[1] = 8;
            location[2] = 90;
        }
        return location;
    }
    
    /**
     * Checks if the tunnel is vertical our horizontal based on its coordinates
     * @return: true if vertical, false horizontal
     */
    public static boolean isTunnelVertical() {
        if (greenTeam == 15) {
          System.out.println(tng.ur.x - tng.ll.x);
            if (tng.ur.x - tng.ll.x == 1.0)
                return true;
            else if (tng.ur.y - tng.ll.y == 1.0) {
                return false;
            }
        }
        
        if (redTeam == 15) {
          System.out.println(tnr.ur.x - tnr.ll.x);
            if (tnr.ur.x - tnr.ll.x == 1.0)
                return true;
            else if (tnr.ur.y - tnr.ll.y == 1.0) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Gives the localization points for the entry and the exit of the tunnel.
     * [0,1] are x,y for entry of tunnel
     * [2,3] are x,y for exit of tunnel
     * [4] is the turn angle to face tunnel after localizing at entrance
     */
    public static double[] tunnelLocalizationPoints() {
     
        if (greenTeam == 15) {
            ourTunnel = tng;
        }
        if (redTeam == 15) {
            ourTunnel = tnr;
        }
        boolean isTunnelVertical = isTunnelVertical();
        int corner = getOurCorner();
    
        double[] localizationPoint = new double[5];
        if (corner == 0) {
            if (isTunnelVertical) {
                if (!checkIfPointIsInWater(ourTunnel.ll.x, ourTunnel.ll.y - 1)) {
                    localizationPoint[0] = ourTunnel.ll.x;
                    localizationPoint[1] = ourTunnel.ll.y - 1;
                } else {
                    localizationPoint[0] = ourTunnel.ll.x + 1;
                    localizationPoint[1] = ourTunnel.ll.y - 1;
                }
    
                if (!checkIfPointIsInWater(ourTunnel.ur.x - 1, ourTunnel.ur.y + 1)) {
                    localizationPoint[2] = ourTunnel.ur.x - 1;
                    localizationPoint[3] = ourTunnel.ur.y + 1;
                } else {
                    localizationPoint[2] = ourTunnel.ur.x;
                    localizationPoint[3] = ourTunnel.ur.y + 1;
                }
    
                localizationPoint[4] = 0.0;
            }
    
            if (!isTunnelVertical) {
                if (!checkIfPointIsInWater(ourTunnel.ll.x - 1, ourTunnel.ll.y)) {
                    localizationPoint[0] = ourTunnel.ll.x - 1;
                    localizationPoint[1] = ourTunnel.ll.y;
                } else {
                    localizationPoint[0] = ourTunnel.ll.x - 1;
                    localizationPoint[1] = ourTunnel.ll.y + 1;
                }
        
                if (!checkIfPointIsInWater(ourTunnel.ur.x + 1, ourTunnel.ur.y)) {
                    localizationPoint[2] = ourTunnel.ur.x + 1;
                    localizationPoint[3] = ourTunnel.ur.y;
                } else {
                    localizationPoint[2] = ourTunnel.ur.x + 1;
                    localizationPoint[3] = ourTunnel.ur.y - 1;
                }
                
                localizationPoint[4] = 90.0;
            }
        }
        
        if (corner == 1) {
            if (isTunnelVertical) {
                if (!checkIfPointIsInWater(ourTunnel.ll.x, ourTunnel.ll.y - 1)) {
                    localizationPoint[0] = ourTunnel.ll.x;
                    localizationPoint[1] = ourTunnel.ll.y - 1;
                } else {
                    localizationPoint[0] = ourTunnel.ll.x + 1;
                    localizationPoint[1] = ourTunnel.ll.y - 1;
                }
    
                if (!checkIfPointIsInWater(ourTunnel.ur.x - 1, ourTunnel.ur.y + 1)) {
                    localizationPoint[2] = ourTunnel.ur.x - 1;
                    localizationPoint[3] = ourTunnel.ur.y + 1;
                } else {
                    localizationPoint[2] = ourTunnel.ur.x;
                    localizationPoint[3] = ourTunnel.ur.y + 1;
                }
        
                localizationPoint[4] = 0.0;
            }
    
            if (!isTunnelVertical) {
                if (!checkIfPointIsInWater(ourTunnel.ur.x + 1, ourTunnel.ur.y)) {
                    localizationPoint[0] = ourTunnel.ur.x + 1;
                    localizationPoint[1] = ourTunnel.ur.y;
                } else {
                    localizationPoint[0] = ourTunnel.ur.x + 1;
                    localizationPoint[1] = ourTunnel.ur.y - 1;
                }
        
                if (!checkIfPointIsInWater(ourTunnel.ll.x - 1, ourTunnel.ll.y)) {
                    localizationPoint[2] = ourTunnel.ll.x - 1;
                    localizationPoint[3] = ourTunnel.ll.y;
                } else {
                    localizationPoint[2] = ourTunnel.ll.x - 1;
                    localizationPoint[3] = ourTunnel.ll.y + 1;
                }
        
                localizationPoint[4] = 270.0;
            }
        }
        
        if (corner == 2) {
            if (isTunnelVertical) {
                if (!checkIfPointIsInWater(ourTunnel.ur.x - 1, ourTunnel.ur.y + 1)) {
                    localizationPoint[0] = ourTunnel.ur.x - 1;
                    localizationPoint[1] = ourTunnel.ur.y + 1;
                } else {
                    localizationPoint[0] = ourTunnel.ur.x;
                    localizationPoint[1] = ourTunnel.ur.y + 1;
                }
    
                if (!checkIfPointIsInWater(ourTunnel.ll.x, ourTunnel.ll.y - 1)) {
                    localizationPoint[2] = ourTunnel.ll.x;
                    localizationPoint[3] = ourTunnel.ll.y - 1;
                } else {
                    localizationPoint[2] = ourTunnel.ll.x + 1;
                    localizationPoint[3] = ourTunnel.ll.y - 1;
                }
                
                localizationPoint[4] = 180.0;
            }
            if (!isTunnelVertical) {
                if (!checkIfPointIsInWater(ourTunnel.ur.x + 1, ourTunnel.ur.y)) {
                    localizationPoint[0] = ourTunnel.ur.x + 1;
                    localizationPoint[1] = ourTunnel.ur.y;
                } else {
                    localizationPoint[0] = ourTunnel.ur.x + 1;
                    localizationPoint[1] = ourTunnel.ur.y - 1;
                }
    
                if (!checkIfPointIsInWater(ourTunnel.ll.x - 1, ourTunnel.ll.y)) {
                    localizationPoint[2] = ourTunnel.ll.x - 1;
                    localizationPoint[3] = ourTunnel.ll.y;
                } else {
                    localizationPoint[2] = ourTunnel.ll.x - 1;
                    localizationPoint[3] = ourTunnel.ll.y + 1;
                }
        
                localizationPoint[4] = 270.0;
            }
    
        }
        
        if (corner == 3) {
            if (isTunnelVertical) {
                if (!checkIfPointIsInWater(ourTunnel.ur.x - 1, ourTunnel.ur.y + 1)) {
                    localizationPoint[0] = ourTunnel.ur.x - 1;
                    localizationPoint[1] = ourTunnel.ur.y + 1;
                } else {
                    localizationPoint[0] = ourTunnel.ur.x;
                    localizationPoint[1] = ourTunnel.ur.y + 1;
                }
    
                if (!checkIfPointIsInWater(ourTunnel.ll.x, ourTunnel.ll.y - 1)) {
                    localizationPoint[2] = ourTunnel.ll.x;
                    localizationPoint[3] = ourTunnel.ll.y - 1;
                } else {
                    localizationPoint[2] = ourTunnel.ll.x + 1;
                    localizationPoint[3] = ourTunnel.ll.y - 1;
                }
                
                localizationPoint[4] = 180.0;
            }
            if (!isTunnelVertical) {
                if (!checkIfPointIsInWater(ourTunnel.ll.x - 1, ourTunnel.ll.y)) {
                    localizationPoint[0] = ourTunnel.ll.x - 1;
                    localizationPoint[1] = ourTunnel.ll.y;
                } else {
                    localizationPoint[0] = ourTunnel.ll.x - 1;
                    localizationPoint[1] = ourTunnel.ll.y + 1;
                }
    
                if (!checkIfPointIsInWater(ourTunnel.ur.x + 1, ourTunnel.ur.y)) {
                    localizationPoint[2] = ourTunnel.ur.x + 1;
                    localizationPoint[3] = ourTunnel.ur.y;
                } else {
                    localizationPoint[2] = ourTunnel.ur.x + 1;
                    localizationPoint[3] = ourTunnel.ur.y - 1;
                }
    
                localizationPoint[4] = 90.0;
            }
        }
        // check if the point is in the water
        return localizationPoint;
    }
    
    /**
     * Checks if the giving localization point is in the water.
     *
     * @param x: X-coordinate localization
     * @param y: Y-coordinate localization
     * @return: true if it is in water, false otherwise.
     */
    public static boolean checkIfPointIsInWater(double x, double y) {
        // Checks if in red zone
        if (x > red.ll.x + 1 && x < red.ur.x - 1 && y > red.ll.y + 1 && y < red.ur.y - 1) {
            return false;
        }
        
        // Checks if in green zone
        if (x > green.ll.x + 1 && x < green.ur.x - 1 && y > green.ll.y + 1 && y < green.ur.y - 1) {
            return false;
        }
    
        // Checks if on Island
        if (x > island.ll.x + 1 && x < island.ur.x - 1 && y > island.ll.y + 1 && y < island.ur.y - 1) {
            return false;
        }
        
        return true;
    }
}

