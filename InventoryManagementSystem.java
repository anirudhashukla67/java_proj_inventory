// ==============================
// FILE: InventoryManagementSystem.java
// ==============================

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InventoryManagementSystem extends JFrame {

    JTextField txtName, txtQuantity, txtPrice, txtCategory;
    JTable table;
    DefaultTableModel model;

    public InventoryManagementSystem() {

        setTitle("Inventory Management System");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---------------- PANEL ----------------

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Product Name"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Quantity"));
        txtQuantity = new JTextField();
        panel.add(txtQuantity);

        panel.add(new JLabel("Price"));
        txtPrice = new JTextField();
        panel.add(txtPrice);

        panel.add(new JLabel("Category"));
        txtCategory = new JTextField();
        panel.add(txtCategory);

        JButton btnAdd = new JButton("Add Product");
        JButton btnUpdate = new JButton("Update Product");

        panel.add(btnAdd);
        panel.add(btnUpdate);

        add(panel, BorderLayout.NORTH);

        // ---------------- TABLE ----------------

        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Quantity");
        model.addColumn("Price");
        model.addColumn("Category");

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // ---------------- BUTTON PANEL ----------------

        JPanel bottomPanel = new JPanel();

        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        bottomPanel.add(btnDelete);
        bottomPanel.add(btnClear);

        add(bottomPanel, BorderLayout.SOUTH);

        // ---------------- LOAD DATA ----------------

        loadTableData();

        // ---------------- TABLE CLICK ----------------

        table.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = table.getSelectedRow();

                txtName.setText(model.getValueAt(row, 1).toString());
                txtQuantity.setText(model.getValueAt(row, 2).toString());
                txtPrice.setText(model.getValueAt(row, 3).toString());
                txtCategory.setText(model.getValueAt(row, 4).toString());
            }
        });

        // ---------------- ADD BUTTON ----------------

        btnAdd.addActionListener(e -> addProduct());

        // ---------------- UPDATE BUTTON ----------------

        btnUpdate.addActionListener(e -> updateProduct());

        // ---------------- DELETE BUTTON ----------------

        btnDelete.addActionListener(e -> deleteProduct());

        // ---------------- CLEAR BUTTON ----------------

        btnClear.addActionListener(e -> clearFields());
    }

    // =========================================
    // ADD PRODUCT
    // =========================================

    public void addProduct() {

        try {

            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO products(product_name, quantity, price, category) VALUES(?,?,?,?)";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, txtName.getText());
            pst.setInt(2, Integer.parseInt(txtQuantity.getText()));
            pst.setDouble(3, Double.parseDouble(txtPrice.getText()));
            pst.setString(4, txtCategory.getText());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Product Added");

            loadTableData();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================
    // LOAD TABLE DATA
    // =========================================

    public void loadTableData() {

        model.setRowCount(0);

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM products";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("category")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================
    // UPDATE PRODUCT
    // =========================================

    public void updateProduct() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product first");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        try {

            Connection con = DBConnection.getConnection();

            String query = "UPDATE products SET product_name=?, quantity=?, price=?, category=? WHERE product_id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, txtName.getText());
            pst.setInt(2, Integer.parseInt(txtQuantity.getText()));
            pst.setDouble(3, Double.parseDouble(txtPrice.getText()));
            pst.setString(4, txtCategory.getText());
            pst.setInt(5, id);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Product Updated");

            loadTableData();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================
    // DELETE PRODUCT
    // =========================================

    public void deleteProduct() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product first");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        try {

            Connection con = DBConnection.getConnection();

            String query = "DELETE FROM products WHERE product_id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, id);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Product Deleted");

            loadTableData();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================
    // CLEAR FIELDS
    // =========================================

    public void clearFields() {

        txtName.setText("");
        txtQuantity.setText("");
        txtPrice.setText("");
        txtCategory.setText("");
    }

    // =========================================
    // MAIN METHOD
    // =========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new InventoryManagementSystem().setVisible(true);
        });
    }
}
