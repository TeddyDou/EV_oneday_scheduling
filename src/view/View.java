package src.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by Ted on 3/20/2018.
 */
public class View extends JFrame {

    final static int WIDTH = 400;
    final static int HEIGHT = 400;
    private JButton browseButton;
    private JButton loadButton;
    private JLabel systemLable;
    private File inputFile;

    public View(String title){
        super(title);
        initComponent();
        initWindow();
    }

    private void initComponent() {
        browseButton = new JButton("Browse");
        loadButton = new JButton("Load");
        systemLable = new JLabel("Select the input file");
//        loadLable = new JLabel("Load and make schedule");
    }

    private void initWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    //add component to frame
    public void init() {
        this.getContentPane().setLayout(new BorderLayout());
        GroupLayout layout = new GroupLayout(this.getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup().addContainerGap(100, 150).addGroup(layout
                .createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(systemLable)
                .addComponent(browseButton).addComponent(loadButton)));
        layout.setVerticalGroup(layout.createSequentialGroup().addContainerGap(50, 80).addComponent(systemLable)
                .addComponent(browseButton).addComponent(loadButton));
        layout.linkSize(SwingConstants.HORIZONTAL, browseButton, loadButton);
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
            System.out.println(fileType);
            if (!inputFile.exists()||(!fileType.equals("xls") && !fileType.equals("xlsx"))){
                System.out.println((fileType.equals("xlsx")));
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
}
