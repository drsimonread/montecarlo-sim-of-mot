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
		System.out.println(min + ", " + max);
//		int binsize = 2*max/;
		mydata.addSeries("freq", data, 500, min, max);
		// create a chart...
		JFreeChart chart = ChartFactory.createHistogram(name, "Velocity", "Frequency",
				mydata, PlotOrientation.VERTICAL, true, true, false);
		// create and display a frame...
		ChartFrame frame = new ChartFrame(name, chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void myHistogram(double[] data, double mean, double stdDev, String name) {
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
		System.out.println(min + ", " + max);
//		int binsize = 2*max/;
		mydata.addSeries("freq", data, 500, min/10, max/10);
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
	
	public static void myScatter(double[] data, String name){
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
		System.out.println(min + ", " + max);
		mydata.addSeries("freq", data, 500, min/10, max/10);
		JFreeChart chart = ChartFactory.createHistogram(name, "Velocity", "Frequency",
				mydata, PlotOrientation.VERTICAL, true, true, false);
		
		JFreeChart mychart = ChartFactory.createScatterPlot("Velocity", "Velocity", "Event", 
				getDataValues(chart), PlotOrientation.VERTICAL, true, true, false);
		
		ChartFrame frame = new ChartFrame(name, mychart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static XYDataset getDataValues(JFreeChart arg){
		XYDataset dataSet = arg.getXYPlot().getDataset();
		double[][] arr = new double [dataSet.getItemCount(0)][dataSet.getItemCount(1)];
//		arr[1][0] = 0;
//		arr[1][dataSet.getItemCount(0)] = 0;
		for (int i = 1; i < dataSet.getItemCount(0); i++){
			arr[0][i] = dataSet.getXValue(0, i);
			arr[1][i] = dataSet.getYValue(1, i);
		}
		DefaultXYDataset mydata = new DefaultXYDataset();
		mydata.addSeries("freq", arr);
		return mydata;
	}
}