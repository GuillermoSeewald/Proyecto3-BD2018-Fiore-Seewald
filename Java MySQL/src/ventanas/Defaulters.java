package ventanas;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class Defaulters extends JPanel {
	private static final long serialVersionUID = 1L;
	private Connection conexionDataBase = null;
	
	private JTable table;

	/**
	 * Create the panel.
	 */
	public Defaulters() {
		setLayout(new BorderLayout(0, 0));
		createTable();
		
		this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
               disconnectDataBase();
            }
            public void componentShown(ComponentEvent evt) {
               connectDataBase();
            }
         });
	}
	
	private void createTable(){
		JScrollPane scrollTable = new JScrollPane();
		add(scrollTable);
		
		String[] columns = new String[] {"Cliente", "Tipo doc.", "Documento", "Nombre", "Apellido", "Préstamo", "Monto", "N° meses", "Valor cuota", "Cuotas atrasadas"};
		@SuppressWarnings("serial")
		TableModel acountModel = new DefaultTableModel(new String[][] {}, columns){
			@SuppressWarnings("rawtypes")
			Class[] types = new Class[] {java.lang.Integer.class, java.lang.String.class,
   	    		 java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
   	    		java.lang.Integer.class, java.lang.Float.class, java.lang.Integer.class,
   	    		java.lang.Float.class, java.lang.Integer.class};
			boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false, false, false, false };
             
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
         TableColumnModel model =  table.getColumnModel();
         DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
         tcr.setHorizontalAlignment(SwingConstants.CENTER);
         for(int i=0;i<model.getColumnCount();i++){
             model.getColumn(i).setPreferredWidth(80);
             model.getColumn(i).setCellRenderer(tcr);
         }
         model.getColumn(9).setPreferredWidth(130);
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
	
	//Muestra los paneles necesesarios
	public void setMostrar(boolean b) {
		setVisible(b);
		if(b) {
			connectDataBase();
			agregarMorosos();
		}
		else
			disconnectDataBase();
	}
	
	//Agrega los clientes morosos a una tabla
	private void agregarMorosos() {
		try {
			String selection= "SELECT c.nro_cliente, tipo_doc, nro_doc, nombre, apellido, a.nro_prestamo, monto, cant_meses, valor_cuota, cant FROM (((SELECT nro_prestamo, count(nro_prestamo) as cant FROM (SELECT * FROM pago WHERE fecha_venc<'" + Fechas.actualDiaSQL() + "' AND fecha_pago is NULL) a GROUP BY nro_prestamo) a JOIN prestamo b ON a.nro_prestamo=b.nro_prestamo) JOIN cliente c ON b.nro_cliente=c.nro_cliente) WHERE cant>=2";
			Statement statement = conexionDataBase.createStatement();
			ResultSet result = statement.executeQuery(selection);
			((DefaultTableModel) table.getModel()).setRowCount(0);
	        int i = 0;
	        while (result.next()){
		            ((DefaultTableModel) table.getModel()).setRowCount(i + 1);
		            table.setValueAt(result.getInt("nro_cliente"), i, 0);
		            table.setValueAt(result.getString("tipo_doc"), i, 1);
		            table.setValueAt(result.getInt("nro_doc"), i, 2);
		            table.setValueAt(result.getString("nombre"), i, 3);
		            table.setValueAt(result.getString("apellido"), i, 4);
		            table.setValueAt(result.getInt("nro_prestamo"), i, 5);
		            table.setValueAt(result.getFloat("monto"), i, 6);
		            table.setValueAt(result.getInt("cant_meses"), i, 7);
		            table.setValueAt(result.getFloat("valor_cuota"), i, 8);
		            table.setValueAt(result.getInt("cant"), i, 9);
		            i++;
	        }
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    ex.getMessage() + "\n", 
                    "Error al ejecutar la consulta.",
                    JOptionPane.ERROR_MESSAGE);
		}
	}

}
