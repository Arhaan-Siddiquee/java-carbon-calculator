import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        // ...

        // Create the UI components
        setTitle("CO2 Emission Calculator");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for title
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 102, 102));
        JLabel title = new JLabel("CO2 Emission Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        topPanel.add(title);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for form inputs
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(new Color(60, 63, 65));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // State Dropdown
        JLabel stateLabel = new JLabel("Select Your State:");
        stateLabel.setForeground(Color.WHITE);
        stateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(stateLabel, gbc);

        stateComboBox = new JComboBox<>(states.keySet().toArray(new String[0]));
        stateComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        centerPanel.add(stateComboBox, gbc);

        // Electricity Input
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel electricityLabel = new JLabel("Electricity Usage (kWh):");
        electricityLabel.setForeground(Color.WHITE);
        electricityLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(electricityLabel, gbc);

        electricityInput = new JTextField();
        electricityInput.setFont(new Font("Arial", Font.PLAIN, 16));
        electricityInput.setToolTipText("Enter electricity usage in kWh");
        gbc.gridx = 1;
        centerPanel.add(electricityInput, gbc);

        // Fuel Input
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel fuelLabel = new JLabel("Fuel Usage (liters):");
        fuelLabel.setForeground(Color.WHITE);
        fuelLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(fuelLabel, gbc);

        fuelInput = new JTextField();
        fuelInput.setFont(new Font("Arial", Font.PLAIN, 16));
        fuelInput.setToolTipText("Enter fuel usage in liters");
        gbc.gridx = 1;
        centerPanel.add(fuelInput, gbc);

        // LPG Input
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lpgLabel = new JLabel("LPG Usage (cylinders):");
        lpgLabel.setForeground(Color.WHITE);
        lpgLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(lpgLabel, gbc);

        lpgInput = new JTextField();
        lpgInput.setFont(new Font("Arial", Font.PLAIN, 16));
        lpgInput.setToolTipText("Enter LPG usage in cylinders");
        gbc.gridx = 1;
        centerPanel.add(lpgInput, gbc);

        // Waste Input
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel wasteLabel = new JLabel("Waste Generation (kg):");
        wasteLabel.setForeground(Color.WHITE);
        wasteLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(wasteLabel, gbc);

        wasteInput = new JTextField();
        wasteInput.setFont(new Font("Arial", Font.PLAIN, 16));
        wasteInput.setToolTipText("Enter waste generation in kg");
        gbc.gridx = 1;
        centerPanel.add(wasteInput, gbc);

        // Members Input
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel membersLabel = new JLabel("Number of Members:");
        membersLabel.setForeground(Color.WHITE);
        membersLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(membersLabel, gbc);

        membersInput = new JTextField();
        membersInput.setFont(new Font("Arial", Font.PLAIN, 16));
        membersInput.setToolTipText("Enter number of members");
        gbc.gridx = 1;
        centerPanel.add(membersInput, gbc);

        // Add all components to the center panel
        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for action buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(0, 102, 102));
        bottomPanel.setLayout(new FlowLayout());

        // Calculate Button
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setFont(new Font("Arial", Font.PLAIN, 16));
        calculateButton.setBackground(new Color(255, 69, 0));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        calculateButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        calculateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottomPanel.add(calculateButton);

        // Result Label
        resultLabel = new JLabel("Total Emission: ");
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        bottomPanel.add(resultLabel);

        // Add bottom panel to the frame
        add(bottomPanel, BorderLayout.SOUTH);

        // Event Handling
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateEmission();
            }
        });

        // Set window location to center
        setLocationRelativeTo(null);
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

            resultLabel.setText(String.format("Total Emission: %.2f kg CO2 per member.", totalEmission));

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CorpCarbonCalculator().setVisible(true));
    }
}
