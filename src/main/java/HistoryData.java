import java.time.LocalDate;

public class HistoryData {

    private LocalDate date;
    private double startingBalance, costTotal, depositTotal, remainingBalance;
    private String depositDescriptions, purchaseDescriptions;
    private Object[] entryInformation = new Object[7];

    HistoryData(LocalDate date, double startingBalance, double costTotal, double depositTotal, double remainingBalance, String depositDescriptions, String purchaseDescriptions){
        this.date = date;
        this.startingBalance = startingBalance;
        this.costTotal = costTotal;
        this.depositTotal = depositTotal;
        this.remainingBalance = remainingBalance;
        this.depositDescriptions = depositDescriptions;
        this.purchaseDescriptions = purchaseDescriptions;

        updateEntryInformation();
    }

    private void updateEntryInformation(){
        entryInformation[0] = date;
        entryInformation[1] = startingBalance;
        entryInformation[2] = costTotal;
        entryInformation[3] = depositTotal;
        entryInformation[4] = remainingBalance;
        entryInformation[5] = depositDescriptions;
        entryInformation[6] = purchaseDescriptions;
    }

    //Getters + Setters
    public Object[] getEntryInformation(){
        return entryInformation;
    }

}
