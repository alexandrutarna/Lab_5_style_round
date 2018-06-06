package it.polito.mad.lab5.beans;

public class BookRequestMessage {


    private String bookTitle;
    private String from;
    private String to;
    private String senderName;

    public BookRequestMessage(String bookTitle, String from, String to, String senderName) {
        this.bookTitle = bookTitle;
        this.from = from;
        this.to = to;
        this.senderName = senderName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return "BookRequestMessage{" +
                "bookTitle='" + bookTitle + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", senderName='" + senderName + '\'' +
                '}';
    }
}
