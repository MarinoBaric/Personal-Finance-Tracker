import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainFrame extends JFrame {
    private final DatabaseHelper dbHelper;
    private DefaultTableModel tableModel;
    private JTable transactionTable;
    private JLabel balanceLabel;
    private JLabel incomeLabel;
    private JLabel expenseLabel;

    public MainFrame() {
        dbHelper = new DatabaseHelper();
        initializeUI();
        loadTransactions();
        updateSummary();
    }

    private void initializeUI() {
        setTitle("Personal Finance Tracker");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create components
        JToolBar toolBar = new JToolBar();
        JButton addButton = new JButton("Add Transaction");
        JButton deleteButton = new JButton("Delete Transaction");
        
        // Configure table
        String[] columns = {"ID", "Date", "Description", "Category", "Amount", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(tableModel);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.getColumnModel().getColumn(0).setMinWidth(0);
        transactionTable.getColumnModel().getColumn(0).setMaxWidth(0);

        // Summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3));
        balanceLabel = new JLabel("Balance: $0.00", SwingConstants.CENTER);
        incomeLabel = new JLabel("Income: $0.00", SwingConstants.CENTER);
        expenseLabel = new JLabel("Expenses: $0.00", SwingConstants.CENTER);
        
        // Styling
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        balanceLabel.setFont(labelFont);
        incomeLabel.setFont(labelFont);
        expenseLabel.setFont(labelFont);
        
        Color incomeColor = new Color(0, 153, 0);
        Color expenseColor = new Color(204, 0, 0);
        incomeLabel.setForeground(incomeColor);
        expenseLabel.setForeground(expenseColor);

        summaryPanel.add(incomeLabel);
        summaryPanel.add(expenseLabel);
        summaryPanel.add(balanceLabel);

        // Add components
        toolBar.add(addButton);
        toolBar.add(deleteButton);
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(transactionTable), BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(this::addTransaction);
        deleteButton.addActionListener(this::deleteTransaction);
    }

    private void addTransaction(ActionEvent e) {
        AddTransactionDialog dialog = new AddTransactionDialog(this, dbHelper);
        dialog.setVisible(true);
        loadTransactions();
        updateSummary();
    }

    private void deleteTransaction(ActionEvent e) {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            dbHelper.deleteTransaction(id);
            loadTransactions();
            updateSummary();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a transaction to delete");
        }
    }

    private void loadTransactions() {
        tableModel.setRowCount(0);
        List<Transaction> transactions = dbHelper.getAllTransactions();
        for (Transaction t : transactions) {
            tableModel.addRow(new Object[]{
                t.getId(),
                t.getDate(),
                t.getDescription(),
                t.getCategory(),
                String.format("$%.2f", t.getAmount()),
                t.getType()
            });
        }
    }

    private void updateSummary() {
        double income = 0;
        double expenses = 0;
        
        for (Transaction t : dbHelper.getAllTransactions()) {
            if (t.getType().equalsIgnoreCase("income")) {
                income += t.getAmount();
            } else {
                expenses += t.getAmount();
            }
        }
        
        double balance = income - expenses;
        incomeLabel.setText(String.format("Income: $%.2f", income));
        expenseLabel.setText(String.format("Expenses: $%.2f", expenses));
        balanceLabel.setText(String.format("Balance: $%.2f", balance));
        
        // Set balance color
        if (balance >= 0) {
            balanceLabel.setForeground(new Color(0, 153, 0));
        } else {
            balanceLabel.setForeground(new Color(204, 0, 0));
        }
    }
}