package main;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.BadLocationException;

import javax.swing.text.Document;


import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.*;
import java.awt.event.*;


/*
 * This class is the UI of the QvMeas system
 * 
 * @author 	Aleksi Oja, alejee@live.com
 * @version 0.2
 * @since 	0.1
 * 
 */
public class Window extends JFrame implements ActionListener, WindowListener {
	
	//The core object to whom window can pass actions occurred
	private Core _controller;	
	
	//generated serial version uid	
	private static final long serialVersionUID = 8782287186626933285L;

	//textfields for values for the measurement
	private JTextField _current, _step, 
					_numberOfSteps, _filePathField,  
					_currentLimit, _voltageLimit;
	private JComboBox<String> _currentScale, _currentCompScale, _voltageCompScale;
	
	//generate outputfile path
	private JButton _generatePath;
	
	//Output filepath selsection button
	private JButton _selectFile;
	
	//Measurement control buttons
	private JButton _init;	
	private JButton _measure;	
	private JButton _stop;
	
	private JRadioButton _calibration;
	
	//textfieldd and comboboxes for plotting
	private JTextField _xMin;
	private JTextField _xMax;
	private JTextField _yMin;
	private JTextField _yMax;
	private JComboBox<String> _yScale;
	private JComboBox<String> _yLinLog;
	
	//panel for measurement parameters
	private JPanel _paramCont;
	private JInternalFrame _paramFrame;
	
	//frame for measurement control
	private JInternalFrame _controlFrame;
	
	//panel for console view
	private JPanel _consoleCont;
	private JInternalFrame _consoleFrame;
	
	//panel and frame for result view
	private JPanel _resultCont;
	private JInternalFrame _resultFrame;
	
	//frame for the meas history
	private JInternalFrame _historyFrame;
	
	//selectbox for past measurement numbers
	private JComboBox<String> _historyBox;
	
	//textpane to view old datas
	private JTextPane _historyPane;
	
	//comments JTextPane
	private JTextArea _comments;
	
	//name of the structure measured
	private JTextField _name;
	
	//number of the measurement
	private JTextField _number;
	
	//console and result console JTextAreas and JScrollPanes
	private JTextPane _console;
	private JTextPane _result;
	private JScrollPane _sp1;
	private JScrollPane _sp2;
	
	//menubar
	private JMenuBar _menuBar; 
	private JMenu _menu; 		
	private JMenu _winMenu;
	private JMenu _helpMenu;
	
	//File menu item set filepath
	private JMenuItem _setFilePath;
	
	//File menu menuitems
	private JMenuItem _exit;
	
	//Window menu items
	private JMenuItem _paramContMenuItem;
	private JMenuItem _controlContMenuItem;
	private JMenuItem _consoleContMenuItem;
	private JMenuItem _resultContMenuItem;
	private JMenuItem _historyContMenuItem;
	private JMenuItem _cvGraphMenuItem;
	private JMenuItem _vtGraphMenuItem;
	private JMenuItem _manualMenuItem;
	
	//list of GraphFrame ui-objects
	private ArrayList<GraphFrame> _graphFrames = new ArrayList<GraphFrame>();

	//history select button to view old data
	private JButton _historySelect;
	
	private JInternalFrame _manual;
	
	private GraphFrame _currentCV;
	private GraphFrame _currentVT;
	
	//manual textpane
	private JTextPane _helpText;
	
	//boolean to know if manual is initialized
	private boolean _manInit = false;
	
	/*
     * constructor which creates the UI window and shows it 
	 */
	public Window() {	
		setTitle("QvMeas");
		setSize(1000, 860);
		setLocation(100,100);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new GridLayout(1,2));		
		JDesktopPane mainPane = new JDesktopPane();
		setContentPane(mainPane);
		this.addWindowListener(this);
		
		ImageIcon img = new ImageIcon(getClass().getResource("/files/qv_icon.png"));
		this.setIconImage(img.getImage());
		/*
		 * set main menubar
		 */
		_menuBar = new JMenuBar();
		
		/*
		 * File menu
		 */
		_menu = new JMenu("File");
		_menu.setMnemonic(KeyEvent.VK_F);
		_menu.getAccessibleContext().setAccessibleDescription(
		        "File menu");
		
		//set default filepath option
		_setFilePath = new JMenuItem("Set Filepath");
		_setFilePath.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		_setFilePath.setPreferredSize(new Dimension(150,20));
		_setFilePath.addActionListener(this);
		_menu.add(_setFilePath);
		
