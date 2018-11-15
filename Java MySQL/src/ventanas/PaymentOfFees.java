package ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class PaymentOfFees extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JPanel pane_information_loans, pane_validate_client;
	private JTable table;
	private Connection conexionDataBase = null;
	private JComboBox<String> comboBoxDocumento;
	private JFormattedTextField textDocumento;
	private int nro_cliente;

	/**
	 * Create the panel.
	 */
	public PaymentOfFees() {
		setLayout(new BorderLayout(0, 0));
		this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
               disconnectDataBase();
            }
            public void componentShown(ComponentEvent evt) {
               connectDataBase();
            }
         });
		
		createPaneClientData();
		createPaneInformationLoans();
	}
	
	private void createPaneClientData(){
		pane_validate_client = new JPanel();
		add(pane_validate_client, BorderLayout.WEST);
		pane_validate_client.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		pane_validate_client.add(panel, BorderLayout.NORTH);
		panel.setBorder(new EmptyBorder(20, 5, 5, 5));
		panel.setLayout(new GridLayout(7, 1, 0, 5));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(1, 2, 5, 0));
		
		JLabel lblNewLabel = new JLabel("Tipo doc.");
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_1.add(lblNewLabel);
		
		comboBoxDocumento = new JComboBox<String>();
		comboBoxDocumento.setModel(new DefaultComboBoxModel<String>(new String[] {"DNI", "LC", "LE"}));
		panel_1.add(comboBoxDocumento);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(1, 2, 5, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Documento");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_2.add(lblNewLabel_1);
		
		textDocumento = new JFormattedTextField();
		textDocumento.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c= arg0.getKeyChar();
				if(! (Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE) || (c==KeyEvent.VK_DELETE)))
					arg0.consume();
			}
		});
		panel_2.add(textDocumento);
		
		JPanel panel_6 = new JPanel();
		panel.add(panel_6);
		panel_6.setLayout(new GridLayout(1, 2, 5, 0));
		
		JLabel lblNewLabel_2 = new JLabel("");
		panel_6.add(lblNewLabel_2);
		
		JButton check_client = new JButton("Controlar");
		check_client.setBackground(Color.LIGHT_GRAY);
		check_client.addActionListener(new PaymentListener());
		panel_6.add(check_client);
	}
	
	private void createPaneInformationLoans(){
		pane_information_loans = new JPanel();
		pane_information_loans.setBorder(new EmptyBorder(20, 5, 10, 5));
		add(pane_information_loans, BorderLayout.CENTER);
		pane_information_loans.setLayout(null);
		JScrollPane scrollTable = new JScrollPane();
		scrollTable.setBounds(10, 0, 275, 265);
		pane_information_loans.add(scrollTable);
		
		String[] columns = new String[] {"Numero", "Valor", "Fecha vencimiento", "Registrar"};
		@SuppressWarnings("serial")
		TableModel acountModel = new DefaultTableModel(new String[][] {}, columns){
			@SuppressWarnings("rawtypes")
			Class[] types = new Class[] {java.lang.Integer.class, java.lang.Float.class,
   	    		 java.lang.String.class, java.lang.Boolean.class };
			boolean[] canEdit = new boolean[] { false, false, false, true };
             
            @SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int columnIndex) 
            {
               return types[columnIndex];
            }
            
            public boolean isCellEditable(int rowIndex, int columnIndex) 
            {
               return canEdit[columnIndex];
            }
         };

         table = new JTable();
         scrollTable.setViewportView(table);
         table.setModel(acountModel);
         table.setAutoCreateRowSorter(true);
         
         JButton registrar = new JButton("Registrar");
         registrar.addActionListener(new ActionListener() {
         	public void actionPerformed(ActionEvent arg0) {
         		registrarPagos();
         		JOptionPane.showMessageDialog(getRootPane(), "Se han registrado todos los pagos seleccionados.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
         		setMostrar(false);
         	}
         });
         registrar.setBackground(Color.LIGHT_GRAY);
         registrar.setBounds(183, 269, 92, 21);
         pane_information_loans.add(registrar);

         pane_information_loans.setVisible(false);
	}
	
	private class PaymentListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!textDocumento.getText().trim().equals("")) {
				String s = e.getActionCommand();
				switch(s){
					case("Controlar"):{
						boolean existe=false;
						nro_cliente=0;
						try {
							String comprobarDNI= "SELECT * FROM cliente WHERE nro_doc=" + textDocumento.getText().trim() + " AND tipo_doc='" + comboBoxDocumento.getSelectedItem() + "'";
							Statement statement = conexionDataBase.createStatement();
							ResultSet result = statement.executeQuery(comprobarDNI);
							while(result.next()){
								existe=true;
								nro_cliente= result.getInt("nro_cliente");
							}
						}catch(SQLException ex) {
						}
						if(!existe) {
							JOptionPane.showMessageDialog(getRootPane(), "No existe un cliente registrado con ese Documento", "Error", JOptionPane.ERROR_MESSAGE);
							pane_information_loans.setVisible(false);
						}
						else {
							if(mostrarImpagos())
								pane_information_loans.setVisible(true);
							else {
								JOptionPane.showMessageDialog(getRootPane(), "El cliente no tiene cuotas impagas", "Información", JOptionPane.INFORMATION_MESSAGE);
								pane_validate_client.setVisible(false);
							}
						}
						break;
					}
			}
			}else
				JOptionPane.showMessageDialog(getRootPane(), "Debe ingresar un número de documento", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private void connectDataBase(){
		if (this.conexionDataBase == null){ 
			try{
	            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	         }
	         catch (Exception ex){}
	     
	         try{
	            String servidor = "localhost:3306";
	            String baseDatos = "banco";
	            String usuario = "empleado";
	            String clave = "empleado";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
	            this.conexionDataBase = DriverManager.getConnection(uriConexion, usuario, clave);
	         }
	         catch (SQLException ex){
	            JOptionPane.showMessageDialog(this,
	                                          "Se produjo un error al intentar conectarse a la base de datos.\n" + 
	                                           ex.getMessage(),
	                                          "Error",
	                                          JOptionPane.ERROR_MESSAGE);
	         }
	      }
	}
	
	private void disconnectDataBase(){
		if (this.conexionDataBase != null){
	         try{
	            this.conexionDataBase.close();
	            this.conexionDataBase = null;
	         }
	         catch (SQLException ex){}
	      }
	}
	
	//Muestra en una tabla los pagos impagos que tiene un determinado cliente
	private boolean mostrarImpagos() {
		boolean tiene=false;
		try {
			String selection= "SELECT nro_pago, valor_cuota, fecha_venc FROM prestamo pr JOIN pago pa ON pr.nro_prestamo=pa.nro_prestamo WHERE nro_cliente="+nro_cliente+" AND fecha_pago is NULL";
			Statement statement = conexionDataBase.createStatement();
			ResultSet result = statement.executeQuery(selection);
			((DefaultTableModel) table.getModel()).setRowCount(0);
			int i = 0;
			while(result.next()){
					((DefaultTableModel) table.getModel()).setRowCount(i + 1);
		            table.setValueAt(result.getInt("nro_pago"), i, 0);
		            table.setValueAt(result.getFloat("valor_cuota"), i, 1);
		            table.setValueAt(Fechas.convertirSQLAString(result.getString("fecha_venc")), i, 2);
		            table.setValueAt(false, i, 3);
		            i++;
		            tiene=true;
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    ex.getMessage() + "\n", 
                    "Error al ejecutar la consulta.",
                    JOptionPane.ERROR_MESSAGE);
		}
		return tiene;
	}
	
	//Registra los pagos seleccionados según la tabla de impagos
	private void registrarPagos() {
		try {
			String selection= "";
			int nro_prestamo=0;
			Statement statement=conexionDataBase.createStatement();;
			ResultSet result;
			int cant= ((DefaultTableModel) table.getModel()).getRowCount();
			for(int i=0;i<cant;i++) {
				if((boolean) table.getValueAt(i, 3)) {
					selection= "SELECT pr.nro_prestamo FROM prestamo pr JOIN pago pa ON pr.nro_prestamo=pa.nro_prestamo WHERE nro_cliente="+nro_cliente+" AND nro_pago=" + table.getValueAt(i, 0) + " AND fecha_pago is NULL";
					statement = conexionDataBase.createStatement();
					result = statement.executeQuery(selection);
					while(result.next())
						nro_prestamo= result.getInt("nro_prestamo");
					selection= "UPDATE pago SET fecha_pago='" + Fechas.actualDiaSQL() + "' WHERE nro_prestamo=" + nro_prestamo + " AND nro_pago=" + table.getValueAt(i, 0);
					statement.execute(selection);
				}
			}
			statement.close();
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    ex.getMessage() + "\n", 
                    "Error al ejecutar la consulta.",
                    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//Muestra los paneles necesarios
	public void setMostrar(boolean b) {
		this.setVisible(b);
		pane_validate_client.setVisible(b);
		pane_information_loans.setVisible(false);
		((DefaultTableModel) table.getModel()).setRowCount(0);
 		textDocumento.setText("");
		
	}
}
