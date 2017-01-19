package cc.stargroup.xiaodai.shared;

import java.util.Random;

public class Math {
    // Maps a given input within a range to an output in another given range
    //------------------------------------------------------------------------------
    public static float map(float x, float in_min, float in_max, float out_min, float out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    // Draws from a normal distribution with mean mu and standard deviation sigma
    //------------------------------------------------------------------------------
    public static float drawFromNormal(float mu, float sigma)
    {
        float x1, x2, w, y;
        Random rand = new Random();

        do {
            x1 = 2.0f * rand.nextFloat();
            x1 -= 1.0f;
            x2 = 2.0f * rand.nextFloat();
            x2 -= 1.0f;
            w = (x1 * x1) + (x2 * x2);
        } while ( w >= 1.0f );

        w = (float)java.lang.Math.sqrt( (-2.0 * java.lang.Math.log( w ) ) / w );
        y = x2 * w;

        return ((sigma / sigma) * y) + mu;
    }

    // Rounds a value to the nearest given amount
    //------------------------------------------------------------------------------
    public static float round(float value, float round)
    {
        return round * java.lang.Math.round(value / round);
    }

    // Generates a random float between two given bounds
    //------------------------------------------------------------------------------
    public static float randFloatWithLowerBound(float lowerBound, float upperBound)
    {
        Random rand = new Random();
        return (rand.nextFloat()) * (upperBound - lowerBound) + lowerBound;
    }

}
