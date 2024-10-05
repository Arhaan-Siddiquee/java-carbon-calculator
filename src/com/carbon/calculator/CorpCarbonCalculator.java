import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        states.put("Assam", 0.56);
        states.put("Bihar", 0.88);
        states.put("Chhattisgarh", 0.83);
        states.put("Goa", 0.77);
        states.put("Gujarat", 0.85);
        states.put("Haryana", 0.92);
        states.put("Himachal Pradesh", 0.34);
        states.put("Jharkhand", 0.80);
        states.put("Karnataka", 0.74);
        states.put("Kerala", 0.64);
        states.put("Madhya Pradesh", 0.90);
        states.put("Maharashtra", 0.89);
        states.put("Manipur", 0.20);
        states.put("Meghalaya", 0.43);
        states.put("Mizoram", 0.25);
        states.put("Nagaland", 0.19);
        states.put("Odisha", 0.78);
        states.put("Punjab", 0.95);
        states.put("Rajasthan", 0.82);
        states.put("Sikkim", 0.28);
        states.put("Tamil Nadu", 0.86);
        states.put("Telangana", 0.87);
        states.put("Tripura", 0.22);
        states.put("Uttar Pradesh", 0.90);
        states.put("Uttarakhand", 0.61);
        states.put("West Bengal", 0.75);
        states.put("Delhi", 1.10);  // Higher due to urban factors

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
            double fuelFactor = 2.32;
            double lpgFactor = 1.8;
            double wasteFactor = 0.44;

            // Calculate total emissions
            double totalEmission = (electricityUsage * electricityFactor + fuelUsage * fuelFactor +
                    lpgUsage * lpgFactor + wasteGeneration * wasteFactor) / members;

            // Display result
            resultLabel.setText(String.format("Total Emission: %.2f kg CO2 per member.", totalEmission));

            // Check emission threshold
            if (totalEmission <= 2000) {
                resultLabel.setForeground(Color.GREEN);
            } else {
                resultLabel.setForeground(Color.RED);
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Please enter valid numeric values.");
            resultLabel.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CorpCarbonCalculator().setVisible(true);
            }
        });
    }
}
