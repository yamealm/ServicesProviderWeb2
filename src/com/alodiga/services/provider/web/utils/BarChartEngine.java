package com.alodiga.services.provider.web.utils;

import java.awt.Color;
import java.awt.GradientPaint;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.zkoss.zkex.zul.impl.JFreeChartEngine;
import org.zkoss.zul.Chart;

public class BarChartEngine extends JFreeChartEngine {

    private static final long serialVersionUID = 7403971915041414493L;
    private String title;

    public BarChartEngine(String title) {
        this.title = title;
    }

    public boolean prepareJFreeChart(JFreeChart jfchart, Chart chart) {
        jfchart.setTitle(title);
        CategoryPlot plot = jfchart.getCategoryPlot();
        jfchart.setBackgroundPaint(Color.red);
        plot.setBackgroundImage(null);
        plot.setBackgroundPaint(Color.blue);
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        CategoryAxis domainAxis = plot.getDomainAxis();
        //Rotation
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 8.0)
        );

        //Gradient paints
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f,  new Color(20, 206, 20), 0.0f, 0.0f, new Color(128, 206, 128));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, new Color(255, 151, 7), 0.0f, 0.0f, new Color(255, 189, 96));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, new Color(53, 171, 255), 0.0f, 0.0f, new Color(132, 197, 255));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        return false;
    }

}