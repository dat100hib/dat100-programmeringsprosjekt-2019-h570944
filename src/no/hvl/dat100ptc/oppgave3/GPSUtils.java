package no.hvl.dat100ptc.oppgave3;

import static java.lang.Math.*;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;

public class GPSUtils {

	public static double findMax(double[] da) {

		double max; 
		
		max = da[0];
		
		for (double d : da) {
			if (d > max) {
				max = d;
			}
		}
		
		return max;
	}

	public static double findMin(double[] da) {

		double min;

		min = da[0];
		
		for (double d : da) {
			if (d < min) {
				min = d;
			}
		}
		
		return min;
	}

	public static double[] getLatitudes(GPSPoint[] gpspoints) {
		
		double[] latitudes = new double[gpspoints.length];
		
		for (int i = 0; i < gpspoints.length; i++) {
			latitudes[i] = gpspoints[i].getLatitude();
		}
		
		return latitudes;
	}

	public static double[] getLongitudes(GPSPoint[] gpspoints) {

		double[] longitudes = new double[gpspoints.length];
		
		for (int i = 0; i < gpspoints.length; i++) {
			longitudes[i] = gpspoints[i].getLongitude();
		}
		
		return longitudes;
	}

	private static int R = 6371000; // jordens radius

	public static double distance(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		double d;
		double latitude1, longitude1, latitude2, longitude2;

		latitude1 = Math.toRadians(gpspoint1.getLatitude());
		longitude1 = Math.toRadians(gpspoint1.getLongitude());
		latitude2 = Math.toRadians(gpspoint2.getLatitude());
		longitude2 = Math.toRadians(gpspoint2.getLongitude());
		
		double deltaLat = latitude2 - latitude1;
		double deltaLong = longitude2 - longitude1;
		
		double a = Math.pow(Math.sin(deltaLat/2), 2) + 
				Math.cos(latitude1)*Math.cos(latitude2)*Math.pow(Math.sin(deltaLong/2), 2);
		double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		d = R * c;
		
		return d;
	}

	public static double speed(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		int secs;
		double speed, distance;
		
		distance = distance(gpspoint1, gpspoint2);
		secs = gpspoint2.getTime() - gpspoint1.getTime();
		
		speed = (distance / secs) * 3.6;
		
		return speed;
	}

	public static String formatTime(int secs) {

		String timestr, h, m, s;
		//String TIMESEP = ":";
		int hours, minutes, sec;
		
		hours = secs / 3600;
		minutes = (secs - hours * 3600) / 60;
		sec = (secs - hours * 3600) - minutes * 60;
		
		h = hours < 10 ? "0" + hours : "" + hours;
		m = minutes < 10 ? "0" + minutes : "" + minutes;
		s = sec < 10 ? "0" + sec : "" + sec;
		
		timestr = String.format("%s:%s:%s", h, m, s);
		
		return String.format("%10s", timestr);
	}
	
	public static String formatDouble(double d) {

		String str;
		
		str = String.format("%10.2f", d);
		str.replace(",", ".");
		
		return str;
	}
}
