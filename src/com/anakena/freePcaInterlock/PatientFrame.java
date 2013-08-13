package com.anakena.freePcaInterlock;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class PatientFrame extends JFrame implements ActionListener {
	
	private static Color green = new Color(10, 210, 20);
	private static Color red = new Color(215, 0, 0);
	private static int defaultWidth = 400;
	private static int defaultHeight = 200;

	private SimulatedPatient patient;
	
	private JButton distress;
	private boolean distressed;
	private JLabel title;
	
	
	public PatientFrame(SimulatedPatient patient)
	{
		super("Simulated Patient");
		this.patient = patient;
		
		title = new JLabel("   Simulated Patient");
		title.setFont(new Font("Arial", Font.PLAIN, 38));
		title.setForeground(Color.WHITE);
		title.setBackground(Color.BLACK);
		setSize(defaultWidth, defaultHeight);
		getContentPane().setBackground(Color.BLACK);
		setLayout(new GridLayout(2, 1));
		
		distress = new JButton("=)");
		distress.addActionListener(this);
		distress.setBackground(green);
		distress.setFocusPainted(false);
		distressed = false;
		
		add(title);
		add(distress);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	public void activate()
	{
		setVisible(true);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		if (button.equals(distress))
		{
			if (distressed == true)
			{
				patient.setDistressed(false);
				distressed = false;
				distress.setText("=)");
				distress.setBackground(green);
			}
			else if (distressed == false)
			{
				patient.setDistressed(true);
				distressed = true;
				distress.setText("=(");
				distress.setBackground(red);
			}
		}
	}
}
