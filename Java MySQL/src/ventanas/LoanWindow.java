package ventanas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.CardLayout;

public class LoanWindow extends JInternalFrame {
	private static final long serialVersionUID = 1L;

	private JPanel content_pane, new_loan, payment_fees, defaulters;
	private String legajo;
	
	/**
	 * Create the frame.
	 */
	public LoanWindow() {
		setBounds(100, 100, 450, 300);
        this.setTitle("Prestamos");
        BorderLayout thisLayout = new BorderLayout();
        getContentPane().setLayout(thisLayout);
        
        setVisible(true);
        
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.WEST);
        panel.setLayout(new GridLayout(7, 1, 0, 10));
        
        LoanListener l = new LoanListener();
        
        JButton button_new_loan = new JButton("Crear pr\u00E9stamo");
        button_new_loan.setBackground(Color.LIGHT_GRAY);
        button_new_loan.setFocusable(false);
        button_new_loan.addActionListener(l);
        panel.add(button_new_loan);
        
        JButton button_payment_fees = new JButton("Pago de cuota");
        button_payment_fees.setBackground(Color.LIGHT_GRAY);
        button_payment_fees.setFocusable(false);
        button_payment_fees.addActionListener(l);
        panel.add(button_payment_fees);
        
        JButton button_defaulters = new JButton("Clientes morosos");
        button_defaulters.setBackground(Color.LIGHT_GRAY);
        button_defaulters.setFocusable(false);
        button_defaulters.addActionListener(l);
        panel.add(button_defaulters);
        
        content_pane = new JPanel();
        
        new_loan = new NewLoan(this);
        payment_fees = new PaymentOfFees();
        defaulters = new Defaulters();
		content_pane.setLayout(new CardLayout(0, 0));
        
		content_pane.add(new_loan, "name_119829474014624");
		content_pane.add(payment_fees, "name_119829488438229");
		content_pane.add(defaulters, "name_119829501624118");
		
		new_loan.setVisible(false);
		payment_fees.setVisible(false);
		defaulters.setVisible(false);
		
        getContentPane().add(content_pane, BorderLayout.CENTER);
        this.setClosable(true);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
	
	private class LoanListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String s = e.getActionCommand();
			switch(s){
				case("Crear pr\u00E9stamo"):{
					((NewLoan) new_loan).pasarLegajo(legajo);
					((NewLoan) new_loan).setMostrar(true);
					((PaymentOfFees) payment_fees).setMostrar(false);
					((Defaulters) defaulters).setMostrar(false);
					break;
				}
				case("Pago de cuota"):{
					((PaymentOfFees) payment_fees).setMostrar(true);
					((NewLoan) new_loan).setMostrar(false);
					((Defaulters) defaulters).setMostrar(false);
					break;
				}
				case("Clientes morosos"):{
					((Defaulters) defaulters).setMostrar(true);
					((NewLoan) new_loan).setMostrar(false);
					((PaymentOfFees) payment_fees).setMostrar(false);
					break;
				}
			}
		}
	}
	
	//Muestra los paneles necesarios
	public void setMostrar(boolean b) {
		this.setVisible(b);
		((NewLoan) new_loan).setMostrar(false);
		((PaymentOfFees) payment_fees).setMostrar(false);
		((Defaulters) defaulters).setMostrar(false);
	}
	
	//Recibe el número de legajo del empleado que se logueo
	public void pasarLegajo(String legajo) {
		this.legajo=legajo;
	}

}
