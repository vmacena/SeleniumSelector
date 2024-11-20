package org.macena.seleniumselector;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class Controller {

    @FXML
    private Label chromeVersionLabel;

    @FXML
    private TextArea logTextArea;

    private String downloadLink;

    @FXML
    private void onDownloadClick() {
        if (downloadLink == null || downloadLink.isEmpty()) {
            log("No download link available. Detect Chrome version first.");
            return;
        }

        try {
            Desktop.getDesktop().browse(new URI(downloadLink));
            log("Opening the link in the browser: " + downloadLink);
        } catch (Exception e) {
            log("Error opening the link: " + e.getMessage());
        }
    }

    private String getDownloadLink(String chromeVersion) {
        try {
            String adjustedVersion = chromeVersion.replaceAll("\\.\\d+$", ".69");
            String baseUrl = "https://storage.googleapis.com/chrome-for-testing-public/";
            String os;

            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                os = System.getProperty("os.arch").contains("64") ? "win64" : "win32";
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                os = System.getProperty("os.arch").contains("aarch64") ? "mac-arm64" : "mac-x64";
            } else if (System.getProperty("os.name").toLowerCase().contains("nux")) {
                os = "linux64";
            } else {
                log("Operating system not supported.");
                return null;
            }

            String downloadLink = baseUrl + adjustedVersion + "/" + os + "/chromedriver-" + os + ".zip";
            log("Link generated: " + downloadLink);
            return downloadLink;

        } catch (Exception e) {
            log("Error generating download link: " + e.getMessage());
            return null;
        }
    }

    @FXML
    private void initialize() {
        log("Detecting Chrome version...");
        String chromeVersion = detectChromeVersion();

        if (chromeVersion != null) {
            chromeVersionLabel.setText("Chrome Version: " + chromeVersion);
            log("Version detected: " + chromeVersion);
            downloadLink = getDownloadLink(chromeVersion);

            if (downloadLink != null) {
                log("Generated download link: " + downloadLink);
            } else {
                log("Unable to generate Chromedriver download link.");
            }
        } else {
            chromeVersionLabel.setText("Chrome Version: Not Detected");
            log("Unable to detect Chrome version.");
        }
    }

    private String detectChromeVersion() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command;

            if (os.contains("win")) {
                command = "reg query \"HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon\" /v version";
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("version")) {
                        return line.replaceAll(".*version\\s+REG_SZ\\s+", "").trim();
                    }
                }
            } else if (os.contains("mac")) {
                command = "/Applications/Google\\ Chrome.app/Contents/MacOS/Google\\ Chrome --version";
                String line = getString(command);
                if (line != null) return line;
            } else if (os.contains("nix") || os.contains("nux")) {
                command = "google-chrome --version";
                String line = getString(command);
                if (line != null) return line;
            }
        } catch (Exception e) {
            log("Error detecting Chrome version: " + e.getMessage());
        }
        return null;
    }

    private String getString(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        if (line != null && line.toLowerCase().contains("chrome")) {
            return line.replaceAll("[^0-9.]", "").trim();
        }
        return null;
    }

    private void log(String message) {
        logTextArea.appendText(message + "\n");
    }
}
