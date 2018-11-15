package ventanas;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
import java.text.ParseException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;



import java.awt.FlowLayout;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JFormattedTextField;
import java.awt.Font;

public class ATMWindow extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JPanel pane_information, panel_1, center_pane, panelTransferir, panelExtraer;
	private JTable table;
	private Connection conexionDataBase = null;
	private String nro_tarjeta;
	private JScrollPane scrollTable;
	JFormattedTextField text_field_start_range, text_field_end_range;
	private JTextField textMonto;
	private JTextField textCaja;
	private JTextField textMonto2;

	/**
	 * Create the frame.
	 */
	public ATMWindow() {
		setBounds(100, 100, 450, 300);
        setVisible(true);
        this.setTitle("Cajero automático");
        getContentPane().setLayout(new BorderLayout(0, 0));
        this.setClosable(true);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        
        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
               disconnectDataBase();
            }
            public void componentShown(ComponentEvent evt) {
               connectDataBase();
            }
         });
        
        createButtonsPane(); 
        createCenterPane();
        createTable();       
        
	}
	
	private void createButtonsPane(){
		JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.WEST);
        
        Listener l = new Listener();
        
        JButton button_query_amount = new JButton("Consultar saldo");
        button_query_amount.setBackground(Color.LIGHT_GRAY);
        button_query_amount.setFocusable(false);
        button_query_amount.setActionCommand("Consultar saldo");
        button_query_amount.addActionListener(l);
        panel.setLayout(new GridLayout(7, 1, 0, 10));
        panel.add(button_query_amount);
        
        JButton button_last_moves = new JButton("Ultimos movimientos");
        button_last_moves.setBackground(Color.LIGHT_GRAY);
        button_last_moves.setFocusable(false);
        button_last_moves.setActionCommand("Ultimos movimientos");
        button_last_moves.addActionListener(l);
        panel.add(button_last_moves);
        
        JButton btnTransferir = new JButton("Transferir");
        btnTransferir.setBackground(Color.LIGHT_GRAY);
        btnTransferir.setFocusable(false);
        btnTransferir.setActionCommand("Transferir");
        btnTransferir.addActionListener(l);
        
        JButton button_range_moves = new JButton("Movimientos por per\u00EDodo");
        button_range_moves.setFocusable(false);
        button_range_moves.setBackground(Color.LIGHT_GRAY);
        button_range_moves.setActionCommand("Movimientos por per\u00EDodo");
        button_range_moves.addActionListener(l);
        panel.add(button_range_moves);
        panel.add(btnTransferir);
        
        JButton btnExtraer = new JButton("Extraer");
        btnExtraer.setFocusable(false);
        btnExtraer.setBackground(Color.LIGHT_GRAY);
        btnExtraer.setActionCommand("Extraer");
        btnExtraer.addActionListener(l);
        panel.add(btnExtraer);
	}
	
	private void createTable(){
		scrollTable = new JScrollPane();
		pane_information.add(scrollTable);
		
		String[] columns = new String[] {"Fecha", "Hora", "Tipo de transacción", "Monto", "Código de caja", "Caja de ahorro destino"};
		@SuppressWarnings("serial")
		TableModel acountModel = new DefaultTableModel(new String[][] {}, columns){
			@SuppressWarnings("rawtypes")
			Class[] types = new Class[] {java.lang.String.class, java.lang.String.class, java.lang.String.class,
   	    		 java.lang.Float.class, java.lang.String.class, java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, false, false, false, false };
             
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

         pane_information.setVisible(false);
	}

	private void createCenterPane(){
		center_pane = new JPanel();
        getContentPane().add(center_pane, BorderLayout.CENTER);
        center_pane.setLayout(new BorderLayout(0, 0));
        
        JPanel panel2 = new JPanel();
        getContentPane().add(center_pane, BorderLayout.CENTER);
        center_pane.setLayout(new BorderLayout(0, 0));
        
        pane_information = new JPanel();
        pane_information.setBorder(new EmptyBorder(5, 5, 5, 5));
        center_pane.add(pane_information, BorderLayout.CENTER);
        pane_information.setLayout(new CardLayout(0, 0));
        
        panelTransferir = new JPanel();
        pane_information.add(panelTransferir);
        panelTransferir.setLayout(null);
        
        panelExtraer = new JPanel();
        pane_information.add(panelExtraer);
        panelExtraer.setLayout(null);
        
        JLabel label_1 = new JLabel("Monto:");
        label_1.setBounds(10, 40, 45, 13);
        panelExtraer.add(label_1);
        
        textMonto2 = new JTextField();
        textMonto2.setColumns(10);
        textMonto2.setBounds(10, 63, 74, 19);
        textMonto2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c= arg0.getKeyChar();
				if(! (Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE) || (c==KeyEvent.VK_DELETE)))
					arg0.consume();
			}
		});
        panelExtraer.add(textMonto2);
        
        JButton button = new JButton("Realizar");
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if(textMonto2.getText().trim().equals(""))
        			JOptionPane.showMessageDialog(getContentPane(), "Debe ingresar un monto", "Error", JOptionPane.ERROR_MESSAGE);
        		else {
        			int dialogResult = JOptionPane.showConfirmDialog(null, "¿Está seguro de realizar la extracción?", "Alerta", JOptionPane.YES_NO_OPTION);
        			if(dialogResult == JOptionPane.YES_OPTION) {
	       				try {
	       					String selection= "SELECT nro_cliente,nro_ca FROM tarjeta where nro_tarjeta="+nro_tarjeta;
							Statement statement = conexionDataBase.createStatement();
							ResultSet result = statement.executeQuery(selection);
							int nro_ca=-1;
							int nro_cliente=-1;
							while(result.next()){
								nro_ca= result.getInt("nro_ca");
								nro_cliente= result.getInt("nro_cliente");
							}
							selection= "call extraer("+ textMonto2.getText().trim() + "," + nro_ca + "," + nro_cliente + ",100)";
							result = statement.executeQuery(selection);
							String resultado="";
							while(result.next()){
								resultado= result.getString("resultado");
							}
							if(resultado.charAt(0)=='L') {
								selection= "SELECT * FROM trans_cajas_ahorro WHERE nro_ca=" + nro_ca;
								result = statement.executeQuery(selection);
								float saldo=0;
								while(result.next()){
									saldo= result.getFloat("saldo");
								}
								resultado= resultado + " Saldo actual: "+ saldo;
								JOptionPane.showMessageDialog(getContentPane(), resultado, "Exito", JOptionPane.INFORMATION_MESSAGE);
								pane_information.setVisible(false);
							}
							else
								JOptionPane.showMessageDialog(getContentPane(), resultado, "Error", JOptionPane.ERROR_MESSAGE);
	        			}catch (SQLException ex){
	        				ex.printStackTrace();
	       				}
        			}
       				else
       					pane_information.setVisible(false);
       			}
        	}
        });
        button.setBounds(10, 99, 82, 21);
        panelExtraer.add(button);
        
        JLabel lblMonto = new JLabel("Monto:");
        lblMonto.setBounds(10, 40, 45, 13);
        panelTransferir.add(lblMonto);
        
        textMonto = new JTextField();
        textMonto.setBounds(10, 63, 82, 19);
        textMonto.setColumns(10);
        textMonto.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c= arg0.getKeyChar();
				if(! (Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE) || (c==KeyEvent.VK_DELETE)))
					arg0.consume();
			}
		});
        panelTransferir.add(textMonto);
        
        JLabel lblCajaAhorroDestino = new JLabel("Caja de ahorro destino:");
        lblCajaAhorroDestino.setBounds(119, 40, 139, 13);
        panelTransferir.add(lblCajaAhorroDestino);
        
        textCaja = new JTextField();
        textCaja.setColumns(10);
        textCaja.setBounds(119, 63, 82, 19);
        textCaja.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c= arg0.getKeyChar();
				if(! (Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE) || (c==KeyEvent.VK_DELETE)))
					arg0.consume();
			}
		});
        panelTransferir.add(textCaja);
        
        JButton btnRealizar = new JButton("Realizar");
        btnRealizar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if(textMonto.getText().trim().equals(""))
        			JOptionPane.showMessageDialog(getContentPane(), "Debe ingresar un monto", "Error", JOptionPane.ERROR_MESSAGE);
        		else
        			if(textCaja.getText().trim().equals(""))
        				JOptionPane.showMessageDialog(getContentPane(), "Debe ingresar una caja ahorro destino", "Error", JOptionPane.ERROR_MESSAGE);
        			else {
        				int dialogResult = JOptionPane.showConfirmDialog(null, "¿Está seguro de realizar la transferencia?", "Alerta", JOptionPane.YES_NO_OPTION);
        				if(dialogResult == JOptionPane.YES_OPTION) {
	        				try {
		        				String selection= "SELECT nro_cliente,nro_ca FROM tarjeta WHERE nro_tarjeta=" + nro_tarjeta;
								Statement statement = conexionDataBase.createStatement();
								ResultSet result = statement.executeQuery(selection);
								int nro_ca=-1;
								int nro_cliente=-1;
								while(result.next()){
									nro_ca= result.getInt("nro_ca");
									nro_cliente= result.getInt("nro_cliente");
								}
								if(nro_ca==Integer.parseInt(textCaja.getText().trim()))
									JOptionPane.showMessageDialog(getContentPane(), "La cajas de ahorro origen y destino no pueden ser iguales", "Error", JOptionPane.ERROR_MESSAGE);
								else {
								selection= "call transferir("+ textMonto.getText().trim() + "," + nro_ca + ","+ textCaja.getText().trim() + "," + nro_cliente + ",100)";
								result = statement.executeQuery(selection);
								String resultado="";
								while(result.next()){
									resultado= result.getString("resultado");
								}
								if(resultado.charAt(0)=='L') {
									JOptionPane.showMessageDialog(getContentPane(), resultado, "Exito", JOptionPane.INFORMATION_MESSAGE);
									pane_information.setVisible(false);
								}
								else
									JOptionPane.showMessageDialog(getContentPane(), resultado, "Error", JOptionPane.ERROR_MESSAGE);
								}
	        				}catch (SQLException ex){
	        					ex.printStackTrace();
	        				}
        				}
        				else
        					pane_information.setVisible(false);
        			}
        	}
        });
        btnRealizar.setBounds(119, 99, 82, 21);
        panelTransferir.add(btnRealizar);
        panelTransferir.setVisible(false);
        
		panel_1 = new JPanel();
        center_pane.add(panel_1, BorderLayout.SOUTH);
        
        text_field_start_range = new JFormattedTextField();
        
        text_field_end_range = new JFormattedTextField();
        panel_1.setLayout(new GridLayout(3, 1, 0, 0));
        
        JPanel section_1 = new JPanel();
        panel_1.add(section_1);
        section_1.setLayout(new GridLayout(0, 3, 5, 0));
        
        JLabel label = new JLabel("");
        section_1.add(label);
        

        
        JLabel label_start_range = new JLabel("Inicio de per\u00EDodo");
        label_start_range.setHorizontalAlignment(SwingConstants.TRAILING);
        section_1.add(label_start_range);
        try {
			text_field_start_range = new JFormattedTextField(new MaskFormatter("##'/##'/####"));
		} catch (ParseException e) {
		}
        text_field_start_range.setFont(new Font("Tahoma", Font.PLAIN, 13));
        section_1.add(text_field_start_range);
        
        JPanel section_2 = new JPanel();
        panel_1.add(section_2);
        
        JLabel label_end_range = new JLabel("Fin de per\u00EDodo");
        label_end_range.setHorizontalAlignment(SwingConstants.TRAILING);
        section_2.setLayout(new GridLayout(0, 3, 5, 0));
        
        JLabel lblNewLabel_1 = new JLabel("");
        section_2.add(lblNewLabel_1);
        section_2.add(label_end_range);
        try {
			text_field_end_range = new JFormattedTextField(new MaskFormatter("##'/##'/####"));
		} catch (ParseException e) {
		}
        text_field_end_range.setFont(new Font("Tahoma", Font.PLAIN, 13));
        section_2.add(text_field_end_range);
        
        JPanel section_3 = new JPanel();
        panel_1.add(section_3);
        
        JLabel lblNewLabel = new JLabel("");
        section_3.setLayout(new FlowLayout(FlowLayout.RIGHT));
        section_3.add(lblNewLabel);
        
        JButton btnConsultar = new JButton("Consultar");
        section_3.add(btnConsultar);
        btnConsultar.setFocusable(false);
        btnConsultar.setActionCommand("Consultar");
        btnConsultar.addActionListener(new Listener());
        
        panel_1.setVisible(false);
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
	            String usuario = "atm";
	            String clave = "atm";
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
	
	//Muestra las últimas 15 transacciones de una cuenta
	private void updateTable(String selection)
	   {
	      try{
	         Statement statement = this.conexionDataBase.createStatement();
	         
	         ResultSet result = statement.executeQuery(selection);
	         ((DefaultTableModel) table.getModel()).setRowCount(0);
	         int i = 0;
	         while (result.next() && i<15){
	            ((DefaultTableModel) table.getModel()).setRowCount(i + 1);
	            table.setValueAt(Fechas.convertirSQLAString(result.getString("fecha")), i, 0);
	            table.setValueAt(result.getString("hora"), i, 1);            
	            table.setValueAt(result.getString("tipo"), i, 2);
	            if(result.getString("tipo").equals("Extraccion") || result.getString("tipo").equals("Transferencia") || result.getString("tipo").equals("Debito"))
	            	table.setValueAt(-1 * result.getFloat("monto"), i, 3);
	            else
	            	table.setValueAt(result.getFloat("monto"), i, 3);
	            table.setValueAt(result.getString("cod_caja"), i, 4);
	            table.setValueAt(result.getString("destino"), i, 5);
	            i++;
	         }
	         
	         result.close();
	         statement.close();
	      }
	      catch (SQLException ex){}
	   }
	
	private class Listener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String s = e.getActionCommand();
			switch(s){
				case("Consultar saldo"):{
					float monto=0;
					try {
						String query = "SELECT saldo FROM (tarjeta t JOIN trans_cajas_ahorro c ON t.nro_ca=c.nro_ca) WHERE nro_tarjeta=" + nro_tarjeta;
						Statement statement = conexionDataBase.createStatement();
						ResultSet result = statement.executeQuery(query);
						while(result.next()){
							monto = result.getInt("saldo");
						}
						
						JOptionPane.showMessageDialog(getContentPane(), "Su monto es de $" + monto, "Monto", JOptionPane.INFORMATION_MESSAGE);
					} catch (SQLException e1) {
					}
					break;
				}
				case("Ultimos movimientos"):{
					table.setAutoCreateRowSorter(true);
					String selection = "SELECT fecha, hora, tipo, monto, cod_caja, destino FROM (trans_cajas_ahorro tca JOIN tarjeta tc ON tca.nro_ca=tc.nro_ca) WHERE nro_tarjeta=" + nro_tarjeta + " ORDER BY fecha DESC, hora DESC";
					updateTable(selection);
					center_pane.setVisible(true);
			        pane_information.setVisible(true);
			        panelTransferir.setVisible(false);
			        scrollTable.setVisible(true);
			        panelExtraer.setVisible(false);
			        panel_1.setVisible(false);
			        textMonto.setText("");
			        textMonto2.setText("");
			        textCaja.setText("");
					break;
				}
				case("Movimientos por per\u00EDodo"):{
					table.setAutoCreateRowSorter(true);
					((DefaultTableModel) table.getModel()).setRowCount(0);
					center_pane.setVisible(true);
			        pane_information.setVisible(true);
			        panelTransferir.setVisible(false);
			        scrollTable.setVisible(true);
			        panelExtraer.setVisible(false);
			        panel_1.setVisible(true);
			        textMonto.setText("");
			        textMonto2.setText("");
			        textCaja.setText("");
					break;
				}
				case("Consultar"):{
					if(validarFechas()) {
						String fecha1= Fechas.convertirStringASQL(text_field_start_range.getText().trim());
						String fecha2= Fechas.convertirStringASQL(text_field_end_range.getText().trim());
						String selection = "SELECT fecha, hora, tipo, monto, cod_caja, destino FROM (trans_cajas_ahorro tca JOIN tarjeta tc ON tca.nro_ca=tc.nro_ca) WHERE nro_tarjeta=" + nro_tarjeta + " AND fecha>='" + fecha1 + "' AND fecha<='" + fecha2 + "' ORDER BY fecha DESC, hora DESC";
						updateTable(selection);
					}
					break;
				}
				case("Transferir"):{
					center_pane.setVisible(true);
					pane_information.setVisible(true);
					panelTransferir.setVisible(true);
					panelExtraer.setVisible(false);
			        scrollTable.setVisible(false);
			        panel_1.setVisible(false);
			        textMonto.setText("");
			        textMonto2.setText("");
			        textCaja.setText("");
					break;
				}
				case("Extraer"):{
					center_pane.setVisible(true);
					pane_information.setVisible(true);
					panelTransferir.setVisible(false);
					panelExtraer.setVisible(true);
			        scrollTable.setVisible(false);
			        panel_1.setVisible(false);
			        textMonto.setText("");
			        textMonto2.setText("");
			        textCaja.setText("");
					break;
				}
			}
		}
		
	}
	
	//Recibe el número de tarjeta con el que se logueo
	public void pasarTarjeta(String tarjeta) {
		nro_tarjeta=tarjeta;
	}
	
	//Valida que las fechas ingresadas sean válidas
	public boolean validarFechas() {
		String mensajeError = null;
		if (this.text_field_start_range.getText().isEmpty())
	      {
	         mensajeError = "Debe ingresar un valor para la primera fecha.";
	      }
		else if (! Fechas.validar(this.text_field_start_range.getText().trim()))
	      {
	         mensajeError = "En la primera fecha debe ingresar un valor con el formato dd/mm/aaaa.";
	      }
		else
			if (this.text_field_end_range.getText().isEmpty())
		      {
		         mensajeError = "Debe ingresar un valor para la segunda fecha.";
		      }
			else if (! Fechas.validar(this.text_field_end_range.getText().trim()))
		      {
		         mensajeError = "En la segunda fecha debe ingresar un valor con el formato dd/mm/aaaa.";
		      }
			else if (! Fechas.fechaMenor(text_field_start_range.getText().trim(), text_field_end_range.getText().trim()))
			{
				mensajeError = "La primer fecha debe ser menor o igual a la segunda";
			}
		if (mensajeError != null)
	      {
	         JOptionPane.showMessageDialog(this,
	                                       mensajeError,
	                                       "Error",
	                                       JOptionPane.ERROR_MESSAGE);
	         return false;
	      }
		return true;
	}
	
	//Muestra los paneles necesarios
	public void setMostrar(boolean b) {
		this.setVisible(b);
		center_pane.setVisible(false);
	}
}
