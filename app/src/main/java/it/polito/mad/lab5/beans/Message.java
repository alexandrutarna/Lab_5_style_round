package it.polito.mad.lab5.beans;


public class Message {

    private long timestamp;
    private long negatedTimestamp;
    private long dayTimestamp;
    private String body;
    private String from;
    private String to;
    private String senderName;
    private Boolean seen;


    public Message(long timestamp,
                   long negatedTimestamp,
                   long dayTimestamp,
                   String body,
                   String from,
                   String to,
                   Boolean seen) {
        this.timestamp = timestamp;
        this.negatedTimestamp = negatedTimestamp;
        this.dayTimestamp = dayTimestamp;
        this.body = body;
        this.from = from;
        this.to = to;
        this.seen = seen;
    }

    public Message(long timestamp,
                   long negatedTimestamp,
                   long dayTimestamp,
                   String body,
                   String from,
                   String to,
                   String sender,
                   Boolean seen) {
        this.timestamp = timestamp;
        this.negatedTimestamp = negatedTimestamp;
        this.dayTimestamp = dayTimestamp;
        this.body = body;
        this.from = from;
        this.to = to;
        this.senderName = sender;
        this.seen = seen;

    }
    public Message() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getNegatedTimestamp() {
        return negatedTimestamp;
    }

    public String getTo() {
        return to;
    }

    public long getDayTimestamp() {
        return dayTimestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }

    public String getSenderName() { return senderName; }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "Message{" +
                "timestamp=" + timestamp +
                ", negatedTimestamp=" + negatedTimestamp +
                ", dayTimestamp=" + dayTimestamp +
                ", body='" + body + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", senderName='" + senderName + '\'' +
                ", seen=" + seen +
                '}';
    }
}
