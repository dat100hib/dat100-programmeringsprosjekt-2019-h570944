package no.hvl.dat100ptc.oppgave6;

import javax.swing.JOptionPane;

import easygraphics.*;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class CycleComputer extends EasyGraphics {

	private static int SPACE = 10;
	private static int MARGIN = 20;
	
	// FIXME: take into account number of measurements / gps points
	private static int ROUTEMAPXSIZE = 800; 
	private static int ROUTEMAPYSIZE = 400;
	private static int HEIGHTSIZE = 200;
	private static int TEXTWIDTH = 200;

	private GPSComputer gpscomp;
	private GPSPoint[] gpspoints;
	
	private int N = 0;

	private double minlon, minlat, maxlon, maxlat;

	private double xstep, ystep;

	public CycleComputer() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");

		gpscomp = new GPSComputer(filename);
		gpspoints = gpscomp.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		N = gpspoints.length; // number of gps points

		minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		maxlat = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));

		xstep = xstep();
		ystep = ystep();

		makeWindow("Cycle Computer", 
				2 * MARGIN + ROUTEMAPXSIZE,
				2 * MARGIN + ROUTEMAPYSIZE + HEIGHTSIZE + SPACE);

		bikeRoute();

	}

	
	public void bikeRoute() {

		int RADIUS = 3;
		int x, y;
		
		double maxlat = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));
		double difflat = maxlat - minlat;
		
		double maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		double difflon = maxlon - minlon;
		
		for (int i = 0; i < gpspoints.length; i++) {
			int X_POINT = 100;
			while ((((maxlon * difflon / ROUTEMAPXSIZE * xstep()) + MARGIN ) * X_POINT ) < ROUTEMAPXSIZE) {
				X_POINT--;
			}
			
			int Y_POINT = 100;
			while ((50 - (maxlat * difflat / ROUTEMAPYSIZE * ystep()) * Y_POINT) < ROUTEMAPYSIZE) {
				Y_POINT--;
			}
			
			x = (int) ((maxlon * (gpspoints[i].getLongitude() - minlon) / ROUTEMAPXSIZE * xstep()) * X_POINT);
			y = (int) ((maxlat * (gpspoints[i].getLatitude() - minlat) / ROUTEMAPYSIZE * ystep() * Y_POINT));
		
			setColor(0, 255, 0);
			
			if (i == gpspoints.length - 1) {
				setColor(0, 0, 255);
			}
			
			fillCircle(x + MARGIN * 3, 200-y, RADIUS);
		}
	}

	public double xstep() {
		
		double maxlon  = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		
		double xstep = ROUTEMAPXSIZE / (Math.abs(maxlon - minlon));
		
		return xstep;
		
	}

	public double ystep() {
		
		double maxlat  = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));
		
		double ystep = ROUTEMAPYSIZE / (Math.abs(maxlat - minlat));
		
		return ystep;
	
	}

}
