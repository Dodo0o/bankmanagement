package com.gorselprog.bankmanagementv3.model;

public class ChatMessage {
    private String from;
    private String text;
    private String role;
    private String roomId; 

  
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
}
