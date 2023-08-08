package jktechno;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

class Journey {
    DayOfWeek day;
    String zones;
    int dailyFare;

    public Journey(DayOfWeek day, String zones, int dailyFare) {
        this.day = day;
        this.zones = zones;
        this.dailyFare = dailyFare;
    }
}

public class blrcard2 {
    private static final int[][] FARES = {
        {30, 25},   // 1-1
        {35, 30},   // 1-2 or 2-1
        {25, 20}    // 2-2
    };
    private static final int[] DAILY_CAPS = {100, 120, 80};
    private static final int[] WEEKLY_CAPS = {500, 600, 400};

    public static int calculateFare(Journey[] journeys) {
        int totalFare = 0;
        Map<DayOfWeek, Integer> dailyFares = new HashMap<>();
        Map<DayOfWeek, Integer> weeklyFares = new HashMap<>();
        DayOfWeek currentWeekStart = null;

        for (Journey journey : journeys) {
            if (currentWeekStart == null || journey.day.compareTo(currentWeekStart) >= 7) {
                currentWeekStart = journey.day;
                weeklyFares.clear();
            }

            int fromZone = Integer.parseInt(journey.zones.split("-")[0].trim());
            int toZone = Integer.parseInt(journey.zones.split("-")[1].trim());
            int fare = isPeakHour(journey.day) ? FARES[toZone - 1][fromZone - 1] : FARES[fromZone - 1][toZone - 1];
            
            dailyFares.put(journey.day, dailyFares.getOrDefault(journey.day, 0) + fare);
            weeklyFares.put(journey.day, weeklyFares.getOrDefault(journey.day, 0) + fare);

            if (dailyFares.get(journey.day) > DAILY_CAPS[fromZone - 1]) {
                totalFare += DAILY_CAPS[fromZone - 1] - (dailyFares.get(journey.day) - fare);
            } else {
                totalFare += fare;
            }

            if (weeklyFares.values().stream().mapToInt(Integer::intValue).sum() > WEEKLY_CAPS[fromZone - 1]) {
                totalFare += WEEKLY_CAPS[fromZone - 1] - weeklyFares.get(journey.day);
            }
        }

        return totalFare;
    }

    private static boolean isPeakHour(DayOfWeek day) {
        return (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY);
    }

    public static void main(String[] args) {
        Journey[] journeys = {
            new Journey(DayOfWeek.MONDAY, "1 - 2", 120),
            new Journey(DayOfWeek.TUESDAY, "1 - 2", 120),
            new Journey(DayOfWeek.WEDNESDAY, "1 - 2", 120),
            new Journey(DayOfWeek.THURSDAY, "1 - 2", 120),
            new Journey(DayOfWeek.FRIDAY, "1 - 1", 80),
            new Journey(DayOfWeek.SATURDAY, "1 - 2", 40),
            new Journey(DayOfWeek.SUNDAY, "1 - 2", 0),
            new Journey(DayOfWeek.MONDAY, "1 - 2", 100)
        };

        int fare = calculateFare(journeys);
        System.out.println("Total Fare: " + fare); // Output: 700
    }
}

