package main;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import flanagan.interpolation.CubicSpline;

import java.text.SimpleDateFormat;
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
	private static final Point _CVLOCATION = new Point(500,200);
	private static final Point _VTLOCATION = new Point(500,500);
	private static final Dimension _SIZE = new Dimension(485,300);
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
		Date rawDate = null;
		String date = null;
		
		
		XYSeries resSerie = null;
		if (r != null) { 
			rawDate = r.getDate(); //date from result
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			date = sdf.format(rawDate);
			//check if there we're just initializing an empty frame and graph 
		if (type==GraphType.CV) { 
			resSerie = new XYSeries("C-V");
			sdf = new SimpleDateFormat("yyyy.MM.dd");
		//	String date = sdf.format(rawDate);
			ArrayList<Float> xValues = r.getTimeSerie();
			// Spline
			CubicSpline QvSpline = r.getQvSpline();
			//

			for (float i=xValues.get(0);i<xValues.get(xValues.size()-1);i=i+(xValues.get(xValues.size()-1)-xValues.get(0))/100){
				resSerie.add(QvSpline.interpolate_for_y_and_dydx(i)[0], r.getCurrent()/QvSpline.interpolate_for_y_and_dydx(i)[1]);
			}
			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(resSerie);
	        _chart = ChartFactory.createXYLineChart(
	        	_result.getName()+
	        	_result.getCurrent()+"A "+
	        	_result.getNumber()+" "+
	        	date,				      // chart title
	            "Bias [V]",               // x axis label
	            "Capacitance [pF]",       // y axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
	        TextTitle tt = _chart.getTitle(); 
	        tt.setFont(new Font(Font.MONOSPACED,1,12));
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
		        	_result.getName()+
		        	_result.getCurrent()+"A "+
		        	_result.getNumber()+" "+
		        	date,				      // chart title
	            "Time [s]",               // x axis label
	            "Voltage [V]",            // y axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
	        
	        TextTitle tt = _chart.getTitle(); 
	        tt.setFont(new Font(Font.MONOSPACED,1,12));
		} else {
			throw new IllegalArgumentException();
		}
		} // end of the not null result case
		else { // if result is null
	    	_chart = ChartFactory.createXYLineChart(
	    		type.toString(),      // chart title
	            "",                      // x axis label
	            "",                      // y axis label
	            null,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
	        TextTitle tt = _chart.getTitle(); 
	        tt.setFont(new Font(Font.MONOSPACED,1,14));
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
	
	/*
	 * method for getting result related to this graph 
	 * 
	 * @version 	0.1
	 * @since		0.2
	 * @return 		Result-object
	 * @.pre		true
	 * @.post 		true
	 */
	public Result getResult() {
		return _result;
	}

}