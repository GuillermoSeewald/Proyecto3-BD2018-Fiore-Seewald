package ventanas;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.text.MaskFormatter;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.TitledBorder;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.MatteBorder;

public class NewLoan extends JPanel {
	private JTextField textMonto;
	private JPanel panel_3, panel_4;
	private JInternalFrame container;
	private Connection conexionDataBase = null;
	private JFormattedTextField formattedTextField;
	private JComboBox<String> comboBoxDNI;
	private String legajo;
	private JComboBox<String> comboBoxMeses;

	/**
	 * Create the panel.
	 */
	public NewLoan(JInternalFrame container) {
		setLayout(new BorderLayout(0, 0));
		this.container = container;
		
		this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
               disconnectDataBase();
            }
            public void componentShown(ComponentEvent evt) {
               connectDataBase();
            }
         });
		
		panel_3 = new JPanel();
		add(panel_3, BorderLayout.WEST);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel_3.add(panel, BorderLayout.NORTH);
		panel.setBorder(new EmptyBorder(20, 5, 5, 5));
		panel.setLayout(new GridLayout(7, 1, 0, 5));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(1, 2, 5, 0));
		
		JLabel lblNewLabel = new JLabel("Tipo doc.");
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_1.add(lblNewLabel);
		
		comboBoxDNI = new JComboBox<String>();
		comboBoxDNI.setModel(new DefaultComboBoxModel<String>(new String[] {"DNI", "LC", "LE"}));
		panel_1.add(comboBoxDNI);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(1, 2, 5, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Documento");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_2.add(lblNewLabel_1);
		
		formattedTextField = new JFormattedTextField();
		formattedTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c= arg0.getKeyChar();
				if(! (Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE) || (c==KeyEvent.VK_DELETE)))
					arg0.consume();
			}
		});
		panel_2.add(formattedTextField);
		
		JPanel panel_6 = new JPanel();
		panel.add(panel_6);
		panel_6.setLayout(new GridLayout(1, 2, 5, 0));
		
		JLabel lblNewLabel_2 = new JLabel("");
		panel_6.add(lblNewLabel_2);
		
		JButton check_client = new JButton("Controlar");
		check_client.setBackground(Color.LIGHT_GRAY);
		check_client.addActionListener(new NewLoanListener());
		panel_6.add(check_client);
		
		panel_4 = new JPanel();
		add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new MatteBorder(0, 1, 0, 0, (Color) new Color(0, 0, 0)));
		FlowLayout flowLayout = (FlowLayout) panel_7.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		panel_4.add(panel_7, BorderLayout.NORTH);
		panel_4.setVisible(false);
		
		JPanel panel_5 = new JPanel();
		panel_7.add(panel_5);
		panel_5.setBorder(new EmptyBorder(10, 15, 5, 5));
		panel_5.setLayout(new GridLayout(6, 1, 0, 0));
		
		JLabel lblIngreseElMonto = new JLabel("Ingrese el monto");
		panel_5.add(lblIngreseElMonto);
		
		textMonto = new JTextField();
		textMonto.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c= arg0.getKeyChar();
				if(! (Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE) || (c==KeyEvent.VK_DELETE)))
					arg0.consume();
			}
		});
		panel_5.add(textMonto);
		textMonto.setColumns(10);
		
		JLabel lblSeleccioneLaCantidad = new JLabel("Seleccione la cantidad de meses");
		panel_5.add(lblSeleccioneLaCantidad);
		
		this.connectDataBase();
		
		comboBoxMeses = new JComboBox<String>();
		comboBoxMeses.setFocusable(false);
		comboBoxMeses.setModel(new DefaultComboBoxModel<String>(cantMeses()));
		panel_5.add(comboBoxMeses);
		
		JButton agregarPrestamo = new JButton("Agregar");
		agregarPrestamo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textMonto.getText().trim().equals(""))
					JOptionPane.showMessageDialog(container.getContentPane(), "Debe ingresar un monto", "Error", JOptionPane.ERROR_MESSAGE);
				else
					if(!validarMonto())
						JOptionPane.showMessageDialog(container.getContentPane(), "Debe ingresar un monto que no supere el máximo", "Error", JOptionPane.ERROR_MESSAGE);
					else {
						calcularValores();
					}
			}
		});
		panel_5.add(agregarPrestamo);
	}
	
	private class NewLoanListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String s = e.getActionCommand();
			switch(s){
				case("Controlar"):{
					boolean existe=false;
					boolean permite=true;  
					try {
						int nro_cliente=0;
						String comprobarDNI= "SELECT * FROM cliente WHERE nro_doc=" + formattedTextField.getText().trim() + " AND tipo_doc='" + comboBoxDNI.getSelectedItem() + "'";
						Statement statement = conexionDataBase.createStatement();
						ResultSet result = statement.executeQuery(comprobarDNI);
						while(result.next()){
							existe=true;
							nro_cliente= result.getInt("nro_cliente");
						}
						String comprobarPago= "SELECT * FROM prestamo pr JOIN pago pa on pr.nro_prestamo=pa.nro_prestamo WHERE fecha_pago is NULL AND nro_cliente=" + nro_cliente;
						statement = conexionDataBase.createStatement();
						result = statement.executeQuery(comprobarPago);
						while(result.next()){
							permite=false;
						}
					}catch(SQLException e1) {
					}
					if(!existe) {
						JOptionPane.showMessageDialog(container.getContentPane(), "No existe un cliente registrado con ese Documento", "Error", JOptionPane.ERROR_MESSAGE);
						panel_4.setVisible(false);
					}
					else
						if(!permite) {
							JOptionPane.showMessageDialog(container.getContentPane(), "El cliente ya tiene un prestamo vigente", "Error", JOptionPane.ERROR_MESSAGE);
							panel_4.setVisible(false);
						}
						else
							panel_4.setVisible(true);
					break;
				}
			}
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
	
	//Muestra los paneles necesarios
	public void setMostrar(boolean b) {
			this.setVisible(b);
			panel_3.setVisible(b);
			panel_4.setVisible(false);
			textMonto.setText("");
			formattedTextField.setText("");
			
	}
	
	//Recibe el número de legajo del empleado que se logueo
	public void pasarLegajo(String legajo) {
		this.legajo=legajo;
	}
	
	//Valida que el monto ingresado sea válido según la tabla tasa_prestamo
	private boolean validarMonto(){
		boolean valido=false;
		try {
			String selection= "SELECT monto_inf, monto_sup FROM tasa_prestamo WHERE " + Integer.parseInt(textMonto.getText().trim()) + ">=monto_inf AND " + Integer.parseInt(textMonto.getText().trim()) + "<=monto_sup";
			Statement statement = conexionDataBase.createStatement();
			ResultSet result = statement.executeQuery(selection);
			while(result.next()){
				valido=true;
			}
		}catch(SQLException e1) {
		}
		return valido;
	}
	
	//Devuelve en un String[], los meses disponibles que se pueden elegir para un préstamo según la tabla tasa_prestamo
	private String[] cantMeses(){
		int cant=1;
		String [] retorno=null;
		try {
			//Este SELECT devuelve la cantidad de meses que se pueden elegir para un préstamo
			String selection= "SELECT count(periodo) cant FROM (SELECT periodo FROM tasa_prestamo GROUP BY periodo) p";
			Statement statement = conexionDataBase.createStatement();
			ResultSet result = statement.executeQuery(selection);
			while(result.next()){
				cant=result.getInt("cant");
			}
			retorno= new String[cant];
			selection= "SELECT periodo FROM tasa_prestamo GROUP BY periodo";
			result = statement.executeQuery(selection);
			int i=0;
			while(result.next()){
				retorno[i]=result.getString("periodo");
				i++;
			}
		}catch(SQLException e1) {
		}
		return retorno;
	}
	
	//Calcula los pagos según un nuevo préstamo
	private void calcularValores(){
		try {  
			String fecha= Fechas.actualDiaSQL();
			int meses= Integer.parseInt((String) comboBoxMeses.getSelectedItem());
			float monto= Integer.parseInt(textMonto.getText().trim());
			float tasaInteres=0;
			float interes;
			float valorCuota;
			int dni= Integer.parseInt(formattedTextField.getText().trim());
			int nro_cliente=0;
			String selection= "SELECT * FROM tasa_prestamo WHERE periodo=" + meses + " AND monto_inf>" + monto + " AND monto_sup<" + monto;
			Statement statement = conexionDataBase.createStatement();
			ResultSet result = statement.executeQuery(selection);
			while(result.next()){
				tasaInteres=result.getFloat("tasa");
			}
			interes= (monto*tasaInteres*meses)/1200;
			valorCuota=(monto+interes)/meses;
			selection= "SELECT * FROM cliente WHERE nro_doc=" + dni + " AND tipo_doc='" + comboBoxDNI.getSelectedItem() + "'";
			result = statement.executeQuery(selection);
			while(result.next()){
				nro_cliente=result.getInt("nro_cliente");
			}
			selection="INSERT INTO prestamo (fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente) VALUES ('"+fecha+"', "+meses+", "+monto+", "+tasaInteres+", "+interes+", "+valorCuota+", "+legajo+", "+nro_cliente+")";
			statement.execute(selection);
			/* Lo siguiente ya no es necesario porque hay un trigger implementado
			selection= "SELECT * FROM prestamo WHERE fecha='"+fecha+"' AND legajo="+legajo+" AND nro_cliente="+nro_cliente;
			result = statement.executeQuery(selection);
			String nro_prestamo=null;
			while(result.next()){
				nro_prestamo=result.getString("nro_prestamo");
			}
			for(int i=1;i<=meses;i++) {
				selection= "INSERT INTO pago (nro_prestamo, nro_pago, fecha_venc) VALUES ("+nro_prestamo+", "+i+", date_add('"+fecha+"', interval "+i+" month))";
				statement.execute(selection);
			}*/
			statement.close();
			this.setMostrar(false);
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    ex.getMessage() + "\n", 
                    "Error al ejecutar la consulta.",
                    JOptionPane.ERROR_MESSAGE);
		                                       
		}
		JOptionPane.showMessageDialog(this,
                "Se registró el nuevo prestamo con sus respectivos pagos.\n",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
		
	}

}
