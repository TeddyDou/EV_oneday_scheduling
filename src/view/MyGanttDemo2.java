package view;


import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;
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

/**
 * A simple demonstration application showing how to create a Gantt chart with
 * multiple bars per task.
 */
public class MyGanttDemo2 extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title the frame title.
     */
    public MyGanttDemo2(String title) {
        super(title);
        IntervalCategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new java.awt.Dimension(600, 300));
        setContentPane(chartPanel);
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset the dataset.
     *
     * @return A sample chart.
     */
    private static JFreeChart createChart(IntervalCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createGanttChart(
                "Machine Center Schedule", // chart title
                "Machine Center", // domain axis label
                "Date", // range axis label
                dataset, // data
                true, // include legend
                true, // tooltips
                false // urls
        );
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        // plot.getDomainAxis().setMaximumCategoryLabelWidthRatio(10.0f);
        MyGanttRenderer renderer = new MyGanttRenderer();
        renderer.setShadowVisible(false);
        plot.setRenderer(renderer);
        renderer.setSeriesPaint(0, Color.blue);
        Paint p = new GradientPaint(0, 0, Color.white, 1000, 0, Color.cyan);
        chart.setBackgroundPaint(p);
        //Paint q = new GradientPaint(0, 0, Color.white, 1000, 0, Color.white);
        plot.setBackgroundPaint(Color.WHITE);
        return chart;
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
            return clut.get(index);
        }
    }

    /**
     * Creates a sample dataset for a Gantt chart, using sub-tasks. In general,
     * you won't hard-code the dataset in this way - it's done here so that the
     * demo is self-contained.
     *
     * @return The dataset.
     */
    private static IntervalCategoryDataset createDataset() {
        TaskSeries s1 = new TaskSeries("Schedule");
        Task t1 = new Task(
                "mc-101",
                date(1, Calendar.APRIL, 2014), date(5, Calendar.APRIL, 2014)
        );
        Task st11 = new Task(
                "w-123 o-10",
                date(1, Calendar.APRIL, 2014), date(5, Calendar.APRIL, 2014)
        );
        s1.add(t1);
        Task t2 = new Task(
                "mc-102",
                date(1, Calendar.APRIL, 2014), date(9, Calendar.APRIL, 2014)
        );
        s1.add(t2);
        Task t3 = new Task(
                "mc-103",
                date(1, Calendar.APRIL, 2014), date(24, Calendar.MAY, 2014)
        );
        Task st31 = new Task(
                "mc-103 1",
                date(1, Calendar.APRIL, 2014), date(7, Calendar.APRIL, 2014)
        );
        Task st32 = new Task(
                "w-123 o-20",
                date(9, Calendar.APRIL, 2014), date(14, Calendar.APRIL, 2014)
        );
        Task st33 = new Task(
                "w-124 o-20",
                date(15, Calendar.APRIL, 2014), date(19, Calendar.APRIL, 2014)
        );
        Task st34 = new Task(
                "mc-103 4",
                date(21, Calendar.APRIL, 2014), date(24, Calendar.APRIL, 2014)
        );
        Task st35 = new Task(
                "mc-103 5",
                date(25, Calendar.APRIL, 2014), date(28, Calendar.APRIL, 2014)
        );
        Task st36 = new Task(
                "mc-103 6",
                date(1, Calendar.MAY, 2014), date(6, Calendar.MAY, 2014)
        );
        Date a = date(11, Calendar.MAY, 2014);
        Task st37 = new Task(
                "mc-103 7",

                date(7, Calendar.MAY, 2014), a
        );
        Task st38 = new Task(
                "mc-103 8",
                a, date(21, Calendar.MAY, 2014)
        );
        t3.addSubtask(st31);
        t3.addSubtask(st32);
        t3.addSubtask(st33);
        t3.addSubtask(st34);
        t3.addSubtask(st35);
        t3.addSubtask(st36);
        t3.addSubtask(st37);
        t3.addSubtask(st38);
        s1.add(t3);
        // and another...
        Task t4 = new Task(
                "mc-104",
                date(1, Calendar.APRIL, 2014), date(27, Calendar.APRIL, 2014)
        );
        Task st41 = new Task(
                "mc-104 1",
                date(1, Calendar.APRIL, 2014), date(3, Calendar.APRIL, 2014)
        );
        //st41.setPercentComplete(1.0);
        Task st42 = new Task(
                "mc-104 2",
                date(4, Calendar.APRIL, 2014), date(12, Calendar.APRIL, 2014)
        );
        Task st43 = new Task(
                "w-124 o-30",
                date(14, Calendar.APRIL, 2014), date(27, Calendar.APRIL, 2014)
        );
        Task st44 = new Task(
                "w-123 o-40",
                date(29, Calendar.APRIL, 2014), date(6, Calendar.MAY, 2014)
        );
        t4.addSubtask(st41);
        t4.addSubtask(st42);
        t4.addSubtask(st43);
        t4.addSubtask(st44);
        s1.add(t4);
        Task t5 = new Task(
                "mc-105",
                date(1, Calendar.APRIL, 2014), date(17, Calendar.APRIL, 2014)
        );
        s1.add(t5);
        TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);
        return collection;
    }

    /**
     * Utility method for creating <code>Date</code> objects.
     *
     * @param day the date.
     * @param month the month.
     * @param year the year.
     *
     * @return A date.
     */
    private static Date date(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;
    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     */
    public static void main(String[] args) {
        MyGanttDemo2 demo = new MyGanttDemo2("Machine Center Schedule");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }
}