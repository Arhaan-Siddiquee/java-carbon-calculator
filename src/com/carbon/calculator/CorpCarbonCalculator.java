import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class CorpCarbonCalculator extends JFrame {
    private HashMap<String, Double> states;
    private JTextField electricityInput, fuelInput, lpgInput, wasteInput, membersInput;
    private JComboBox<String> stateComboBox;
    private JLabel resultLabel;

    public CorpCarbonCalculator() {
        // Set up the emission factor data
        states = new HashMap<>();
        states.put("Andaman and Nicobar Islands", 0.79);
        states.put("Andhra Pradesh", 0.91);
        states.put("Arunachal Pradesh", 0.11);
        // Add the rest of the states...
        states.put("Assam", 0.79);
        states.put("Bihar", 0.70);
        states.put("Chhattisgarh", 0.92);
        states.put("Goa", 0.61);
        states.put("Gujarat", 0.95);
        states.put("Haryana", 0.90);
        states.put("Himachal Pradesh", 0.35);
        states.put("Jammu and Kashmir", 0.70);
        states.put("Jharkhand", 0.85);
        states.put("Karnataka", 0.77);
        states.put("Kerala", 0.80);
        states.put("Madhya Pradesh", 0.88);
        states.put("Maharashtra", 0.75);
        states.put("Manipur", 0.44);
        states.put("Meghalaya", 0.45);
        states.put("Mizoram", 0.30);
        states.put("Nagaland", 0.11);
        states.put("Odisha", 0.92);
        states.put("Punjab", 0.87);
        states.put("Rajasthan", 0.92);
        states.put("Sikkim", 0.39);
        states.put("Tamil Nadu", 0.75);
        states.put("Telangana", 0.85);
        states.put("Tripura", 0.79);
        states.put("Uttar Pradesh", 0.83);
        states.put("Uttarakhand", 0.52);
        states.put("West Bengal", 0.75);

        // Create the UI components
        setTitle("CO2 Emission Calculator");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Title
        JLabel title = new JLabel("CO2 Emission Calculator");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.RED);
        add(title, gbc);

        // State Dropdown
        gbc.gridy++;
        JLabel stateLabel = new JLabel("Select Your State:");
        stateLabel.setForeground(Color.WHITE);
        add(stateLabel, gbc);

        stateComboBox = new JComboBox<>(states.keySet().toArray(new String[0]));
        gbc.gridx = 1;
        add(stateComboBox, gbc);

        // Electricity Input
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel electricityLabel = new JLabel("Electricity Usage (kWh):");
        electricityLabel.setForeground(Color.WHITE);
        add(electricityLabel, gbc);

        electricityInput = new JTextField();
        gbc.gridx = 1;
        add(electricityInput, gbc);

        // Fuel Input
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel fuelLabel = new JLabel("Fuel Usage (liters):");
        fuelLabel.setForeground(Color.WHITE);
        add(fuelLabel, gbc);

        fuelInput = new JTextField();
        gbc.gridx = 1;
        add(fuelInput, gbc);

        // LPG Input
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lpgLabel = new JLabel("LPG Usage (cylinders):");
        lpgLabel.setForeground(Color.WHITE);
        add(lpgLabel, gbc);

        lpgInput = new JTextField();
        gbc.gridx = 1;
        add(lpgInput, gbc);

        // Waste Input
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel wasteLabel = new JLabel("Waste Generation (kg):");
        wasteLabel.setForeground(Color.WHITE);
        add(wasteLabel, gbc);

        wasteInput = new JTextField();
        gbc.gridx = 1;
        add(wasteInput, gbc);

        // Members Input
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel membersLabel = new JLabel("Number of Members:");
        membersLabel.setForeground(Color.WHITE);
        add(membersLabel, gbc);

        membersInput = new JTextField();
        gbc.gridx = 1;
        add(membersInput, gbc);

        // Calculate Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton calculateButton = new JButton("Calculate");
        add(calculateButton, gbc);

        // Result Label
        gbc.gridy++;
        resultLabel = new JLabel("Total Emission: ");
        resultLabel.setForeground(Color.GREEN);
        add(resultLabel, gbc);

        // Event Handling
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateEmission();
            }
        });

        // Background and panel styling
        getContentPane().setBackground(Color.BLACK);
    }

    private void calculateEmission() {
        try {
            String state = (String) stateComboBox.getSelectedItem();
            double electricityUsage = Double.parseDouble(electricityInput.getText());
            double fuelUsage = Double.parseDouble(fuelInput.getText());
            double lpgUsage = Double.parseDouble(lpgInput.getText());
            double wasteGeneration = Double.parseDouble(wasteInput.getText());
            int members = Integer.parseInt(membersInput.getText());

            // Emission factors
            double electricityFactor = states.get(state);
            double fuelFactor = 2.32; // Example factor for fuel
            double lpgFactor = 1.8; // Example factor for LPG
            double wasteFactor = 0.44; // Example factor for waste

            // Calculate total emissions
            double totalEmission = (electricityUsage * electricityFactor + fuelUsage * fuelFactor +
                    lpgUsage * lpgFactor + wasteGeneration * wasteFactor) / members;

            // Save emission data to database
            saveToDatabase(state, electricityUsage, fuelUsage, lpgUsage, wasteGeneration, totalEmission, members);

            // Display result
            resultLabel.setText(String.format("Total Emission: %.2f kg CO2 per member.", totalEmission));

            // Check emission threshold
            if (totalEmission <= 2000) {
                resultLabel.setForeground(Color.GREEN);
            } else {
                resultLabel.setForeground(Color.RED);
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Please enter valid numbers.");
            resultLabel.setForeground(Color.RED);
        }
    }

    // Method to save calculated emission data to MySQL database
    private void saveToDatabase(String state, double electricityUsage, double fuelUsage, double lpgUsage, double wasteGeneration, double totalEmission, int members) {
        String url = "jdbc:mysql://localhost:3306/corp_carbon"; // Change this if necessary
        String user = "root"; // Your MySQL username
        String password = "password"; // Your MySQL password

        String query = "INSERT INTO emissions (state, electricity_usage, fuel_usage, lpg_usage, waste_generation, total_emission, members) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, state);
            stmt.setDouble(2, electricityUsage);
            stmt.setDouble(3, fuelUsage);
            stmt.setDouble(4, lpgUsage);
            stmt.setDouble(5, wasteGeneration);
            stmt.setDouble(6, totalEmission);
            stmt.setInt(7, members);

            stmt.executeUpdate();
            System.out.println("Data saved to database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CorpCarbonCalculator calculator = new CorpCarbonCalculator();
        calculator.setVisible(true);
    }
}
