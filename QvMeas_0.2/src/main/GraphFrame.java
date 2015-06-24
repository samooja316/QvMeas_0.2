package main;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.*;
import java.awt.*;

/*
 * GraphFrame is a ui-object for plotting graphs 
 * @version 0.1
 * @since 0.2
 */
public class GraphFrame extends JInternalFrame {


	private static final long serialVersionUID = 1L;

	//size and positions for different graphs
	private static final Point _CVLOCATION = new Point(500,0);
	private static final Point _VTLOCATION = new Point(500,335);
	private static final Dimension _SIZE = new Dimension(485,335);
	private JFreeChart _chart;
	
	//Result of this frame
	private Result _result;
	
	public GraphFrame(Result r, GraphType type) {
		super();
		_result = r;
		this.setContentPane(createChartPanel(r, type));
		this.setVisible(true);
	}
	
	
	/*
	 * Initializes cv or vt graph
	 */
	private ChartPanel createChartPanel(Result r, GraphType type) 
			throws IllegalArgumentException {
		//ArrayList<Float> xValues; 
		//ArrayList<Float> yValues;
		
		XYSeries resSerie = null;
		if (type==GraphType.CV) { 
			resSerie = new XYSeries("C-V");
			//xValues = r.getVoltageSerie();
			//yValues = r.getCapacitanceSerie();
			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(resSerie);
	        _chart = ChartFactory.createXYLineChart(
	    		"C-V",      // chart title
	            "Bias [V]",                      // x axis label
	            "Capacitance [pF]",                      // y axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
		}
		else if (type==GraphType.VT){
			resSerie = new XYSeries("V-T");
			//yValues = r.getVoltageSerie();
			//xValues = r.getTimeSerie();
			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(resSerie);
	        _chart = ChartFactory.createXYLineChart(
	    		"V-T",      // chart title
	            "Time [s]",                      // x axis label
	            "Voltage [V]",                      // y axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );		
		} else {
			throw new IllegalArgumentException();
		}
		

		resSerie.add(-2.0, 2.7);
		resSerie.add(-1.0, 1.2);
		resSerie.add(0.0, 1.0);
		resSerie.add(1.0, 1.0);
		resSerie.add(2.0, 4.0);
		resSerie.add(3.0, 3.0);
		resSerie.add(4.0, 5.0);
		resSerie.add(5.0, 5.0);
		resSerie.add(6.0, 7.0);
		resSerie.add(7.0, 17.0);
		resSerie.add(8.0, 8.0);
		
		//creating series from the result's data
	/*	for(int i = 0; i <= xValues.size(); i++) {
			resSerie.add(xValues.get(i), yValues.get(i));										
		}*/
		        
        //chart color selections
        _chart.setBackgroundPaint(Color.WHITE);
        XYPlot plot = _chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);
                
        //renderer object
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);
        
        //Panel for the graph
        ChartPanel cPanel = new ChartPanel(_chart);
        cPanel.setPreferredSize(new Dimension(450,300)); //important to set the whole chart to right size
		//xValues.forEach(xVal -> resSerie.add(item);
		JPanel panel = new JPanel();
		
		//content pane for the graph
		this.setContentPane(panel);
		this.setSize(_SIZE);
		if(type==GraphType.CV) this.setLocation(_CVLOCATION);
		else if(type==GraphType.VT) this.setLocation(_VTLOCATION);
		
		//add graph to the frame
		//this.getContentPane().add(cPanel);
		return cPanel;
	}
	
	

}