
package utils;

public class AirQualityEvaluator {

    public static String evaluateAirQuality(double so2, double no2, double pm10, double pm25, double o3, double co) {
        int score = Math.max(
                Math.max(getIndexSO2(so2), getIndexNO2(no2)),
                Math.max(Math.max(getIndexPM10(pm10), getIndexPM25(pm25)),
                        Math.max(getIndexO3(o3), getIndexCO(co)))
        );

        switch (score) {
            case 1: return "Good";
            case 2: return "Fair";
            case 3: return "Moderate";
            case 4: return "Poor";
            case 5: return "Very Poor";
            default: return "Unknown";
        }

    }

    private static int getIndexSO2(double value) {
        if (value < 20) return 1;
        if (value < 80) return 2;
        if (value < 250) return 3;
        if (value < 350) return 4;
        return 5;
    }

    private static int getIndexNO2(double value) {
        if (value < 40) return 1;
        if (value < 70) return 2;
        if (value < 150) return 3;
        if (value < 200) return 4;
        return 5;
    }

    private static int getIndexPM10(double value) {
        if (value < 20) return 1;
        if (value < 50) return 2;
        if (value < 100) return 3;
        if (value < 200) return 4;
        return 5;
    }

    private static int getIndexPM25(double value) {
        if (value < 10) return 1;
        if (value < 25) return 2;
        if (value < 50) return 3;
        if (value < 75) return 4;
        return 5;
    }

    private static int getIndexO3(double value) {
        if (value < 60) return 1;
        if (value < 100) return 2;
        if (value < 140) return 3;
        if (value < 180) return 4;
        return 5;
    }

    private static int getIndexCO(double value) {
        if (value < 4400) return 1;
        if (value < 9400) return 2;
        if (value < 12400) return 3;
        if (value < 15400) return 4;
        return 5;
    }
}
