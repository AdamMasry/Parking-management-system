import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity;
        try{
            System.out.println("Please enter the capacity: ");
            capacity = sc.nextInt();
            if(capacity <= 0){
                throw new NumberFormatException();
            }
        } catch(NumberFormatException e){
            System.out.println("Invalid input: The assgined capacity is 100.");
            capacity = 100;
        }
        ParkingLot PL = new ParkingLot(capacity);
        System.out.println("Parking lot created with capacity: " + capacity);

        String choice = sc.nextLine();

        while(!choice.equals("5")){

            Display.menu(PL);
            choice = sc.nextLine();
            switch(choice){
                case "1":
                    handlePark(PL, sc);
                    break;
                case "2":
                    handleRemove(PL, sc);
                    break;
                case "3":
                    Display.status(PL);
                    break;
                case "4":
                    handleSearch(PL, sc);
                    break;
                case "5":
                    System.out.println("Exiting the program!!");
                    break;
                default:
                    System.out.println("Invalid Choice, Please enter 1 - 5");
            }
        }
        sc.close();
    }
    private static void handlePark(ParkingLot lot, Scanner sc) { 
        if (lot.isFull()){
            System.out.println("Lot is full — no available slots.");
            return;
        }
 
        System.out.print("Plate number: ");
        String plate = sc.nextLine().trim();
        if (plate.isEmpty()){
            System.out.println("Plate cannot be empty.");
            return;
        }
        if (lot.findVehicle(plate) != null){
            System.out.println("Plate " + plate.toUpperCase() + " is already parked.");
            return;
        }
 
        Display.vehicleTypeOptions();
        VehicleType type;
        switch (sc.nextLine().trim()){
            case "1": type = VehicleType.SEDAN; break;
            case "2": type = VehicleType.SUV;   break;
            case "3": type = VehicleType.TRUCK; break;
            default:  System.out.println("Invalid type."); 
            return;
        }
 
        Vehicle v = new Vehicle(plate, type.toString());
        lot.parkVehicle(v);
        Display.parkSuccess(v);
    }
 
    private static void handleRemove(ParkingLot lot, Scanner sc) {
 
        System.out.print("Plate number: ");
        String plate = sc.nextLine().trim();
 
        Vehicle v = lot.removeVehicle(plate);
        if (v == null) {
            System.out.println("No vehicle with plate \"" + plate.toUpperCase() + "\" found.");
            return;
        }
 
        Display.printReceipt(v);
    }
 
    private static void handleSearch(ParkingLot lot, Scanner sc) {
 
        System.out.print("Plate number: ");
        String plate = sc.nextLine().trim();
 
        Vehicle v = lot.findVehicle(plate);
        if (v == null) {
            System.out.println("No vehicle with plate \"" + plate.toUpperCase() + "\" found.");
            return;
        }
 
        Display.vehicleDetails(v);
    }
}
