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

import flanagan.interpolation.CubicSpline;

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
	private static final Point _VTLOCATION = new Point(500,385);
	private static final Dimension _SIZE = new Dimension(485,385);
	private JFreeChart _chart;
	
	//Result of this frame
	private Result _result;
	
	public GraphFrame(Result r, GraphType type) {
		super(type.toString());
		
    	setResizable(true);
    	setClosable(true);
    	setMaximizable(true);
    	setIconifiable(true);
    	setVisible(true);
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
		if (r != null) { //check if there we're just initializing an empty frame and graph 
		if (type==GraphType.CV) { 
			resSerie = new XYSeries("C-V");
			//xValues = r.getVoltageSerie();
			//yValues = r.getCapacitanceSerie();
			ArrayList<Float> xValues = r.getTimeSerie();
			// Spline
			CubicSpline QvSpline = r.getRoundedQvSpline();
			//

			for (float i=xValues.get(0);i<xValues.get(xValues.size()-1);i=i+(xValues.get(xValues.size()-1)-xValues.get(0))/100){
				resSerie.add(QvSpline.interpolate_for_y_and_dydx(i)[0], r.getCurrent()/QvSpline.interpolate_for_y_and_dydx(i)[1]);
			}
			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(resSerie);
	        _chart = ChartFactory.createXYLineChart(
	        	"Qv c-v"+
	        	_result.getCurrent()+" "+
	        	_result.getMeasurementName(),      // chart title
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
			ArrayList<Float> xValues = r.getTimeSerie();
			// Spline
			CubicSpline QvSpline = r.getQvSpline();
			//

			for (float i=xValues.get(0);i<xValues.get(xValues.size()-1);i=i+(xValues.get(xValues.size()-1)-xValues.get(0))/100){
				resSerie.add(i, QvSpline.interpolate(i));
			}
			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(resSerie);
	        _chart = ChartFactory.createXYLineChart(
		        "Qv v-t"+
		    	_result.getCurrent()+" "+
		    	_result.getMeasurementName(),      // chart title
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
		} // checking null result
		else {
	    	_chart = ChartFactory.createXYLineChart(
	    		type.toString(),      // chart title
	            "Bias [V]",                      // x axis label
	            "Capacitance [pF]",                      // y axis label
	            null,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
		}

		
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
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true,false);
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