package ca.mcgill.ecse211;

import static ca.mcgill.ecse211.Resources.*;

/**
 * Parses wifi parameter from the server in order to deal with edge cases
 */
public class WifiParser {
    public static int ourCorner;
    
    //todo, idk where we should call this, it HAS to be after we received wifi params
    
    /**
     * corner number
     */
    public static void setOurCorner() {
        if (greenTeam == 15) {
            ourCorner = greenCorner;
        }
        if (redTeam == 15) {
            ourCorner = redCorner;
        }
    }
    
    public static int getOurCorner() {
        return ourCorner;
    }
    
    /**
     * Localization point
     * [0] is x, [1] is y, [2] is angle
     *
     * @return
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
     * vertical is in y
     * horizontal is in x
     *
     * @return: true if vertical, false horizontal
     */
    public static boolean isTunnelVertical() {
        if (greenTeam == 15) {
            if (green.ur.x - 1 == green.ll.x)
                return true;
            if (green.ur.y - 1 == green.ll.y) {
                return false;
            }
        }
        
        if (redTeam == 15) {
            if (red.ur.x - 1 == red.ll.x)
                return true;
            if (red.ur.y - 1 == red.ll.y) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Localization point before entering the tunnel
     * [0,1] are x,y for entry of tunnel
     * [2,3] are x,y for exit of tunnel
     */
    public static double[] tunnelLocalizationPoints() {
        Region ourTunnel = null; // this shouldnt matter just so compiler doesnt scream at me
        if (greenTeam == 15) {
            ourTunnel = green;
        }
        if (redTeam == 15) {
            ourTunnel = red;
        }
        boolean isTunnelVertical = isTunnelVertical();
        int corner = getOurCorner();
        
        //todo, check if the point return is IN THE WATER, if it is, return the another point
        double[] localizationPoint = new double[4];
        if (corner == 0) {
            if (isTunnelVertical) {
                localizationPoint[0] = ourTunnel.ll.x;
                localizationPoint[1] = ourTunnel.ll.y - 1;
                localizationPoint[2] = ourTunnel.ur.x;
                localizationPoint[3] = ourTunnel.ur.y + 1;
            }
            if (!isTunnelVertical) {
                localizationPoint[0] = ourTunnel.ll.x - 1;
                localizationPoint[1] = ourTunnel.ll.y;
                localizationPoint[2] = ourTunnel.ur.x + 1;
                localizationPoint[3] = ourTunnel.ur.y;
            }
        }
        
        if (corner == 1) {
            if (isTunnelVertical) {
                localizationPoint[0] = ourTunnel.ll.x;
                localizationPoint[1] = ourTunnel.ll.y - 1;
                localizationPoint[2] = ourTunnel.ur.x;
                localizationPoint[3] = ourTunnel.ur.y + 1;
            }
            if (!isTunnelVertical) {
                localizationPoint[0] = ourTunnel.ur.x + 1;
                localizationPoint[1] = ourTunnel.ur.y;
                localizationPoint[2] = ourTunnel.ll.x - 1;
                localizationPoint[3] = ourTunnel.ll.y;
            }
        }
        
        if (corner == 2) {
            if (isTunnelVertical) {
                localizationPoint[0] = ourTunnel.ur.x;
                localizationPoint[1] = ourTunnel.ur.y + 1;
                localizationPoint[2] = ourTunnel.ll.x;
                localizationPoint[3] = ourTunnel.ll.y - 1;
            }
            if (!isTunnelVertical) {
                localizationPoint[0] = ourTunnel.ur.x + 1;
                localizationPoint[1] = ourTunnel.ur.y;
                localizationPoint[2] = ourTunnel.ll.x - 1;
                localizationPoint[3] = ourTunnel.ll.y;
            }
        }
        
        if (corner == 3) {
            if (isTunnelVertical) {
                localizationPoint[0] = ourTunnel.ur.x;
                localizationPoint[1] = ourTunnel.ur.y + 1;
                localizationPoint[2] = ourTunnel.ll.x;
                localizationPoint[3] = ourTunnel.ll.y - 1;
            }
            if (!isTunnelVertical) {
                localizationPoint[0] = ourTunnel.ll.x - 1;
                localizationPoint[1] = ourTunnel.ll.y;
                localizationPoint[2] = ourTunnel.ur.x + 1;
                localizationPoint[3] = ourTunnel.ur.y;
            }
        }
        // check if the point is in the water
        return localizationPoint;
    }
    
    /*
    after we generate the point, we have to check if it is in the water
    if it is, generate the opposite one
    if not, return the same point
     */
    public boolean checkIfPointIsInWater() {
        return false;
    }
}

