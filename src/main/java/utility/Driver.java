package utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Paths;


public class Driver {
    public static UiAutomator2Options uiAutomator2Options;
    public static XCUITestOptions xcuiTestOptions;
    public static AppiumDriver driver;
    public static String deviceName;  // Declare deviceName here

    public static AppiumDriverLocalService service;
    public static ExtentReports report;
    public static ExtentTest test;
    public static ExtentSparkReporter spark;
    public static String appiumJsPath = "";
    public static String platformVersion="";
    public static String UDID="";

    public static void quitDriver() throws InterruptedException {
        Thread.sleep(5000); // Wait for 5 seconds before quitting
        if (driver != null) {
            try {
                if (driver.getSessionId() != null) {
                    driver.quit();
                    System.out.println("Quit driver successfully");
                }
            } catch (Exception e) {
                System.out.println("Exception occurred while quitting the driver: " + e.getMessage());
            }
        } else {
            System.out.println("Driver is already null.");
        }
    }

    public static void launchIOSApplication(String simulator_real_device) throws MalformedURLException {
        xcuiTestOptions = xcuiTestOptions_for_iOS(simulator_real_device);
        driver = new AppiumDriver(new URL("http://127.0.0.1:4723"), xcuiTestOptions);
        System.out.println("iOS App Launched!");
    }


    public static void launchAndroidApplication(String virtual_real_device) throws MalformedURLException {
        uiAutomator2Options = uiAutomator2Options_for_android(virtual_real_device);
        driver = new AppiumDriver(new URL("http://127.0.0.1:4723"), uiAutomator2Options);
        System.out.println("Android App Launched!");
    }

    public static UiAutomator2Options uiAutomator2Options_for_android(String virtual_real_device) {
        try {
            // Read JSON file
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/java/utility/DesiredCap.json")));
            JSONObject json = new JSONObject(jsonContent);

            // Get the capabilities block directly
            JSONObject dc = json.getJSONObject(virtual_real_device);

            System.out.println("Desired capability for Android: \n" + dc.toString(2));
            System.out.println(":-------------------------------------------------------------------------------------------------------:");

            // Directly set UiAutomator2Options from JSON
            return new UiAutomator2Options()
                    .setDeviceName(dc.getString("deviceName"))
                    .setPlatformName(dc.getString("platformName"))
                    .setPlatformVersion(dc.getString("platformVersion"))
                    .setAppPackage(dc.getString("appPackage"))
                    .setAppActivity(dc.getString("appActivity"))
                    .setNoReset(dc.getBoolean("noReset"))
                    .setFullReset(dc.getBoolean("fullReset"))
                    .setAutoGrantPermissions(dc.getBoolean("setAutoGrantPermissions"));

        } catch (Exception e) {
            throw new RuntimeException("Failed to load capabilities from JSON", e);
        }
    }

    public static XCUITestOptions xcuiTestOptions_for_iOS(String virtual_real_device) {
        try {
            // Read JSON file content
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/java/utility/DesiredCap.json")));
            JSONObject json = new JSONObject(jsonContent);

            // Get JSON object for iOS simulator device
            JSONObject dc = json.getJSONObject(virtual_real_device);
            String appPath = new Utility().getCurrentDirectory()+""+dc.getString("appPath");
            System.out.println("AppPath: "+appPath);
            System.out.println("iOS Desired Capability: \n" + dc.toString(2));
            System.out.println(":-------------------------------------------------------------------------------------------------------:");

            XCUITestOptions options = new XCUITestOptions();

            options.setDeviceName(dc.getString("deviceName"));
            options.setPlatformName(dc.getString("platformName"));
            options.setPlatformVersion(dc.getString("platformVersion"));
            options.setAutomationName(dc.optString("automationName", "XCUITest"));
            options.setApp(appPath); // note appPath key in your JSON
            options.setNoReset(dc.optBoolean("noReset", false));
            options.setFullReset(dc.optBoolean("fullReset", false));
            options.setAutoAcceptAlerts(dc.optBoolean("autoAcceptAlerts", true));
            options.setUseNewWDA(dc.optBoolean("UseNewWDA", true));
            options.setClearSystemFiles(dc.optBoolean("ClearSystemFiles", true));
            options.setWdaLaunchTimeout(Duration.ofSeconds(dc.optInt("WdaLaunchTimeout", 60)));
            options.setNewCommandTimeout(Duration.ofSeconds(dc.optInt("newCommandTimeout", 120)));

            return options;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load iOS capabilities from JSON", e);
        }
    }

    // Get Android platform version for given device
    public static String getAndroidPlatformVersion(String deviceName) {
        String platformVersion = "";
        try {
            if (deviceName == null || deviceName.isEmpty()) {
                System.err.println("Device name is empty. Cannot get platform version.");
                return platformVersion;
            }

            Process process = Runtime.getRuntime().exec(
                    "adb -s " + deviceName + " shell getprop ro.build.version.release"
            );

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();

            if (line != null) {
                platformVersion = line.trim();
            } else {
                System.err.println("No output from adb command to get platform version.");
            }

            reader.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return platformVersion;
    }

    // Get connected Android device name from adb
    public static String getAndroidDeviceName() {
        String foundDevice = "";
        try {
            Process process = Runtime.getRuntime().exec("adb devices");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().endsWith("device") && !line.startsWith("List")) {
                    foundDevice = line.split("\\s+")[0];
                    break;
                }
            }
            reader.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foundDevice;
    }

