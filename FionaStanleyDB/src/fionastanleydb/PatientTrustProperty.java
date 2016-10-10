/*
 *Student Name: Matthew Pearce
 *Student ID: 131600732
 *Project: Fiona Stanley Lost and Found Database
 */
package fionastanleydb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
 import java.util.*;
import java.lang.String;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.AbstractTableModel;


/**
 *
 * @author 131600732
 */
public class PatientTrustProperty extends javax.swing.JFrame{

    int NOK = 2;
    String selectedData;
    String url = "jdbc:mysql://localhost:3306/lostandfound"; 
    String user = "root";
    String password = "";
    
    /**
     * Creates new form PatientTrustProperty and populates the tables
     */
    public PatientTrustProperty() {
        initComponents();
        setTimeDate();
        populateTable();
        populateArchive();
        SelectItem();     
    }
    
    public Connection getConnection() throws SQLException{
    
                Connection con = DriverManager.getConnection(url, user, password);
                //Statement stmt = con.createStatement();
                return con;
    
    }
    /**
     * Displays all the items that have been returned
     */
    public void populateArchive(){
        try{
         //Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = getConnection().createStatement();
                    
            String ownIDQuery= "SELECT owner.FirstName, owner.LastName, property.CollectionDate, property.WorkOrderNo, property.description, t1.teamname, property.loggedby, t2.teamname, property.PropertyReleasedTo, property.DateReleased \n" +
                                "FROM property\n" +
                                "INNER JOIN owner\n" +
                                "ON owner.ownerID=Property.ownerID_FK\n" +
                                "LEFT OUTER JOIN staff t1\n" +
                                "on t1.staffID=property.propertyReceivedby_FK\n" +
                                "LEFT OUTER JOIN  staff t2\n" +
                                "on t2.staffID=property.propertyReleasedBy\n" +
                                "WHERE DateReleased IS NOT NULL;";
                    
            ResultSet result = stmt.executeQuery(ownIDQuery);
                    
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);
            
            while(result.next()){
                      
                String fName = result.getString("FirstName");
                String lName = result.getString("LastName");
                Date cDate = result.getDate("CollectionDate");
                String wrkOrder = result.getString("WorkOrderNo");
                String descript = result.getString("description");
                String teamName = result.getString("t1.teamname");
                String loggedBy = result.getString("loggedby");
                String teamName2 = result.getString("t2.teamname");
                String propReleaseTo = result.getString("PropertyReleasedTo");
                Date rDate = result.getDate("DateReleased");
                
                model.addRow(new String[]{fName,lName,cDate.toString(),wrkOrder,descript,teamName,loggedBy,teamName2,propReleaseTo,rDate.toString()});                
            }
            getConnection().close();              
        }
        catch(Exception ex){
        System.out.println(ex.toString());
        }
    }
    
    /**
     * Displays all items that are currently in storage
     */
    public void populateTable(){
        
       DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
  try{
       model.setRowCount(0);
  }
  catch(Exception e){
  System.out.println("This setRow problem is " + e);
  }
        try{
            
            //Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = getConnection().createStatement();
                    
            String ownIDQuery= "SELECT owner.FirstName, owner.LastName, property.Bagnumber, property.CollectionDate, property.WorkOrderNo, property.description, staff.teamname, property.loggedby, storagelocation.storlocationName \n" +
                                "FROM property\n" +
                                "INNER JOIN owner\n" +
                                "ON owner.ownerID=Property.ownerID_FK\n" +
                                "INNER JOIN staff\n" +
                                "on staff.staffID=property.propertyReceivedby_FK\n" +
                                "inner join storagelocation \n" +
                                "on storagelocation.StorlocationID=property.StoragelocationID_fk"
                    + " WHERE DateReleased IS NULL;";
                    
                
            ResultSet result = stmt.executeQuery(ownIDQuery);
                
               
                while(result.next()){
                    
                    String fName = result.getString("FirstName");
                    String lName = result.getString("LastName");
                    String bNumber = result.getString("Bagnumber");
                    Date cDate = result.getDate("CollectionDate");
                    String wrkOrder = result.getString("WorkOrderNo");
                    String descript = result.getString("description");
                    String teamName = result.getString("teamname");
                    String loggedBy = result.getString("loggedby");
                    String storLocName = result.getString("storlocationName");
                    
                    model.addRow(new String[]{fName,lName,bNumber,cDate.toString(),wrkOrder,descript,teamName,loggedBy,storLocName});
                }                
            
            getConnection().close();              
        }
        catch(Exception ex){
        System.out.println("SQl Query beiing added"+ex.toString());
        }
    }
     
    /**
     * Clears all text boxes and resets combo boxes
     */
    public void clearBoxes(){
        tbFirstName.setText("");
        tbLastName.setText("");                   
        tbDescription.setText("");
        tbWrkOrdNo.setText("");
        tbBagNo.setText("");
        tbPatientSticker.setText("");
        cbLoggedBy.setSelectedIndex(0);
        cbStorageLoc.setSelectedIndex(0);
        cbOwner.setSelected(false);
        cbNextOfKin.setSelected(false);
        tbReturnWorkOrdNum.setText("");
        tbReturnedTo.setText("");
        tbIDNum.setText("");
    }
    
    /**
     * Displays selected item details into its corresponding text boxes 
     */
    public void DisplayInfo(){
        try{
           
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
                    
            String ownIDQuery="SELECT owner.FirstName, owner.LastName, owner.patientsticker, property.Bagnumber, property.CollectionDate, property.WorkOrderNo, property.description, staff.teamname, property.loggedby, storagelocation.storlocationName \n" +
                                "FROM property\n" +
                                "INNER JOIN owner\n" +
                                "ON owner.ownerID=Property.ownerID_FK\n" +
                                "INNER JOIN staff\n" +
                                "on staff.staffID=property.propertyReceivedby_FK\n" +
                                "inner join storagelocation \n" +
                                "on storagelocation.StorlocationID=property.StoragelocationID_fk \n" +
                                "WHERE property.BagNumber =\"" + selectedData +"\";";
                    
            ResultSet result = stmt.executeQuery(ownIDQuery);                   
                    
            result.next();
            String str = result.getString("CollectionDate");
            String[] splitted = str.split("\\s+");  
            String timeOut = splitted[1].substring(0,5);
            tbDate.setText(splitted[0]);
            tbCollectTime.setText(timeOut);
            tbFirstName.setText(result.getString("FirstName"));
            tbLastName.setText(result.getString("LastName"));                   
            tbDescription.setText(result.getString("Description"));
            tbWrkOrdNo.setText(result.getString("WorkOrderNO"));
            tbBagNo.setText(result.getString("BagNumber"));
            tbPatientSticker.setText(result.getString("PatientSticker"));
            cbLoggedBy.setSelectedItem(result.getString("teamname"));
            cbStorageLoc.setSelectedItem(result.getString("storlocationName"));
            tbReturnWorkOrdNum.setText(result.getString("WorkOrderNO"));
            result.close();
            con.close();
               
        }   
        catch(Exception ex){
            System.out.println(ex.toString());
        }
    }
    
    /**
     * Select item event listener
     */
    public void SelectItem(){
        
        ListSelectionModel cellSelectionModel = jTable1.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = jTable1.getSelectedRow();
                selectedData  = (String) jTable1.getValueAt(selectedRow, 2);
                        
                DisplayInfo();
            }
        });                 
    }
    
    /**
     * Sets the date and time on the form (Only updates when method is called)
     */
    public void setTimeDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");//HH:mm:ss
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        tbCollectTime.setText( timeFormat.format(cal.getTime()));
        tbTimeReturned.setText( timeFormat.format(cal.getTime()));
        Date date = new Date();
        tbDate.setText(dateFormat.format(date.getTime()));
        tbDateReturned.setText(dateFormat.format(date.getTime()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tbCollectTime = new javax.swing.JFormattedTextField();
        jSeparator1 = new javax.swing.JSeparator();
        tbBagNo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tbWrkOrdNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tbFirstName = new javax.swing.JTextField();
        tbPatientSticker = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDescription = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnAddProp = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        tbReturnedTo = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        tbIDNum = new javax.swing.JTextField();
        btnRemoveProp = new javax.swing.JButton();
        cbOwner = new javax.swing.JCheckBox();
        cbNextOfKin = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        tbReturnWorkOrdNum = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        tbSearch = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        tbLastName = new javax.swing.JTextField();
        tbDate = new javax.swing.JTextField();
        cbLoggedBy = new javax.swing.JComboBox();
        cbStorageLoc = new javax.swing.JComboBox();
        tbTimeReturned = new javax.swing.JTextField();
        cbReturnedBy = new javax.swing.JComboBox();
        tbDateReturned = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnClrSrch = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(567, 636));

        jLabel1.setText("Collected Date:");

        jLabel3.setText("Collected Time");

        tbCollectTime.setEditable(false);
        tbCollectTime.setText("24:00:00");

        tbBagNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbBagNoKeyTyped(evt);
            }
        });

        jLabel4.setText("Property Bag Number");

        jLabel5.setText("Work Order #:");

        tbWrkOrdNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbWrkOrdNoKeyTyped(evt);
            }
        });

        jLabel6.setText("Description:");

        jLabel7.setText("First Name:");

        jLabel8.setText("Patient Sticker:");

        tbFirstName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbFirstNameKeyTyped(evt);
            }
        });

        tbPatientSticker.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbPatientStickerKeyTyped(evt);
            }
        });

        tbDescription.setColumns(20);
        tbDescription.setRows(5);
        jScrollPane1.setViewportView(tbDescription);

        jLabel9.setText("Logged by Sims Officer:");

        jLabel10.setText("Storage Location:");

        btnAddProp.setText("Add Property");
        btnAddProp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPropActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Patient Trust Returned");

        jLabel12.setText("Property Returned To:");

        jLabel14.setText("Date Returned:");

        jLabel15.setText("Returned By:");

        jLabel17.setText("Time Returned:");

        jLabel18.setText("Returned To:");

        jLabel19.setText("I.D. Number:");

        btnRemoveProp.setText("Remove Property");
        btnRemoveProp.setName(""); // NOI18N
        btnRemoveProp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovePropActionPerformed(evt);
            }
        });

        cbOwner.setText("Owner");
        cbOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOwnerActionPerformed(evt);
            }
        });

        cbNextOfKin.setText("Next of KIN");
        cbNextOfKin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNextOfKinActionPerformed(evt);
            }
        });

        jLabel20.setText("Work Order#:");

        jLabel13.setText("Search:");

        jLabel16.setText("Last Name:");

        tbLastName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbLastNameKeyTyped(evt);
            }
        });

        tbDate.setEditable(false);

        cbLoggedBy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-Please Select One-", "Admin Team", "Gold Team", "Red Team", "Green Team", "Blue Team" }));

        cbStorageLoc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-Please Select One-", "Patient Trust-Monday", "Patient Trust-Tuesday", "Patient Trust-Wednesday", "Patient Trust-Thursday", "Patient Trust-Friday", "Patient Trust-Saturday/Sunday" }));

        tbTimeReturned.setEditable(false);

        cbReturnedBy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-Please Select One-", "Admin Team", "Gold Team", "Red Team", "Green Team", "Blue Team" }));

        tbDateReturned.setEditable(false);

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnClrSrch.setText("Clear Search");
        btnClrSrch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClrSrchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel14)
                                            .addComponent(jLabel15))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(cbReturnedBy, 0, 162, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(tbDateReturned, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(tbReturnedTo))
                                                .addGap(18, 18, 18))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addGap(191, 191, 191)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel17)
                                            .addComponent(jLabel19))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tbIDNum, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tbTimeReturned, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tbReturnWorkOrdNum, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(269, 269, 269)
                                        .addComponent(btnSearch))
                                    .addComponent(tbSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRemoveProp, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(197, 197, 197)
                                        .addComponent(jLabel11))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(18, 18, 18)
                                        .addComponent(cbOwner)
                                        .addGap(18, 18, 18)
                                        .addComponent(cbNextOfKin))
                                    .addComponent(jLabel13)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel9)
                                                .addComponent(jLabel10))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(cbStorageLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(btnAddProp, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(cbLoggedBy, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel5)
                                                .addComponent(jLabel1))
                                            .addGap(11, 11, 11)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(tbWrkOrdNo, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                                .addComponent(tbDate))
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel4)
                                                .addComponent(jLabel3))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(tbCollectTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(tbBagNo, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                                .addComponent(tbLastName)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel7)
                                                .addComponent(jLabel8)
                                                .addComponent(jLabel6))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane1)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(tbPatientSticker, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(tbFirstName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                                    .addComponent(jLabel16)
                                                    .addGap(201, 201, 201)))))
                                    .addComponent(btnClrSrch))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(31, 31, 31)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(tbCollectTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbBagNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(tbWrkOrdNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tbFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(tbLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbPatientSticker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cbLoggedBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddProp)
                    .addComponent(jLabel10)
                    .addComponent(cbStorageLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(cbOwner)
                    .addComponent(cbNextOfKin))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbReturnedTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19)
                    .addComponent(tbIDNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17)
                    .addComponent(tbTimeReturned, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbDateReturned, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel20)
                    .addComponent(tbReturnWorkOrdNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbReturnedBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRemoveProp)
                    .addComponent(tbSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClrSrch, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Patient Trust Property", jPanel1);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setText("Lost & Found Property");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "First Name", "Last Name", "Bag Number", "Collection Date", "Work Order #", "Description", "Collected By", "Logged By", "Storage Location"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable1);

        jTabbedPane2.addTab("Property", jScrollPane4);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "First Name", "Last Name", "Collection Date", "Work Order#", "Description", "Collected By", "Logged By", "Returned By", "Returned To", "Returned Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jTabbedPane2.addTab("Archive", jScrollPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 453, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
        );

        setBounds(0, 0, 1083, 959);
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Adds item details to the database
     * @param evt 
     */
    private void btnAddPropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPropActionPerformed
        
        setTimeDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
        Date date = new Date();
        String dateTimeStamp = dateFormat.format(date.getTime());
        
        try{             
                Connection con = DriverManager.getConnection(url, user, password);
                Statement stmt = con.createStatement();
                    
                String firstName = tbFirstName.getText();
                String lastName = tbLastName.getText();
                String patientSticker = tbPatientSticker.getText();
    
                String ownerQuery="INSERT INTO OWNER (FirstName, LastName, PatientSticker) VALUES (\" " + firstName + "\",\"" + lastName + "\",\""+ patientSticker + "\");";
                stmt.executeUpdate(ownerQuery);
                
                String ownIDQuery="SELECT OwnerID FROM owner WHERE PatientSticker = \"" +  patientSticker +"\";";
                ResultSet result = stmt.executeQuery(ownIDQuery);
                result.next();
                
                int name = result.getInt("OwnerId");
                
                String bagNum = tbBagNo.getText();
                String wrkOrdNum = tbWrkOrdNo.getText();
                String descript = tbDescription.getText();
                int staffID = (int)cbLoggedBy.getSelectedIndex();
                int storeLoc = (int)cbStorageLoc.getSelectedIndex() + 1;
                
                String propertyQuery="INSERT INTO Property (OwnerID_FK, CollectionDate, BagNumber, WorkOrderNO, Description, PropertyReceivedBy_FK, LoggedBy, StorageLocationID_FK) "
                           + "VALUES( "+ name+ ",'"+dateTimeStamp+"',\""+ bagNum + "\",\"" + wrkOrdNum+ "\",\"" + descript + "\"," + staffID + ",\"Patient Property\"," + storeLoc +");";
                  
                stmt.executeUpdate(propertyQuery);
                
                result.close();
                con.close();
                
                populateTable();
                clearBoxes();
                   
        }catch(SQLException ex){
            System.out.println(ex);
        }
    }//GEN-LAST:event_btnAddPropActionPerformed

    /**
     * When checked makes sure other check box is un-checked
     * @param evt 
     */
    private void cbNextOfKinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNextOfKinActionPerformed
        cbOwner.setSelected(false);
        NOK=1;
    }//GEN-LAST:event_cbNextOfKinActionPerformed
    
    /**
     * When checked makes sure other check box is un-checked and makes sure there is text in First Name text box
     * @param evt 
     */
    private void cbOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOwnerActionPerformed
        cbNextOfKin.setSelected(false);
        NOK=0;
        if(!tbFirstName.getText().isEmpty()){
            tbReturnedTo.setText(tbFirstName.getText() + " " +tbLastName.getText());
        }
        else{
            System.out.println("Pick an item");
            cbOwner.setSelected(false);
        }
    }//GEN-LAST:event_cbOwnerActionPerformed

    /**
     * Updates the property table in the database (Archives it)
     * @param evt 
     */
    private void btnRemovePropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovePropActionPerformed
        setTimeDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
        Date date = new Date();
        String dateTimeStamp = dateFormat.format(date.getTime());
        if(tbFirstName.getText().isEmpty())
        {
            System.out.println("Wroong");
        }
        else if(NOK == 2){
            System.out.println("Wroong");
        }
        else{
            try{               
                Statement stmt =getConnection().createStatement();
                String bagNumber = tbBagNo.getText();
                String ownIDQuery="SELECT PropertyID, OwnerID_FK FROM property WHERE BagNumber = \"" +  bagNumber +"\";";
                ResultSet result = stmt.executeQuery(ownIDQuery);
                result.next();
                
                int itemID = result.getInt("PropertyID");
                int ownerID = result.getInt("OwnerID_FK");
                
                String prfOfID = tbIDNum.getText();
                String ownerQuery = "UPDATE Owner SET ProofOfID = \"" + prfOfID +"\" WHERE OwnerID = "+ ownerID + ";";
                stmt.executeUpdate(ownerQuery);
                int staffID = (int)cbReturnedBy.getSelectedIndex();
                String rtrnTo = tbReturnedTo.getText();
                
                String propertyQuery="UPDATE Property SET BackToOwnerOrNOK = "+NOK+ ", PropertyReleasedBy = " +staffID+ ", PropertyReleasedTo= \"" +rtrnTo+ "\", DateReleased ='"+ dateTimeStamp+"' WHERE PropertyID ="+itemID+";";
                stmt.executeUpdate(propertyQuery);
               
                result.close();
                DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();
                //model.fireTableDataChanged();
                 
  try{
       model1.setRowCount(0);
  }
  catch(Exception e){
  
 }
               
              String ownID= "SELECT owner.FirstName, owner.LastName, property.Bagnumber, property.CollectionDate, property.WorkOrderNo, property.description, staff.teamname, property.loggedby, storagelocation.storlocationName \n" +
                                "FROM property\n" +
                                "INNER JOIN owner\n" +
                                "ON owner.ownerID=Property.ownerID_FK\n" +
                                "INNER JOIN staff\n" +
                                "on staff.staffID=property.propertyReceivedby_FK\n" +
                                "inner join storagelocation \n" +
                                "on storagelocation.StorlocationID=property.StoragelocationID_fk"
                    + " WHERE DateReleased IS NULL;";
                    
                
            ResultSet result1 = stmt.executeQuery(ownID);
                
               
                while(result1.next()){
                    
                    String fName = result1.getString("FirstName");
                    String lName = result1.getString("LastName");
                    String bNumber = result1.getString("Bagnumber");
                    Date cDate = result1.getDate("CollectionDate");
                    String wrkOrder = result1.getString("WorkOrderNo");
                    String descript = result1.getString("description");
                    String teamName = result1.getString("teamname");
                    String loggedBy = result1.getString("loggedby");
                    String storLocName = result1.getString("storlocationName");
                    
                    model1.addRow(new String[]{fName,lName,bNumber,cDate.toString(),wrkOrder,descript,teamName,loggedBy,storLocName});
                }                
                getConnection().close();                       
            }catch(SQLException ex){
            System.out.println(ex);
            }            
                populateArchive();
                clearBoxes();       
        }        
    }//GEN-LAST:event_btnRemovePropActionPerformed

    /**
     * Searches database for specified item
     * @param evt 
     */
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        try{
                Connection con = DriverManager.getConnection(url, user, password);
                Statement stmt = con.createStatement();
                //String bagNumber = tbBagNo.getText();
                String srching = tbSearch.getText();
                String ownIDQuery= "SELECT owner.FirstName, owner.LastName, property.Bagnumber, property.CollectionDate, property.WorkOrderNo, property.description, staff.teamname, property.loggedby, storagelocation.storlocationName \n" +
                                "FROM property\n" +
                                "INNER JOIN owner\n" +
                                "ON owner.ownerID=Property.ownerID_FK\n" +
                                "INNER JOIN staff\n" +
                                "on staff.staffID=property.propertyReceivedby_FK\n" +
                                "inner join storagelocation \n" +
                                "on storagelocation.StorlocationID=property.StoragelocationID_fk"
                    + " WHERE Description LIKE '%"+  srching +"%' OR owner.FirstName LIKE'%"+  srching +"%' OR owner.LastName LIKE'%"+  srching +"%' OR property.WorkOrderNo LIKE'%"+  srching +"%' ;";
                ResultSet result = stmt.executeQuery(ownIDQuery);
                    
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);
            while(result.next()){
                      
                String fName = result.getString("FirstName");
                String lName = result.getString("LastName");
                String bNumber = result.getString("Bagnumber");
                Date cDate = result.getDate("CollectionDate");
                String wrkOrder = result.getString("WorkOrderNo");
                String descript = result.getString("description");
                String teamName = result.getString("teamname");
                String loggedBy = result.getString("loggedby");
                String storLocName = result.getString("storlocationName");
                
                model.addRow(new String[]{fName,lName,bNumber,cDate.toString(),wrkOrder,descript,teamName,loggedBy,storLocName});                
            }
            con.close();              
                
            }catch(SQLException ex){
            System.out.println(ex);
            }   
    }//GEN-LAST:event_btnSearchActionPerformed

    /**
     * Clears the search results from the table
     * @param evt 
     */
    private void btnClrSrchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClrSrchActionPerformed
        populateTable();
    }//GEN-LAST:event_btnClrSrchActionPerformed

    /**
     * Prevents the user from add to many characters to the text box 
     * @param evt 
     */
    private void tbPatientStickerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPatientStickerKeyTyped
        if(tbPatientSticker.getText().length()>=11) {  
        evt.consume();
        }
    }//GEN-LAST:event_tbPatientStickerKeyTyped

    /**
     * Prevents the user from add to many characters to the text box 
     * @param evt 
     */
    private void tbBagNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbBagNoKeyTyped
        if(tbBagNo.getText().length()>=8) {  
        evt.consume();
        }
    }//GEN-LAST:event_tbBagNoKeyTyped

    /**
     * Prevents the user from add to many characters to the text box 
     * @param evt 
     */
    private void tbWrkOrdNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbWrkOrdNoKeyTyped
        if(tbWrkOrdNo.getText().length()>=15) {  
        evt.consume();
        }
    }//GEN-LAST:event_tbWrkOrdNoKeyTyped

    /**
     * Prevents the user from add to many characters to the text box 
     * @param evt 
     */
    private void tbFirstNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbFirstNameKeyTyped
        if(tbFirstName.getText().length()>=25) {  
        evt.consume();
        }
    }//GEN-LAST:event_tbFirstNameKeyTyped

    /**
     * Prevents the user from add to many characters to the text box 
     * @param evt 
     */
    private void tbLastNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbLastNameKeyTyped
        if(tbLastName.getText().length()>=25) {  
        evt.consume();
        }
    }//GEN-LAST:event_tbLastNameKeyTyped

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
            java.util.logging.Logger.getLogger(PatientTrustProperty.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PatientTrustProperty.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PatientTrustProperty.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PatientTrustProperty.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PatientTrustProperty().setVisible(true);
                
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddProp;
    private javax.swing.JButton btnClrSrch;
    private javax.swing.JButton btnRemoveProp;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox cbLoggedBy;
    private javax.swing.JCheckBox cbNextOfKin;
    private javax.swing.JCheckBox cbOwner;
    private javax.swing.JComboBox cbReturnedBy;
    private javax.swing.JComboBox cbStorageLoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField tbBagNo;
    private javax.swing.JFormattedTextField tbCollectTime;
    private javax.swing.JTextField tbDate;
    private javax.swing.JTextField tbDateReturned;
    private javax.swing.JTextArea tbDescription;
    private javax.swing.JTextField tbFirstName;
    private javax.swing.JTextField tbIDNum;
    private javax.swing.JTextField tbLastName;
    private javax.swing.JTextField tbPatientSticker;
    private javax.swing.JTextField tbReturnWorkOrdNum;
    private javax.swing.JTextField tbReturnedTo;
    private javax.swing.JTextField tbSearch;
    private javax.swing.JTextField tbTimeReturned;
    private javax.swing.JTextField tbWrkOrdNo;
    // End of variables declaration//GEN-END:variables
}
