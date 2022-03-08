package server.util;

public class MathUtil {
	public static float linearMap(float value, float inMin, float inMax, float outMin, float outMax) {
		return (((value - inMin) / (inMax - inMin)) * (outMax - outMin)) + outMin;
	}
}
