package edu.smcm.physics.mot;
import java.util.LinkedList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;


public class Histogram {

	private static String graphName;
	private String seriesName;
	private static String xLabel;
	private static String yLabel;
	private static int bins;
	private double[]data;
	private static LinkedList<double[]> dataSeries = new LinkedList<double[]>();
	private static LinkedList<String> nameSeries = new LinkedList<String>();
	
	public Histogram(String graph_Name, String x_Label, String y_Label, int Bins_){
		graphName = graph_Name;
		xLabel = x_Label;
		yLabel = y_Label;
		bins = Bins_;
	}
	
	public void addSeries(String series_Name, double[] data_){
		this.seriesName = series_Name;
		this.data = data_;
		dataSeries.add(data);
		nameSeries.add(seriesName);
	}
	
	public static void plotIT(){
		HistogramDataset dataSet = new HistogramDataset();	// create a dataset...
		double min, max;
		
		while(!dataSeries.isEmpty()){
			min = DataAnalysis.min(dataSeries.getFirst());
			max = DataAnalysis.max(dataSeries.getFirst());
			dataSet.addSeries(nameSeries.pollFirst(), dataSeries.pollFirst(), bins, min, max);
		}
		
		JFreeChart mychart = ChartFactory.createXYLineChart(graphName, xLabel, yLabel, 
				dataSet, PlotOrientation.VERTICAL, true, true, false);
		
		ChartFrame frame = new ChartFrame(graphName, mychart);
		frame.pack();
		frame.setVisible(true);
	}
}
