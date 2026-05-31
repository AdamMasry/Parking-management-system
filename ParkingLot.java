import java.util.*;

public class ParkingLot {
    private final int capacity;
    private Map<String, Vehicle> slots = new LinkedHashMap<>();

    public ParkingLot(int capacity){
        this.capacity = capacity;
    }

    public boolean isFull(){
        return slots.size() >= capacity;
    }
    public int getCapacity(){
        return capacity;
    }
    public int getOccupied(){
        return slots.size();
    }
    public int getAvailable(){
        return capacity - slots.size();
    }

    public boolean parkVehicle(Vehicle v){
        if(isFull())
            return false;
        if(slots.containsKey(v.getPlateNumber()))
            return false;
        slots.put(v.getPlateNumber(), v);
        return true;
    }

    public Vehicle removeVehicle(String plate){
        return slots.remove(plate.toUpperCase());
    }

    public Vehicle findVehicle(String plate){
        return slots.get(plate.toUpperCase());
    }

    public Collection<Vehicle> getAllVehicles(){
        return slots.values();
    }
    
}
