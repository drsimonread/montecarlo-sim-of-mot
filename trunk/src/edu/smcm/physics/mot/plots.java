import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.*;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
/**
* A simple introduction to using JFreeChart. This demo is described in the
* JFreeChart Developer Guide.
*/
public class plots {
	/**
	* The starting point for the demo.
	*/
	public static void histogram(double[] data, String name) {
		double min, max;
		min = DataAnalysis.min(data);
		max = DataAnalysis.max(data);
		HistogramDataset mydata = new HistogramDataset();	// create a dataset...
		mydata.addSeries("freq", data, 10000, min, max);
		JFreeChart chart = ChartFactory.createHistogram(name, "Velocity", "Frequency",
				mydata, PlotOrientation.VERTICAL, true, true, false);	// create a chart...
		ChartFrame frame = new ChartFrame(name, chart);		// create and display a frame...
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void scatter(double[][] data, String name, String xLabel, String yLabel){
		DefaultXYDataset myscater = new DefaultXYDataset(); 
		myscater.addSeries("Velocity", data);
		JFreeChart chart = ChartFactory.createScatterPlot(name, xLabel, yLabel, 
				myscater, PlotOrientation.VERTICAL, true, true, false);
		ChartFrame frame = new ChartFrame(name, chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void myLine(double[][] data, String name, String xLabel, String yLabel){
		DefaultXYDataset dataSet = new DefaultXYDataset();
		dataSet.addSeries(name, data);
		JFreeChart mychart = ChartFactory.createXYLineChart(name, xLabel, yLabel, 
				dataSet, PlotOrientation.VERTICAL, true, true, false);
		ChartFrame frame = new ChartFrame(name, mychart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static double[][] getDataValues(JFreeChart arg){
		XYDataset dataSet = arg.getXYPlot().getDataset();
		double[][] arr = new double [2][dataSet.getItemCount(0)];
		for (int i = 1; i < dataSet.getItemCount(0); i++){
			arr[0][i] = dataSet.getX(0, i).doubleValue();
			arr[1][i] = dataSet.getY(0, i).doubleValue();
		}
		return arr;
	}
}