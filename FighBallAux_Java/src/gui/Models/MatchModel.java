package gui.Models;



public class MatchModel {

    public MatchModel(int id, String date, double length, int charWinnerId, int charLoserId, int userWinnerId, int userLoserId) {
        this.id = id;
        this.date = date;
        this.length = length;
        this.charWinnerId = charWinnerId;
        this.charLoserId = charLoserId;
        this.userWinnerId = userWinnerId;
        this.userLoserId = userLoserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getCharWinnerId() {
        return charWinnerId;
    }

    public void setCharWinnerId(int charWinnerId) {
        this.charWinnerId = charWinnerId;
    }

    public int getCharLoserId() {
        return charLoserId;
    }

    public void setCharLoserId(int charLoserId) {
        this.charLoserId = charLoserId;
    }

    public int getUserWinnerId() {
        return userWinnerId;
    }

    public void setUserWinnerId(int userWinnerId) {
        this.userWinnerId = userWinnerId;
    }

    public int getUserLoserId() {
        return userLoserId;
    }

    public void setUserLoserId(int userLoserId) {
        this.userLoserId = userLoserId;
    }

    private int id;
    private String date;
    private double length;

    private int charWinnerId;
    private int charLoserId;

    private int userWinnerId;
    private int userLoserId;

    @Override
    public String toString() {
        return "MatchModel{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", length=" + length +
                ", charWinnerId=" + charWinnerId +
                ", charLoserId=" + charLoserId +
                ", userWinnerId=" + userWinnerId +
                ", userLoserId=" + userLoserId +
                '}';
    }
}
