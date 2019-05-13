package com.iovation.launchkey.sdk.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

public class KobitonManager {
    private final String userName;
    private final String sdkKey;
    private final String apiBaseURL;
    private final HttpClient httpClient;
    private final ObjectMapper objectMaper;

    public KobitonManager(String userName, String sdkKey, String apiBaseURL, HttpClient httpClient,
                          ObjectMapper objectMapper) {
        this.userName = userName;
        this.sdkKey = sdkKey;
        this.apiBaseURL = apiBaseURL;
        this.httpClient = httpClient;
        this.objectMaper = objectMapper;
    }

    public App getApp(String appId) throws Exception {
        HttpResponse response = executeGet("/apps/".concat(appId));
        App app = objectMaper.readValue(response.getEntity().getContent(), App.class);
        return app;
    }

    public List<App> getApps() throws Exception {
        HttpResponse response = executeGet("/apps");
        App[] apps = objectMaper.readValue(response.getEntity().getContent(), App[].class);
        return Arrays.asList(apps);
    }

    public CreatedApp createApp(String appPath, String appName) throws Exception {
        AppUploadResponse appUpload = generateAppUploadURL(appName);
        uploadAppToS3(appPath, appUpload.getUrl());
        return createAppEntity(appName, appPath);
    }

    public void deleteApp(String appId) throws Exception {
        executeDelete("/apps/".concat(appId));
    }

    public List<Device> getDevices() throws Exception {
        HttpResponse response = executeGet("/devices");
        Device[] devices = objectMaper.readValue(response.getEntity().getContent(), Device[].class);
        return Arrays.asList(devices);
    }

    private AppUploadResponse generateAppUploadURL(String fileName) throws Exception {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("filename", fileName);
        HttpResponse response = executePost("/apps/uploadUrl/", requestData);
        InputStream responseInputStream = response.getEntity().getContent();
        return objectMaper.readValue(responseInputStream, AppUploadResponse.class);
    }

    private HttpResponse executePost(String path, Map<String, Object> data) throws Exception {
        HttpPost request = new HttpPost(apiBaseURL.concat(path));
        request.setHeader("content-type", "application/json");
        request.setHeader("accept", "application/json");
        byte[] dataBytes =  objectMaper.writeValueAsBytes(data);
        InputStream dataStream = new ByteArrayInputStream(dataBytes);
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(dataStream);
        entity.setContentLength(dataBytes.length);
        request.setEntity(entity);
        return executeHttpRequest(request);
    }

    private HttpResponse executeGet(String path) throws Exception {
        HttpUriRequest request = new HttpGet(apiBaseURL.concat(path));
        request.setHeader("accept", "application/json");
        return executeHttpRequest(request);
    }

    private void executeDelete(String path) throws Exception {
        HttpUriRequest request = new HttpDelete(apiBaseURL.concat(path));
        executeHttpRequest(request);
    }

    private HttpResponse executeHttpRequest(HttpUriRequest request) throws Exception {
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() > 299) {
            throw new Exception("An error occurred processing request: " + response.getStatusLine().toString());
        }
        return response;
    }


    public class App {
        private final String id;
        private final Date createdAt;
        private final String name;
        private final boolean privateAccess;
        private final String os;
        private final String createdBy;
        private final String state;
        private final String iconUrl;
        private final List<Version> versions;

        public App(String id, Date createdAt, String name, boolean privateAccess, String os, String createdBy, String state, String iconUrl, List<Version> versions) {
            this.id = id;
            this.createdAt = createdAt;
            this.name = name;
            this.privateAccess = privateAccess;
            this.os = os;
            this.createdBy = createdBy;
            this.state = state;
            this.iconUrl = iconUrl;
            this.versions = versions;
        }

        public String getId() {
            return id;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public String getName() {
            return name;
        }

        public boolean isPrivateAccess() {
            return privateAccess;
        }

        public String getOs() {
            return os;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public String getState() {
            return state;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public List<Version> getVersions() {
            return versions;
        }
    }

    public class Device {
        private final int groupId;
        private final String udid;
        private final boolean isBooked;
        private final boolean isOnline;
        private final String modelName;
        private final String deviceName;
        private final Resolution resolution;
        private final String platformName;
        private final String platformVersion;
        private final String deviceImageUrl;
        private final boolean isFavorite;
        private final boolean isCloud;
        private final boolean isMyOrg;
        private final boolean isMyOwn;
        private final List<Browser> installedBrowsers;

        public Device(int groupId, String udid, boolean isBooked, boolean isOnline, String modelName,
                      String deviceName, Resolution resolution, String platformName, String platformVersion,
                      String deviceImageUrl, boolean isFavorite, boolean isCloud, boolean isMyOrg, boolean isMyOwn,
                      List<Browser> installedBrowsers) {
            this.groupId = groupId;
            this.udid = udid;
            this.isBooked = isBooked;
            this.isOnline = isOnline;
            this.modelName = modelName;
            this.deviceName = deviceName;
            this.resolution = resolution;
            this.platformName = platformName;
            this.platformVersion = platformVersion;
            this.deviceImageUrl = deviceImageUrl;
            this.isFavorite = isFavorite;
            this.isCloud = isCloud;
            this.isMyOrg = isMyOrg;
            this.isMyOwn = isMyOwn;
            this.installedBrowsers = installedBrowsers;
        }

        public int getGroupId() {
            return groupId;
        }

        public String getUdid() {
            return udid;
        }

        public boolean isBooked() {
            return isBooked;
        }

        public boolean isOnline() {
            return isOnline;
        }

        public String getModelName() {
            return modelName;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public Resolution getResolution() {
            return resolution;
        }

        public String getPlatformName() {
            return platformName;
        }

        public String getPlatformVersion() {
            return platformVersion;
        }

        public String getDeviceImageUrl() {
            return deviceImageUrl;
        }

        public boolean isFavorite() {
            return isFavorite;
        }

        public boolean isCloud() {
            return isCloud;
        }

        public boolean isMyOrg() {
            return isMyOrg;
        }

        public boolean isMyOwn() {
            return isMyOwn;
        }

        public List<Browser> getInstalledBrowsers() {
            return installedBrowsers;
        }
    }

    public class GetDevicesResponse {
        private final List<Device> cloudDevices;

        public GetDevicesResponse(List<Device> cloudDevices) {
            this.cloudDevices = cloudDevices;
        }
    }

    public class Resolution {
        private final int width;
        private final int height;

        private Resolution(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public class Browser {
        private final String name;
        private final String version;

        private Browser(String name, String version) {
            this.name = name;
            this.version = version;
        }

        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }
    }

    public class Version {
        private final String id;
        private final Date createdAt;
        private final String name;
        private final String version;
        private final String createdBy;
        private final String state;

        private Version(String id, Date createdAt, String name, String version, String createdBy, String state) {
            this.id = id;
            this.createdAt = createdAt;
            this.name = name;
            this.version = version;
            this.createdBy = createdBy;
            this.state = state;
        }

        public String getId() {
            return id;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public String getState() {
            return state;
        }
    }

    public class AppUploadResponse {
        private final String appPath;
        private final String url;

        public AppUploadResponse(String appPath, String url) {
            this.appPath = appPath;
            this.url = url;
        }

        public String getAppPath() {
            return appPath;
        }

        public String getUrl() {
            return url;
        }
    }

    public class CreatedApp {
        public final String appId;
        public final String versionId;

        public CreatedApp(String appId, String versionId) {
            this.appId = appId;
            this.versionId = versionId;
        }

        public String getAppId() {
            return appId;
        }

        public String getVersionId() {
            return versionId;
        }
    }
}
