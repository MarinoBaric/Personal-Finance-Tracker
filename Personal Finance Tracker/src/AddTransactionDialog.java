import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddTransactionDialog extends JDialog {
    private final DatabaseHelper dbHelper;
    private JComboBox<String> typeComboBox;
    private JTextField dateField;
    private JTextField descriptionField;
    private JComboBox<String> categoryComboBox;
    private JTextField amountField;

    public AddTransactionDialog(JFrame parent, DatabaseHelper dbHelper) {
        super(parent, "Add New Transaction", true);
        this.dbHelper = dbHelper;
        initializeUI();
    }

    private void initializeUI() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form components
        typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"});
        dateField = new JTextField();
        descriptionField = new JTextField();
        categoryComboBox = new JComboBox<>(new String[]{"Food", "Housing", "Transport", "Salary", "Utilities", "Other"});
        amountField = new JTextField();
        JButton saveButton = new JButton("Save");

        // Add components
        panel.add(new JLabel("Type:"));
        panel.add(typeComboBox);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryComboBox);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel());
        panel.add(saveButton);

        add(panel);

        // Save action
        saveButton.addActionListener(this::saveTransaction);
    }

    private void saveTransaction(ActionEvent e) {
        try {
            String type = typeComboBox.getSelectedItem().toString();
            String date = dateField.getText();
            String description = descriptionField.getText();
            String category = categoryComboBox.getSelectedItem().toString();
            double amount = Double.parseDouble(amountField.getText());

            if (date.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            Transaction transaction = new Transaction(
                0, date, description, category, amount, type
            );
            dbHelper.addTransaction(transaction);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format");
        }
    }
}