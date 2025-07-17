package com.weatherapi.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports createInstance(String fileName) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fileName);
        sparkReporter.config().setDocumentTitle("Weather API Test Report");
        sparkReporter.config().setReportName("Geo API Test Results");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        return extent;
    }
}

