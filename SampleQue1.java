package jktechno;
import java.util.*;

class BLRCard {
    static final int ZONE_1 = 1;
    static final int ZONE_2 = 2;

    static final int PEAK_HOUR_START_1 = 700;
    static final int PEAK_HOUR_END_1 = 1030;
    static final int PEAK_HOUR_START_2 = 1700;
    static final int PEAK_HOUR_END_2 = 2000;

    static final int PEAK_HOUR_START_WEEKEND_1 = 900;
    static final int PEAK_HOUR_END_WEEKEND_1 = 1100;
    static final int PEAK_HOUR_START_WEEKEND_2 = 1800;
    static final int PEAK_HOUR_END_WEEKEND_2 = 2200;

    static final int PEAK_FARE_ZONE_1 = 30;
    static final int PEAK_FARE_ZONE_2 = 35;
    static final int OFF_PEAK_FARE_ZONE_1 = 25;
    static final int OFF_PEAK_FARE_ZONE_2 = 30;

    static final int DAILY_CAP_ZONE_1_1 = 100;
    static final int DAILY_CAP_ZONE_1_2 = 120;
    static final int DAILY_CAP_ZONE_2 = 80;

    static final int WEEKLY_CAP_ZONE_1_1 = 500;
    static final int WEEKLY_CAP_ZONE_1_2 = 600;
    static final int WEEKLY_CAP_ZONE_2 = 400;

    public static int calculateFare(int fromZone, int toZone, int time) {
        int fare;

        if ((fromZone == ZONE_1 && toZone == ZONE_1) || (fromZone == ZONE_2 && toZone == ZONE_2)) {
            if (isPeakHour(time)) {
                fare = (fromZone == ZONE_1) ? PEAK_FARE_ZONE_1 : PEAK_FARE_ZONE_2;
            } else {
                fare = (fromZone == ZONE_1) ? OFF_PEAK_FARE_ZONE_1 : OFF_PEAK_FARE_ZONE_2;
            }
        } else if ((fromZone == ZONE_1 && toZone == ZONE_2) || (fromZone == ZONE_2 && toZone == ZONE_1)) {
            fare = (isPeakHour(time)) ? PEAK_FARE_ZONE_2 : OFF_PEAK_FARE_ZONE_2;
        } else {
            fare = 0; // Invalid zone combination
        }

        return fare;
    }

    public static boolean isPeakHour(int time) {
        int hours = time / 100;
        int minutes = time % 100;

        if ((hours >= PEAK_HOUR_START_1 && hours < PEAK_HOUR_END_1) ||
            (hours >= PEAK_HOUR_START_2 && hours < PEAK_HOUR_END_2)) {
            return true;
        }

        if ((hours >= PEAK_HOUR_START_WEEKEND_1 && hours < PEAK_HOUR_END_WEEKEND_1) ||
            (hours >= PEAK_HOUR_START_WEEKEND_2 && hours < PEAK_HOUR_END_WEEKEND_2)) {
            return true;
        }

        return false;
    }

    public static int applyDailyCap(int totalFare, int fromZone, int toZone) {
        int dailyCap;

        if (fromZone == ZONE_1 && toZone == ZONE_2) {
            dailyCap = DAILY_CAP_ZONE_1_2;
        } else if (fromZone == ZONE_2 && toZone == ZONE_1) {
            dailyCap = DAILY_CAP_ZONE_1_2;
        } else {
            dailyCap = (fromZone == ZONE_1) ? DAILY_CAP_ZONE_1_1 : DAILY_CAP_ZONE_2;
        }

        return Math.min(totalFare, dailyCap);
    }

    public static int applyWeeklyCap(int weeklyTotalFare) {
        return Math.min(weeklyTotalFare, WEEKLY_CAP_ZONE_1_1);
    }

    public static void main(String[] args) {
        // Sample scenarios
        int[] journeys = { 1020, ZONE_2, ZONE_1, 1045, ZONE_1, ZONE_1, 1615, ZONE_1, ZONE_1, 1815, ZONE_1, ZONE_1,
            1900, ZONE_1, ZONE_2, 2200, ZONE_1, ZONE_2 };

        int totalFare = 0;
        for (int i = 0; i < journeys.length; i += 3) {
            int time = journeys[i];
            int fromZone = journeys[i + 1];
            int toZone = journeys[i + 2];

            int journeyFare = calculateFare(fromZone, toZone, time);
            totalFare += journeyFare;
        }

        int cappedFare = applyDailyCap(totalFare, ZONE_1, ZONE_2);
        int weeklyCappedFare = applyWeeklyCap(cappedFare);

        System.out.println("Total Fare: " + totalFare);
        System.out.println("Capped Fare: " + cappedFare);
        System.out.println("Weekly Capped Fare: " + weeklyCappedFare);
    }
}