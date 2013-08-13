package com.anakena.freePcaInterlock;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.anakena.freePcaInterlock.Enumeration.Disassociation;
import com.anakena.freePcaInterlock.Enumeration.IVMode;
import com.anakena.freePcaInterlock.Enumeration.WarningType;
import com.anakena.device.infusion.pump.PumpMode;
import com.anakena.device.infusion.pump.PumpStatus;

@SuppressWarnings("serial")
public class SimulatedIVPump2 extends JFrame implements ActionListener {
	
	private static Color green = new Color(102, 204, 0);
	private static Color regular = new Color(51, 153, 255);
	private static int defaultWidth = 450;
	private static int defaultHeight = 600;
	
	private JButton Q4;
	private JButton Q5;
	private JButton Active;
	private JButton Standby;
	private JButton Manual;
	private JButton MStart;
	private JButton MStop;
	private JLabel title;
	private Association Q4AssociationStatus = new Association();
	private Association Q5AssociationStatus = new Association();
	private PumpMode ModeStatus = PumpMode.STANDBY;
	private IVMode requestedMode = IVMode.NULL;
	private boolean stop;
	private boolean stopped;
	private boolean modeLock;
	private JLabel pumpNotice;
	private WarningType warningType;
	private Disassociation requestedDisassociation;
	
	Spawner spawner;
	boolean spawned;
	InitialJFrame application;
	InfusionPumpGenerateThread generator;
	
