package Funcoes;

import javax.swing.JPanel;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import javax.swing.border.EtchedBorder;
import javax.swing.JScrollPane;


public class Modelojanela extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
       	private JTextArea textEntrada1;
        private JTextArea textEntrada2;
	public Modelojanela() {
            
        setTitle("Algoritmo");
        setSize(250, 250);
        
        // Cria uma barra de menu para o JFrame
        JMenuBar menuBar = new JMenuBar();
        
        // Adiciona a barra de menu ao  frame
        setJMenuBar(menuBar);
        
        // Define e adiciona dois menus drop down na barra de menus
        JMenu fileMenu = new JMenu("Arquivo");
        JMenu editMenu = new JMenu("Ferramentas");
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        
        // Cria e adiciona um item simples para o menu
        JMenuItem newAction = new JMenuItem("Abrir");
        
        JMenuItem salvaAction = new JMenuItem("Salvar");
        
        JMenuItem exitAction = new JMenuItem("Sair");
        
        //Ferramentas-----//
        
        JMenuItem newAction1 = new JMenuItem("AFN-AFD");
        
        JMenuItem newAction2 = new JMenuItem("Minimização");
        
        JMenuItem newAction3 = new JMenuItem("Produto");
        
        JMenuItem newAction4 = new JMenuItem("Vizualizar");
        
        JMenuItem newAction5 = new JMenuItem("TRIM");
        
        JMenuItem newAction6 = new JMenuItem("Comp_Paralela");
        
        
        
        // Cria e aiciona um CheckButton como um item de menu
       // JCheckBoxMenuItem checkAction = new JCheckBoxMenuItem("Check Action");
        
		newAction1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Modelo.AFN_AFD(textEntrada1.getText());
			}
		});
                
                
		newAction2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Modelo.mimimizar(textEntrada1.getText());
			}
		});
                
                
		newAction3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Modelo.produto(textEntrada1.getText(), textEntrada2.getText());
			}
		});
                
                
		newAction4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Modelo.visualizarEntrada(textEntrada1.getText(), textEntrada2.getText());
			}
		});
                
                newAction5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Modelo.TRIM(textEntrada1.getText());
			}
		});
                
                newAction6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Modelo.comp_Paralela(textEntrada1.getText(), textEntrada2.getText());
			}
		});
		
		
        
       
        
       
        ButtonGroup bg = new ButtonGroup();
        fileMenu.add(newAction);
        fileMenu.add(salvaAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        editMenu.add(newAction1);
        editMenu.add(newAction2);
        editMenu.add(newAction3);
        editMenu.add(newAction4);
        editMenu.add(newAction5);
        editMenu.add(newAction6);

       
		getContentPane().setLayout(null);
		setSize(1000, 350);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 20, 950, 436);
		getContentPane().add(panel);
		panel.setLayout(null);
         
                
                                
                textEntrada1 = new JTextArea();
		textEntrada1.setFont(new Font("Monospaced", Font.BOLD, 13));
		textEntrada1.setText(
				"T = {a, b, c}\n" +
				"Q = {Q0, Q1, Q2}\n" +
				"P(Q0, a) = Q1\n" +
                                "P(Q0, a) = Q2\n" +
				"P(Q1, b) = Q2\n" +
                                "P(Q2, c) = Q2\n" +
				"q0 = Q0\n" +
				"F = {Q1, Q2}");
                textEntrada1.setBounds(10, 36, 444, 183);
                
		JScrollPane scrollPane = new JScrollPane(textEntrada1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 36, 444, 183);
		panel.add(scrollPane);
                
        
		
		textEntrada2 = new JTextArea();
		panel.add(textEntrada2);
		textEntrada2.setFont(new Font("Monospaced", Font.BOLD, 13));
		textEntrada2.setText(
				"T = {a, b}\n" +
				"Q = {Q0, Q1, Q2}\n" +
				"P(Q0, a) = Q2\n" +
				"P(Q0, b) = Q1\n" +
				"q0 = Q0\n" +
				"F = {Q1, Q2}");
		textEntrada2.setBounds(10, 351, 444, 183);

		JScrollPane scrollPane_1 = new JScrollPane(textEntrada2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
 		scrollPane_1.setBounds(470, 36, 455, 183);
 		panel.add(scrollPane_1);


                
                           
                               
		JLabel lblEntrada = new JLabel("Entrada1");
		lblEntrada.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEntrada.setBounds(10, 11, 136, 14);
		panel.add(lblEntrada);
		
                
                JLabel lblEntrada2 = new JLabel("Automato2");
		lblEntrada2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEntrada2.setBounds(480, 11, 136, 14);
		panel.add(lblEntrada2);
	}
}
