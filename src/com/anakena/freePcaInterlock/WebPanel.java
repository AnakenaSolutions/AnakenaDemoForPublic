package com.anakena.freePcaInterlock;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import java.util.ArrayList;
import com.anakena.freePcaInterlock.DynamicImagePanel;

public class WebPanel extends JPanel {
	
	private ArrayList<Object> LeftObjects = new ArrayList<Object>();
	private ArrayList<Object> RightObjects = new ArrayList<Object>();
	private ArrayList<Boolean> Target = new ArrayList<Boolean>();
	private ArrayList<Color> Colors = new ArrayList<Color>();
	private int next = 0;
	
	WebPanel() {

	}
	
	public int associate(Object obj1, Object obj2, Boolean isTarget, Color c)
	{
		LeftObjects.add(obj1);
		RightObjects.add(obj2);
		Target.add(isTarget);
		Colors.add(c);
		next++;
		return next-1;
	}
	
	public void dissociate()
	{
		LeftObjects.remove(LeftObjects.size()-1);
		RightObjects.remove(RightObjects.size()-1);
		Target.remove(Target.size()-1);
		Colors.remove(Colors.size()-1);
		next--;
	}
	
	public void updateIndexColor(int i, Color c)
	{
		Colors.set(i, c);
	}
	
    public void paint(Graphics g) {
    	
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(2));
        
        for (int i = 0; i < next; i++)
        {
        	if (RightObjects.get(i).getClass().equals(TickerPanel.class))
        		g2.setColor((((TickerPanel)RightObjects.get(i)).getColor()));
        	if (LeftObjects.get(i).getClass().equals(TickerPanel.class))
        		g2.setColor((((TickerPanel)LeftObjects.get(i)).getColor()));
        	
        	int finalLX = leftGenerateX(LeftObjects.get(i));
        	int finalLY = GenerateY(LeftObjects.get(i));
        	
        	int finalRX = rightGenerateX(RightObjects.get(i));
        	int finalRY = GenerateY(RightObjects.get(i));
        	
        	if (RightObjects.get(i).getClass().equals(TickerPanel.class))
        		finalRX -= 5;
        	if (LeftObjects.get(i).getClass().equals(DynamicImagePanel.class))
        		finalLX -= 5;
        	if (RightObjects.get(i).getClass().equals(ImageButton.class))
        		finalRX -= 5;
        	
        	g2.drawLine(finalLX, finalLY, finalRX, finalRY);
        	
        	if (Target.get(i) == true)
        	{
        		double length = 12;
        		
        		int A = finalRX - finalLX;
        		int B = finalRY - finalLY;
        		double C = Math.sqrt(A*A + B*B);
        		int E = 25;
        		
        		double con = 57.2957795;
        		double D = Math.asin(B/C) * con;
        		//if (D < 0) D += 360;
        		double Y = Math.sin((D-E+180)/con);
        		double X = Math.cos((D-E+180)/con);
        		
        		int iX = (int) (X*length);
        		int iY = (int) (Y*length);
        		
        		g2.drawLine(finalRX, finalRY, finalRX+iX, finalRY+iY);
        		Y = Math.sin((D+E+180)/con);
        		X = Math.cos((D+E+180)/con);
        		iX = (int) (X*length);
        		iY = (int) (Y*length);
        		g2.drawLine(finalRX, finalRY, finalRX+iX, finalRY+iY);
        	}
        }
      }
    
    private int leftGenerateX(Object obj)
    {
    	if (obj.getClass().equals(DynamicImagePanel.class))
    	{
    		int finalX = ((DynamicImagePanel)obj).getX() + ((DynamicImagePanel)obj).getWidth();
    		finalX = finalX + ((DynamicImagePanel)obj).getParent().getX();
    		return finalX;
    	}
    	else if (obj.getClass().equals(TickerPanel.class))
    	{
    		int finalX = ((TickerPanel)obj).getX() + ((TickerPanel)obj).getWidth();
    		finalX = finalX + ((TickerPanel)obj).getParent().getX();
    		return finalX;
    	}
    	else if (obj.getClass().equals(ImageButton.class))
    	{
    		int finalX = ((ImageButton)obj).getX() + ((ImageButton)obj).getWidth();
    		finalX = finalX + ((ImageButton)obj).getParent().getX();
    		return finalX;
    	}
    	else {
    		System.out.println("leftGenerateX Error");
    		System.exit(0);
    		return 0;
    	}
    }
    
    private int GenerateY(Object obj)
    {
    	if (obj.getClass().equals(DynamicImagePanel.class))
    	{
    		int Y = ((DynamicImagePanel)obj).getY();
    		int height = ((DynamicImagePanel)obj).getHeight();
    		int finalY = (height/2) + Y;
    		return finalY;
    	}
    	else if (obj.getClass().equals(TickerPanel.class))
    	{
    		int Y = ((TickerPanel)obj).getY();
    		int height = ((TickerPanel)obj).getHeight();
    		int finalY = (height/2) + Y;
    		return finalY;
    	}
    	else if (obj.getClass().equals(ImageButton.class))
    	{
    		int Y = ((ImageButton)obj).getParent().getY();
    		int height = ((ImageButton)obj).getHeight();
    		int finalY = (height/2) + Y;
    		return finalY;
    	}
    	else {
    		System.out.println("GenerateY Error");
    		System.exit(0);
    		return 0;
    	}
    }
    
    private int rightGenerateX(Object obj)
    {
    	if (obj.getClass().equals(DynamicImagePanel.class))
    	{
    		int X = ((DynamicImagePanel)obj).getX();
    		X = X + ((DynamicImagePanel)obj).getParent().getX();
    		return X;
    	}
    	else if (obj.getClass().equals(TickerPanel.class))
    	{
    		int X = ((TickerPanel)obj).getX();
    		X = X + ((TickerPanel)obj).getParent().getX();
    		return X;
    	}
    	else if (obj.getClass().equals(ImageButton.class))
    	{
    		int X = ((ImageButton)obj).getX();
    		X = X + ((ImageButton)obj).getParent().getX();
    		return X;
    	}
    	else {
    		System.out.println("rightGenerateX Error");
    		System.exit(0);
    		return 0;
    	}
    }

}