    // Get Appium main.js path depending on OS
    public static String getAppiumJsPath() {
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            String command = osName.contains("win") ? "where appium" : "which appium";

            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String commandOutput = reader.readLine();

            if (commandOutput != null) {
                if (command.contains("where")) {
                    // Windows - remove appium.cmd and append node_modules path
                    appiumJsPath = commandOutput.replaceAll("(?i)appium\\.cmd$", "") + "node_modules\\appium\\build\\lib\\main.js";
                } else {
                    // Mac/Linux - replace /bin/appium with lib path
                    appiumJsPath = commandOutput.replace("/bin/appium", "/lib/node_modules/appium/build/lib/main.js");
                }
            }

            reader.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appiumJsPath;
    }
    public static void updateDesiredCapJson(String virtual_Real_Device, String automationName) {
        try {
            if(automationName.equals("Android")){
                if(virtual_Real_Device.equalsIgnoreCase("emulator")){
                    deviceName = getAndroidDeviceName();
                    appiumJsPath=getAppiumJsPath();
                    platformVersion=getAndroidPlatformVersion(deviceName);

                }else{
                    UDID = getRealAndroidDeviceUDID();
                    deviceName=getRealAndroidDeviceName(UDID);
                    appiumJsPath=getAppiumJsPath();
                    platformVersion=getRealAndroidPlatformVersion(UDID);

                    System.out.println("UDID: " + UDID);

                }

            }else{
                if(virtual_Real_Device.equalsIgnoreCase("simulator")){
                    deviceName=getIOSDeviceName();
                    appiumJsPath=getAppiumJsPath();
                    platformVersion=getIOSPlatformVersion();
                    UDID = getIOSDeviceUDID();
                    System.out.println("UDID: " + UDID);
                }
                else {

                }

            }

            System.out.println("Connected device: " + deviceName);
            System.out.println("AppiumJs Path: " + appiumJsPath);
            System.out.println("Platform version: " + platformVersion);


            // 1. Read existing JSON file into JsonObject
            FileReader reader = new FileReader("src/main/java/utility/DesiredCap.json");
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            // 2. Get the "emulators1" object
            JsonObject virtual_Real_Devices = jsonObject.getAsJsonObject(virtual_Real_Device);

            // 3. Update values
            if(automationName.equalsIgnoreCase("Android")){
                virtual_Real_Devices.addProperty("deviceName", deviceName);
                virtual_Real_Devices.addProperty("platformVersion", platformVersion);
            }else{
                virtual_Real_Devices.addProperty("deviceName", deviceName);
                virtual_Real_Devices.addProperty("platformVersion", platformVersion);
                virtual_Real_Devices.addProperty("UDID", UDID);

            }
            virtual_Real_Devices.addProperty("deviceName", deviceName);
            virtual_Real_Devices.addProperty("platformVersion", platformVersion);

            // 4. Write updated JSON back to file with pretty printing
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter("src/main/java/utility/DesiredCap.json");
            gson.toJson(jsonObject, writer);
            writer.flush();
            writer.close();

            System.out.println("JSON updated successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initializeExtentReport() {
        if (report == null) {
            report = new ExtentReports();
            // Use OS independent path separator for report location
            String reportPath = "Extent Report" + File.separator + "report.html";
            spark = new ExtentSparkReporter(reportPath);
            report.attachReporter(spark);
        }
    }

    public static ExtentTest createExtentTestLog(ITestResult result) {
        test = report.createTest(result.getMethod().getMethodName())
                .assignCategory(result.getTestClass().getName())
                .assignAuthor("Sachin")
                .assignDevice(System.getProperty("os.name"));
        return test;
    }

    public static void attachScreenshotToReport(ExtentTest test) {
        try {
            String base64Screenshot = ((TakesScreenshot) Driver.driver).getScreenshotAs(OutputType.BASE64);
            test.fail("Test case failed", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        } catch (Exception e) {
            test.fail("Failed to capture screenshot: " + e.getMessage());
        }
    }
    // Get the booted iOS simulator device name
    public static String getIOSDeviceName() throws IOException {
        Process simProcess = Runtime.getRuntime().exec("xcrun simctl list devices booted");
        BufferedReader simReader = new BufferedReader(new InputStreamReader(simProcess.getInputStream()));
        String line;
        String deviceName = "";
        while ((line = simReader.readLine()) != null) {
            // Example line: "    iPhone 16 Pro (5B2F69A1-DDE3-4E47-B3C5-800000E42031) (Booted)"
            if (line.contains("(Booted)")) {
                deviceName = line.split("\\(")[0].trim(); // Gets the name before first "("
                break;
            }
        }
        simReader.close();
        return deviceName;
    }

    // Get the platform version (runtime version) of the booted simulator
    public static String getIOSPlatformVersion() throws IOException {
        // Get booted device's UUID
        Process simProcess = Runtime.getRuntime().exec("xcrun simctl list devices booted");
        BufferedReader simReader = new BufferedReader(new InputStreamReader(simProcess.getInputStream()));
        String deviceUUID = "";
        String line;
        while ((line = simReader.readLine()) != null) {
            if (line.contains("(Booted)")) {
                // Extract UUID between parentheses, example: (5B2F69A1-DDE3-4E47-B3C5-800000E42031)
                int firstParen = line.indexOf('(');
                int secondParen = line.indexOf(')', firstParen + 1);
                int thirdParen = line.indexOf('(', secondParen + 1); // For (Booted)
                deviceUUID = line.substring(firstParen + 1, secondParen).trim();
                break;
            }
        }
        simReader.close();

        if (deviceUUID.isEmpty()) {
            System.err.println("No booted device UUID found.");
            return "";
        }

        // Now get the runtime of that device UUID
        Process runtimeProcess = Runtime.getRuntime().exec("xcrun simctl list devices " + deviceUUID + " -j");
        // The above command may not give runtime info directly; instead, let's use simctl list devices -j and parse JSON:

        // Alternative: get JSON list of devices and runtimes
        Process jsonProcess = Runtime.getRuntime().exec("xcrun simctl list devices -j");
        BufferedReader jsonReader = new BufferedReader(new InputStreamReader(jsonProcess.getInputStream()));
        StringBuilder jsonSb = new StringBuilder();
        String jsonLine;
        while ((jsonLine = jsonReader.readLine()) != null) {
            jsonSb.append(jsonLine);
        }
        jsonReader.close();

        String jsonString = jsonSb.toString();

        // Parse JSON manually or use a library (e.g. org.json)
        org.json.JSONObject obj = new org.json.JSONObject(jsonString);
        org.json.JSONObject devices = obj.getJSONObject("devices");

        for (String runtime : devices.keySet()) {
            org.json.JSONArray deviceArray = devices.getJSONArray(runtime);
            for (int i = 0; i < deviceArray.length(); i++) {
                org.json.JSONObject device = deviceArray.getJSONObject(i);
                if (deviceUUID.equals(device.getString("udid"))) {
                    // runtime string format is like "com.apple.CoreSimulator.SimRuntime.iOS-18-6"
                    // Extract version from runtime key
                    String version = runtime.replace("com.apple.CoreSimulator.SimRuntime.iOS-", "").replace("-", ".");
                    return version;
                }
            }
        }

        return "";
    }

    public static String getRealIOSDeviceUDID() {
        try {
            Process process = Runtime.getRuntime().exec("idevice_id -l");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String udid = reader.readLine();
            reader.close();
            return udid != null ? udid.trim() : "";
        } catch (IOException e) {
            System.err.println("idevice_id not found or no device connected. Falling back to simulator.");
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getBootedSimulatorUDID() throws IOException {
        Process process = Runtime.getRuntime().exec("xcrun simctl list devices booted");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            // Example line format: "iPhone 16 Pro (C4237BA8-1234-5678-90AB-CDEF12345678) (Booted)"
            if (line.contains("(Booted)")) {
                int start = line.indexOf('(');
                int end = line.indexOf(')', start);
                if (start != -1 && end != -1) {
                    String udid = line.substring(start + 1, end);
                    return udid.trim();
                }
            }
        }
        return "";
    }

    public static String getIOSDeviceUDID() {
        try {
            // Try real device UDID
            Process realDeviceProcess = Runtime.getRuntime().exec("idevice_id -l");
            BufferedReader realReader = new BufferedReader(new InputStreamReader(realDeviceProcess.getInputStream()));
            String udid = realReader.readLine();
            realReader.close();
            if (udid != null && !udid.trim().isEmpty()) {
                return udid.trim();
            }
        } catch (IOException e) {
            System.err.println("idevice_id not found or no device connected. Falling back to simulator.");
        }

        // Fallback to simulator UDID
        try {
            return getBootedSimulatorUDID();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    // Get UDID (serial) of connected real Android device
    public static String getRealAndroidDeviceUDID() {
        String udid = "";
        try {
            Process process = Runtime.getRuntime().exec("adb devices");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().endsWith("device") && !line.startsWith("List")) {
                    udid = line.split("\\s+")[0];
                    break; // take first device only
                }
            }
            reader.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return udid;
    }

    // Get device model name of real Android device by UDID
    public static String getRealAndroidDeviceName(String udid) {
        String deviceName = "";
        try {
            Process process = Runtime.getRuntime().exec("adb -s " + udid + " shell getprop ro.product.model");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            deviceName = reader.readLine();
            if (deviceName != null) deviceName = deviceName.trim();
            reader.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceName;
    }

    // Get Android OS version of real device by UDID
    public static String getRealAndroidPlatformVersion(String udid) {
        String version = "";
        try {
            Process process = Runtime.getRuntime().exec("adb -s " + udid + " shell getprop ro.build.version.release");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            version = reader.readLine();
            if (version != null) version = version.trim();
            reader.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return version;

    }








}
