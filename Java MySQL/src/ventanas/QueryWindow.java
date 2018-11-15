package ventanas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;

import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import quick.dbtable.DBTable;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class QueryWindow extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	
	private JTextArea query_text_area;
	private DBTable table;
	private Connection conexionDataBase = null;
	private JList<String> list_table_names, list_table_attributes;
	private JScrollPane scrollPane_attributes;

	/**
	 * Create the frame.
	 */
	public QueryWindow() {
		setBounds(100, 100, 450, 300);
        setVisible(true);
        this.setTitle("Consultas con sentencias SQL");
        BorderLayout thisLayout = new BorderLayout();
        getContentPane().setLayout(thisLayout);
        this.setClosable(true);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        
        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
            	disconnectDataBase();
            }
            public void componentShown(ComponentEvent evt) {
            	connectDataBase();
            	query_text_area.setText("");
            	scrollPane_attributes.setVisible(false);
            }
        });
        
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new BorderLayout(0, 0));
        
        table = new DBTable();
        table.setEditable(false);
        getContentPane().add(table, BorderLayout.CENTER);
        connectDataBase();
        
        createQueryPane(panel);
        createListPane(panel);
	}
	
	private void createQueryPane(JPanel panel){
		JPanel panel_query_text = new JPanel();
        panel_query_text.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(panel_query_text, BorderLayout.WEST);
        panel_query_text.setLayout(new BorderLayout(0, 0));
        
        JLabel label_title_insert_query = new JLabel("Ingrese la consulta");
        label_title_insert_query.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        label_title_insert_query.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label_title_insert_query.setBackground(Color.BLACK);
        panel_query_text.add(label_title_insert_query, BorderLayout.NORTH);
        
        query_text_area = new JTextArea();
        panel_query_text.add(query_text_area);
        query_text_area.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        query_text_area.setColumns(40);
        query_text_area.setRows(6);
        
        JPanel panel_query_button = new JPanel();
        panel_query_text.add(panel_query_button, BorderLayout.SOUTH);
        panel_query_button.setLayout(new BorderLayout(0, 0));
        
        JButton query_button = new JButton("Consultar");
        query_button.setFocusable(false);
        query_button.setHorizontalTextPosition(SwingConstants.LEADING);
        query_button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String s= query_text_area.getText().trim();
				if(!s.equals("")) {
					int i=0;
					while(i<s.length() && s.charAt(i)==' ') {
						i++;
					}
					if(s.charAt(i)=='s' || s.charAt(i)=='S')
						updateTable();
					else
						otraConsulta();
				}
				else
					JOptionPane.showMessageDialog(getContentPane(), "Debe ingresar una consulta\n", "Error", JOptionPane.ERROR_MESSAGE);
			}
        	
        });
        panel_query_button.add(query_button, BorderLayout.EAST);
	}
	
	private void createListPane(JPanel panel){
		JPanel panel_tables_list = new JPanel();
        panel_tables_list.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(panel_tables_list, BorderLayout.CENTER);
        panel_tables_list.setLayout(new GridLayout(0, 2, 0, 0));
       
        
        list_table_names = new JList<String>();
        list_table_names.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
        list_table_names.setModel(getListTableNames());
        list_table_names.setVisibleRowCount(5);
        list_table_names.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list_table_names.setLayoutOrientation(JList.VERTICAL);
        list_table_names.addListSelectionListener(new ListListener());
        
        JScrollPane scrollList = new JScrollPane(list_table_names);
        panel_tables_list.add(scrollList);
        
        JLabel list_tables_label = new JLabel("Tablas");
        list_tables_label.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        list_tables_label.setFont(new Font("Tahoma", Font.PLAIN, 14));
        list_tables_label.setBackground(Color.BLACK);
        list_tables_label.setForeground(Color.DARK_GRAY);
        list_tables_label.setHorizontalAlignment(SwingConstants.CENTER);
        scrollList.setColumnHeaderView(list_tables_label);
        
        list_table_attributes = new JList<String>();
        list_table_attributes.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
        list_table_attributes.setModel(getListTableNames());
        list_table_attributes.setVisibleRowCount(5);
        list_table_attributes.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list_table_attributes.setLayoutOrientation(JList.VERTICAL);
        
        scrollPane_attributes = new JScrollPane(list_table_attributes);
        scrollPane_attributes.setVisible(false);
        panel_tables_list.add(scrollPane_attributes);
        
        JLabel list_attributes_label = new JLabel("Atributos");
        list_attributes_label.setForeground(Color.DARK_GRAY);
        list_attributes_label.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        list_attributes_label.setBackground(Color.BLACK);
        list_attributes_label.setFont(new Font("Tahoma", Font.PLAIN, 14));
        list_attributes_label.setHorizontalAlignment(SwingConstants.CENTER);
        scrollPane_attributes.setColumnHeaderView(list_attributes_label);
	}
	
	private DefaultListModel<String> getListTableNames(){
		DefaultListModel<String> model = new DefaultListModel<String>();
		if(conexionDataBase!=null) {
		try {
			//Muestra las tablas de la base de datos banco
			String selection = "SHOW TABLES";
			Statement statement = this.conexionDataBase.createStatement();
			ResultSet result = statement.executeQuery(selection);
			
			while(result.next()){
				model.addElement(result.getString("Tables_in_banco"));
			}
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
        return model;
	}
	
	private void connectDataBase(){
        try {
    		String driver ="com.mysql.cj.jdbc.Driver";
        	String servidor = "localhost:3306";
            String baseDatos = "banco";
            String usuario = "admin";
            String clave = "admin";
            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
            this.conexionDataBase = DriverManager.getConnection(uriConexion, usuario, clave);
            table.connectDatabase(driver, uriConexion, usuario, clave);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			String output_message = "Se produjo un error al intentar conectarse a la base de datos.\n" + e.getMessage();
			JOptionPane.showMessageDialog(this, output_message, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void disconnectDataBase(){
		if (this.conexionDataBase != null) {
		try {
            this.conexionDataBase.close();
            this.conexionDataBase = null;
			table.close();
		} catch (SQLException e) {
		}
		}
	}
	
	private void updateTable(){
		try {
			table.setSelectSql(this.query_text_area.getText().trim());
			table.createColumnModelFromQuery();
			for (int i = 0; i < table.getColumnCount(); i++){
				if(table.getColumn(i).getType()==Types.TIME){
					table.getColumn(i).setType(Types.CHAR);
				}
				if(table.getColumn(i).getType()==Types.DATE){
					table.getColumn(i).setDateFormat("dd/MM/YYYY");
		  		 }
		    }
			table.refresh();
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
	         System.out.println("SQLState: " + e.getSQLState());
	         System.out.println("VendorError: " + e.getErrorCode());
	         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
	                                       e.getMessage() + "\n", 
	                                       "Error al ejecutar la consulta.",
	                                       JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void otraConsulta() {
		try {
			String selection= this.query_text_area.getText().trim();
			Statement statement = conexionDataBase.createStatement();
			statement.execute(selection);
			statement.close();
			list_table_names.setModel(getListTableNames());
			list_table_names.setSelectedIndex(-1);
			scrollPane_attributes.setVisible(false);
			if(table.getSelectSql()!=null)
				table.refresh();
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    ex.getMessage() + "\n", 
                    "Error al ejecutar la consulta.",
                    JOptionPane.ERROR_MESSAGE);
		                                       
		}
	}
	
	private class ListListener implements ListSelectionListener {
	    public void valueChanged(ListSelectionEvent e) {
	    	String s= list_table_names.getSelectedValue(); 
	    	DefaultListModel<String> model = new DefaultListModel<String>();
			if(conexionDataBase!=null) {
			try {
				if(s!=null) {
					//Muestra los atributos de la tabla seleccionada
					String selection = "DESCRIBE " + s;
					Statement statement = conexionDataBase.createStatement();
					ResultSet result = statement.executeQuery(selection);
					
					while(result.next()){
						model.addElement(result.getString("Field"));
					}
					scrollPane_attributes.setVisible(true);
				}
		        
			} catch (SQLException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			}
	        scrollPane_attributes.setVisible(true);
	        list_table_attributes.setModel(model);
	    }
	}
	
	//Muestra los paneles necesarios
	public void setMostrar(boolean b) {
		this.setVisible(b);
		query_text_area.setText("");
	}
	
}
