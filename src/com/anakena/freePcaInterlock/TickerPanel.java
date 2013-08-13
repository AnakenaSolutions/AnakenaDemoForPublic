package com.anakena.freePcaInterlock;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.anakena.freePcaInterlock.Enumeration.IVMode;

public class TickerPanel extends JPanel {

	private JLabel name;
	private JLabel subinfo;
	private JLabel lvalue;
	private JPanel ticker;
	private Color color;
	private Dimension defaultSize;
	private IVMode Mode; //Only relevant to TickerPanel corresponding to IV Pump Status
	
	TickerPanel(Dimension asize, Color acolor, JLabel aname)
	{
		setPreferredSize(asize);
		setMaximumSize(asize);
		color = acolor;
		defaultSize = asize;
		setBackground(acolor);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		ticker = new JPanel();
		ticker.setPreferredSize(new Dimension(50, asize.height));
		ticker.setMaximumSize(new Dimension(50, asize.height));
		ticker.setBackground(acolor);
		ticker.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.WHITE));
		name = aname;
		name.setFont(new Font("Arial", Font.PLAIN, (int)(asize.height/1.8)));
		name.setForeground(Color.WHITE);
		subinfo = new JLabel("");
		add(Box.createRigidArea(new Dimension((int)(asize.width/12.5), asize.height)));
		add(name);
		add(Box.createHorizontalGlue());
		lvalue = new JLabel("");
		lvalue.setFont(new Font("Arial", Font.PLAIN, (int)(asize.height/1.8)));
		lvalue.setForeground(Color.WHITE);
		ticker.add(lvalue);
		Mode = IVMode.NULL;
	}
	
	public void updateValue(int newvalue)
	{
		lvalue.setText(Integer.toString(newvalue));
		color = new Color(10, 210, 20);
		setBackground(new Color(10, 210, 20));
		ticker.setBackground(new Color(10, 210, 20));
		bornAgain();
	}
	
	public int getValue()
	{
		String valueToReturn = lvalue.getText();
		if (valueToReturn.equals("X"))
			return -1;
		if (valueToReturn.equals("O"))
			return -2;
		if (valueToReturn.equals("Z"))
			return -3;
		int integerToReturn = Integer.valueOf(valueToReturn);
		return integerToReturn;
	}
	
	public void updateValue(String newvalue)
	{
		lvalue.setText(newvalue);
		if (newvalue.equals("X"))
		{
			color = new Color(215, 0, 0);
			setBackground(new Color(215, 0, 0));
			ticker.setBackground(new Color(215, 0, 0));
			bornAgain();
		}
		else if (newvalue.equals("Z"))
		{
			color = new Color(238, 201, 0);
			setBackground(color);
			ticker.setBackground(color);
			bornAgain();
		}
		else
		{
			color = new Color(10, 210, 20);
			setBackground(new Color(10, 210, 20));
			ticker.setBackground(new Color(10, 210, 20));
			bornAgain();
		}
	}
	
	public void updateLabel(String label)
	{
		name.setText(label);
	}
	
	public String getLabel()
	{
		return subinfo.getText();
	}
	
	public void sublimate(String label)
	{
		subinfo.setText(label);
		subinfo.setFont(new Font("Arial", Font.PLAIN, (int)(15)));
		subinfo.setForeground(Color.RED);
		removeAll();
		updateSize(new Dimension(250, 65));
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 7));
		if (Mode == IVMode.INTERLOCK)
			updateValue("O");
		else updateValue("Z");
		//remove(ticker);
		add(name);
		add(subinfo);
		updateUI();
		bornAgain();
	}
	
	public void updateSubInfo(String label)
	{
		subinfo.setText(label);
	}
	
	public void suplimate()
	{
		subinfo.setText("");
		removeAll();
		updateSize(defaultSize);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(new Color(215, 0, 0));
		add(Box.createRigidArea(new Dimension((int)(defaultSize.width/12.5), defaultSize.height)));
		add(name);
		add(Box.createHorizontalGlue());
		add(ticker);
		bornAgain();
	}
	
	public void setIVMode(IVMode mode)
	{
		Mode = mode;
	}
	
	public IVMode getIVMode()
	{
		return Mode;
	}
	
	public void updateColor(Color c)
	{
		color = c;
		setBackground(color);
		ticker.setBackground(color);
	}
	
	public void updateSize(Dimension aSize)
	{
		setPreferredSize(aSize);
		setMaximumSize(aSize);
		ticker.setPreferredSize(new Dimension(50, aSize.height));
		ticker.setMaximumSize(new Dimension(50, aSize.height));
	}
	
	public void bornAgain()
	{
		if (getParent() != null)
		{
			if (getParent().getParent() != null)
			{
				getParent().getParent().repaint();
			}
		}
	}
	
	public void initialize()
	{
		add(ticker);
	}
	
	public void uninitialize()
	{
		remove(ticker);
	}
	
	public Color getColor()
	{
		return color;
	}
	
}
