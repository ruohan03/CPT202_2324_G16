package com.example.demo.Model;

public class PlanSalesData {
    private double totalSales;
    private int count;

    public PlanSalesData(double totalSales, int count) {
        this.totalSales = totalSales;
        this.count = count;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public int getCount() {
        return count;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
