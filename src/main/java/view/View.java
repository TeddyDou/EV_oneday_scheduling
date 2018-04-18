package view;

import model.EVCharger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Created by Ted on 3/20/2018.
 */
public class View extends JFrame {

    final static int WIDTH = 400;
    final static int HEIGHT = 400;
    private JButton browseButton;
    private JButton loadButton;
    private JButton excelButton;
    private JButton ganttButton;
    private JLabel systemLable;
    private File inputFile;
    private List<EVCharger> chargers;

    public View(String title){
        super(title);
        initComponent();
        initWindow();
    }

    private void initComponent() {
        browseButton = new JButton("Browse");
        loadButton = new JButton("Load");
        ganttButton = new JButton("Gantt Chart");
        excelButton = new JButton("Excel");
        systemLable = new JLabel("Select the input file");
    }

    private void initWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    //add component to frame
    public void init(JButton button1, JButton button2) {
        this.getContentPane().removeAll();
        this.getContentPane().setLayout(new BorderLayout());
        GroupLayout layout = new GroupLayout(this.getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup().addContainerGap(100, 150).addGroup(layout
                .createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(systemLable)
                .addComponent(button1).addComponent(button2)));
        layout.setVerticalGroup(layout.createSequentialGroup().addContainerGap(50, 80).addComponent(systemLable)
                .addComponent(button1).addComponent(button2));
        layout.linkSize(SwingConstants.HORIZONTAL, button1, button2);
        this.getContentPane().setLayout(layout);
    }

    public void selectFile(){
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("../implementation"));
        int returnVal = fc.showOpenDialog(this.getContentPane());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            inputFile = fc.getSelectedFile();
            systemLable.setText("File selected: " + inputFile.getName() + ".");
        }
    }

    public JLabel getSystemLable() {
        return systemLable;
    }

    public void setSystemLable(JLabel systemLable) {
        this.systemLable = systemLable;
    }

    public JButton getBrowseButton() {
        return browseButton;
    }

    public void setBrowseButton(JButton browseButton) {
        this.browseButton = browseButton;
    }

    public JButton getLoadButton() {
        return loadButton;
    }

    public void setLoadButton(JButton loadButton) {
        this.loadButton = loadButton;
    }

    public File getInputFile() {
        return inputFile;
    }

    public boolean isFileValid() {
        try{
            String fileType = inputFile.getName().substring(inputFile.getName().lastIndexOf(".") + 1);
//            System.out.println(fileType);
            if (!inputFile.exists()||(!fileType.equals("xls") && !fileType.equals("xlsx"))){
                invalidFileMsg();
                return false;
            }
        }catch (NullPointerException e) {
            noFileMsg();
            return false;
        }
        return true;
    }

    public void noFileMsg() {
        JOptionPane.showMessageDialog(this, "Please select input Excel file",
                "Invalid File", JOptionPane.WARNING_MESSAGE);
    }

    public void invalidFileMsg() {
        JOptionPane.showMessageDialog(this, "Input file should be in Excel format",
                "Invalid File", JOptionPane.WARNING_MESSAGE);
        //reset buttons on user interface
        browseButton.setEnabled(true);
        loadButton.setEnabled(true);
    }

    public void fileNoInputMsg(){
        JOptionPane.showMessageDialog(this, "Couldn't find sheet named 'input'",
                "Invalid File", JOptionPane.WARNING_MESSAGE);
        //reset buttons on user interface
        browseButton.setEnabled(true);
        loadButton.setEnabled(true);
    }

    public void showProcessing() {
        browseButton.setEnabled(false);
        loadButton.setEnabled(false);
        systemLable.setText("The system is Scheduling...");
    }

    public JButton getExcelButton() {
        return excelButton;
    }

    public void setExcelButton(JButton excelButton) {
        this.excelButton = excelButton;
    }

    public JButton getGanttButton() {
        return ganttButton;
    }

    public void setGanttButton(JButton ganttButton) {
        this.ganttButton = ganttButton;
    }

    public void showPostprocessing(List<EVCharger> chargers) {
        systemLable.setText("Finish calculation. Select your preferred output format.");
        this.init(ganttButton, excelButton);
        this.chargers = chargers;
    }

    public void generateGantt(int number){
        new GanttChart(chargers, "Scheduling Gantt Chart", number).run();
    }

}
