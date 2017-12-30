public class QueueMessage {

    public String dnsName;
    public int totalExecutionTime;
    public int queryExecutionTime;
    public String exceptionMessage;
    public boolean hasException;
    public String requestType;
    //public String awsReciept;
    public Long timeBucket;
    //public String guid;

    public QueueMessage(String dnsName, int totalExecutionTime, int queryExecutionTime, String exceptionMessage,
                        boolean hasException, String requestType, Long timeBucket) {
        this.dnsName = dnsName;
        this.totalExecutionTime = totalExecutionTime;
        this.queryExecutionTime = queryExecutionTime;
        this.exceptionMessage = exceptionMessage;
        this.hasException = hasException;
        this.requestType = requestType;
        this.timeBucket = timeBucket;

        //UUID uuid = UUID.randomUUID(); // Generate random GUID for uniqueness (Primary Key in database)
        //this.guid = uuid.toString();
    }

    public QueueMessage(String dnsName, int totalExecutionTime, int queryExecutionTime, String
        exceptionMessage, boolean hasException, String requestType, Long
        timeBucket, String guid) {
        this.dnsName = dnsName;
        this.totalExecutionTime = totalExecutionTime;
        this.queryExecutionTime = queryExecutionTime;
        this.exceptionMessage = exceptionMessage;
        this.hasException = hasException;
        this.requestType = requestType;
        this.timeBucket = timeBucket;
        //this.guid = guid;
    }
}
