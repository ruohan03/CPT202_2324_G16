package com.example.demo.Service;

import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Service
public interface PdfReportService {
    void generatePdfReport(Map<String, PurchaseService.PlanSalesData> salesData,
                           Map<LocalDate, Map<String, PurchaseService.PlanSalesData>> dailySalesData,
                           PurchaseService.PlanSalesData totalSalesData,
                           HttpServletResponse response) throws IOException;


    void generateChartPdfReport(Map<String, PurchaseService.PlanSalesData> salesData,
                                Map<LocalDate, Map<String, PurchaseService.PlanSalesData>> dailySalesData,
                                PurchaseService.PlanSalesData totalSalesData,
                                HttpServletResponse response) throws IOException;
}
