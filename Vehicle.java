import java.time.*;
import java.time.format.DateTimeFormatter;

public class Vehicle {
    private final String plateNumber;
    private final String type;
    private final LocalDateTime entryTime;

    public Vehicle(String plateNumber, String type) {
        this.plateNumber = plateNumber;
        this.type = type;
        this.entryTime = LocalDateTime.now();
    }
    public Vehicle(String plateNumber, String type, String entryTimeStr) {
        this.plateNumber = plateNumber;
        this.type = type;
        this.entryTime = LocalDateTime.parse(entryTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public String getType(){
        return type;
    }
    public String getPlateNumber(){
        return plateNumber;
    }
    public LocalDateTime getEntryTime(){
        return entryTime;
    }

    public long getBilledHours(){
        long minutes = Duration.between(entryTime, LocalDateTime.now()).toMinutes();
        return Math.max(1, (long) Math.ceil(minutes / 60.0));
    }

    public String getDurationString(){
        Duration d = Duration.between(entryTime, LocalDateTime.now());
        long h = d.toHours();
        long m = d.toMinutesPart();

        return String.format("%dh%02dm", h, m);
    }

    public double calculateFee(){
        return getBilledHours() * VehicleType.valueOf(type.toUpperCase()).getHourlyRate();
    }

    public String getEntryTimeString(){
        return entryTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}