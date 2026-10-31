# Parking Lot Management System

A Java parking lot manager with two ways to use it: a desktop app (Swing) that saves everything to an SQLite database, and a simple console version.

## What it does

- Park a vehicle by plate number and vehicle type
- Remove a vehicle and get a receipt with the final fee
- See the lot status live: capacity, occupied spots, available spots
- Show each parked vehicle with its entry time, how long it has been parked, and the fee so far (the desktop app updates this every second)
- Find a vehicle by plate number (console version)
- Reject duplicate plates and block parking when the lot is full

## How fees work

Each vehicle type has an hourly rate:

| Type  | Rate per hour |
|-------|---------------|
| Sedan | $2.00         |
| SUV   | $3.50         |
| Truck | $6.00         |

You are charged for every hour you have started, with a minimum of 1 hour. For example, 61 minutes counts as 2 hours.

## Built with

- Java
- Swing (desktop interface)
- SQLite with JDBC (saving data)

## Project structure

| File | What it does |
|------|--------------|
| `ParkingGUI.java` | The desktop app. Also creates the database and saves and loads data. |
| `Main.java` | The console version, with a text menu. |
| `ParkingLot.java` | The lot itself: capacity, parking, removing, and finding vehicles. |
| `Vehicle.java` | A parked vehicle: plate, type, entry time, and the fee calculation. |
| `VehicleType.java` | The vehicle types and their hourly rates. |
| `Display.java` | Prints the menus, status table, and receipts for the console version. |

## How the data is saved

The desktop app creates `parking_system.db` automatically on first run, with two tables:

- `parked_vehicles`: the cars parked right now (plate, type, entry time). When you reopen the app, these cars are loaded back in.
- `history`: one row for every car that has left (plate, type, entry time, exit time, fee).

The console version keeps everything in memory only, so the data is gone when you close it.

## How to run

You need the JDK installed and the SQLite JDBC driver (the `sqlite-jdbc` jar file).

1. Put the jar file in a folder named `lib` inside the project folder.
2. Compile everything:

   ```
   javac *.java
   ```

3. Run the desktop app:

   - Windows: `java -cp ".;lib/*" ParkingGUI`
   - macOS or Linux: `java -cp ".:lib/*" ParkingGUI`

4. Or run the console version (no jar needed):

   ```
   java Main
   ```

The desktop app starts with a capacity of 50 spots. The console version asks you for the capacity when it starts.

## Author

Adam Elmasry
