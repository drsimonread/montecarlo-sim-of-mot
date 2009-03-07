import java.util.LinkedList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;


public class LineGraph {
	
	private static String graphName;
	private String seriesName;
	private static String xLabel;
	private static String yLabel;
	private double[][] data;
	private static LinkedList<double[][]> dataSeries = new LinkedList<double[][]>();
	private static LinkedList<String> nameSeries = new LinkedList<String>();
	
	public LineGraph(String graph_Name, String x_Label, String y_Label){
		graphName = graph_Name;
		xLabel = x_Label;
		yLabel = y_Label;
	}
	
	public void addSeries(String series_Name, double[][] data_){
		this.seriesName = series_Name;
		this.data = data_;
		dataSeries.add(data);
		nameSeries.add(seriesName);
	}
	
	public void plotIT(){
		DefaultXYDataset dataSet = new DefaultXYDataset();
		
		while(!dataSeries.isEmpty()){
			dataSet.addSeries(nameSeries.pollFirst(), dataSeries.pollFirst());
		}
		
		JFreeChart mychart = ChartFactory.createXYLineChart(graphName, xLabel, yLabel, 
				dataSet, PlotOrientation.VERTICAL, true, true, false);
		ChartFrame frame = new ChartFrame(graphName, mychart);
		frame.pack();
		frame.setVisible(true);
	}
}
