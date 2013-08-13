package com.anakena.freePcaInterlock;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class DynamicImagePanel extends JPanel {
	
	  public boolean isAssociated = false; //Only for when DynamicImagePanel is used for a QUIC visual
	  private Image img = null;

	  DynamicImagePanel()
	  {
		  this.setPreferredSize(new Dimension(500, 300));
	  }
	  
	  public DynamicImagePanel(String img)
	  {
	    this(new ImageIcon(img).getImage());
	  }

	  public DynamicImagePanel(Image img) 
	  {
	    this.img = img;
	    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	  }
	  
	  public void updateImage(String img) 
	  {
		  updateImage(new ImageIcon(img).getImage());
		  if (getParent().getParent() != null)
			  getParent().getParent().repaint();
	  }
	  
	  public void updateImage(Image img) 
	  {
	    this.img = img;
	    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	    if (this.getParent() != null)
	    	this.getParent().repaint();
	  }

	    public void paint(Graphics g) {
	    	
	    	if (img == null)
		        super.paint(g);
	    	else
	    		g.drawImage(img, 0, 0, null);
	      }

}
