package it.polito.mad.lab5.beans;

public class StatusMessage {
    private String bookTitle;
    private String from;
    private String to;
    private String status;

    public StatusMessage(String bookTtitle, String from, String to, String status) {
        this.bookTitle = bookTtitle;
        this.from = from;
        this.to = to;
        this.status = status;
    }

    public String getbookTitle() {
        return bookTitle;
    }

    public void setbookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
