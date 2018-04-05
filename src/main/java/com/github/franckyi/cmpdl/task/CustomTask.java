package com.github.franckyi.cmpdl.task;

import com.github.franckyi.cmpdl.CMPDL;
import com.github.franckyi.cmpdl.InterfaceController;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

abstract class CustomTask<V> extends Task<V> {

    @Override
    protected V call() throws Exception {
        try {
            return call0();
        } catch (Exception e) {
            e.printStackTrace();
            CMPDL.exceptions.add(e);
            CMPDL.controller.trace(e);
        }
        return null;
    }

    protected abstract V call0() throws Exception;

    void log(String s) {
        Platform.runLater(() -> getController().log(s));
    }

    void trace(Throwable t) {
        Platform.runLater(() -> getController().trace(t));
    }

    InterfaceController getController() {
        return CMPDL.controller;
    }

    String crawl(String url) throws IOException, URISyntaxException {
        URL url0 = new URL(crawl0(url));
        return new URI(url0.getProtocol(), url0.getHost(), url0.getFile(), null).toASCIIString();
    }

    String crawlAddHost(String url) throws IOException, URISyntaxException {
        URL url0 = new URL(crawlAddHost0(url));
        return new URI(url0.getProtocol(), url0.getHost(), url0.getFile(), null).toASCIIString();
    }

    private String crawl0(String url) throws IOException, IllegalArgumentException, URISyntaxException {
        String location = getLocation(url);
        return location != null ? crawl0(location) : url;
    }

    private String crawlAddHost0(String url) throws IOException, IllegalArgumentException, URISyntaxException {
        URL url0 = new URL(url);
        String location = getLocation(url);
        return location != null ? location.contains("://") ? crawlAddHost0(location) : crawl0(url0.getProtocol() + "://" + url0.getHost() + location) : url;
    }

    private String getLocation(String url) throws IOException, URISyntaxException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setInstanceFollowRedirects(false);
        if (connection.getResponseCode() < 200 || connection.getResponseCode() >= 400)
            throw new IOException("Error " + connection.getResponseCode() + " at " + url);
        return connection.getHeaderField("location");
    }
}
