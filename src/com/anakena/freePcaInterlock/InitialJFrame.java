package com.anakena.freePcaInterlock;

import javax.swing.*;

import com.anakena.freePcaInterlock.Enumeration.IVMode;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class InitialJFrame extends JFrame implements ActionListener {
	
	private static final int defaultWidth = 1200;
	private static final int defaultHeight = 750;
	private static int latitudinalHeight = 50;
	private static Color green = new Color(10, 210, 20);
	private static Color red = new Color(215, 0, 0);
	private static Color gold = new Color(238, 201, 0);
	
	enum Mode { UNDEFINED, MANUAL, PLETH, HRPR }
	private Mode mode = Mode.UNDEFINED;

	private ImageButton manualModeButton = new ImageButton("src/com/anakena/freePcaInterlock/InactiveManual.png");
	private ImageButton plethModeButton = new ImageButton("src/com/anakena/freePcaInterlock/InactivePleth.png");
	private ImageButton hrprModeButton = new ImageButton("src/com/anakena/freePcaInterlock/InactiveHRPR.png");
	private ImageButton interlockButton = new ImageButton("src/com/anakena/freePcaInterlock/LimboSafetyLock.png");
	
	private DynamicImagePanel pulseOximetryQuicImage = new DynamicImagePanel("src/com/anakena/freePcaInterlock/Q2InactiveQUIC.png");
	private DynamicImagePanel ecgQuicImage = new DynamicImagePanel("src/com/anakena/freePcaInterlock/Q3InactiveQUIC.png");
	private DynamicImagePanel pumpStatusQuicImage = new DynamicImagePanel("src/com/anakena/freePcaInterlock/Q4InactiveQUIC.png");
	private DynamicImagePanel pumpCommandQuicImage = new DynamicImagePanel("src/com/anakena/freePcaInterlock/Q5InactiveQUIC.png");
	
	private TickerPanel o2Panel = new TickerPanel(new Dimension(250, 40), new Color(215, 0, 0), new JLabel("O2 Saturation"));
	private TickerPanel pulseRatePanel = new TickerPanel(new Dimension(250, 40), new Color(215, 0, 0), new JLabel("Pulse Rate"));
	private TickerPanel plethPanel = new TickerPanel(new Dimension(250, 40), new Color(215, 0, 0), new JLabel("Plethysmograph"));
	private TickerPanel ecgPanel = new TickerPanel(new Dimension(250, 40), new Color(215, 0, 0), new JLabel("ECG Data"));
	private TickerPanel pumpStatusPanel = new TickerPanel(new Dimension(250, 40), new Color(215, 0, 0), new JLabel("IV Pump Status"));
	private TickerPanel pumpCommandPanel = new TickerPanel(new Dimension(250, 40), new Color(215, 0, 0), new JLabel("IV Pump Stop"));
	
	//shared indicator variables
	private Boolean Q2Associated;
	private Boolean Q3Associated;
	private Boolean Q4Associated;
	private Boolean Q5Associated;
	private boolean modeIsActivated;
	
	private JLayeredPane layerPane;
	private JPanel zeroLayer;
	
	WebPanel middle;
	
	public InitialJFrame()
	{
		//initialize the application frame
		super("Theta PCA Interlock");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(defaultWidth, defaultHeight);
		getContentPane().setBackground(Color.BLACK);
		setLayout(new BorderLayout(0, 0));
		
		Q2Associated = new Boolean(false);
		Q3Associated = new Boolean(false);
		Q4Associated = new Boolean(false);
		Q5Associated = new Boolean(false);
		modeIsActivated = false;
		
		layerPane = new JLayeredPane();
		layerPane.setSize(defaultWidth, defaultHeight);
		//layerPane.setBounds(0, 0, defaultWidth, defaultHeight); //NEED A LISTENER TO DYNAMICALLY CHANGE BOUNDS WHEN FRAME CHANGES
		layerPane.setBackground(Color.BLACK);
		layerPane.setLayout(new BorderLayout());
		
		zeroLayer = new JPanel();
		zeroLayer.setSize(defaultWidth, defaultHeight);
		//zeroLayer.setBounds(0, 0, defaultWidth, defaultHeight);  //NEED A LISTENER TO DYNAMICALLY CHANGE BOUNDS WHEN LAYERPANE SIZE CHANGES
		zeroLayer.setBackground(Color.BLACK);
		zeroLayer.setLayout(new BorderLayout(0, 0));
		
		//layerPane.add(zeroLayer, new Integer(0), 0);
		layerPane.add(zeroLayer, BorderLayout.CENTER);
		add(layerPane);
	
		//set up the north side of the border layout
		JPanel top = new JPanel();
		top.setPreferredSize(new Dimension(defaultWidth, latitudinalHeight));
		top.setMaximumSize(new Dimension(defaultWidth, latitudinalHeight));
		top.setBackground(Color.BLACK);
		JLabel applicationTitle = new JLabel("Theta PCA Safety Interlock");
		applicationTitle.setFont(new Font("Arial", Font.PLAIN, (int)(latitudinalHeight/(1.5))));
		applicationTitle.setForeground(gold);
		top.add(applicationTitle);
		zeroLayer.add(top, BorderLayout.NORTH);
		
		//set up the south side of the border layout
		JPanel bottom = new JPanel();
		bottom.setPreferredSize(new Dimension(defaultWidth, (int)(latitudinalHeight*1.5)));
		bottom.setBackground(Color.BLACK);  //Originally Pink
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		JLabel modeSelect = new JLabel("Mode Select : " );
		modeSelect.setFont(new Font("Arial", Font.PLAIN, 38));
		modeSelect.setForeground(Color.WHITE);
		bottom.add(Box.createHorizontalGlue()); 
		bottom.add(modeSelect);
		bottom.add(Box.createRigidArea(new Dimension(20, 80)));;
		bottom.add(manualModeButton);
		bottom.add(Box.createRigidArea(new Dimension(20, 80)));; 
		bottom.add(plethModeButton);
		bottom.add(Box.createRigidArea(new Dimension(20, 80)));; 
		bottom.add(hrprModeButton);
		bottom.add(Box.createHorizontalGlue());
		zeroLayer.add(bottom, BorderLayout.SOUTH);
		
		//set up the middle of the border layout
		middle = new WebPanel();
		middle.setBackground(Color.DARK_GRAY); //Originally Dark Gray
		middle.setLayout(new BoxLayout(middle, BoxLayout.X_AXIS));
		
		//each Jpanel will arrange collections of objects vertically, and be displayed horizontally
		//within 'middle'.
		
			JPanel leftQuicImages = new JPanel();
			leftQuicImages.setBackground(Color.DARK_GRAY); //Originally Gray
			leftQuicImages.setLayout(new BoxLayout(leftQuicImages, BoxLayout.Y_AXIS));
			
				leftQuicImages.add(Box.createVerticalGlue());
				leftQuicImages.add(pulseOximetryQuicImage);
				leftQuicImages.add(Box.createVerticalGlue());
				leftQuicImages.add(ecgQuicImage);
				leftQuicImages.add(Box.createVerticalGlue());
				leftQuicImages.add(pumpStatusQuicImage);
				leftQuicImages.add(Box.createVerticalGlue());
			
			JPanel rightQuicImages = new JPanel();
			rightQuicImages.setBackground(Color.DARK_GRAY);
			rightQuicImages.setLayout(new BoxLayout(rightQuicImages, BoxLayout.Y_AXIS));
				
				rightQuicImages.add(Box.createVerticalGlue());
				rightQuicImages.add(pumpCommandQuicImage);
				rightQuicImages.add(Box.createVerticalGlue());
			
			JPanel leftTickerPanels = new JPanel();
			leftTickerPanels.setBackground(Color.DARK_GRAY); //Originally blue
			leftTickerPanels.setLayout(new BoxLayout(leftTickerPanels, BoxLayout.Y_AXIS));
			
				leftTickerPanels.add(Box.createVerticalGlue());
				leftTickerPanels.add(o2Panel);
				leftTickerPanels.add(Box.createVerticalGlue());
				leftTickerPanels.add(pulseRatePanel);
				leftTickerPanels.add(Box.createVerticalGlue());
				leftTickerPanels.add(plethPanel);
				leftTickerPanels.add(Box.createVerticalGlue());
				leftTickerPanels.add(ecgPanel);
				leftTickerPanels.add(Box.createVerticalGlue());
				leftTickerPanels.add(pumpStatusPanel);
				leftTickerPanels.add(Box.createVerticalGlue());
			
			JPanel activatorPanel = new JPanel();
			activatorPanel.setBackground(Color.DARK_GRAY);
			activatorPanel.setLayout(new BoxLayout(activatorPanel, BoxLayout.Y_AXIS));	
			
				activatorPanel.add(interlockButton);
			
			JPanel rightTickerPanels = new JPanel();
			rightTickerPanels.setBackground(Color.DARK_GRAY);
			rightTickerPanels.setLayout(new BoxLayout(rightTickerPanels, BoxLayout.Y_AXIS));
			
				rightTickerPanels.add(Box.createVerticalGlue());
				rightTickerPanels.add(pumpCommandPanel);
				rightTickerPanels.add(Box.createVerticalGlue());
		
		middle.add(Box.createHorizontalGlue());
		middle.add(leftQuicImages);
		middle.add(Box.createHorizontalGlue());
		middle.add(leftTickerPanels);
		middle.add(Box.createHorizontalGlue());
		middle.add(activatorPanel);
		middle.add(Box.createHorizontalGlue());
		middle.add(rightTickerPanels);
		middle.add(Box.createHorizontalGlue());
		middle.add(rightQuicImages);
		middle.add(Box.createHorizontalGlue());
		
		zeroLayer.add(middle, BorderLayout.CENTER);
		//ends set up of the middle of the border layout
		
		//initialize the visual objects, and notify their outer-most container ('middle') of their 'associations',
		//which visually translate into appropriately drawn lines that indicate relationships between
		//the visual objects
		o2Panel.initialize();
		o2Panel.updateValue("X");
		pulseRatePanel.initialize();
		pulseRatePanel.updateValue("X");
		plethPanel.initialize();
		plethPanel.updateValue("X");
		ecgPanel.initialize();
		ecgPanel.updateValue("X");
		pumpStatusPanel.initialize();
		pumpStatusPanel.updateValue("X");
		
		middle.associate(pulseOximetryQuicImage, o2Panel, true, Color.WHITE);
		middle.associate(pulseOximetryQuicImage, pulseRatePanel, true, Color.WHITE);
		middle.associate(pulseOximetryQuicImage, plethPanel, true, Color.WHITE);
		middle.associate(ecgQuicImage, ecgPanel, true, Color.WHITE);
		middle.associate(pumpStatusQuicImage, pumpStatusPanel, true, Color.WHITE);
		middle.associate(interlockButton, pumpCommandPanel, true, Color.WHITE);	
		middle.associate(pumpCommandPanel, pumpCommandQuicImage, true, Color.WHITE);
		
		interlockButton.addActionListener(this);
		manualModeButton.addActionListener(this);
		plethModeButton.addActionListener(this);
		hrprModeButton.addActionListener(this);
		//end of initialization of visual objects
		
		Spawner spawner = new Spawner(this);
		spawner.activate();
		
		InterlockLogic logicThread = new InterlockLogic();
		logicThread.start();
	}
	
	public TickerPanel getO2Panel()
	{
		return o2Panel;
	}
	
	public TickerPanel getPulseRatePanel()
	{
		return pulseRatePanel;
	}
	
	public TickerPanel getPlethPanel()
	{
		return plethPanel;
	}
	
	public TickerPanel getHeartRatePanel()
	{
		return ecgPanel;
	}
	
	public TickerPanel getPumpStatusPanel()
	{
		return pumpStatusPanel;
	}
	
	public TickerPanel getPumpCommandPanel()
	{
		return pumpCommandPanel;
	}
	
	public void toggleQ2()
	{
		if (Q2Associated.equals(true))
		{
			pulseOximetryQuicImage.updateImage("src/com/anakena/freePcaInterlock/Q2InactiveQUIC.png");
			o2Panel.updateValue("X");
			pulseRatePanel.updateValue("X");
			plethPanel.updateValue("X");
			Q2Associated = false;
		}
		else if (Q2Associated.equals(false))
		{
			pulseOximetryQuicImage.updateImage("src/com/anakena/freePcaInterlock/Q2ActiveQUIC.png");
			Q2Associated = true;
		}
	}
	
	public void toggleQ3()
	{
		if (Q3Associated.equals(true))
		{
			ecgQuicImage.updateImage("src/com/anakena/freePcaInterlock/Q3InactiveQUIC.png");
			ecgPanel.updateValue("X");
			Q3Associated = false;
		}
		else if (Q3Associated.equals(false))
		{
			ecgQuicImage.updateImage("src/com/anakena/freePcaInterlock/Q3ActiveQUIC.png");
			Q3Associated = true;
		}
	}
	
	public void toggleQ4()
	{
		if (Q4Associated.equals(true))
		{
			pumpStatusQuicImage.updateImage("src/com/anakena/freePcaInterlock/Q4InactiveQUIC.png");
			pumpStatusPanel.updateValue("X");
			pumpStatusPanel.suplimate();
			Q4Associated = false;
		}
		else if (Q4Associated.equals(false))
		{
			pumpStatusQuicImage.updateImage("src/com/anakena/freePcaInterlock/Q4ActiveQUIC.png");
			Q4Associated = true;
		}
	}
	
	public void toggleQ5()
	{
		if (Q5Associated.equals(true))
		{
			pumpCommandQuicImage.updateImage("src/com/anakena/freePcaInterlock/Q5InactiveQUIC.png");
			pumpCommandPanel.updateValue("X");
			Q5Associated = false;
		}
		else if (Q5Associated.equals(false))
		{
			pumpCommandQuicImage.updateImage("src/com/anakena/freePcaInterlock/Q5ActiveQUIC.png");
			Q5Associated = true;
		}
	}
	
	public Boolean isQ2Associated()
	{
		return Q2Associated;
	}
	
	public Boolean isQ3Associated()
	{
		return Q3Associated;
	}
	
	public Boolean isQ4Associated()
	{
		return Q4Associated;
	}
	
	public Boolean isQ5Associated()
	{
		return Q5Associated;
	}
	
	public Mode getMode()
	{
		return mode;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		ImageButton b = (ImageButton)e.getSource();
		if (b.equals(manualModeButton))
		{
			if (mode.equals(Mode.MANUAL));
			else if (modeIsActivated == false)
			{
				if (mode.equals(Mode.PLETH))
					comingFromPleth();
				else if (mode.equals(Mode.HRPR))
					comingFromHRPR();
				manualModeButton.updateImage("src/com/anakena/freePcaInterlock/ActiveManual.png");
				plethModeButton.updateImage("src/com/anakena/freePcaInterlock/InactivePleth.png");
				hrprModeButton.updateImage("src/com/anakena/freePcaInterlock/InactiveHRPR.png");
				if (isQ5Associated())
				{
					interlockButton.updateImage("src/com/anakena/freePcaInterlock/InactiveSafetyLock.png");
					pumpCommandPanel.updateColor(gold);
				}
				else 
				{
					pumpCommandPanel.updateColor(red);
					interlockButton.updateImage("src/com/anakena/freePcaInterlock/IncompleteSafetyLock.png");
				}
				middle.associate(pumpStatusPanel, interlockButton, true, Color.WHITE);
				middle.repaint();
				mode = Mode.MANUAL;
			}
			else if (modeIsActivated == true)
			{
				
			}
		}
		
		else if (b.equals(plethModeButton))
		{
			if (mode.equals(Mode.PLETH));
			else if (modeIsActivated == false)
			{
				if (mode.equals(Mode.MANUAL))
					comingFromManual();
				else if (mode.equals(Mode.HRPR))
					comingFromHRPR();
				mode = Mode.PLETH;
				manualModeButton.updateImage("src/com/anakena/freePcaInterlock/InactiveManual.png");
				plethModeButton.updateImage("src/com/anakena/freePcaInterlock/ActivePleth.png");
				hrprModeButton.updateImage("src/com/anakena/freePcaInterlock/InactiveHRPR.png");
				interlockButton.updateImage("src/com/anakena/freePcaInterlock/IncompleteSafetyLock.png");
				pumpCommandPanel.updateColor(red);
				middle.associate(o2Panel, interlockButton, true, Color.WHITE);
				middle.associate(plethPanel, interlockButton, true, Color.WHITE);
				middle.associate(pumpStatusPanel, interlockButton, true, Color.WHITE);
				middle.repaint();
			}
		}
		
		else if (b.equals(hrprModeButton))
		{
			if (mode.equals(Mode.HRPR));
			else if (modeIsActivated == false)
			{
				if (mode.equals(Mode.PLETH))
					comingFromPleth();
				else if (mode.equals(Mode.MANUAL))
					comingFromManual();
				mode = Mode.HRPR;
				manualModeButton.updateImage("src/com/anakena/freePcaInterlock/InactiveManual.png");
				plethModeButton.updateImage("src/com/anakena/freePcaInterlock/InactivePleth.png");
				hrprModeButton.updateImage("src/com/anakena/freePcaInterlock/ActiveHRPR.png");
				interlockButton.updateImage("src/com/anakena/freePcaInterlock/IncompleteSafetyLock.png");
				pumpCommandPanel.updateColor(red);
				middle.associate(o2Panel, interlockButton, true, Color.WHITE);
				middle.associate(pulseRatePanel, interlockButton, true, Color.WHITE);
				middle.associate(ecgPanel, interlockButton, true, Color.WHITE);
				middle.associate(pumpStatusPanel, interlockButton, true, Color.WHITE);
				middle.repaint();
			}
		}
		else if (b.equals(interlockButton))
		{
			if (modeIsActivated == false)
			{
				if (mode.equals(Mode.UNDEFINED));
				else if (mode.equals(Mode.PLETH))
				{
					if (pumpStatusPanel.getIVMode().equals(IVMode.INTERLOCK))
					{
						if (plethPanel.getValue() != -1 && o2Panel.getValue() != -1)
						{
							if (interlockCheck())
							{
							modeIsActivated = true;
							sendStart();
							interlockButton.updateImage("src/com/anakena/freePcaInterlock/ActiveSafetyLock.png");
							pumpCommandPanel.updateColor(green);
							}
						}
					}
				}
				else if (mode.equals(Mode.MANUAL))
				{
					if (pumpStatusPanel.getIVMode().equals(IVMode.INTERLOCK))
					{
						if (interlockCheck())
						{
						modeIsActivated = true;
						sendStart();
						interlockButton.updateImage("src/com/anakena/freePcaInterlock/ActiveSafetyLock.png");
						pumpCommandPanel.updateColor(green);
						}
					}
				}
				else if (mode.equals(Mode.HRPR))
				{
					if (pumpStatusPanel.getIVMode().equals(IVMode.INTERLOCK))
					{
						if (pulseRatePanel.getValue() != -1 && ecgPanel.getValue() != -1)
						{
							if (interlockCheck())
							{
							modeIsActivated = true;
							sendStart();
							interlockButton.updateImage("src/com/anakena/freePcaInterlock/ActiveSafetyLock.png");
							pumpCommandPanel.updateColor(green);
							}
						}
					}
				}
			}
			else if (modeIsActivated == true)
			{
				
			}
		}
	}
	
	public boolean interlockCheck()
	{
		int confirm = JOptionPane.showOptionDialog(null, interlockCheckString, "Interlock Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (confirm == 0)
        	return true;
        else return false;
	}
	
	public void comingFromPleth()
	{
		middle.dissociate();
		middle.dissociate();
		middle.dissociate();
	}
	
	public void comingFromHRPR()
	{
		middle.dissociate();
		middle.dissociate();
		middle.dissociate();
		middle.dissociate();
	}
	
	public void comingFromManual()
	{
		middle.dissociate();
	}
	
	private void sendStop()
	{
		
	}
	
	private void sendStart()
	{

	}
	
	private class InterlockLogic extends Thread 
	{
		private boolean stopSent = false;
		
		InterlockLogic()
		{
			
		}
		
		public void run()
		{
			while (!Thread.currentThread().isInterrupted())
			{
				try {
				    Thread.sleep(100);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				    break;
				}
				
				if (modeIsActivated == false)
				{
					if (mode.equals(Mode.MANUAL))
					{
						if (pumpStatusPanel.getValue() == -2 && isQ5Associated())
						{
							interlockButton.updateImage("src/com/anakena/freePcaInterlock/InactiveSafetyLock.png");
							pumpCommandPanel.updateColor(gold);
						}
						else
						{
							interlockButton.updateImage("src/com/anakena/freePcaInterlock/IncompleteSafetyLock.png");
							pumpCommandPanel.updateColor(red);
						}
					}
					if (mode.equals(Mode.HRPR))
					{
						if (pulseRatePanel.getValue() != -1 && ecgPanel.getValue() != -1 && pumpStatusPanel.getValue() == -2 && isQ5Associated())
						{
							interlockButton.updateImage("src/com/anakena/freePcaInterlock/InactiveSafetyLock.png");
							pumpCommandPanel.updateColor(gold);
						}
						else
						{
							interlockButton.updateImage("src/com/anakena/freePcaInterlock/IncompleteSafetyLock.png");
							pumpCommandPanel.updateColor(red);
						}
					}
					if (mode.equals(Mode.PLETH))
					{
						if (plethPanel.getValue() != -1 && pumpStatusPanel.getValue() == -2 && isQ5Associated())
						{
							interlockButton.updateImage("src/com/anakena/freePcaInterlock/InactiveSafetyLock.png");
							pumpCommandPanel.updateColor(gold);
						}
						else
						{
							interlockButton.updateImage("src/com/anakena/freePcaInterlock/IncompleteSafetyLock.png");
							pumpCommandPanel.updateColor(red);
						}
					}
				}
				else if (modeIsActivated == true)
				{
					if (mode.equals(Mode.PLETH) || mode.equals(Mode.HRPR))
					{
						if (o2Panel.getValue() < 90)
						{
							if (stopSent == false)
							{
								sendStop();
								stopSent = true;
							}
						}
					}
				}
			}
			handleInterrupt();
		}
		
		public void handleInterrupt()
		{
			
		}
	}
	
	private static String interlockCheckString = new String("\nEngaging the safety interlock may result in the PCA stopping unexpectedly."
			+ "\nAre you sure you want to do this?\n");
	
}
