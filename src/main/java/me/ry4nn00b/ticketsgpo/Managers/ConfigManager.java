package me.ry4nn00b.ticketsgpo.Managers;

import java.io.*;
import java.util.Properties;

public class ConfigManager {

    private Properties properties = new Properties();
    private final String configFilePath;

    public ConfigManager() {

        this.configFilePath = System.getProperty("user.dir") + "/config.properties";
        loadConfig();

    }

    private void loadConfig() {
        File configFile = new File(configFilePath);
        if (configFile.exists()) {
            try (InputStream input = new FileInputStream(configFile)) {
                properties.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            properties.setProperty("DiscordToken", "");
            properties.setProperty("MPToken", "");
            properties.setProperty("openedTickets", "0");
            properties.setProperty("allRole", "");
            properties.setProperty("devRole", "");
            properties.setProperty("ownerRole", "");
            properties.setProperty("subOwnerRole", "");
            properties.setProperty("directorRole", "");
            properties.setProperty("managerRole", "");
            properties.setProperty("adminRole", "");
            properties.setProperty("modRole", "");
            properties.setProperty("supportRole", "");
            properties.setProperty("deliveryRole", "");
            properties.setProperty("deliveryLogChannelID", "");
            properties.setProperty("logChannelID", "");
            properties.setProperty("ticketCategoryID", "");
            saveConfig();
        }
    }

    //Tokens
    public String getDiscordToken() {
        return properties.getProperty("DiscordToken");
    }
    public String getMPToken() {
        return properties.getProperty("MPToken");
    }

    //Prices
    public String getTickets() {
        return properties.getProperty("openedTickets");
    }
    public void setTickets(String tickets) {
        properties.setProperty("openedTickets", tickets);
        saveConfig();
    }

    //AssumeTickets
    public String getAssumeTicket(String ticketChannelID) {
        return properties.getProperty(ticketChannelID);
    }
    public void setAssumeTicket(String ticketChannelID, String staffID) {
        properties.setProperty(ticketChannelID, staffID);
        saveConfig();
    }
    public void remAssumeTicket(String ticketChannelID) {
        properties.remove(ticketChannelID);
        saveConfig();
    }

    //Staff Roles
    public String getAllRole() {
        return properties.getProperty("allRole");
    }
    public String getDevRole() {
        return properties.getProperty("devRole");
    }
    public String getOwnerRole() {
        return properties.getProperty("ownerRole");
    }
    public String getSubOwnerRole() {
        return properties.getProperty("subOwnerRole");
    }
    public String getDirectorRole() {
        return properties.getProperty("directorRole");
    }
    public String getManagerRole() {
        return properties.getProperty("managerRole");
    }
    public String getAdminRole() {
        return properties.getProperty("adminRole");
    }
    public String getModRole() {
        return properties.getProperty("modRole");
    }
    public String getSupportRole() {
        return properties.getProperty("supportRole");
    }
    public String getDeliveryRole() {
        return properties.getProperty("deliveryRole");
    }

    //Channels ID's
    public String getDeliveryLogChannelID() {
        return properties.getProperty("deliveryLogChannelID");
    }
    public String getLogChannel() {
        return properties.getProperty("logChannelID");
    }
    public String getTicketCategoryID() {
        return properties.getProperty("ticketCategoryID");
    }

    private void saveConfig() {
        try (OutputStream output = new FileOutputStream(configFilePath)) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
