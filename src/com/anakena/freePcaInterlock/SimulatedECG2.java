package com.anakena.freePcaInterlock;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class SimulatedECG2 extends JFrame implements ActionListener {
	
	private static Color green = new Color(10, 210, 20);
	private static Color red = new Color(215, 0, 0);
	private static Color purple = new Color(166, 35, 109);
	private static int defaultWidth = 400;
	private static int defaultHeight = 300;
	
	InitialJFrame application;
	
	private SimulatedPatient patient;
	private ecgGenerateThread generator;
	private Spawner spawner;
	
	private JButton Q3;
	private JButton patientConnect;
	private JLabel title;
	private boolean associatedQ3;
	private boolean connected;
	
	private boolean spawned;
	
	
	public SimulatedECG2(SimulatedPatient patient, Spawner spawn, InitialJFrame app)
	{
		super("Simulated ECG");
		this.patient = patient;
		this.application = app;
		spawner = spawn;
		
		title = new JLabel("   Simulated ECG");
		title.setFont(new Font("Arial", Font.PLAIN, 38));
		title.setForeground(Color.WHITE);
		title.setBackground(Color.BLACK);
		setSize(defaultWidth, defaultHeight);
		getContentPane().setBackground(Color.BLACK);
		setLayout(new GridLayout(3, 1));
		
		Q3 = new JButton("Q3 - NOT ASSOCIATED (Press to Associate over Q3)");
		Q3.addActionListener(this);
		associatedQ3 = false;
		Q3.setBackground(red);
		Q3.setFocusPainted(false);
		
		patientConnect = new JButton("Connect to Patient");
		patientConnect.addActionListener(this);
		patientConnect.setBackground(purple);
		patientConnect.setFocusPainted(false);
		connected = false;
		
		spawned = false;
		
		add(title);
		add(Q3);
		add(patientConnect);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		WindowAdapter exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(null, "Are You Sure You Want To Close Simulated ECG?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                   generator.interrupt();
                   spawner.electrocardiogramClosed();
                   if (application.isQ3Associated().equals(true))
                	   application.toggleQ3();
                   dispose();
                }
            }
        };
        addWindowListener(exitListener);
	}
	
	public void activate()
	{
		setVisible(true);
		generator = new ecgGenerateThread();
		generator.start();
	}
	
	private class ecgGenerateThread extends Thread 
	{	
		ecgGenerateThread()
		{

		}
		
		public void run()
		{
			while (!Thread.currentThread().isInterrupted())
			{
				try {
				    Thread.sleep(250);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				    break;
				}
				
				if (connected && associatedQ3)
				{	
					application.getHeartRatePanel().updateValue(patient.getHeartRate());
				}
				else
				{
					application.getHeartRatePanel().updateValue("X");
				}
			}
		}
	}
	
	public void setSpawned(boolean spawned)
	{
		this.spawned = spawned;
	}
	
	public boolean isSpawned()
	{
		return spawned;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		if (button.equals(Q3))
		{
			Boolean applicationQ3 = application.isQ3Associated();
			if (associatedQ3 == false && applicationQ3.equals(false))
			{
				Q3.setText("Q3 - ASSOCIATED (Click to Disassociate over Q3)");
				Q3.setBackground(green);
				associatedQ3 = true;
				application.toggleQ3();
			}
			else if (associatedQ3 == true && applicationQ3.equals(true))
			{
				Q3.setText("Q3 - NOT ASSOCIATED (Click to Associate over Q3");
				Q3.setBackground(red);
				associatedQ3 = false;
				application.toggleQ3();
			}
			else { /*DO NOTHING*/ }
		}
		else if (button.equals(patientConnect))
		{
			if (connected == true)
			{
				connected = false;
				patientConnect.setText("Connect to Patient");
			}
			else if (connected == false)
			{
				connected = true;
				patientConnect.setText("Disconnect Patient");
			}
		}
	}
}
