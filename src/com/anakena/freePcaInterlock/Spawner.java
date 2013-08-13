package com.anakena.freePcaInterlock;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class Spawner extends JFrame implements ActionListener {
	
	//private static Color green = new Color(10, 210, 20);
	//private static Color red = new Color(215, 0, 0);
	//private static Color purple = new Color(166, 35, 109);
	
	private static Color pulseOximeterColor = Color.orange;
	private static Color electrocardiogramColor = new Color(255, 51, 153);
	private static Color infusionPumpColor = new Color(0, 128, 255);
	private static int defaultWidth = 400;
	private static int defaultHeight = 500;
	
	InitialJFrame application;
	SimulatedPatient patient;
	
	private JLabel title;
	
	private ArrayList<JFrame> pulseOximeters;
	private ArrayList<JFrame> electrocardiograms;
	private ArrayList<JFrame> infusionPumps;
	private ArrayList<JButton> pulseOximeterButtons;
	private ArrayList<JButton> electrocardiogramButtons;
	private ArrayList<JButton> infusionPumpButtons;
	
	private boolean pulseOximeterIsSpawned;
	private boolean electrocardiogramIsSpawned;
	private boolean infusionPumpIsSpawned;
	
	public Spawner(InitialJFrame application)
	{
		super("Spawner");
		this.application = application;
		patient = new SimulatedPatient();
		patient.activate();
		
		PatientFrame patientFrame = new PatientFrame(patient);
		patientFrame.activate();
		
		title = new JLabel("          Spawner");
		title.setFont(new Font("Arial", Font.PLAIN, 38));
		title.setForeground(Color.WHITE);
		title.setBackground(Color.BLACK);
		setSize(defaultWidth, defaultHeight);
		getContentPane().setBackground(Color.BLACK);
		setLayout(new GridLayout(10, 1));
		
		add(title);
		
		pulseOximeters = new ArrayList<JFrame>();
		infusionPumps = new ArrayList<JFrame>();
		electrocardiograms = new ArrayList<JFrame>();
		
		SimulatedPulseOx pulseOximeter1 = new SimulatedPulseOx(patient, this, application);
		pulseOximeters.add(pulseOximeter1);
		
		SimulatedECG2 electrocardiogram1 = new SimulatedECG2(patient, this, application);
		electrocardiograms.add(electrocardiogram1);
		
		SimulatedIVPump2 infusionPump1 = new SimulatedIVPump2(this, application);
		infusionPumps.add(infusionPump1);
		
		pulseOximeterButtons = new ArrayList<JButton>();
		infusionPumpButtons = new ArrayList<JButton>();
		electrocardiogramButtons = new ArrayList<JButton>();
		
		pulseOximeterIsSpawned = false;
		electrocardiogramIsSpawned = false;
		infusionPumpIsSpawned = false ;
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		WindowAdapter exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	
            	UIManager.put("OptionPane.background", Color.black);
            	UIManager.put("Panel.background", Color.BLACK);
            	//UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 20));
            	UIManager.put("OptionPane.messageFont", new Font("Arial", Font.ITALIC, 20));
            	Icon yesIcon = new ImageIcon("src/com/anakena/freePcaInterlock/Yes.png", null);
            	//UIManager.
            	//UIManager.put("OptionPane.buttonAreaBorder", BorderFactory.createLineBorder(Color.green));
            	//UIManager.put("OptionPane.buttonPadding", 100);
            	UIManager.put("OptionPane.yesIcon", yesIcon);
            	UIManager.put("OptionPane.yesButtonText", "");
            	
            	
            	final JButton conButton = new JButton("Yes");
            	conButton.setBackground(Color.ORANGE);
            	Object[] buttonRowObjects = new Object[] {
            	  conButton, "Cancel"
            	};
            	
                int confirm = JOptionPane.showOptionDialog(null, "Closing the" +
                		" spawner application will result in shutting down the system.  \nContinue?"
                		, "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                   dispose();
                   System.exit(0);
                }
            }
        };
        addWindowListener(exitListener);
	}
	
	public void activate()
	{
		for (JFrame appOrDevice : pulseOximeters)
		{
			if (appOrDevice.getClass() == SimulatedPulseOx.class)
			{
				JButton button = new JButton(((SimulatedPulseOx)appOrDevice).getTitle());
				pulseOximeterButtons.add(button);
				button.setFocusPainted(false);
				button.setBackground(pulseOximeterColor);
				add(button);
				button.addActionListener(this);
			}
			else 
			{
				System.err.println("Spawner Activation Error : Unrecognized Pulse Oximeter");
				System.exit(0);
			}
		}
		
		for (JFrame appOrDevice : electrocardiograms)
		{
			if (appOrDevice.getClass() == SimulatedECG2.class)
			{
				JButton button = new JButton(((SimulatedECG2)appOrDevice).getTitle());
				electrocardiogramButtons.add(button);
				button.setFocusPainted(false);
				button.setBackground(electrocardiogramColor);
				add(button);
				button.addActionListener(this);
			}
			else
			{
				System.err.println("Spawner Activation Error : Unrecognized Electrocardiogram");
				System.exit(0);
			}
		}
		
		for (JFrame appOrDevice : infusionPumps)
		{
			if (appOrDevice.getClass() == SimulatedIVPump2.class)
			{
				JButton button = new JButton(((SimulatedIVPump2)appOrDevice).getTitle());
				infusionPumpButtons.add(button);
				button.setFocusPainted(false);
				button.setBackground(infusionPumpColor);
				add(button);
				button.addActionListener(this);
			}
			else
			{
				System.err.println("Spawner Activation Error : Unrecognized Infusion Pump");
				System.exit(0);
			}
		}
		
		setVisible(true);
	}

	public void pulseOximeterClosed()
	{
		pulseOximeterIsSpawned = false;
	}
	
	public void electrocardiogramClosed()
	{
		electrocardiogramIsSpawned = false;
	}
	
	public void infusionPumpClosed()
	{
		infusionPumpIsSpawned = false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		JButton actionButton = (JButton)e.getSource();
		boolean identified = false;
		
		
		if (identified == false)
		{
			Iterator<JButton> ibutton = pulseOximeterButtons.iterator();
			int index = -1;
			
			while (ibutton.hasNext())
			{
				JButton pulseOxButton = (JButton) ibutton.next();
				index++;
				if (actionButton.equals(pulseOxButton))
				{
					identified = true;
					if (pulseOximeterIsSpawned == true)
					{
						JOptionPane.showMessageDialog(this, "You must exit the currently running pulse oximeter to spawn a new one.");
						break;
					}
					pulseOximeterIsSpawned = true;
					JFrame iPulseOx = pulseOximeters.get(index);
					if (iPulseOx.getClass() == SimulatedPulseOx.class)
					{
						iPulseOx = (JFrame) new SimulatedPulseOx(patient, this, application);
						((SimulatedPulseOx)iPulseOx).activate();
						break;
					}
					else
					{
						System.err.println("Spawner Action Error : Unrecognized Device Type Selection");
						System.exit(0);
					}
				}
			}
		}
		
		if (identified == false)
		{
			Iterator<JButton> ibutton = electrocardiogramButtons.iterator();
			int index = -1;
			
			while (ibutton.hasNext())
			{
				JButton ecgButton = (JButton) ibutton.next();
				index++;
				if (actionButton.equals(ecgButton))
				{
					identified = true;
					if (electrocardiogramIsSpawned == true)
					{
						JOptionPane.showMessageDialog(this, "You must exit the currently running electrocardiogram to spawn a new one.");
						break;
					}
					electrocardiogramIsSpawned = true;
					JFrame iECG = electrocardiograms.get(index);
					if (iECG.getClass() == SimulatedECG2.class)
					{
						iECG = (JFrame) new SimulatedECG2(patient, this, application);
						((SimulatedECG2)iECG).activate();
						break;
					}
					else
					{
						System.err.println("Spawner Action Error : Unrecognized Device Type Selection");
						System.exit(0);
					}
				}
			}
		}
		
		if (identified == false)
		{
			Iterator<JButton> ibutton = infusionPumpButtons.iterator();
			int index = -1;
			
			while (ibutton.hasNext())
			{
				JButton infusionButton = (JButton) ibutton.next();
				index++;
				if (actionButton.equals(infusionButton))
				{
					identified = true;
					if (infusionPumpIsSpawned == true)
					{
						JOptionPane.showMessageDialog(this, "You must exit the currently running infusion pump to spawn a new one.");
						break;
					}
					infusionPumpIsSpawned = true;
					JFrame iPump = infusionPumps.get(index);
					if (iPump.getClass() == SimulatedIVPump2.class)
					{
						iPump = (JFrame) new SimulatedIVPump2(this, application);
						((SimulatedIVPump2)iPump).activate();
						break;
					}
					else
					{
						System.err.println("Spawner Action Error : Unrecognized Device Type Selection");
						System.exit(0);
					}
				}
			}
		}
	}
}
