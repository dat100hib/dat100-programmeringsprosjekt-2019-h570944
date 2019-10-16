package no.hvl.dat100ptc.oppgave5;

import javax.swing.JOptionPane;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class ShowSpeed extends EasyGraphics {
			
	private static int MARGIN = 50;
	private static int BARHEIGHT = 200; // assume no speed above 200 km/t

	private GPSComputer gpscomputer;
	private GPSPoint[] gpspoints;
	
	public ShowSpeed() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		gpscomputer = new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();
		
	}
	
	// read in the files and draw into using EasyGraphics
	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		int N = gpspoints.length-1; // number of data points
		
		makeWindow("Speed profile", 2*MARGIN + 2 * N, 2 * MARGIN + BARHEIGHT);
		
		showSpeedProfile(MARGIN + BARHEIGHT,N);
	}
	
	public void showSpeedProfile(int ybase, int N) {
		
		System.out.println("Angi tidsskalering i tegnevinduet ...");
		int timescaling = Integer.parseInt(getText("Tidsskalering"));
		int windowHeight = 2 * MARGIN + BARHEIGHT;
		int heightscaling, d = 0;
		double maxSpeed, averageSpeed;
		
		averageSpeed = gpscomputer.averageSpeed();
		
		maxSpeed = GPSUtils.findMax(gpscomputer.speeds());
		heightscaling = (int)(BARHEIGHT / maxSpeed);
		
		setColor(0,0,255);
		
		for (int i = 0; i < N; i++) {
			GPSPoint gpspoint1 = gpspoints[i];
			GPSPoint gpspoint2 = gpspoints[i+1];
	
			//Scaling height of rectangles by (BARHEIGHT / maxSpeed) if height doesn't reach above border of the frame
			int height = (int) (GPSUtils.speed(gpspoint1, gpspoint2) * heightscaling);
			int minX = MARGIN + d;
			int minY = windowHeight - MARGIN - height;
			
			if (height < 0) {
				height = 0;
			}

			drawRectangle(minX, minY, 0, height);
			d += 2;
			pause((gpspoint2.getTime() - gpspoint1.getTime()) * 1000 / timescaling);
		}
		
		//Scaling height, if possible, by (BARHEIGHT / maxSpeed)
		int y = (int)(windowHeight - MARGIN - averageSpeed * heightscaling);
		setColor(0, 255, 0);
		fillRectangle(MARGIN, y, d, 2);
	}
}