		//exit option
		_exit = new JMenuItem("Exit");
		_exit.setAccelerator(KeyStroke.getKeyStroke(
					KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		_exit.setPreferredSize(new Dimension(150,20));
		_exit.addActionListener(this);
		_menu.add(_exit);
		
		_menuBar.add(_menu);
		
		
		/*
		 * Window menu
		 */
		_winMenu = new JMenu("Window");
		_winMenu.addMenuListener(new MenuListener() {
			
			@Override
			public void menuSelected(MenuEvent e) {				
				updateWindowMenu();
			}
			
			@Override
			public void menuDeselected(MenuEvent e) {}
			
			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});
		_winMenu.setMnemonic(KeyEvent.VK_W);
		_winMenu.getAccessibleContext().setAccessibleDescription("Window handling menu");
		
		//show / hide parameters
		_paramContMenuItem = new JMenuItem("Parameters");
		_paramContMenuItem.setPreferredSize(new Dimension(150,20));
		_paramContMenuItem.addActionListener(this);
		_winMenu.add(_paramContMenuItem);
		
		//show/hide controls
		_controlContMenuItem = new JMenuItem("Controls");
		_controlContMenuItem.setPreferredSize(new Dimension(150,20));
		_controlContMenuItem.addActionListener(this);
		_winMenu.add(_controlContMenuItem);
		
		//show/hide console
		_consoleContMenuItem = new JMenuItem("Console");
		_consoleContMenuItem.setPreferredSize(new Dimension(150,20));
		_consoleContMenuItem.addActionListener(this);
		_winMenu.add(_consoleContMenuItem);
		
		//show/hide result	
		_resultContMenuItem = new JMenuItem("Result");
		_resultContMenuItem.setPreferredSize(new Dimension(150,20));
		_resultContMenuItem.addActionListener(this);
		_winMenu.add(_resultContMenuItem);
		
		//show/hide history
		_historyContMenuItem = new JMenuItem("History");
		_historyContMenuItem.setPreferredSize(new Dimension(150,20));
		_historyContMenuItem.addActionListener(this);
		_winMenu.add(_historyContMenuItem);
		
		//show/hide cv graph
		_cvGraphMenuItem = new JMenuItem("C-V Graph");
		_cvGraphMenuItem.setPreferredSize(new Dimension(150,20));
		_cvGraphMenuItem.addActionListener(this);
		_winMenu.add(_cvGraphMenuItem);

		//show/hide vt graph
		_vtGraphMenuItem = new JMenuItem("V-T Graph");
		_vtGraphMenuItem.setPreferredSize(new Dimension(150,20));
		_vtGraphMenuItem.addActionListener(this);
		_winMenu.add(_vtGraphMenuItem);
		
		//add window menu to the menubar
		_menuBar.add(_winMenu);
		

		
		/*
		 * Help menu
		 */
		_helpMenu = new JMenu("Help");
		_helpMenu.setMnemonic(KeyEvent.VK_H);
		_helpMenu.getAccessibleContext().setAccessibleDescription("Help menu");
		
		//read manual
		_manualMenuItem = new JMenuItem("Read Manual", KeyEvent.VK_ESCAPE);
		_manualMenuItem.setPreferredSize(new Dimension(150,20));
		_manualMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		_manualMenuItem.addActionListener(this);
		_helpMenu.add(_manualMenuItem);
		
		//attach helpmenu to the menubar
		_menuBar.add(_helpMenu);
		
		//set menubar to the window
		this.setJMenuBar(_menuBar);
		
		
		/*
		 * Parameter main container panel and frame
		 * About the functionality: we're adding small JPanels to large
		 * Panel _paramCont, which is set to be JInternalFrame _paramFrame's
		 * contentpane. Other JFrames and the corresponding JPanel's work the 
		 * same way.
		 */
		_paramCont = new JPanel();
		_paramFrame = createFrame("Meas Parameters");
		_paramFrame.setSize(250,800);
		_paramFrame.setLocation(0,0);
		_paramFrame.setContentPane(_paramCont);
		this.getContentPane().add(_paramFrame);
		
		//control main container panel
		JPanel controlCont = new JPanel();
		_controlFrame = createFrame("Controls");
		_controlFrame.setContentPane(controlCont);
		_controlFrame.setSize(250, 150);
		_controlFrame.setLocation(250,0);
		this.getContentPane().add(_controlFrame);
		
		
		//console main container panel
		_consoleCont = new JPanel();
		_consoleFrame = createFrame("Console");
		_consoleFrame.setContentPane(_consoleCont);
		_consoleFrame.setSize(250,370);
		_consoleFrame.setLocation(250,150);
		//_consoleCont.setPreferredSize(new Dimension(250,670));		
		this.getContentPane().add(_consoleFrame);
		
		//result main container panel and frame
		_resultCont = new JPanel();
		_resultFrame = createFrame("Result");
		_resultFrame.setContentPane(_resultCont);
		_resultFrame.setSize(250,280);
		_resultFrame.setLocation(250,520);
		this.getContentPane().add(_resultFrame);
		
		//measurement history main frame
		JPanel historyCont = new JPanel();
		_historyFrame = createFrame("History");
		_historyFrame.setContentPane(historyCont);
		_historyFrame.setSize(485,200);
		_historyFrame.setLocation(500,0);
		this.getContentPane().add(_historyFrame);
		
		
		/*
		 * Initializing empty graphs for placeholders while waiting test results
		 */
	    drawGraph(null, GraphType.VT); //plotting empty initializing graphs
	    drawGraph(null, GraphType.CV);
		
	    //init components
		initParamComponents();
		initControlComponents();
		initConsoleComponents();
		initHistoryComponents();
		initManual();
	}
	
	
	
	
	/*
	 * Method for initializing components related to measure
	 * parameter input
	 * 
	 * @version 0.1
	 * @since 0.1
	 * @.pre true
	 * @.post (ui window's measurement parameter controls shown on the screen)
	 */
	public void initParamComponents() {
			
		
		/*
		 * Panel for the name of the struct measured
		 */
		JPanel nameTopPanel = new JPanel(); //current settings top container
		nameTopPanel.setPreferredSize(new Dimension(220,100));
		nameTopPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED)
				, "Title"));				

		/*
		 * name setting
		 */		
		JPanel namePanel = new JPanel();
		namePanel.setPreferredSize(new Dimension(80,60));		
		JLabel nameLabel = new JLabel("name");
		nameLabel.setBounds(new Rectangle(80,20));
		namePanel.add(nameLabel);
		_name = new JTextField(6);		
		namePanel.add(_name);
		nameTopPanel.add(namePanel);
		
		/*
		 * Meas no settings
		 */
		JPanel numberPanel = new JPanel();
		numberPanel.setPreferredSize(new Dimension(80,60));		
		JLabel numberLabel = new JLabel("number");
		numberLabel.setBounds(new Rectangle(80,20));
		numberPanel.add(numberLabel);
		_number = new JTextField(4);		
		numberPanel.add(_number);
		
		nameTopPanel.add(numberPanel);		
					
		_paramCont.add(nameTopPanel);
		
		
		/*
		 * Panel for all current related settings
		 */
		JPanel currentTopPanel = new JPanel(); //current settings top container
		currentTopPanel.setPreferredSize(new Dimension(220,100));
		currentTopPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED)
				, "Current"));
		
		
		/*
		 * Current setting
		 */		
		JPanel currentPanel = new JPanel();
		currentPanel.setPreferredSize(new Dimension(80,60));		
		JLabel currentLabel = new JLabel("current");
		currentLabel.setBounds(new Rectangle(80,20));
		currentPanel.add(currentLabel);
		_current = new JTextField(4);
		currentPanel.add(_current);
		currentTopPanel.add(currentPanel);
			
		//current unit ("scale") selection
		JPanel currentScalePanel = new JPanel();
		currentScalePanel.setPreferredSize(new Dimension(80,60));
		JLabel currentScaleLabel = new JLabel("        ");
		currentScaleLabel.setBounds(new Rectangle(80,20));
		currentScalePanel.add(currentScaleLabel);
		_currentScale = new JComboBox<String>();
		_currentScale.setPreferredSize(new Dimension(50,20));
		_currentScale.setEditable(false);
		_currentScale.addItem("pA");
		_currentScale.addItem("nA");
		_currentScale.addItem("uA");
		_currentScale.addItem("mA");
		currentScalePanel.add(_currentScale);
		currentTopPanel.add(currentScalePanel);
		
		_paramCont.add(currentTopPanel); //add current selections and labels to the mainframe
		
		
		/*
		 * Time related values
		 */
		JPanel timeTopPanel = new JPanel(); //Measurement duration top container
		timeTopPanel.setPreferredSize(new Dimension(220,100));
		timeTopPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED)
				, "Measurement Time"));
		
		//step value
		JPanel stepPanel = new JPanel();
		stepPanel.setPreferredSize(new Dimension(80,70));
		JLabel stepLabel = new JLabel("step   ");
		stepLabel.setBounds(new Rectangle(60,20));
		stepPanel.add(stepLabel);
		_step = new JTextField(3);
		stepPanel.add(_step);
		JLabel stepUnitLabel = new JLabel("[s]");
		stepUnitLabel.setBounds(new Rectangle(10,20));
		stepPanel.add(stepUnitLabel);
		timeTopPanel.add(stepPanel);		
		
		//numberOfSteps value
		JPanel numberOfStepsPanel = new JPanel();
		numberOfStepsPanel.setPreferredSize(new Dimension(80,70));
		JLabel numberOfStepsLabel = new JLabel("step count");
		numberOfStepsLabel.setBounds(new Rectangle(80,20));
		numberOfStepsPanel.add(numberOfStepsLabel);
		_numberOfSteps = new JTextField(3);
		numberOfStepsPanel.add(_numberOfSteps);		
		timeTopPanel.add(numberOfStepsPanel);
		
		_paramCont.add(timeTopPanel);
		
		
		/*
		 * compliance values
		 */
		
		//compliances
		JPanel compTopPanel = new JPanel(); //Compliance values top container
		compTopPanel.setPreferredSize(new Dimension(220,110));
		compTopPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED)
				, "Compliance"));
		
		//current comp
		JPanel currentCompPanel = new JPanel();
		currentCompPanel.setPreferredSize(new Dimension(80,80));
		JLabel currentCompLabel = new JLabel("Current");
		currentCompLabel.setBounds(new Rectangle(60,20));
		currentCompPanel.add(currentCompLabel);
		_currentLimit = new JTextField(3);
		currentCompPanel.add(_currentLimit);

		//unit for current compliance
		_currentCompScale = new JComboBox<String>();
		_currentCompScale.setPreferredSize(new Dimension(46,20));
		_currentCompScale.setEditable(false);
		_currentCompScale.addItem("pA");
		_currentCompScale.addItem("nA");
		_currentCompScale.addItem("uA");
		_currentCompScale.addItem("mA");
		currentCompPanel.add(_currentCompScale);
		
		compTopPanel.add(currentCompPanel);
		
		//voltage comp
		JPanel voltageCompPanel = new JPanel();
		voltageCompPanel.setPreferredSize(new Dimension(80,80));
		JLabel voltageCompLabel = new JLabel("Voltage");
		voltageCompLabel.setBounds(new Rectangle(60,20));
		voltageCompPanel.add(voltageCompLabel);
		
		_voltageLimit = new JTextField(3);
		voltageCompPanel.add(_voltageLimit);
	
		_voltageCompScale = new JComboBox<String>();
		_voltageCompScale.setPreferredSize(new Dimension(46,20));
		_voltageCompScale.setEditable(false);
		_voltageCompScale.addItem("mV");
		_voltageCompScale.addItem("V");
		_voltageCompScale.setSelectedItem("V");
		voltageCompPanel.add(_voltageCompScale);

		compTopPanel.add(voltageCompPanel);
		
		_paramCont.add(compTopPanel);
			
		
		
		/*
		 * Set plotting parameters
		 */
		JPanel plotTopPanel = new JPanel();
		plotTopPanel.setLayout(new GridLayout(1,2));
		plotTopPanel.setPreferredSize(new Dimension(220,110));
		//plotTopPanel.setBorder(BorderFactory.createEtchedBorder());
		plotTopPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
											.createEtchedBorder(EtchedBorder.LOWERED)
											, "Plot parameters"));
		
		//xMin Panel
		JPanel xPanel = new JPanel();
		xPanel.setPreferredSize(new Dimension(110,100));
		JLabel xMinLabel = new JLabel("min X ");
		xPanel.add(xMinLabel);
		_xMin = new JTextField(4); //x axis min value field
		xPanel.add(_xMin); 
		JLabel xMaxLabel = new JLabel("max X");
		xPanel.add(xMaxLabel); 
		_xMax = new JTextField(4); //x axis max value field
		xPanel.add(_xMax);
		JLabel xScaleLabel = new JLabel("[s]  ");
		xPanel.add(xScaleLabel);
		JLabel xLinLabel = new JLabel("Lin");
		xPanel.add(xLinLabel);
		plotTopPanel.add(xPanel);
		
		
		//y panel
		JPanel yPanel = new JPanel();
		yPanel.setPreferredSize(new Dimension(110,100));
		JLabel yMinLabel = new JLabel("min Y "); 
		yPanel.add(yMinLabel);
		_yMin = new JTextField(4); // y axis min value field
		yPanel.add(_yMin);
		JLabel yMaxLabel = new JLabel("max Y");
		yPanel.add(yMaxLabel); 
		_yMax = new JTextField(4); // y axis max value field
		yPanel.add(_yMax);
		
		
		_yScale = new JComboBox<String>();
		_yScale.setPreferredSize(new Dimension(44,20));
		_yScale.setEditable(false);		
		_yScale.addItem("mV");
		_yScale.addItem("V");		
		_yScale.setSelectedItem("V");
		yPanel.add(_yScale);
		
		_yLinLog = new JComboBox<String>();
		_yLinLog.setPreferredSize(new Dimension(44,20));
		_yLinLog.addItem("Lin");
		_yLinLog.addItem("Lg");
		yPanel.add(_yLinLog);
		
		
		plotTopPanel.add(yPanel);
		_paramCont.add(plotTopPanel);
		
		
		/*
		 * comments 
		 */
		JPanel commentTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		commentTopPanel.setPreferredSize(new Dimension(220,100));
		commentTopPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory	
			.createEtchedBorder(EtchedBorder.LOWERED),
			 "Comments"));
		_comments = new JTextArea(4,18);
		_comments.setLineWrap(true);
		_comments.setWrapStyleWord(true);
		JScrollPane com = new JScrollPane(_comments);
		com.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		com.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		commentTopPanel.add(com);
		_paramCont.add(commentTopPanel);
		
		_paramCont.add(commentTopPanel);
		/*
		 * file browsing
		 */
		JPanel fileTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); //filebrowsing top container
		fileTopPanel.setPreferredSize(new Dimension(220,100));
		fileTopPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED)
				, "Output File"));

		
		//Select filepath button
		_selectFile = new JButton("Browse");
		setEnterPress(_selectFile);		
		_selectFile.addActionListener(this);
		fileTopPanel.add(_selectFile);
		
		//Genereate filepath button
		_generatePath = new JButton("Generate");
		setEnterPress(_generatePath);
		_generatePath.addActionListener(this);
		fileTopPanel.add(_generatePath);
		
		
		_filePathField = new JTextField(18); //filepath
		_filePathField.setText("");
		fileTopPanel.add(_filePathField);
		
		_paramCont.add(fileTopPanel);
		
	
	}//End of initParamComponents()

	private void initControlComponents() {
		/*
		 * start/stop the system
		 */
		 
		JPanel buttonTopPanel = new JPanel();
		buttonTopPanel.setPreferredSize(new Dimension(220,100));
		buttonTopPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED)
				, "Main controls"));
		
		//init button
		_init = new JButton("INIT");
		_init.addActionListener(this);
		setEnterPress(_init);
		buttonTopPanel.add(_init);
		
		//start button
		_measure = new JButton("START");
		_measure.addActionListener(this);
		setEnterPress(_measure);
		_measure.setEnabled(false);
		buttonTopPanel.add(_measure);
		
		//stop button
		_stop = new JButton("STOP");
		_stop.addActionListener(this);
		setEnterPress(_stop);
		_stop.setEnabled(false);
		buttonTopPanel.add(_stop);
		
		//Auto calibration radio button
		_calibration = new JRadioButton("AUTO CALIBRATION");
		buttonTopPanel.add(_calibration);

		_controlFrame.getContentPane().add(buttonTopPanel);
	}
	
	/*
	 * Method for setting VK_ENTER-KeyEvent to fire JButtons action
	 *	
	 * @version 0.1
	 * @since 	0.2
	 * 
	 * @.pre 	true
	 * @.post 	JButton given as a parameter will be fired with ENTER
	 * 			when focused
	 */
	private void setEnterPress(JButton b) {
		b.registerKeyboardAction(this, 
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), 
				JComponent.WHEN_FOCUSED);
	}
	
	
	
	/*
	 * Method for creating console view related components
	 * 
	 * @version 	0.1
	 * @since 		0.1
	 * @.pre 		true
	 * @.post 		(ui window's console views - console and result console - shown on the screen)
	 */
	public void initConsoleComponents() {
		/*
		 * Console
		 */
		_console = new JTextPane();
		_sp1 = new JScrollPane(_console);
		_sp1.setPreferredSize(new Dimension(220,320));
		_sp1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		_sp1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_console.setEditable(false);
		_console.setBorder(BorderFactory.createEtchedBorder());
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        _console.setFont(font);
		_consoleFrame.getContentPane().add(_sp1);
		
		/*
		 * Result console
		 */
		_result = new JTextPane();
		_sp2 = new JScrollPane(_result);
		_sp2.setPreferredSize(new Dimension(220,230));
		_sp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		_sp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_result.setEditable(false);
		_result.setPreferredSize(new Dimension(220,230));
		_result.setBorder(BorderFactory.createEtchedBorder());
		_result.setFont(font);
        _resultFrame.getContentPane().add(_sp2);
	}
	
	
	/*
	 * Method for creating console view related components
	 * 
	 * @version 	0.1
	 * @since 		0.2
	 * @.pre 		true
	 * @.post 		(ui window's History view on the screen)
	 */
	public void initHistoryComponents() {
		/*
		 * Panel for the meas selection
		 */		
		JPanel historySelectPanel = new JPanel();
		historySelectPanel.setPreferredSize(new Dimension(100,160));
		historySelectPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED)
				, "Meas"));
		//Selectbox for meashistory
		_historyBox = new JComboBox<String>();
		_historyBox.setPreferredSize(new Dimension(70,20));		
		_historyBox.setEditable(false);
		_historyBox.addItem("number");
		historySelectPanel.add(_historyBox);
		
		//history select button
		_historySelect = new JButton("select");
		_historySelect.setPreferredSize(new Dimension(70,25));
		_historySelect.addActionListener(this);
		historySelectPanel.add(_historySelect);
		
		_historyFrame.getContentPane().add(historySelectPanel);
		
		//panel for measurement text data
		JPanel historyTextPanel = new JPanel();
		historyTextPanel.setPreferredSize(new Dimension(350,160));
		//textpane for history meas' data
		_historyPane = new JTextPane();
		_historyPane.setEditable(false);
		JScrollPane historyScroll = new JScrollPane(_historyPane);
		historyScroll.setPreferredSize(new Dimension(350,155));
		historyScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		historyScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		historyTextPanel.add(historyScroll);
		_historyFrame.getContentPane().add(historyTextPanel);
		
	}
	
	
	/*
	 * Method for appending new element to the meas history list
	 * @version 	0.1
	 * @since		0.2
	 * @.pre		true
	 * @.post		UI will contain newItem in the measure select box
	 * 				in the history window
	 */
	public void updateHistoryListing(String newItem) {
		_historyBox.addItem(newItem);
	}
	
	
	/*
	 * Method for showing chosen measurement's data (from the current session)
	 * in the history console
	 * @version 	0.1
	 * @since		0.2
	 * @.pre		true
	 * @.post		history console will contain data of the measurement
	 * 				specified in the select box
	 * 
	 */
	public void updateHistoryData() {		
		String meas = _historyBox.getSelectedItem().toString();
		System.out.println("measurement "+meas+" selected");
		if(!meas.equals("number")) { //to check that there really is a meas chosen
			ArrayList<Result> resArray = _controller.getResults();
			for(Result res : resArray) {
				if (res.getNumber().equals(meas)){
					//init data for the history console
					Document doc = _historyPane.getDocument();
					String nbr = res.getNumber() + "\n";
					String name = res.getName() + "\n";
					String comment = res.getComment() + "\n";
					String data = res.getRawData();
					//print stuff to the history console		
					try {
						doc.remove(0,doc.getLength());
						doc.insertString(doc.getLength(), "Measurement: e"+nbr, null);
						doc.insertString(doc.getLength(), name, null);
						doc.insertString(doc.getLength(), comment, null);
						doc.insertString(doc.getLength(), data, null);
					} catch (Exception e) {
						System.out.println("Couldn't insert to the history console");
					}
					//show chosen graphs - hide others
					showChosenGraphs(meas);
				}
			}
		}
	}
	
	
	/*
	 * Method for showing graphs related to the measurement chosen
	 * 
	 * @version		0.1
	 * @since		0.2
	 * @params 		measNumber - number as String specifying the measurement
	 * 				wanted to be shown
	 * @.pre		true
	 * @.post		Graphs related to the measNumber parameter will be shown
	 */
	private void showChosenGraphs(String measNumber) {
		GraphFrame cv = getGraphFrame(measNumber, GraphType.CV);
		System.out.println("found: "+cv);
		_currentCV.setVisible(false);
		_currentCV = cv;
		cv.setVisible(true);
		GraphFrame vt = getGraphFrame(measNumber, GraphType.VT);
		System.out.println("found: "+vt);
		_currentVT.setVisible(false);
		_currentVT = vt;
		vt.setVisible(true);
	}
	
	
	/*
	 * Method for creating basic JInternalFrame without size or position or contents
	 * 
	 * @version 	0.1
	 * @since 		0.2
	 * 
	 * @return 		empty JInternalFrame without size or position
	 * 
	 * @.pre 		true
	 * @.post 		(new JInternalFrame will be added to the corresponding ArrayList)
	 */
    private JInternalFrame createFrame(String t) {
    	JInternalFrame f = new JInternalFrame(t);
    	f.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
    	f.setResizable(true);
    	f.setClosable(true);
    	f.setMaximizable(true);
    	f.setIconifiable(true);
    	f.setVisible(true);
    	return f;
	}
    
    
    
	/*
	 * Following three methods for setting start and measurement buttons active or inactive 
	 * 
	 * @version		0.2
	 * @since 		0.1
	 * @.pre 		b != null
	 * @.post 		(mearurement control buttons start, stop and init will be active or
	 * 				inactive depending on the boolean b parameter) 
	 */
	public void setStartStatus(boolean b) {
		_measure.setEnabled(b);		
		_measure.requestFocus();
	}	
	public void setInitStatus(boolean b) {
		_init.setEnabled(b);
		_init.requestFocus();
	}
	public void setStopStatus(boolean b) {
		_stop.setEnabled(b);
		_stop.requestFocus();
	}
	
	
	
	
	/*
	 * Method for giving a reference to a Core object for this
	 * Then the UI events can be passed for the Core controller object
	 * 
	 * @version	 0.1
	 * @since	 0.1
	 * @.pre	 controller != null
	 * @.post	 (Core-type controller object will be added to this so 
	 * 			 	user event's can be passed forward) 
	 */	
	public void setController(Core controller) {
		_controller = controller;
		System.out.println("controller set for window");
	}
	
	
	
	/*
	 * Method for measurement name generation
	 * @.pre 		true
	 * @.post		true
	 * @return		Name for a measurement based on the current date and time
	 * 				and the user specified parameters
	 */
	private String generateName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date rawDate = new Date();
		String date = sdf.format(rawDate);
		String genPath = "";
		genPath+=_name.getText()+" "+
				_current.getText()+"A "+
				_step.getText()+"s "+
				"e"+_number.getText()+" "+
				date+".qv";
		return genPath;
	}
	
	
	
	/*
	 * Method for showing and hiding the window specified
	 * 
	 * @.post	parameter specified frame will be turned off or on (visibility) depending
	 * 			its current state. The corresponding menu item text will be changed too
	 */
	private void switchWindowState(JInternalFrame frame) {
		if(frame.isVisible()) {
			frame.setVisible(false);
		}
		else {
			frame.setVisible(true);
		}
	}
	
	
	/*
	 * Method for getting a GraphFrame from a measurement done
	 * 
	 * @version				0.1
	 * @since				0.2
	 * @param	measNumber	The number of the measurement which graph is wanted to
	 * 						be drawn to the screen
	 * @param	type		Type of the graph - each measurement has two: CV and VT graphs		
	 * @return				GraphFrame of the specified measurement and type
	 * @.pre				true
	 * @.post				true
	 * 
	 */
	public GraphFrame getGraphFrame(String measNumber, GraphType type) {
		GraphFrame ret = null;
		System.out.println("found "+_graphFrames.size()+" frames");
		for (GraphFrame gf : _graphFrames) {
			if(gf.getResult()!=null) {
				if(gf.getResult().getNumber().equals(measNumber)&&gf.getType()==type) {
					ret=gf;
				}			
			}
		}
		return ret;	
	}
	
	
	
	/*
	 * ActionListener for ui events. Method will call Core object's corresponding
	 * event handler methods
	 * 
	 * @version 0.2
	 * @since 0.1
	 * @.pre event != null
	 * @.post (Core objects corresponding the event handler method will be called) 
	 *  
	 */	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(_setFilePath)) {	//Menuevent setting def filepath
			System.out.println("new file path setting");
			_controller.chooseDirectory();			    
		} 
		else if (event.getSource().equals(_paramContMenuItem)) { // Menuevent show/hide parameter window
			switchWindowState(_paramFrame);			
		} 
		else if (event.getSource().equals(_controlContMenuItem)) {
			switchWindowState(_controlFrame);
		} 
		else if (event.getSource().equals(_consoleContMenuItem)) {
			switchWindowState(_consoleFrame);
		}
		else if (event.getSource().equals(_resultContMenuItem)) {
			switchWindowState(_resultFrame);
		}
		else if (event.getSource().equals(_historyContMenuItem)) {
			switchWindowState(_historyFrame);
		}
		else if (event.getSource().equals(_cvGraphMenuItem)) {
			switchWindowState(_currentCV);
		}
		else if (event.getSource().equals(_vtGraphMenuItem)) {
			switchWindowState(_currentVT);					
		}
		else if (event.getSource().equals(_manualMenuItem)) {
			System.out.println("Using manual");
			//init manual text if not yet initialized
			if(!_manInit) {
				String text = _controller.helpRequest();
				Document doc = _helpText.getDocument();
				try {
					doc.insertString(doc.getLength(), text, null);		
					_manInit=true;
				} catch (BadLocationException e) {}				
				_helpText.setEditable(false);
				_helpText.setFont(new Font(Font.MONOSPACED,1,12));
			}
			_helpText.setCaretPosition(0);
			//show manual 
			_manual.setVisible(true);			
		}
		else if(event.getSource().equals(_historySelect)) { //old measurement selected in history
			updateHistoryData();
		}
		else if(event.getSource().equals(_selectFile)) { //file selection 
			_controller.chooseFile(generateName());
		} 
		else if(event.getSource().equals(_generatePath)){ //autogenerate filename 
			if (_controller.getDefaultDirectory().equals("")) {
				JOptionPane.showMessageDialog(new JFrame(),					    
					    "First choose the default directory for the results",
					    "Default path needed",
					    JOptionPane.OK_CANCEL_OPTION);
				_controller.chooseDirectory();
			}
			_filePathField.setText(_controller.getDefaultDirectory()+"\\"+generateName());
		}
		else if (event.getSource().equals(_measure)){	//Start measurement clicked
			try {
				_controller.start();
			} catch(MeasNotInitException e) {
				printToConsole("Measurement not initialized!\n");
			}		
		} 
		else if(event.getSource().equals(_stop)){ //stop measurement clicked
			_controller.stopMeas();
		} 
		else if(event.getSource().equals(_init)) { //init measurement clicked

			try{
				if(_controller.instrumentCreated()){			
					//meas params: current, currentscale, step, nosteps, currentcomp, ccscale, voltagecomp, vcscale, xMin, xMax, yMin, yMax, ycompscale, yLinLog, comments, name (of the struct), calibration			
					ArrayList<String> params = new ArrayList<String>();
		/* 0 */			params.add(_current.getText());
		/* 1 */			params.add(convertToExpVal(_currentScale.getSelectedItem().toString()));
		/* 2 */			params.add(_step.getText());
		/* 3 */			params.add(_numberOfSteps.getText());
		/* 4 */			params.add(_currentLimit.getText());
		/* 5 */			params.add(convertToExpVal(_currentCompScale.getSelectedItem().toString()));
		/* 6 */			params.add(_voltageLimit.getText());
		/* 7 */			params.add(convertToExpVal(_voltageCompScale.getSelectedItem().toString()));
		/* 8 */			params.add(_xMin.getText());
		/* 9 */			params.add(_xMax.getText());
		/* 10 */ 		params.add(_yMin.getText());
		/* 11 */		params.add(_yMax.getText());
		/* 12 */		params.add(convertToExpVal(_yScale.getSelectedItem().toString()));
		/* 13 */		params.add(_yLinLog.getSelectedItem().toString());
		/* 14 */		params.add(_comments.getText());
		/* 15 */		params.add(_name.getText());
		/* 16 */		params.add(_number.getText());
						if(_calibration.isSelected()){
		/* 17 */				params.add("ON");
						} else {
		/* 17 */				params.add("OFF");
						}
				_controller.initMeas(params);
				} else {
					printToConsole("Couldn't access hardware\n");
				}
			} catch(ValuesNotValidException e){
				printToConsole(e.getMessage()); 
			}
		
		} else if(event.getSource().equals(_exit)) {
			exitSystem();
		}
		
	}
	

	
	/*
	 * Method for updating Window-menu's components' state
	 * 
	 * @version			0.1
	 * @since			0.2
	 * @.pre			true
	 * @.post			Window menu will contain the proper MenuItem texts based on the
	 * 					current state of the windows' visibility.
	 */
	private void updateWindowMenu() {
		//parameters
		if(_paramFrame.isVisible()) _paramContMenuItem.setText("Hide Parameters");
		else _paramContMenuItem.setText("Show Parameters");
		//controls
		if(_controlFrame.isVisible()) _controlContMenuItem.setText("Hide Controls");
		else _controlContMenuItem.setText("Show Controls");
		//console
		if(_consoleFrame.isVisible()) _consoleContMenuItem.setText("Hide Console");
		else _consoleContMenuItem.setText("Show Console");
		//result
		if(_resultFrame.isVisible()) _resultContMenuItem.setText("Hide Result");
		else _resultContMenuItem.setText("Show Result");
		//history
		if(_historyFrame.isVisible()) _historyContMenuItem.setText("Hide History");
		else _historyContMenuItem.setText("Show History");
		//cv graph
		if(_currentCV.isVisible()) _cvGraphMenuItem.setText("Hide C-V Graph");
		else _cvGraphMenuItem.setText("Show C-V Graph");
		//vt graph
		if(_currentVT.isVisible()) _vtGraphMenuItem.setText("Hide V-T Graph");
		else _vtGraphMenuItem.setText("Show V-T Graph");
	}
	
	
	/*
	 * Method for converting input field values to exponent format
	 * @version 0.1
	 * @since 0.1
	 * @.pre s != null
	 * @.post @return == (exponent val - type "E-12" - String corresponding the
	 * 			parameter' - "pA" - type input. 
	 */
	public String convertToExpVal(String s) {
		String res = "";
		if(s.equals("pA")) res = "E-12";
		else if(s.equals("nA")) res = "E-9";
		else if(s.equals("uA")) res = "E-6";
		else if(s.equals("mA")||s.equals("mV")) res = "E-3";
		else res = "";
		return res;
	}
	
	public void writeFilePath(String path) {
		System.out.println(path);
		_filePathField.setText(path);
	}
	
	public String readFilePath() {
		return _filePathField.getText();
	}
	
	
	
	
	
	/*
	 * Printing string-object to the console view
	 * @version 0.1
	 * @since 0.1
	 * @.pre s != null
	 * @.post s will be appended to the end of the console text AND
	 *  		console will be scrolled down with the text
	 */
	public void printToConsole(String s) {
		try {			
			Document doc = _console.getDocument(); //getting document to which JTextArea is writing to
			doc.insertString(doc.getLength(), s, null);	//insert new text to the end of the document	
			_console.setCaretPosition(_console.getDocument().getLength()); //set caret position to the end: scroll effect because of JScrollPane usage
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/*
	 * Printing to the result console
	 * @version 0.1
	 * @since 0.1
	 * @.pre s != null
	 * @.post s will be appended to the end of the result console text AND
	 *  		result console will be scrolled down with the text
	 */
	public void printToResultConsole(String s) {
		try {		
			Document doc = _result.getDocument(); //get doc to write
			doc.insertString(doc.getLength(), s, null); // insert text
			_result.setCaretPosition(_result.getDocument().getLength()); //move caret for scrolling effect
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/*
	 * draw to the screen  
	 * @version 0.1
	 * @since 	0.2
	 * @.pre 	true
	 * @.post 	graph - type defined by type-param - will be plotted from
	 * 			the data in the Result-parameter 
	 * 			AND the last element int the getGraphs() -list will be the Result-parameter
	 */
	public void drawGraph(Result res, GraphType type) {	
		//do GraphWindow initialization
		GraphFrame gf = new GraphFrame(res, type);
		gf.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);		
		this.getContentPane().add(gf); //add new graph to the main window
		gf.moveToFront(); // bring it on top
		_graphFrames.add(gf);			
		if(type.equals(GraphType.CV)) {
			if(_currentCV!=null) _currentCV.setVisible(false); //hide the previous graphs
			_currentCV = gf; //mark the new GraphFrame drawn to the screen
		}
		else if(type.equals(GraphType.VT)) {
			if(_currentVT!=null) _currentVT.setVisible(false); //hide the previous graphs
			_currentVT = gf; //mark the new GraphFrame drawn to the screen
		}
	}
	
	
	
	/*
	 * Return all GraphFrames representing results measured during the session
	 * @version 	0.1
	 * @since 		0.2
	 * @.pre 		true
	 * @.post		true
	 * @.result		(All graphframes from this session)
	 */
	public ArrayList<GraphFrame> getGraphs() { return _graphFrames; } 
	
	
	
	/*
	 * Method for creating manual window which then can be opened from the menu
	 * @version 	0.1
	 * @since 		0.2
	 * @.pre 		true
	 * @.post		Manual window will be shown on top of other windows
	 */
	public void initManual() {
		_manual = new JInternalFrame();
    	_manual.setResizable(true);
    	_manual.setClosable(true);
    	_manual.setMaximizable(true);
    	_manual.setIconifiable(true);
    	_manual.setSize(985,800);		
    	_manual.setLocation(0,0);
    	_manual.setContentPane(new JPanel());
    	_manual.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
    	this.getContentPane().add(_manual);
		_manual.moveToFront();
		_helpText = new JTextPane();
		JScrollPane helpScroll = new JScrollPane(_helpText);
		helpScroll.setPreferredSize(new Dimension(975,760));
		helpScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		helpScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_manual.getContentPane().add(helpScroll);
	}

	
	/*
	 * Method for showing the exit confirmation dialog and 
	 * exiting if wanted
	 * 
	 * @version		0.1
	 * @since 		0.2
	 * @.pre		true
	 * @.post		Depending on user action the system will exit or continue without any change
	 * 
	 */
	public void exitSystem() {
		 int result = JOptionPane.showConfirmDialog((Component) null, 
					"QvMeas_0.2 doesn't save your session. Just data is saved. Do you still want to quit?",
					"alert", JOptionPane.OK_CANCEL_OPTION);
		 if(result==0) System.exit(0);		
	}


	/*
	 * (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 *
	 * Window listener for main window operations (at least exit confirmation is used)
	 */
	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowClosing(WindowEvent e) {
		exitSystem();
	}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}
}
