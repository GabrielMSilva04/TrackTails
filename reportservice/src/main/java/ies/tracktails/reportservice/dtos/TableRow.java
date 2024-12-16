package ies.tracktails.reportservice.dtos;

public class TableRow {
    private String timestamp;
    private String dynamicValue;

    // Getters e Setters
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDynamicValue() {
        return dynamicValue;
    }

    public void setDynamicValue(String dynamicValue) {
        this.dynamicValue = dynamicValue;
    }
}
