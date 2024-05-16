package com.example.demo.Service.impl;
import com.example.demo.Service.PdfReportService;
import com.example.demo.Service.PurchaseService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Service
public class PdfReportServiceImpl implements PdfReportService {

    @Override
    public void generatePdfReport(Map<String, PurchaseService.PlanSalesData> salesData,
                                  Map<LocalDate, Map<String, PurchaseService.PlanSalesData>> dailySalesData,
                                  PurchaseService.PlanSalesData totalSalesData,
                                  HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=SalesReport.pdf");
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 添加报告标题
        document.add(new Paragraph("Sales Report"));
        document.add(new Paragraph("Total Sales: " + totalSalesData.getTotalSales()));
        document.add(new Paragraph("Total Count: " + totalSalesData.getCount()));

        // 添加每日销售数据
        Table table = new Table(new float[]{4, 4, 4});
        table.addHeaderCell("Date");
        table.addHeaderCell("Plan");
        table.addHeaderCell("Sales Data");

        dailySalesData.forEach((date, planSalesDataMap) -> {
            planSalesDataMap.forEach((plan, planSalesData) -> {
                table.addCell(date.toString());
                table.addCell(plan);
                table.addCell(String.valueOf(planSalesData.getTotalSales()));
            });
        });

        document.add(table);

        // 关闭文档
        document.close();
    }

    @Override
    public void generateChartPdfReport(Map<String, PurchaseService.PlanSalesData> salesData,
                                       Map<LocalDate, Map<String, PurchaseService.PlanSalesData>> dailySalesData,
                                       PurchaseService.PlanSalesData totalSalesData,
                                       HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=SalesReportWithChart.pdf");
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 添加报告标题
        document.add(new Paragraph("Sales Report"));
        document.add(new Paragraph("Total Sales: " + totalSalesData.getTotalSales()));
        document.add(new Paragraph("Total Count: " + totalSalesData.getCount()));

        // 添加每日销售数据表格
        Table table = new Table(new float[]{4, 4, 4});
        table.addHeaderCell("Date");
        table.addHeaderCell("Plan");
        table.addHeaderCell("Sales Data");

        dailySalesData.forEach((date, planSalesDataMap) -> {
            planSalesDataMap.forEach((plan, planSalesData) -> {
                table.addCell(date.toString());
                table.addCell(plan);
                table.addCell(String.valueOf(planSalesData.getTotalSales()));
            });
        });

        document.add(table);

        // 生成图表
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dailySalesData.forEach((date, planSalesDataMap) -> {
            planSalesDataMap.forEach((plan, planSalesData) -> {
                dataset.addValue(planSalesData.getTotalSales(), plan, date);
            });
        });

        JFreeChart barChart = ChartFactory.createBarChart(
                "Daily Sales Report",
                "Date",
                "Sales",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        barChart.setTitle(new TextTitle("Daily Sales Report", new Font("Serif", Font.BOLD, 18)));
        BarRenderer renderer = (BarRenderer) barChart.getCategoryPlot().getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setSeriesPaint(0, new Color(79, 129, 189));
        renderer.setSeriesPaint(1, new Color(192, 80, 77));
        renderer.setSeriesPaint(2, new Color(155, 187, 89));

        int width = 640;
        int height = 480;
        BufferedImage bufferedImage = barChart.createBufferedImage(width, height);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        byte[] chartImage = byteArrayOutputStream.toByteArray();
        ImageData imageData = ImageDataFactory.create(chartImage);
        Image image = new Image(imageData);

        document.add(image);

        // 关闭文档
        document.close();
    }


}



