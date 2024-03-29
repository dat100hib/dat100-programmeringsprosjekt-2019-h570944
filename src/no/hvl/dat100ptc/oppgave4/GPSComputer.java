package no.hvl.dat100ptc.oppgave4;

import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataConverter;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;

public class GPSComputer {
	
	private GPSPoint[] gpspoints;
	
	public GPSComputer(String filename) {

		GPSData gpsdata = GPSDataFileReader.readGPSFile(filename);
		gpspoints = gpsdata.getGPSPoints();

	}

	public GPSComputer(GPSPoint[] gpspoints) {
		this.gpspoints = gpspoints;
	}
	
	public GPSPoint[] getGPSPoints() {
		return this.gpspoints;
	}
	
	// beregn total distances (i meter)
	public double totalDistance() {

		double distance = 0;

		for (int i = 0; i < gpspoints.length - 1; i++) {
			distance += GPSUtils.distance(gpspoints[i], gpspoints[i+1]);
		}
		
		return distance;
	}

	// beregn totale høydemeter (i meter)
	public double totalElevation() {

		double elevation = 0;

		for (int i = 0; i < gpspoints.length - 1; i++) {
			double elevation1 = gpspoints[i].getElevation();
			double elevation2 = gpspoints[i+1].getElevation();
			elevation += elevation2 > elevation1 ? elevation2 - elevation1 : 0;
		}
		
		return elevation;
	}

	// beregn total tiden for hele turen (i sekunder)
	public int totalTime() {
		
		int time = 0;
		
		for (int i = 0; i < gpspoints.length - 1; i++) {
			time += gpspoints[i+1].getTime() - gpspoints[i].getTime();
		}
		
		return time;
	}
		
	// beregn gjennomsnitshastighets mellom hver av gps punktene

	public double[] speeds() {
		
		double[] speeds = new double[gpspoints.length - 1];
		
		for (int i = 0; i < speeds.length; i++) {
			speeds[i] = GPSUtils.speed(gpspoints[i], gpspoints[i+1]);
		}
		
		return speeds;
	}
	
	public double maxSpeed() {
		
		double maxspeed = 0;
		double[] speeds = speeds();
		
		for (double speed : speeds) {
			maxspeed = speed > maxspeed ? speed : maxspeed;
		}
		
		return maxspeed;
	}

	public double averageSpeed() {

		double average;
		
		average = (totalDistance() / totalTime()) * 3.6;
		
		return average;
	}

	/*
	 * bicycling, <10 mph, leisure, to work or for pleasure 4.0 bicycling,
	 * general 8.0 bicycling, 10-11.9 mph, leisure, slow, light effort 6.0
	 * bicycling, 12-13.9 mph, leisure, moderate effort 8.0 bicycling, 14-15.9
	 * mph, racing or leisure, fast, vigorous effort 10.0 bicycling, 16-19 mph,
	 * racing/not drafting or >19 mph drafting, very fast, racing general 12.0
	 * bicycling, >20 mph, racing, not drafting 16.0
	 */

	// conversion factor m/s to miles per hour
	public static double MS = 2.236936;

	// beregn kcal gitt weight og tid der kjøres med en gitt hastighet
	public double kcal(double weight, int secs, double speed) {

		double kcal;

		// MET: Metabolic equivalent of task angir (kcal x kg-1 x h-1)
		double met = 0;		
		double speedmph = speed * MS;
		
		if (speedmph <= 9.9) {
			met = 4.0;
		} else if (speedmph < 12) {
			met = 6.0;
		} else if (speedmph < 14) {
			met = 8.0;
		} else if (speedmph < 16) {
			met = 10.0;
		} else if (speedmph < 20) {
			met = 12.0;
		} else {
			met = 16.0;
		}
		
		kcal = met * weight * (secs / 3600.);
		return kcal;
	}

	public double totalKcal(double weight) {

		double totalkcal = 0.0;
		
		totalkcal = kcal(weight, totalTime(), averageSpeed());
		
		return totalkcal;
	}
	
	public void climbs() {
		double[] climbs = new double[gpspoints.length - 1];
		for (int i = 0; i < climbs.length; i++) {
			climbs[i] = (gpspoints[i+1].getElevation() - gpspoints[i].getElevation())
					/GPSUtils.distance(gpspoints[i], gpspoints[i+1]);
		}
		
		for (int i = 0; i < climbs.length; i++) {
			System.out.printf("Stigningsprosent mellom punkt %d og %d: %.2f", i, i+1, climbs[i]);
		}
	}
	
	public void maxClimb() {
		double[] climbs = new double[gpspoints.length - 1];
		for (int i = 0; i < climbs.length; i++) {
			GPSPoint gpspoint1 = gpspoints[i];
			GPSPoint gpspoint2 = gpspoints[i+1];
			climbs[i] = (gpspoint2.getElevation() - gpspoint1.getElevation())
					/GPSUtils.distance(gpspoint1, gpspoint2);
		}
		
		double max = climbs[0];
		for (double climb : climbs) {
			max = climb > max ? climb : max;
		}
		
		System.out.println(max);
	}
	
	private static double WEIGHT = 80.0;
	
	public void displayStatistics() {
		
		String T = "%-15s";
		
		System.out.println("==============================================");
		
		System.out.printf(T + ": %s\n", "Total time", GPSUtils.formatTime(totalTime()));
		System.out.printf(T + ":%s km\n", "Total distance", GPSUtils.formatDouble(totalDistance()/1000));
		System.out.printf(T + ":%sm\n", "Total elevation", GPSUtils.formatDouble(totalElevation()));
		System.out.printf(T + ":%skm/h\n", "Max speed", GPSUtils.formatDouble(maxSpeed()));
		System.out.printf(T + ":%skm/h\n", "Average speed", GPSUtils.formatDouble(averageSpeed()));
		System.out.printf(T + ":%skcal\n", "Energy", GPSUtils.formatDouble(totalKcal(WEIGHT)));
		
		System.out.println("==============================================");
		
	}

}
