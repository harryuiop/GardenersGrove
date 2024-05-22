package nz.ac.canterbury.seng302.gardenersgrove.utility;

public enum Status {

    ACCEPTED("Accepted"),
    PENDING("Pending"),
    DECLINED("Declined");

    public final String string;

    private Status(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
