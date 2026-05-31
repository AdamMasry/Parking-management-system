public enum VehicleType{
    SEDAN("Sedan", 2.0),
    SUV("SUV", 3.5),
    TRUCK("Truck", 6.0);

    private final double hourlyRate;
    private final String label;

    private VehicleType(String type, double hourlyRate) {
        this.label = type;
        this.hourlyRate = hourlyRate;
    }

    public double getHourlyRate(){
        return hourlyRate;
    }
    public String getLabel(){
        return label;
    }
}