	SimulatedIVPump2(Spawner spawn, InitialJFrame app)
	{
		super("Gamma Infusion Pump");
		title = new JLabel("   Gamma Infusion Pump");
		title.setFont(new Font("Arial", Font.PLAIN, 32));
		title.setForeground(Color.WHITE);
		title.setBackground(Color.BLACK);
		setSize(defaultWidth, defaultHeight);
		getContentPane().setBackground(Color.BLACK);
		setLayout(new GridLayout(7, 1));
		
		Standby = new JButton("STANDBY");
		Standby.setBackground(green);
		Standby.setFocusPainted(false);
		Standby.addActionListener(this);
		
		Active = new JButton("INTERLOCK");
		Active.setBackground(regular);
		Active.setFocusPainted(false);
		Active.addActionListener(this);
		
		Manual = new JButton("MANUAL");
		Manual.setBackground(regular);
		Manual.setFocusPainted(false);
		Manual.addActionListener(this);
		
		Q4 = new JButton("Q4 - NOT ASSOCIATED (Click to Associate)");
		Q4.setBackground(regular);
		Q4.setFocusPainted(false);
		Q4AssociationStatus.Q = false;
		Q4.addActionListener(this);
		
		Q5 = new JButton("Q5 - NOT ASSOCIATED (Click to Associate)");
		Q5.setBackground(regular);
		Q5.setFocusPainted(false);
		Q5AssociationStatus.Q = false;
		Q5.addActionListener(this);
		
		MStart = new JButton("X");
		MStop = new JButton("X");
		MStart.setBackground(Color.GRAY);
		MStop.setBackground(Color.GRAY);
		MStart.setFocusPainted(false);
		MStop.setFocusPainted(false);
		MStart.addActionListener(this);
		MStop.addActionListener(this);
		
		JPanel manualContainer = new JPanel();
		manualContainer.setLayout(new GridLayout(1, 3));
		manualContainer.setBackground(Color.BLACK);
		manualContainer.add(Manual);
		manualContainer.add(MStart);
		manualContainer.add(MStop);
		
		ModeStatus = PumpMode.STANDBY;
		stop = true;
		stopped = false;
		modeLock = false;
		
		pumpNotice = new JLabel(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
		pumpNotice.setFont(new Font("Arial", Font.PLAIN, (int)(15)));
		pumpNotice.setForeground(Color.YELLOW);
		pumpNotice.setBackground(Color.BLACK);

		JPanel pumpNoticeContainer = new JPanel();
		pumpNoticeContainer.setLayout(new BoxLayout(pumpNoticeContainer, BoxLayout.X_AXIS));
		pumpNoticeContainer.setBackground(Color.BLACK);
		pumpNoticeContainer.add(Box.createHorizontalGlue());
		pumpNoticeContainer.add(pumpNotice);
		pumpNoticeContainer.add(Box.createHorizontalGlue());
		
		add(title);
		add(Q4);
		add(Q5);
		add(Active);
		add(Standby);
		add(manualContainer);
		add(pumpNoticeContainer);
		
		warningType = WarningType.NULL;
		requestedMode = IVMode.NULL;
		requestedDisassociation = Disassociation.NULL;
		
		application = app;
		spawner = spawn;
		spawned = false;
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		WindowAdapter exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(null, "Are You Sure You Want To Close Simulated IV Pump?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                   generator.interrupt();
                   spawner.infusionPumpClosed();
                   if (application.isQ4Associated().equals(true))
                	   application.toggleQ4();
                   if (application.isQ5Associated().equals(true))
                	   application.toggleQ5();
                   dispose();
                }
            }
        };
        addWindowListener(exitListener);
	}
	
	public void activate()
	{
		generator = new InfusionPumpGenerateThread(this);
		generator.start();
		setVisible(true);
		spawned = true;
	}
	
	public boolean isStopped()
	{
		return stopped;
	}
	
	public boolean isInactive()
	{
		return stop;
	}
	
	public void engageInterlock()
	{
		if (modeLock == false && ModeStatus.equals(PumpMode.INTERLOCK))
		{
			modeLock = true;
			stop = false;
			stopped = false;
			pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA On : Stopped" : "PCA On"));
		}
	}
	
	public void processStop()
	{
		if (modeLock == true && ModeStatus.equals(PumpMode.INTERLOCK))
		{
			stop = true;
			stopped = true;
			pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA On : Stopped" : "PCA On"));
		}
	}
	
	public void actionPerformed(ActionEvent e) {

		JButton button = (JButton)e.getSource();
		if (button.equals(Q5))
		{
			if (Q5AssociationStatus.Q == false)
			{
				int confirm = JOptionPane.showOptionDialog(null, warningMessageQ5, "Association Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0)
                {
					Q5.setText("Q5 - ASSOCIATED (Click to Disassociate)");
					Q5.setBackground(green);
					Q5AssociationStatus.Q = true;
					if (application.isQ5Associated().equals(false))
						application.toggleQ5();
                }
			}
			else if (Q5AssociationStatus.Q == true)
			{
				if (ModeStatus.equals(PumpMode.INTERLOCK) && modeLock == true)
				{
					warningType = WarningType.DISS;
					requestedDisassociation = Disassociation.Q5;
					int confirm = JOptionPane.showOptionDialog(null, warningMessageDISS, "Disassociation Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
	                if (confirm == 0)
	                	handle(true);
	                else handle(false);
				}
				else 
				{
					Q5.setText("Q5 - NOT ASSOCIATED (Click to Associate)");
					Q5.setBackground(regular);
					Q5AssociationStatus.Q = false;
					if (application.isQ5Associated().equals(true))
						application.toggleQ5();
				}
			}
		}
		else if (button.equals(Q4))
		{
			if (Q4AssociationStatus.Q == false)
			{
				Q4.setText("Q4 - ASSOCIATED (Click to Disassociate)");
				Q4.setBackground(green);
				Q4AssociationStatus.Q = true;
				if (application.isQ4Associated().equals(false))
					application.toggleQ4();
				pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
			}
			else if (Q4AssociationStatus.Q == true)
			{	
				if (ModeStatus.equals(PumpMode.INTERLOCK) && modeLock == true)
				{
					warningType = WarningType.DISS;
					requestedDisassociation = Disassociation.Q4;
					int confirm = JOptionPane.showOptionDialog(null, warningMessageDISS, "Disassociation Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
	                if (confirm == 0)
	                	handle(true);
	                else handle(false);
				}
				else
				{
					Q4.setText("Q4 - NOT ASSOCIATED (Click to Associate)");
					Q4.setBackground(regular);
					Q4AssociationStatus.Q = false;
					if (application.isQ4Associated().equals(true))
						application.toggleQ4();
					pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
				}
			}
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		else if (button.equals(Standby))
		{
			if (ModeStatus == PumpMode.INTERLOCK)
			{
				if (modeLock == false)
				{
					Standby.setBackground(green);
					Manual.setBackground(regular);
					Active.setBackground(regular);
					ModeStatus = PumpMode.STANDBY;
					stop = true;
					stopped = false;
					pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
				}
				else if (modeLock == true)
				{
					warningType = WarningType.SWITCH;
					requestedMode = IVMode.STANDBY;
					int confirm = JOptionPane.showOptionDialog(null, warningMessageSWITCH, "Switch Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
	                if (confirm == 0)
	                	handle(true);
	                else handle(false);
				}
			}
			else if (ModeStatus == PumpMode.MANUAL)
			{
				if (modeLock == false)
				{
					Standby.setBackground(green);
					Manual.setBackground(regular);
					Active.setBackground(regular);
					ModeStatus = PumpMode.STANDBY;
					stop = true;
					stopped = false;
					pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
					MStart.setBackground(Color.GRAY);
					MStart.setText("X");
					MStop.setBackground(Color.GRAY);
					MStop.setText("X");
				}
				else if (modeLock == true)
				{
					warningType = WarningType.SWITCH;
					requestedMode = IVMode.STANDBY;
					int confirm = JOptionPane.showOptionDialog(null, warningMessageSWITCH, "Switch Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
	                if (confirm == 0)
	                	handle(true);
	                else handle(false);
				}
			}
			else { /* DO NOTHING */ }
			
		}
		else if (button.equals(Manual))
		{
			if (ModeStatus == PumpMode.INTERLOCK)
			{
				if (modeLock == false)
				{
					Standby.setBackground(regular);
					Manual.setBackground(green);
					Active.setBackground(regular);
					ModeStatus = PumpMode.MANUAL;
					stop = true;
					stopped = false;
					pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
					MStart.setBackground(regular);
					MStart.setText("Administer Dose");
				}
				else if (modeLock == true)
				{
					warningType = WarningType.SWITCH;
					requestedMode = IVMode.MANUAL;
					int confirm = JOptionPane.showOptionDialog(null, warningMessageSWITCH, "Switch Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
	                if (confirm == 0)
	                	handle(true);
	                else handle(false);
				}
			}
			else if (ModeStatus == PumpMode.STANDBY)
			{
				Standby.setBackground(regular);
				Manual.setBackground(green);
				Active.setBackground(regular);
				ModeStatus = PumpMode.MANUAL;
				stop = true;
				stopped = false;
				pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
				MStart.setBackground(regular);
				MStart.setText("Administer Dose");
			}
		}
		else if (button.equals(Active))
		{
			if (ModeStatus == PumpMode.MANUAL)
			{
				if (modeLock == false)
				{
					Standby.setBackground(regular);
					Manual.setBackground(regular);
					Active.setBackground(green);
					ModeStatus = PumpMode.INTERLOCK;
					stop = true;
					stopped = false;
					pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
					MStart.setBackground(Color.GRAY);
					MStart.setText("X");
					MStop.setBackground(Color.GRAY);
					MStop.setText("X");
				}
				else if (modeLock == true)
				{
					warningType = WarningType.SWITCH;
					requestedMode = IVMode.INTERLOCK;
					int confirm = JOptionPane.showOptionDialog(null, warningMessageSWITCH, "Switch Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
	                if (confirm == 0)
	                	handle(true);
	                else handle(false);
				}
			}
			else if (ModeStatus == PumpMode.STANDBY)
			{
				Standby.setBackground(regular);
				Manual.setBackground(regular);
				Active.setBackground(green);
				ModeStatus = PumpMode.INTERLOCK;
				stop = true;
				stopped = false;
				pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
			}
		}
		else if (button.equals(MStart))
		{
			if (ModeStatus == PumpMode.MANUAL)
			{
				if (modeLock == false)
				{
					modeLock = true;
					stop = false;
					stopped = false;
					MStart.setText("Quit");
					MStart.setBackground(regular);
					MStop.setText("Command : Stop");
					MStop.setBackground(regular);
					Standby.setBackground(Color.GRAY);
					Active.setBackground(Color.GRAY);
				}
				else if (modeLock == true)
				{
					modeLock = false;
					stop = true;
					stopped = false;
					MStart.setText("Administer Dose");
					MStart.setBackground(regular);
					MStop.setText("X");
					MStop.setBackground(Color.GRAY);
					Standby.setBackground(regular);
					Active.setBackground(regular);
				}
				pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
			}
			else { /*DO NOTHING*/ }
		}
		else if (button.equals(MStop))
		{
			if (ModeStatus == PumpMode.MANUAL && modeLock == true)
			{
				if (stop == true)
				{
					stop = false;
					MStop.setText("Command : Stop");
					MStop.setBackground(regular);
					pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
					stopped = false;
				}
				else if (stop == false)
				{
					stop = true;
					MStop.setText("Command : Start");
					MStop.setBackground(regular);
					pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA On : Stopped" : "PCA On"));
					stopped = true;
				}
			}
			else { /*DO NOTHING*/ }
		}
		else 
		{
			//DO NOTHING
			System.out.println("ERROR! 000");
		}
	}
	
	
	private void handle(boolean pressed)
	{
		if (warningType != WarningType.NULL)
		{
			if (warningType == WarningType.SWITCH)
			{
				if (pressed == false)
				{
					warningType = WarningType.NULL;
					requestedMode = IVMode.NULL;
				}
				else if (pressed == true)
				{
					if (requestedMode == IVMode.STANDBY)
					{
						Active.setBackground(regular);
						Manual.setBackground(regular);
						Standby.setBackground(green);
						ModeStatus = PumpMode.STANDBY;
						stop = true;
						stopped = false;
						pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
						modeLock = false;
						warningType = WarningType.NULL;
						requestedMode = IVMode.NULL;
						MStart.setText("X");
						MStart.setBackground(Color.GRAY);
						MStop.setText("X");
						MStop.setBackground(Color.GRAY);
					}
					else if (requestedMode == IVMode.MANUAL)
					{
						Active.setBackground(regular);
						Manual.setBackground(green);
						Standby.setBackground(regular);
						ModeStatus = PumpMode.MANUAL;
						stop = true;
						stopped = false;
						pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
						modeLock = false;
						warningType = WarningType.NULL;
						requestedMode = IVMode.NULL;
						MStart.setText("Administer Dose");
						MStart.setBackground(regular);
						MStop.setText("X");
						MStop.setBackground(Color.GRAY);
					}
					else if (requestedMode == IVMode.INTERLOCK)
					{
						Active.setBackground(green);
						Manual.setBackground(regular);
						Standby.setBackground(regular);
						ModeStatus = PumpMode.INTERLOCK;
						stop = true;
						stopped = false;
						pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
						modeLock = false;
						warningType = WarningType.NULL;
						requestedMode = IVMode.NULL;
						MStart.setText("X");
						MStart.setBackground(Color.GRAY);
						MStop.setText("X");
						MStop.setBackground(Color.GRAY);
					}
				}
			}
			
			else if (warningType == WarningType.DISS)
			{
				if (pressed == false)
				{
					warningType = WarningType.NULL;
				}
				else if (pressed == true)
				{
					warningType = WarningType.NULL;
					Standby.setBackground(green);
					Manual.setBackground(regular);
					Active.setBackground(regular);
					ModeStatus = PumpMode.STANDBY;
					modeLock = false;
					stop = true;
					stopped = false;
					pumpNotice.setText(ModeStatus.toString() + " Mode - " + (stop == true ? "PCA Off" : "PCA On"));
					if (requestedDisassociation == Disassociation.Q5)
					{
						Q5.setText("Q5 - NOT ASSOCIATED (Click to Associate)");
						Q5.setBackground(regular);
						Q5AssociationStatus.Q = false;
					}
					else if (requestedDisassociation == Disassociation.Q4)
					{
						Q4.setText("Q4 - NOT ASSOCIATED (Click to Associate)");
						Q4.setBackground(regular);
						Q4AssociationStatus.Q = false;
					}
					requestedDisassociation = Disassociation.NULL;
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
	
	private class InfusionPumpGenerateThread extends Thread 
	{
		SimulatedIVPump2 pump;
		
		InfusionPumpGenerateThread(SimulatedIVPump2 sim)
		{
			pump = sim;
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
				
				PumpStatus pumpStatusToPass = null;
				if (pump.isInactive() == true)
				{
					if (pump.isStopped() == true)
						pumpStatusToPass = PumpStatus.PCAACTIVE_STOPPED;
					else pumpStatusToPass = PumpStatus.PCAINACTIVE;
				}
				else pumpStatusToPass = PumpStatus.PCAACTIVE_RUNNING;

				if (application.isQ4Associated())
				{
					PumpMode pumpMode = ModeStatus;
					String label = application.getPumpStatusPanel().getLabel();
					if (label.length() == 0)
					{
						application.getPumpStatusPanel().sublimate(pumpMode.toString() + " Mode - ");
					}
					else
					{
						int index = label.indexOf("Mode");
						String append = label.substring(index);
						if (pumpMode == PumpMode.INTERLOCK)
						{
							application.getPumpStatusPanel().updateValue("O");
							application.getPumpStatusPanel().setIVMode(IVMode.INTERLOCK);
						}
						else if (pumpMode == PumpMode.MANUAL)
						{
							application.getPumpStatusPanel().setIVMode(IVMode.MANUAL);
							application.getPumpStatusPanel().updateValue("Z");
						}
						else if (pumpMode == PumpMode.STANDBY)
						{
							application.getPumpStatusPanel().setIVMode(IVMode.STANDBY);
							application.getPumpStatusPanel().updateValue("Z");
						}
						
						application.getPumpStatusPanel().sublimate(pumpMode.toString() + " " + append);
					}
					
					label = application.getPumpStatusPanel().getLabel();
					if (label.length() == 0)
					{
						//DO NOTHING
					}
					else
					{
						int index = label.indexOf("Mode");
						if (index != -1)
						{
							String prepend = label.substring(0, index + 4);
							String append = new String();
							if (pumpStatusToPass == PumpStatus.PCAINACTIVE)
								append = new String("PCA Off");
							else if (pumpStatusToPass == PumpStatus.PCAACTIVE_RUNNING)
								append = new String("PCA On");
							else if (pumpStatusToPass == PumpStatus.PCAACTIVE_STOPPED)
								append = new String("PCA On : Stopped");
							
							application.getPumpStatusPanel().sublimate(prepend + " - " + append);
						}
					}
				}
				else
				{
					application.getPumpStatusPanel().setIVMode(IVMode.NULL);
					application.getPumpStatusPanel().updateValue("X");
					application.getPumpStatusPanel().suplimate();
				}
			}
		}
	}
	
	private final String warningMessageDISS = "\nYou're attempting to disassociate while the IV Pump is being controlled by" +
			" a safety interlock application.  Doing so will disrupt the interlock.\n\nAre you sure you want to continue?";
	private final String warningMessageSWITCH = "\nYou have requested to switch modes, however the IV Pump is"
			+ " currently being controlled by a clinician or an interlock application.  Switching will disrupt the current control mechanism." +
			"\n\nAre you sure you want to continue?";
	private final String warningMessageQ5 = "\n\nThe requested association may result in the PCA being controlled remotely."
			+ " Failure of the ICE system or external control mechanism may require \nmanual intervention by a clinician to ensure patient safety."
			+ "\n\nDo you want to continue?";
}
