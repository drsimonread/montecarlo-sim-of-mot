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
		mydata.addSeries("freq", data, 500, min, max);
		// create a chart...
		JFreeChart chart = ChartFactory.createHistogram(name, "Velocity", "Frequency",
				mydata, PlotOrientation.VERTICAL, true, true, false);
		// create and display a frame...
		ChartFrame frame = new ChartFrame(name, chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void scatter(double[][] data, String name){
		DefaultXYDataset myscater = new DefaultXYDataset(); 
		myscater.addSeries("Velocity", data);
		JFreeChart chart = ChartFactory.createScatterPlot("Velocity", "Velocity", "Event", 
				myscater, PlotOrientation.VERTICAL, true, true, false);
		ChartFrame frame = new ChartFrame(name, chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void truncatedScatter(double[] data, String name, String xLabel, String yLabel){
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
		
		DefaultXYDataset otherdata = new DefaultXYDataset();
		otherdata.addSeries("freq", truncateArray(getDataValues(chart)));
		
		JFreeChart mychart = ChartFactory.createScatterPlot(name, xLabel, yLabel, 
				otherdata, PlotOrientation.VERTICAL, true, true, false);
		
		ChartFrame frame = new ChartFrame(name, mychart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static double[][] truncateArray(double[][] arr){
		double min1, min2, max;
		int min1Index, min2Index;
		min1Index = min2Index = 0;
		min1 = min2 = max = arr[1][0];
		double [][] temp = new double [2][arr.length];
		for(int i = 0; i < arr. length; i++){
			if (arr[1][i] < min1){
				if (arr[1][i-1] > min1 && min1 < arr[1][i+1]){
					min1 = arr[1][i];
				}
				min1Index = i;	
			}else if(arr[1][i] > max){
				max = arr[1][i];
				if (arr[1][i] < min2){
					if (arr[1][i-1] > min2 && min2 < arr[1][i+1]){
						min2 = arr[1][i];
					}
					min2Index = i;
				}
			}
		}	
		for(int j = 0; j < min1Index-min2Index; j++){
			System.arraycopy(arr, min1Index, temp, j, (min1Index-min2Index));
		}
		return temp;
	}
	
	public static double[][] getDataValues(JFreeChart arg){
		XYDataset dataSet = arg.getXYPlot().getDataset();
		double[][] arr = new double [dataSet.getItemCount(0)][dataSet.getItemCount(1)];
		for (int i = 1; i < dataSet.getItemCount(0); i++){
			arr[0][i] = dataSet.getXValue(0, i);
			arr[1][i] = dataSet.getYValue(1, i);
		}
		return arr;
	}
}