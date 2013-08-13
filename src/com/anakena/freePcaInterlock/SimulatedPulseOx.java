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
public class SimulatedPulseOx extends JFrame implements ActionListener {
	
	private static Color green = new Color(10, 210, 20);
	private static Color red = new Color(215, 0, 0);
	private static Color purple = new Color(166, 35, 109);
	private static int defaultWidth = 400;
	private static int defaultHeight = 300;
	
	InitialJFrame application;
	
	private SimulatedPatient patient;
	private PulseOxGenerateThread generator;
	private Spawner spawner;
	
	private JButton Q2;
	private JButton patientConnect;
	private JLabel title;
	private boolean associatedQ2;
	private boolean connected;
	private boolean spawned;
	
	
	public SimulatedPulseOx(SimulatedPatient patient, Spawner spawn, InitialJFrame app)
	{
		super("Simulated PulseOx");
		this.patient = patient;
		application = app;
		spawner = spawn;
		
		title = new JLabel("   Simulated PulseOx");
		title.setFont(new Font("Arial", Font.PLAIN, 38));
		title.setForeground(Color.WHITE);
		title.setBackground(Color.BLACK);
		setSize(defaultWidth, defaultHeight);
		getContentPane().setBackground(Color.BLACK);
		setLayout(new GridLayout(3, 1));
		
		Q2 = new JButton("Q2 - NOT ASSOCIATED (Press to Associate over Q2)");
		Q2.addActionListener(this);
		associatedQ2 = false;
		Q2.setBackground(red);
		Q2.setFocusPainted(false);
		
		patientConnect = new JButton("Connect to Patient");
		patientConnect.addActionListener(this);
		patientConnect.setBackground(purple);
		patientConnect.setFocusPainted(false);
		connected = false;
		
		spawned = false;
		
		add(title);
		add(Q2);
		add(patientConnect);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		WindowAdapter exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(null, "Are You Sure You Want To Close Simulated PulseOx?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                   generator.interrupt();
                   spawner.pulseOximeterClosed();
                   if (application.isQ2Associated().equals(true))
                	   application.toggleQ2();
                   dispose();
                }
            }
        };
        addWindowListener(exitListener);
	}
	
	public void activate()
	{
		setVisible(true);
		generator = new PulseOxGenerateThread();
		generator.start();
	}
	
	private class PulseOxGenerateThread extends Thread 
	{
		
		PulseOxGenerateThread()
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
				
				if (connected)
				{	
					application.getO2Panel().updateValue(patient.getOxygenSaturation());
					application.getPulseRatePanel().updateValue(patient.getPulseRate());
					application.getPlethPanel().updateValue(patient.getPlethysmograph().get(0).intValue()%1000);
				}
				else
				{
					application.getO2Panel().updateValue("X");
					application.getPulseRatePanel().updateValue("X");
					application.getPlethPanel().updateValue("X");
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
		if (button.equals(Q2))
		{
			Boolean applicationQ2 = application.isQ2Associated();
			if (associatedQ2 == false && applicationQ2.equals(false))
			{
				Q2.setText("Q2 - ASSOCIATED (Click to Disassociate over Q2)");
				Q2.setBackground(green);
				associatedQ2 = true;
				application.toggleQ2();
			}
			else if (associatedQ2 == true && applicationQ2.equals(true))
			{
				Q2.setText("Q2 - NOT ASSOCIATED (Click to Associate over Q2");
				Q2.setBackground(red);
				associatedQ2 = false;
				application.toggleQ2();
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
