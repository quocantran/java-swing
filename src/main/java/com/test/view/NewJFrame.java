package com.test.view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.test.dbs.Jdbc;
import com.test.model.SinhVien;

public class NewJFrame extends javax.swing.JFrame {
        private static final Connection connection = Jdbc.getInstance().getConnection();
        private static ArrayList<SinhVien> list = new ArrayList<>();

        /**
         * Creates new form NewJFrame
         */
        public NewJFrame() {
                initComponents();
                loadData();
                view.addActionListener(e -> {
                        if (jTable1.getRowCount() > 0) {
                                return;
                        }

                        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

                        for (SinhVien sv : list) {
                                model.addRow(sv.toObject());
                        }
                });

                add.addActionListener(e -> {
                        String id = idInp.getText().trim();

                        String name = nameInp.getText().trim();
                        String className = classInp.getText().trim();

                        if (id.equals("") || name.equals("") || className.equals("")) {
                                JOptionPane.showMessageDialog(null, "Không được để trống thông tin");
                                return;
                        }

                        if (checkExsit(id)) {
                                JOptionPane.showMessageDialog(null, "Mã sinh viên đã tồn tại");
                                return;
                        }

                        try {
                                float gpa = Float.parseFloat(gpaInp.getText());
                        } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "GPA phải là số thực");
                                return;

                        }
                        float gpa = Float.parseFloat(gpaInp.getText());

                        try {
                                Statement statement = connection.createStatement();
                                String sql = "INSERT INTO sinhvien VALUES ('" + id + "', '" + name + "', '" + className
                                                + "', " + gpa + ")";
                                statement.executeUpdate(sql);

                        } catch (SQLException ex) {
                                ex.printStackTrace();
                        }

                        SinhVien sv = new SinhVien();
                        sv.setId(id);
                        sv.setName(name);
                        sv.setClassName(className);
                        sv.setGpa(gpa);

                        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                        list.add(sv);
                        model.addRow(sv.toObject());
                        JOptionPane.showMessageDialog(null, "Thêm sinh viên thành công");
                });

                jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                int row = jTable1.getSelectedRow();
                                idInp.setText(jTable1.getValueAt(row, 0).toString());
                                nameInp.setText(jTable1.getValueAt(row, 1).toString());
                                classInp.setText(jTable1.getValueAt(row, 2).toString());
                                gpaInp.setText(jTable1.getValueAt(row, 3).toString());

                                idInp.setEditable(false);
                        }
                });

                update.addActionListener(e -> {
                        int row = jTable1.getSelectedRow();
                        if (row == -1) {
                                JOptionPane.showMessageDialog(null, "Chọn sinh viên cần cập nhật");
                                return;
                        }

                        String id = jTable1.getValueAt(row, 0).toString();

                        String name = nameInp.getText().trim();
                        String className = classInp.getText().trim();
                        try {
                                float gpa = Float.parseFloat(gpaInp.getText());
                        } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "GPA phải là số thực");
                                return;
                        }

                        float gpa = Float.parseFloat(gpaInp.getText());

                        try {
                                Statement statement = connection.createStatement();
                                String sql = "UPDATE sinhvien SET name = '" + name + "', class = '" + className
                                                + "', gpa = " + gpa + " WHERE id = '" + id + "'";
                                statement.executeUpdate(sql);

                        } catch (SQLException ex) {
                                ex.printStackTrace();
                        }

                        SinhVien sv = list.get(row);
                        sv.setName(name);
                        sv.setClassName(className);
                        sv.setGpa(gpa);

                        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                        model.setValueAt(name, row, 1);
                        model.setValueAt(className, row, 2);
                        model.setValueAt(gpa, row, 3);

                        JOptionPane.showMessageDialog(null, "Cập nhật sinh viên thành công");
                });

                jButton1.addActionListener(e -> {
                        int row = jTable1.getSelectedRow();
                        if (row == -1) {
                                JOptionPane.showMessageDialog(null, "Chọn sinh viên cần xóa");
                                return;
                        }
                        String id = jTable1.getValueAt(row, 0).toString();

                        try {
                                Statement statement = connection.createStatement();
                                String sql = "DELETE FROM sinhvien WHERE id = '" + id + "'";
                                statement.executeUpdate(sql);

                        } catch (SQLException ex) {
                                ex.printStackTrace();
                        }

                        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                        model.removeRow(row);
                        list.remove(row);

                        idInp.setText("");
                        nameInp.setText("");
                        classInp.setText("");
                        gpaInp.setText("");
                        idInp.setEditable(true);

                        JOptionPane.showMessageDialog(null, "Xóa sinh viên thành công");
                });

                reset.addActionListener(e -> {
                        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                        model.setRowCount(0);
                        idInp.setText("");
                        nameInp.setText("");
                        classInp.setText("");
                        gpaInp.setText("");
                        idInp.setEditable(true);
                });
        }

        private boolean checkExsit(String id) {
                for (SinhVien sv : list) {
                        if (sv.getId().equals(id)) {
                                return true;
                        }
                }
                return false;
        }

        private void loadData() {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

                try {
                        Statement statement = connection.createStatement();
                        String sql = "SELECT * FROM sinhvien";
                        ResultSet res = statement.executeQuery(sql);

                        while (res.next()) {
                                String id = res.getString("id");
                                String name = res.getString("name");
                                String className = res.getString("class");
                                float gpa = res.getFloat("gpa");

                                SinhVien sv = new SinhVien();
                                sv.setId(id);
                                sv.setName(name);
                                sv.setClassName(className);
                                sv.setGpa(gpa);

                                list.add(sv);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                reset = new javax.swing.JButton();
                view = new javax.swing.JButton();
                update = new javax.swing.JButton();
                add = new javax.swing.JButton();
                jButton1 = new javax.swing.JButton();
                jLabel1 = new javax.swing.JLabel();
                idInp = new javax.swing.JTextField();
                jLabel2 = new javax.swing.JLabel();
                nameInp = new javax.swing.JTextField();
                jLabel3 = new javax.swing.JLabel();
                classInp = new javax.swing.JTextField();
                jLabel4 = new javax.swing.JLabel();
                gpaInp = new javax.swing.JTextField();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                jTable1.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {

                                },
                                new String[] {
                                                "Mã Sinh Viên", "Họ Tên", "Lớp", "GPA"
                                }));
                jScrollPane1.setViewportView(jTable1);

                reset.setText("Làm mới");

                view.setText("Hiển thị");
                view.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                viewActionPerformed(evt);
                        }
                });

                update.setText("Cập nhật");

                add.setText("Thêm mới");

                jButton1.setText("Xóa");

                jLabel1.setText("Mã Sinh Viên");

                jLabel2.setText("Họ tên");

                jLabel3.setText("Lớp");

                classInp.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                classInpActionPerformed(evt);
                        }
                });

                jLabel4.setText("Gpa");

                gpaInp.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                gpaInpActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(36, 36, 36)
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(jLabel4)
                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                .createParallelGroup(
                                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                                                false)
                                                                                                                                .addComponent(gpaInp,
                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                .createSequentialGroup()
                                                                                                                                                .addComponent(update)
                                                                                                                                                .addPreferredGap(
                                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                .addComponent(add)))
                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                .createParallelGroup(
                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                                false)
                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                .createSequentialGroup()
                                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                                .createParallelGroup(
                                                                                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                                                                                .addComponent(jLabel1,
                                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                                .addComponent(jLabel2,
                                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                                .addComponent(jLabel3,
                                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING))
                                                                                                                                                                .addGap(166, 166,
                                                                                                                                                                                166))
                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                .createSequentialGroup()
                                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                                .createParallelGroup(
                                                                                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                                                                                .addComponent(idInp,
                                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                                .addComponent(nameInp,
                                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                                .addComponent(classInp))
                                                                                                                                                                .addGap(66, 66, 66)))
                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                .createParallelGroup(
                                                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                                                .addComponent(jScrollPane1,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                .createSequentialGroup()
                                                                                                                                                                .addComponent(view)
                                                                                                                                                                .addPreferredGap(
                                                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                                                .addComponent(reset))))))
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(78, 78, 78)
                                                                                                .addComponent(jButton1,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                71,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(40, 40, 40)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel1)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(reset)
                                                                                                .addComponent(view)))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(idInp,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel2)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(nameInp,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(jLabel3)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(classInp,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(jScrollPane1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                107,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(16, 16, 16)
                                                                .addComponent(jLabel4)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(gpaInp,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(update)
                                                                                .addComponent(add))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jButton1)
                                                                .addContainerGap(34, Short.MAX_VALUE)));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addContainerGap()));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 138, Short.MAX_VALUE)));

                pack();
        }// </editor-fold>

        private void viewActionPerformed(java.awt.event.ActionEvent evt) {
                // TODO add your handling code here:
        }

        private void classInpActionPerformed(java.awt.event.ActionEvent evt) {
                // TODO add your handling code here:
        }

        private void gpaInpActionPerformed(java.awt.event.ActionEvent evt) {
                // TODO add your handling code here:
        }

        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
                /* Set the Nimbus look and feel */
                // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
                // (optional) ">
                /*
                 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
                 * look and feel.
                 * For details see
                 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
                 */
                try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                                        .getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (ClassNotFoundException ex) {
                        java.util.logging.Logger.getLogger(NewJFrame.class.getName())
                                        .log(java.util.logging.Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                        java.util.logging.Logger.getLogger(NewJFrame.class.getName())
                                        .log(java.util.logging.Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                        java.util.logging.Logger.getLogger(NewJFrame.class.getName())
                                        .log(java.util.logging.Level.SEVERE, null, ex);
                } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(NewJFrame.class.getName())
                                        .log(java.util.logging.Level.SEVERE, null, ex);
                }
                // </editor-fold>

                /* Create and display the form */
                java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                new NewJFrame().setVisible(true);

                        }
                });
        }

        // Variables declaration - do not modify
        private javax.swing.JButton add;
        private javax.swing.JTextField classInp;
        private javax.swing.JTextField gpaInp;
        private javax.swing.JTextField idInp;
        private javax.swing.JButton jButton1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTable jTable1;
        private javax.swing.JTextField nameInp;
        private javax.swing.JButton reset;
        private javax.swing.JButton update;
        private javax.swing.JButton view;
        // End of variables declaration
}
