package com.anakena.freePcaInterlock;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedPatient {

	private physiologicalDataGenerator patient;
	
	public SimulatedPatient()
	{
		patient = new physiologicalDataGenerator();
	}
	
	public void activate()
	{
		patient.start();
	}
	
	public void terminatePatient()
	{
		patient.interrupt();
	}
	
	public Boolean isDistressed()
	{
		return patient.isDistressed();
	}
	
	public void setDistressed(boolean newCondition)
	{
		patient.setDistressed(newCondition);
	}
	
	public Integer getOxygenSaturation()
	{
		return patient.getOxygenSaturation();
	}
	
	public Integer getPulseRate()
	{
		return patient.getPulseRate();
	}
	
	public Integer getHeartRate()
	{
		return patient.getHeartRate();
	}
	
	public ArrayList<Number> getPlethysmograph()
	{
		return patient.getPlethysmograph();
	}
	
	private class physiologicalDataGenerator extends Thread 
	{
		private Boolean distressed;
		private Integer pulseRate;
		private Integer oxygenSaturation;
		private Integer heartRate;
		private ArrayList<Number> plethysmographSet1;
		private ArrayList<Number> plethysmographSet2;
		private ArrayList<Number> plethysmographSet3;
		private ArrayList<Number> nextPlethysmograph;
		private int current;
		
		public physiologicalDataGenerator()
		{
			distressed = new Boolean(false);
			pulseRate = new Integer(80);
			heartRate = new Integer(80);
			oxygenSaturation = new Integer(98);
			plethysmographSet1 = new ArrayList<Number>();
			plethysmographSet2 = new ArrayList<Number>();
			plethysmographSet3 = new ArrayList<Number>();
			nextPlethysmograph = plethysmographSet1;
			current = 1;
			initializePleths();
		}
		
		public Boolean isDistressed()
		{
			return distressed;
		}
		
		public void setDistressed(boolean newCondition)
		{
			distressed = newCondition;
		}
		
		public Integer getOxygenSaturation()
		{
			return oxygenSaturation;
		}
		
		public Integer getPulseRate()
		{
			return pulseRate;
		}
		
		public Integer getHeartRate()
		{
			return heartRate;
		}
		
		public ArrayList<Number> getPlethysmograph()
		{
			ArrayList<Number> plethysmographToSend = nextPlethysmograph;
			if (current < 3) current++;
			else current = 1;
			if (current == 1) nextPlethysmograph = plethysmographSet1;
			else if (current == 2) nextPlethysmograph = plethysmographSet2;
			else if (current == 3) nextPlethysmograph = plethysmographSet3;
			return plethysmographToSend;
		}
		
		public void run()
		{
			Random randomNumberGenerator = new Random();

			while (!Thread.currentThread().isInterrupted())
			{
				while (true)
				{
					try {
					    Thread.sleep(250);
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					    return;
					}
					if (distressed.equals(true))
					{
						if (oxygenSaturation > 85) oxygenSaturation -= randomNumberGenerator.nextInt(3);
						else if (randomNumberGenerator.nextInt(2) == 0)
							oxygenSaturation += randomNumberGenerator.nextInt(3);
						else oxygenSaturation -= randomNumberGenerator.nextInt(3);
						if (pulseRate > 44) pulseRate -= randomNumberGenerator.nextInt(3);
						else if (randomNumberGenerator.nextInt(2) == 0)
							pulseRate += randomNumberGenerator.nextInt(3);
						else pulseRate += randomNumberGenerator.nextInt(3);
						
						if (randomNumberGenerator.nextInt(10) == 4)
							heartRate = pulseRate + randomNumberGenerator.nextInt(5);
						else if (randomNumberGenerator.nextInt(10) == 6)
							heartRate = pulseRate + randomNumberGenerator.nextInt(5);
					}
					else
					{
						if (randomNumberGenerator.nextInt(2) == 0)
							oxygenSaturation = oxygenSaturation + randomNumberGenerator.nextInt(2);
						else oxygenSaturation = oxygenSaturation - randomNumberGenerator.nextInt(2);
						if (oxygenSaturation < 96) oxygenSaturation += 1;
						else if (oxygenSaturation > 99) oxygenSaturation -= 1;
						if (randomNumberGenerator.nextInt(2) == 0)
							pulseRate = pulseRate + randomNumberGenerator.nextInt(3);
						else oxygenSaturation = oxygenSaturation - randomNumberGenerator.nextInt(3);
						if (pulseRate > 90) pulseRate -= 2;
						else if (pulseRate < 45) pulseRate += 2;
						
						if (randomNumberGenerator.nextInt(20) == 5)
							heartRate = pulseRate + randomNumberGenerator.nextInt(3);
						else if (randomNumberGenerator.nextInt(20) == 15)
							heartRate = pulseRate + randomNumberGenerator.nextInt(3);
					}
				}
			}
		}
		
		private void initializePleths()
		{
			plethysmographSet1.add(32194);
			plethysmographSet1.add(32137);
			plethysmographSet1.add(32083);
			plethysmographSet1.add(32033);
			plethysmographSet1.add(31989);
			plethysmographSet1.add(31951);
			plethysmographSet1.add(31921);
			plethysmographSet1.add(31895);
			plethysmographSet1.add(31874);
			plethysmographSet1.add(31859);
			plethysmographSet1.add(31848);
			plethysmographSet1.add(31837);
			plethysmographSet1.add(31830);
			plethysmographSet1.add(31828);
			plethysmographSet1.add(31827);
			plethysmographSet1.add(31825);
			plethysmographSet1.add(31823);
			plethysmographSet1.add(31824);
			plethysmographSet1.add(31823);
			plethysmographSet1.add(31823);
			plethysmographSet1.add(31827);
			plethysmographSet1.add(31839);
			plethysmographSet1.add(31851);
			plethysmographSet1.add(31860);
			plethysmographSet1.add(31867);

			plethysmographSet2.add(31869);
			plethysmographSet2.add(31866);
			plethysmographSet2.add(31861);
			plethysmographSet2.add(31862);
			plethysmographSet2.add(31885);
			plethysmographSet2.add(31962);
			plethysmographSet2.add(32136);
			plethysmographSet2.add(32432);
			plethysmographSet2.add(32843);
			plethysmographSet2.add(33333);
			plethysmographSet2.add(33840);
			plethysmographSet2.add(34288);
			plethysmographSet2.add(34627);
			plethysmographSet2.add(34836);
			plethysmographSet2.add(34930);
			plethysmographSet2.add(34928);
			plethysmographSet2.add(34857);
			plethysmographSet2.add(34739);
			plethysmographSet2.add(34602);
			plethysmographSet2.add(34459);
			plethysmographSet2.add(34320);
			plethysmographSet2.add(34185);
			plethysmographSet2.add(34054);
			plethysmographSet2.add(33919);
			plethysmographSet2.add(33771);

			plethysmographSet3.add(33614);
			plethysmographSet3.add(33458);
			plethysmographSet3.add(33315);
			plethysmographSet3.add(33196);
			plethysmographSet3.add(33108);
			plethysmographSet3.add(33053);
			plethysmographSet3.add(33027);
			plethysmographSet3.add(33020);
			plethysmographSet3.add(33020);
			plethysmographSet3.add(33019);
			plethysmographSet3.add(33007);
			plethysmographSet3.add(32982);
			plethysmographSet3.add(32939);
			plethysmographSet3.add(32884);
			plethysmographSet3.add(32819);
			plethysmographSet3.add(32748);
			plethysmographSet3.add(32670);
			plethysmographSet3.add(32590);
			plethysmographSet3.add(32511);
			plethysmographSet3.add(32436);
			plethysmographSet3.add(32366);
			plethysmographSet3.add(32303);
			plethysmographSet3.add(32247);
			plethysmographSet3.add(32222);
			plethysmographSet3.add(32197);
		}
	}
}
