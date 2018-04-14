package view;

import model.EVCharger;
import model.EVCustomer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class GanttChart extends ApplicationFrame {

    private List<EVCharger> chargers;
    private int number;
    private static final int YEAR = 2018;
    private static final int MONTH = Calendar.APRIL;
    private static final int DAY = 13;


    public GanttChart(List<EVCharger> chargers, final String title, int number) {
        super(title);
        this.chargers = chargers;
        this.number = number;

        final IntervalCategoryDataset dataset = createSampleDataset();
        final JFreeChart chart = createChart(dataset, title);
        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    public static JFreeChart createChart(IntervalCategoryDataset dataset, String title){
        final JFreeChart chart = ChartFactory.createGanttChart(
                title,  // chart title
                "EV Chargers",              // domain axis label
                "Time",              // range axis label
                dataset,             // data
                true,                // include legend
                true,                // tooltips
                false                // urls
        );
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();

        MyGanttRenderer renderer = new MyGanttRenderer();
        renderer.setShadowVisible(false);
        plot.setRenderer(renderer);
        renderer.setSeriesPaint(0, Color.blue);
        Paint p = new GradientPaint(0, 0, Color.white, 1000, 0, Color.cyan);
        chart.setBackgroundPaint(p);
        plot.setBackgroundPaint(Color.white);
        return chart;
    }

    private IntervalCategoryDataset createSampleDataset() {
        TaskSeries s = new TaskSeries(number + " EVs Scheduled");
        for(EVCharger c : chargers){
            if (c.getAssignedList().isEmpty())
                continue;
            Task task = new Task(
                    "Charger ID " + c.getID() + " Type " + c.getChargerType(),
                    date(0, 0), date(24, 0));
//            List<Task> sbtask = new ArrayList<>();
            for(EVCustomer ev : c.getAssignedList()){
//                System.out.println("======");
                Task subtask = new Task(
                    "EV ID " + ev.getEvID() + " Type " + ev.getTypeID(),
                    date(ev.getAssignedStartTime().getHour(), ev.getAssignedStartTime().getMinute()),
                    date(ev.getAssignedEndTime().getHour(), ev.getAssignedEndTime().getMinute())
                );
                task.addSubtask(subtask);
            }
            s.add(task);
        }

        TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s);
        return collection;
    }

    private static Date date(int hour, int minute) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(YEAR, MONTH, DAY, hour, minute, 0);
        final Date result = calendar.getTime();
        return result;
    }

    public void run () {
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }

    private static class MyGanttRenderer extends GanttRenderer {

        // private static final int PASS = 2; // assumes two passes
        private final List<Color> clut = new ArrayList<Color>();
        private int row;
        private int col;
        private int index = -1;

        public MyGanttRenderer() {
            // initialize clut
            clut.add(0, new Color(51, 102, 255));   // blue
            clut.add(1, new Color(100, 255, 102));  // light green
            clut.add(2, new Color(255, 102, 102));  // light red
            clut.add(3, new Color(204, 153, 255));  // light light purple
            clut.add(4, new Color(100, 204, 204));  // cyan
            clut.add(5, new Color(153, 153, 255));  // light blue
            clut.add(6, new Color(255, 204, 102));  // yellow
        }
        int r = 0;

        @Override

        public Paint getItemPaint(int row, int col) {

            if (this.row == row && this.col == col) {

                if (index < 6) {
                    index++;
                } else {
                    index = 0;
                }
            } else {
                this.row = row;
                this.col = col;
                index = 0;
            }
            System.out.println("this is index: "+index);
            return clut.get(index);
        }
    }
}
