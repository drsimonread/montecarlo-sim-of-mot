import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.*;
import org.jfree.data.xy.*;
/**
* A simple introduction to using JFreeChart. This demo is described in the
* JFreeChart Developer Guide.
*/
public class plots {
	/**
	* The starting point for the demo.
	*
	* @param args ignored.
	*/
	public static void histogram(double[] data, double mean, double stdDev, String name) {
		// create a dataset...
		double min, max;
		min = max = data[0];
		HistogramDataset mydata = new HistogramDataset();
		for (int i = 0; i < data.length; i++){
			if (data[i] > max) {
	            max = data[i];   // new maximum
	        }else if (data[i] < min){
	        	min = data[i];
	        }
		}
//		System.out.println(min + ", " + max);
		mydata.addSeries("freq", data, 20, min, max);
		// create a chart...
		JFreeChart chart = ChartFactory.createHistogram(name, "Velocity", "Frequency",
				mydata, PlotOrientation.VERTICAL, true, true, false);
		// create and display a frame...
		ChartFrame frame = new ChartFrame(name, chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void scatter(double[][] data, String name){
		//double[][] mydata = new double[2][10000];  
		DefaultXYDataset myscater = new DefaultXYDataset(); 
//		for (int i = 0; i < data.length; i++){
//			mydata[0][i] = i;
//			mydata[1][i] = data[i];	
//		}
		myscater.addSeries("Velocity", data);
		JFreeChart chart = ChartFactory.createScatterPlot("Velocity", "Velocity", "Event", 
				myscater, PlotOrientation.VERTICAL, true, true, false);
		ChartFrame frame = new ChartFrame(name, chart);
		frame.pack();
		frame.setVisible(true);
	}
}