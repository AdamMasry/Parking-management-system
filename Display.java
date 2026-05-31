public class Display {
    private static final String divider = "-".repeat(65);
    public static void menu(ParkingLot PL){
        System.out.println("1: Park a vehicle.");
        System.out.println("2: Remove a vehicle.");
        System.out.println("3: View current situation.");
        System.out.println("4: Find a vehicle.");
        System.out.println("5: Exit.");
        System.out.println(divider);
        System.out.println("\nChoice: ");
    }

    public static void vehicleTypeOptions(){
        System.out.println("Vehicle Types: ");
        for(VehicleType t: VehicleType.values()){
            System.out.printf("%d: %s $%.2f/hr%n", t.ordinal() + 1, t.getLabel(), t.getHourlyRate());
        }
        System.out.println(divider);
        System.out.println("Select (1-3): ");
    }

    public static void parkSuccess(Vehicle v){
        System.out.println("\nVehicle Parked Successfully.\n");
        System.out.printf("Plate: %s.%n", v.getPlateNumber());
        System.out.printf("Vehicle Type: %s.%n", v.getType());
        System.out.printf("Entry time: %s.%n", v.getEntryTimeString());
        System.out.printf("Rate: $%.2f/hr.%n%n", VehicleType.valueOf(v.getType().toUpperCase()).getHourlyRate());
    }

    public static void printReceipt(Vehicle v){
        System.out.println("Vehicle removed.");
        System.out.println(divider);

        System.out.printf("Plate: %s%n", v.getPlateNumber());
        System.out.printf("Type: %s%n", v.getType());
        System.out.printf("Entry Time: %s%n", v.getEntryTimeString());
        System.out.printf("Duration: %s%n", v.getDurationString());
        System.out.printf("Billed Hours: %d Hours%n", v.getBilledHours());
        System.out.printf("Rate: %.2f/hr%n", VehicleType.valueOf(v.getType().toUpperCase()).getHourlyRate());
        System.out.println(divider);
        System.out.printf("Total Fee: %.2f%n", v.calculateFee());

        System.out.println("Thank you for choosing us!!");
        System.out.println(divider);
    }

    public static void status(ParkingLot PL){

        System.out.println(divider);
        System.out.printf("Capacity: %d | Occupied: %d | Available: %d%n",
                PL.getCapacity(),
                PL.getOccupied(),
                PL.getAvailable());
        System.out.println(divider);

        if(PL.getAllVehicles().isEmpty()){
            System.out.println("No vehicles currently parked!");
            System.out.println(divider);
            return;
        }

        System.out.printf("%-15s %-10s %-22s %-12s %-10s%n", "Plate", "Type", "Entry Time", "Duration", "Fee");

        System.out.println(divider);

        for(Vehicle v : PL.getAllVehicles()){

            System.out.printf("%-15s %-10s %-22s %-12s $%-10.2f%n",
                    v.getPlateNumber(),
                    v.getType(),
                    v.getEntryTimeString(),
                    v.getDurationString(),
                    v.calculateFee());
        }

        System.out.println(divider);
    }

    public static void vehicleDetails(Vehicle v){
        System.out.println("Vehicle Found\n");
    
        System.out.printf("Plate: %s%n", v.getPlateNumber());
        System.out.printf("Type: %s%n", v.getType());
        System.out.printf("Entry Time: %s%n", v.getEntryTimeString());
        System.out.printf("Duration: %s%n", v.getDurationString());
        System.out.printf("Billed Hours: %d Hours%n", v.getBilledHours());
        System.out.printf("Rate: %.2f/hr%n", VehicleType.valueOf(v.getType().toUpperCase()).getHourlyRate());
        System.out.printf("Total Fee: %.2f%n", v.calculateFee());
    }
}
