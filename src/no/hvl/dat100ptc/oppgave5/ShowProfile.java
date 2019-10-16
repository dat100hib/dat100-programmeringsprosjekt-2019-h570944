package no.hvl.dat100ptc.oppgave5;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataConverter;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

import javax.swing.JOptionPane;

public class ShowProfile extends EasyGraphics {

	private static int MARGIN = 50;		// margin on the sides 
	
	//FIXME: use highest point and scale accordingly
	private static int MAXBARHEIGHT = 500; // assume no height above 500 meters
	
	private GPSPoint[] gpspoints;

	public ShowProfile() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		GPSComputer gpscomputer =  new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		int N = gpspoints.length; // number of data points

		makeWindow("Height profile", 2 * MARGIN + 3 * N, 2 * MARGIN + MAXBARHEIGHT);

		// top margin + height of drawing area
		showHeightProfile(MARGIN + MAXBARHEIGHT); 
	}

	public void showHeightProfile(int ybase) {
		/*drawRectangle(int minX, int minY, int width, int height)
		* @param minX - x-koordinat for venstre side
		* @param minY - y-koordinat for øvre side
		* @param width - bredde, må være større enn 0
		* @param height - høyde, må være større enn 0
		*/
		// ybase indicates the position on the y-axis where the columns should start
		
		double[] elevations = new double[gpspoints.length];
		int timescaling = Integer.parseInt(getText("Tidsskalering"));
		
		for (int i = 0; i < gpspoints.length; i++) {
			elevations[i] = gpspoints[i].getElevation();
		}
		
		double max = GPSUtils.findMax(elevations);
		
		setColor(0,0, 255);
		int windowHeight = 2 * MARGIN + MAXBARHEIGHT;
		int d = 0;
		int startX = 0;
		int startY = 0;
		
		int circle = fillCircle(MARGIN + d,
				windowHeight - MARGIN - (int) (gpspoints[0].getElevation() * (MAXBARHEIGHT / max)), 5);
		
		for (int i = 0; i < gpspoints.length; i++) {
			GPSPoint gpspoint = gpspoints[i];
			//Use max to calculate if scaling is possible
			int height = (int) (gpspoint.getElevation() *(MAXBARHEIGHT / max));
			int minY = windowHeight - MARGIN - height;
			
			if (height < 0) {
				height = 0;
			}

			drawRectangle(MARGIN + i + d, minY, 1, height);
			d += 2;
			
			if (gpspoint != gpspoints[0]) {
				drawLine(startX, startY, MARGIN + i + d, minY);
			}
			
			startX = MARGIN + i + d;
			startY = minY;
			
			moveCircle(circle, startX, startY);
			
			if (gpspoint != gpspoints[gpspoints.length - 1]) {
				pause((gpspoints[i+1].getTime() - gpspoint.getTime()) * 1000 / timescaling);
			}
		}
	}
}
