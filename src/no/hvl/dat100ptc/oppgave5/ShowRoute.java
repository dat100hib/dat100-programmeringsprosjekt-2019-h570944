package no.hvl.dat100ptc.oppgave5;

import javax.swing.JOptionPane;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class ShowRoute extends EasyGraphics {

	private static int MARGIN = 50;
	private static int MAPXSIZE = 600;
	private static int MAPYSIZE = 600;

	private GPSPoint[] gpspoints;
	private GPSComputer gpscomputer;
	
	public ShowRoute() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		gpscomputer = new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		makeWindow("Route", MAPXSIZE + 2 * MARGIN, MAPYSIZE + 2 * MARGIN);

		showRouteMap(MARGIN + MAPYSIZE);

		playRoute(MARGIN + MAPYSIZE);
		
		showStatistics();
	}

	// antall x-pixels per lengdegrad
	public double xstep() {

		double maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));

		double xstep = MAPXSIZE / (Math.abs(maxlon - minlon)); 

		return xstep;
	}

	// antall y-pixels per breddegrad
	public double ystep() {
	
		double maxlat  = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));
		
		double ystep = MAPYSIZE / (Math.abs(maxlat - minlat));
		
		return ystep;
	}

	public void showRouteMap(int ybase) {
		
		int radius = 3;
		int x, y, x0 = 0, y0 = 0;
		
		
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		
		for (int i = 0; i < gpspoints.length; i++) {
			
			x = MARGIN + (int) ((gpspoints[i].getLongitude() - minlon) * xstep());
			y = (int) (ybase - (gpspoints[i].getLatitude() - minlat) * (ystep()));
			
			if (i == 0) { 
				setColor(0, 255, 0);
			} else if (gpspoints[i].getElevation() >= gpspoints[i-1].getElevation() && i > 0 && i != gpspoints.length) {
				setColor(0, 255, 0);
			} else if (gpspoints[i].getElevation() < gpspoints[i-1].getElevation() && i > 0 && i != gpspoints.length){
				setColor(255, 0, 0);
			}
			
			if (i == 0) {
				x0 = x;
				y0 = y;
			}
			
			drawLine(x, y, x0, y0);
			x0 = x;
			y0 = y;
			fillCircle(x, y, radius);
			
			
		}
    }

	public void showStatistics() {

		int TEXTDISTANCE = 20;

		setColor(0,0,0);
		setFont("Courier",12);
		
		String[] strings = {
				String.format("%-15s:%s  ", "Total time", GPSUtils.formatTime(gpscomputer.totalTime())),
				String.format("%-15s:%10.2f km  ", "Total distance", gpscomputer.totalDistance()/1000),
				String.format("%-15s:%10.2f m  ", "Total elevation", gpscomputer.totalElevation()),
				String.format("%-15s:%10.2f km/h  ", "Max speed", gpscomputer.maxSpeed()),
				String.format("%-15s:%10.2f km/h  ", "Average speed", gpscomputer.averageSpeed()),
				String.format("%-15s:%10.2f kcal  ", "Energy", gpscomputer.totalKcal(80))
		};
		
		for (int i = 0; i < strings.length; i++) {
			drawString(strings[i], 10, 20 + TEXTDISTANCE * i);
		}
		
	}

	public void playRoute(int ybase) {

		// TODO - START
		
		
		// TODO - SLUTT
	}

}
