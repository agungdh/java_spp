/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test.Forms;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DBException;
import org.javalite.activejdbc.LazyList;
import test.test.Helpers.ADHhelper;
import test.test.Models.JabatanModel;

import test.test.Models.GajiModel;
import test.test.Models.KaryawanModel;
import test.test.Models.KelasModel;
import test.test.Models.PembiayaanModel;
import test.test.Models.SiswaModel;
import test.test.Reports.Config;

/**
 *
 * @author user
 */
public class Kelas extends javax.swing.JFrame {
    private List<Integer> comboSiswaID = new ArrayList<Integer>();
    private int comboSiswaIndex;
    private int selectedComboSiswaIndex;
    
    private DefaultTableModel model = new DefaultTableModel();
    private String ID;
    private String state;
    /**
     * Creates new form PangkatGol
     */
    public Kelas() {
        initComponents();
                
        loadTable();
                
        TextCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                cari();
            }

            public void removeUpdate(DocumentEvent e) {
                cari();
            }

            public void changedUpdate(DocumentEvent e) {
                cari();
            }
        });
        
        loadComboBox();
        
        selectKode();
        Siswa.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                selectKode();
            }
        });
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    public void selectKode() {
        comboSiswaIndex = Siswa.getSelectedIndex();
        if (comboSiswaIndex >= 0) {
            selectedComboSiswaIndex = comboSiswaID.get(comboSiswaIndex);

            Base.open();
            SiswaModel p = SiswaModel.findById(selectedComboSiswaIndex);
            Base.close();

            Nama.setText(p.getString("nama"));
            Nis.setText(p.getString("nis"));
            Kelamin.setText(p.getString("jenis_kelamin"));
        }
    }
    
    public void loadComboBox() {
        Siswa.removeAllItems();
        Base.open();
        LazyList<SiswaModel> siswas = SiswaModel.findAll();
        
        for(SiswaModel siswa : siswas) {
            comboSiswaID.add(Integer.parseInt(siswa.getString("id")));
            Siswa.addItem(siswa.getString("nama"));
        }

        Base.close();
    }
    
    public void cari() {
        if (TextCari.getText().equals("")) {
            loadTable();
        } else {
            loadTable(TextCari.getText());
        }
    }
    
    private void loadTableHelper(LazyList<KelasModel> kelass) {
        model = new DefaultTableModel();
                
        model.addColumn("#ID");
        model.addColumn("NIS Siswa");
        model.addColumn("Nama Siswa");
        model.addColumn("Nama Kelas");
        model.addColumn("Tahun Pelajaran");
        model.addColumn("Uang SPP");
        model.addColumn("Uang Operasional");
        model.addColumn("Uang Beras");
        model.addColumn("Daftar Ulang");
        
        Base.open();
        
        try {
            for(KelasModel kelas : kelass) {
                SiswaModel siswa = kelas.parent(SiswaModel.class);                
                model.addRow(new Object[]{
                    kelas.getId(),
                    siswa.getString("nis"),
                    siswa.getString("nama"),
                    kelas.getString("nama"),
                    kelas.getString("tahun_pelajaran"),
                    ADHhelper.rupiah(kelas.getInteger("spp")),
                    ADHhelper.rupiah(kelas.getInteger("operasional")),
                    ADHhelper.rupiah(kelas.getInteger("beras")),
                    ADHhelper.rupiah(kelas.getInteger("daftar_ulang"))
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
        
        TablePegawai.setModel(model);
        
        setState("index");
    }
    
    private void formatTextInteger() {
       
    }
    
    private void loadTable() {
        Base.open();
        LazyList<KelasModel> kelass = KelasModel.findAll();
        Base.close();
        
        loadTableHelper(kelass);
    }

    private void loadTable(String cari) {
        Base.open();
        LazyList<KelasModel> kelass = KelasModel.findBySQL("SELECT k.* FROM kelas k, siswa s WHERE k.id_siswa = s.id AND (s.nis like ? OR s.nama like ?)", '%' + cari + '%', '%' + cari + '%');
        Base.close();
        
        loadTableHelper(kelass);
    }

    
    private void hapusData() {
        Base.open();
        KelasModel kelas = KelasModel.findById(ID);
        try {
            kelas.delete();
        } catch (DBException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        Base.close();
    }
    
    private void setState(String IndexOrEdit) {
        if (IndexOrEdit.equals("index")) {
            ButtonTambahUbah.setText("Tambah");
            ButtonResetHapus.setText("Reset");
            
            state = IndexOrEdit;
        } else if (IndexOrEdit.equals("edit")) {
            ButtonTambahUbah.setText("Ubah");
            ButtonResetHapus.setText("Hapus");
            
            state = IndexOrEdit;
        } else {
            JOptionPane.showMessageDialog(null, "Index/Edit ?");
        }
    }
    
    private void tambahData() {
        Base.open();
        try {
            KelasModel kelas = new KelasModel();
            kelas.set("id_siswa", selectedComboSiswaIndex);
            kelas.set("nama", Kelas.getText());
            kelas.set("tahun_pelajaran", Tahun.getText());
            kelas.set("spp", Spp.getValue());
            kelas.set("operasional", Operasional.getValue());
            kelas.set("beras", Beras.getValue());
            kelas.set("daftar_ulang", Daftar.getValue());
            kelas.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }
    
    private void ubahData() {
        Base.open();
        try {
            KelasModel kelas = KelasModel.findById(ID);
            kelas.set("id_siswa", selectedComboSiswaIndex);
            kelas.set("nama", Kelas.getText());
            kelas.set("tahun_pelajaran", Tahun.getText());
            kelas.set("spp", Spp.getValue());
            kelas.set("operasional", Operasional.getValue());
            kelas.set("beras", Beras.getValue());
            kelas.set("daftar_ulang", Daftar.getValue());
            kelas.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }

    private void resetForm() {
        Siswa.setSelectedIndex(0);
        Nama.setText("");
        Nis.setText("");
        Kelamin.setText("");
        Kelas.setText("");
        Tahun.setText("");
        Spp.setValue(0);
        Operasional.setValue(0);
        Beras.setValue(0);
        Daftar.setValue(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ScrollPane = new javax.swing.JScrollPane();
        TablePegawai = new javax.swing.JTable();
        TextCari = new javax.swing.JTextField();
        LabelCari = new javax.swing.JLabel();
        Nama = new javax.swing.JTextField();
        LabelCari1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ButtonRefresh = new javax.swing.JButton();
        ButtonTambahUbah = new javax.swing.JButton();
        ButtonResetHapus = new javax.swing.JButton();
        LabelCari2 = new javax.swing.JLabel();
        Nis = new javax.swing.JTextField();
        LabelCari3 = new javax.swing.JLabel();
        Kelamin = new javax.swing.JTextField();
        LabelCari4 = new javax.swing.JLabel();
        Siswa = new javax.swing.JComboBox<>();
        LabelCari5 = new javax.swing.JLabel();
        LabelCari6 = new javax.swing.JLabel();
        LabelCari7 = new javax.swing.JLabel();
        Kelas = new javax.swing.JTextField();
        Tahun = new javax.swing.JTextField();
        Spp = new javax.swing.JSpinner();
        LabelCari8 = new javax.swing.JLabel();
        LabelCari9 = new javax.swing.JLabel();
        LabelCari10 = new javax.swing.JLabel();
        Operasional = new javax.swing.JSpinner();
        Daftar = new javax.swing.JSpinner();
        Beras = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Kelas");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        TablePegawai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TablePegawai.getTableHeader().setReorderingAllowed(false);
        TablePegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablePegawaiMouseClicked(evt);
            }
        });
        ScrollPane.setViewportView(TablePegawai);

        TextCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextCariActionPerformed(evt);
            }
        });

        LabelCari.setText("Cari (NIS/Nama)");

        Nama.setEditable(false);
        Nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NamaActionPerformed(evt);
            }
        });

        LabelCari1.setText("Nama");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("KELAS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(415, 415, 415))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        ButtonRefresh.setText("Refresh");
        ButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonRefreshActionPerformed(evt);
            }
        });

        ButtonTambahUbah.setText("Tambah");
        ButtonTambahUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonTambahUbahActionPerformed(evt);
            }
        });

        ButtonResetHapus.setText("Reset");
        ButtonResetHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonResetHapusActionPerformed(evt);
            }
        });

        LabelCari2.setText("NIS");

        Nis.setEditable(false);
        Nis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NisActionPerformed(evt);
            }
        });

        LabelCari3.setText("Jenis Kelamin");

        Kelamin.setEditable(false);
        Kelamin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KelaminActionPerformed(evt);
            }
        });

        LabelCari4.setText("Siswa");

        Siswa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Siswa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SiswaItemStateChanged(evt);
            }
        });
        Siswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SiswaActionPerformed(evt);
            }
        });

        LabelCari5.setText("Nama Kelas");

        LabelCari6.setText("Tahun Pelajaran");

        LabelCari7.setText("Uang SPP");

        LabelCari8.setText("Uang Operasional");

        LabelCari9.setText("Uang Beras");

        LabelCari10.setText("Daftar Ulang");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LabelCari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TextCari))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(LabelCari1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(Nama))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(LabelCari4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(Siswa, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelCari2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LabelCari3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(Nis, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                    .addComponent(Kelamin))))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LabelCari6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelCari7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelCari5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Kelas)
                            .addComponent(Tahun)
                            .addComponent(Spp, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 25, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LabelCari9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelCari8, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                            .addComponent(LabelCari10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Operasional, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Beras, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Daftar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ButtonTambahUbah)
                        .addGap(44, 44, 44)
                        .addComponent(ButtonRefresh)
                        .addGap(51, 51, 51)
                        .addComponent(ButtonResetHapus)
                        .addGap(158, 158, 158)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(LabelCari4)
                                .addComponent(Siswa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(LabelCari1)
                                .addComponent(Nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(LabelCari2)
                                .addComponent(Nis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(LabelCari8)
                                .addComponent(Operasional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(LabelCari9)
                                .addComponent(Beras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(LabelCari10)
                                .addComponent(Daftar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari5)
                            .addComponent(Kelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari6)
                            .addComponent(Tahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari7)
                            .addComponent(Spp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCari3)
                    .addComponent(Kelamin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonTambahUbah)
                    .addComponent(ButtonRefresh)
                    .addComponent(ButtonResetHapus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelCari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonRefreshActionPerformed
        resetForm();
        loadTable();
        TextCari.setText("");
    }//GEN-LAST:event_ButtonRefreshActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        new MenuUtama().setVisible(true);
        
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void TablePegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablePegawaiMouseClicked
        int i =TablePegawai.getSelectedRow();
        if(i>=0){
            ID = model.getValueAt(i, 0).toString();

            Base.open();
            KelasModel kelas = KelasModel.findById(ID);
            Base.close();

            Kelas.setText(kelas.getString("nama"));
            Tahun.setText(kelas.getString("tahun_pelajaran"));
            Spp.setValue(kelas.getInteger("spp"));
            Operasional.setValue(kelas.getInteger("operasional"));
            Beras.setValue(kelas.getInteger("beras"));
            Daftar.setValue(kelas.getInteger("daftar_ulang"));
            
            setState("edit");
        }
    }//GEN-LAST:event_TablePegawaiMouseClicked

    private void ButtonTambahUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonTambahUbahActionPerformed
        if (state.equals("index")) {
            if (Kelas.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form Kelas Masih Kosong !!!");
            } else if (Tahun.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form Tahun Masih Kosong !!!");
            } else {
                tambahData();
                resetForm();
                loadTable();
            }
        } else {
            if (Kelas.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form Kelas Masih Kosong !!!");
            } else if (Tahun.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form Tahun Masih Kosong !!!");
            } else {
                ubahData();
                resetForm();
                loadTable();
            }
        }
    }//GEN-LAST:event_ButtonTambahUbahActionPerformed

    private void ButtonResetHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonResetHapusActionPerformed
        if (state.equals("edit")) {
            hapusData();
            loadTable();
        }

        resetForm();
    }//GEN-LAST:event_ButtonResetHapusActionPerformed

    private void TextCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextCariActionPerformed
        cari();
    }//GEN-LAST:event_TextCariActionPerformed

    private void NamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NamaActionPerformed

    private void NisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NisActionPerformed

    private void KelaminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KelaminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KelaminActionPerformed

    private void SiswaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SiswaItemStateChanged

    }//GEN-LAST:event_SiswaItemStateChanged

    private void SiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SiswaActionPerformed
        comboSiswaIndex = Siswa.getSelectedIndex();
        if (comboSiswaIndex >= 0) {
            selectedComboSiswaIndex = comboSiswaID.get(comboSiswaIndex);
        }
    }//GEN-LAST:event_SiswaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Kelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Kelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Kelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Kelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Kelas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner Beras;
    private javax.swing.JButton ButtonRefresh;
    private javax.swing.JButton ButtonResetHapus;
    private javax.swing.JButton ButtonTambahUbah;
    private javax.swing.JSpinner Daftar;
    private javax.swing.JTextField Kelamin;
    private javax.swing.JTextField Kelas;
    private javax.swing.JLabel LabelCari;
    private javax.swing.JLabel LabelCari1;
    private javax.swing.JLabel LabelCari10;
    private javax.swing.JLabel LabelCari2;
    private javax.swing.JLabel LabelCari3;
    private javax.swing.JLabel LabelCari4;
    private javax.swing.JLabel LabelCari5;
    private javax.swing.JLabel LabelCari6;
    private javax.swing.JLabel LabelCari7;
    private javax.swing.JLabel LabelCari8;
    private javax.swing.JLabel LabelCari9;
    private javax.swing.JTextField Nama;
    private javax.swing.JTextField Nis;
    private javax.swing.JSpinner Operasional;
    private javax.swing.JScrollPane ScrollPane;
    private javax.swing.JComboBox<String> Siswa;
    private javax.swing.JSpinner Spp;
    private javax.swing.JTable TablePegawai;
    private javax.swing.JTextField Tahun;
    private javax.swing.JTextField TextCari;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
