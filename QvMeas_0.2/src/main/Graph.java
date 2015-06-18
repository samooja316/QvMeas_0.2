package main;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/*
 * Graph class is a ui-object for maintaining state of a graph window for one graph.
 * This class also provides methods for manipulating the graph after its creation 
 */
public class Graph {
	
	//the core of the graph
	private JFreeChart _chart;
	
	
	/*
	 * Constructor, Graph can be created empty, it will be useful if there's not 
	 *  ready to show data present but user want's to highlight that there will be later
	 */
	public Graph() {
		
	}
}
