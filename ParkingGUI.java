import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class ParkingGUI extends JFrame {
    private ParkingLot lot;
    private DefaultTableModel tableModel;
    private JLabel lblCapacity, lblOccupied, lblAvailable;
    
    private final String DB_URL = "jdbc:sqlite:parking_system.db";

    public ParkingGUI(ParkingLot lot) {
        this.lot = lot;

        initDatabase();
        loadFromDatabase();

        setTitle("Parking Lot System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        refreshAll();
        Timer timer = new Timer(1000, e -> refreshAll());
        timer.start();

        setVisible(true);
    }

    private void initDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            
            try (Connection conn = DriverManager.getConnection(DB_URL); Statement st = conn.createStatement()) {
                st.execute("CREATE TABLE IF NOT EXISTS parked_vehicles (" +
                             "plate TEXT PRIMARY KEY, " +
                             "type TEXT, " +
                             "entry_time TEXT)");

                st.execute("CREATE TABLE IF NOT EXISTS history (" +
                             "plate TEXT, " +
                             "type TEXT, " +
                             "entry_time TEXT, " +
                             "exit_time TEXT, " +
                             "fee REAL)");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Init Error: " + e.getMessage());
        }
    }

    private void loadFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM parked_vehicles")) {
            
            while (rs.next()) {
                Vehicle v = new Vehicle(rs.getString("plate"), rs.getString("type"), rs.getString("entry_time"));
                lot.parkVehicle(v);
            }
        } catch (SQLException e) {
            System.err.println("Load Error: " + e.getMessage());
        }
    }

    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        lblCapacity = new JLabel();
        lblOccupied = new JLabel();
        lblAvailable = new JLabel();
        
        for (JLabel l : new JLabel[]{lblCapacity, lblOccupied, lblAvailable}) {
            l.setFont(new Font("Segoe UI", Font.BOLD, 16));
            l.setForeground(new Color(45, 52, 54));
            panel.add(l);
        }
        return panel;
    }

    private JScrollPane buildTablePanel() {
        String[] cols = {"Plate", "Type", "Entry Time", "Duration", "Fee"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35); 
        table.setGridColor(new Color(230, 230, 230)); 
        table.setSelectionForeground(Color.BLACK);
        table.setBackground(Color.WHITE);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(45, 52, 54)); 
        table.getTableHeader().setForeground(Color.WHITE);           
        table.getTableHeader().setPreferredSize(new Dimension(0, 40)); 
        table.getTableHeader().setReorderingAllowed(false);          

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        scrollPane.getViewport().setBackground(Color.WHITE);     
        
        return scrollPane;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));

        JButton btnPark = new JButton("Park Vehicle");
        JButton btnRemove = new JButton("Remove Vehicle");
        JButton btnExit = new JButton("Exit");

        btnPark.setBackground(new Color(0, 76, 153));
        btnPark.setForeground(Color.WHITE);
        btnRemove.setBackground(new Color(0, 102, 204));
        btnRemove.setForeground(Color.WHITE);
        btnExit.setBackground(new Color(0, 128, 255));
        btnExit.setForeground(Color.WHITE);


        btnPark.addActionListener(e -> doPark());
        btnRemove.addActionListener(e -> doRemove());
        btnExit.addActionListener(e -> System.exit(0));

        panel.add(btnPark);
        panel.add(btnRemove);
        panel.add(btnExit);
        return panel;
    }

    private void doPark() {
        if (lot.isFull()) {
            JOptionPane.showMessageDialog(this, "Lot is full!");
            return;
        }

        String plate = JOptionPane.showInputDialog(this, "Enter plate number:");
        if (plate == null || plate.trim().isEmpty())
            return;
        plate = plate.trim().toUpperCase();

        if (lot.findVehicle(plate) != null) {
            JOptionPane.showMessageDialog(this, plate + " is already parked.");
            return;
        }

        String[] types = {"Sedan", "SUV", "Truck"};
        int choice = JOptionPane.showOptionDialog(this, "Select vehicle type:", "Type", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, types, types[0]);

        if (choice < 0)
            return;

        Vehicle v = new Vehicle(plate, types[choice]);
        
        if (lot.parkVehicle(v)) {
            try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement ps = conn.prepareStatement("INSERT INTO parked_vehicles VALUES(?,?,?)")) {
                ps.setString(1, v.getPlateNumber());
                ps.setString(2, v.getType());
                ps.setString(3, v.getEntryTimeString());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        refreshAll();
    }

    private void doRemove() {
        String input = JOptionPane.showInputDialog(this, "Enter plate to remove:");
        if (input == null || input.trim().isEmpty())
            return;
        String plate = input.trim().toUpperCase();

        Vehicle v = lot.removeVehicle(plate);
        if (v == null) {
            JOptionPane.showMessageDialog(this, "Vehicle not found.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement ps = conn.prepareStatement("DELETE FROM parked_vehicles WHERE plate = ?")) {
            ps.setString(1, plate);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement ps = conn.prepareStatement("INSERT INTO history VALUES(?,?,?,?,?)")) {

            ps.setString(1, v.getPlateNumber());
            ps.setString(2, v.getType());
            ps.setString(3, v.getEntryTimeString());
            ps.setString(4, java.time.LocalDateTime.now().toString());
            ps.setDouble(5, v.calculateFee());

            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "Receipt:\nPlate: " + v.getPlateNumber() + "\nFee: $" + String.format("%.2f", v.calculateFee()));
        refreshAll();
    }

    private void refreshAll() {
        lblCapacity.setText("Capacity: " + lot.getCapacity());
        lblOccupied.setText("Occupied: " + lot.getOccupied());
        lblAvailable.setText("Available: " + lot.getAvailable());

        tableModel.setRowCount(0);
        for (Vehicle v : lot.getAllVehicles()) {
            tableModel.addRow(new Object[]{
                v.getPlateNumber(),
                v.getType(),
                v.getEntryTimeString(),
                v.getDurationString(),
                String.format("$%.2f", v.calculateFee())
            });
        }
    }

    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot(50);
        new ParkingGUI(lot);
    }
}