package nz.ac.canterbury.seng302.gardenersgrove.utility;

import java.util.List;

public class AdviceMessage {

    private final String adviceMessage;
    private final List<String> referenceList;

    public AdviceMessage(String adviceMessage, List<String> referenceList) {
        this.adviceMessage = adviceMessage;
        this.referenceList = referenceList;
    }

    public String getAdviceMessage() {
        return adviceMessage;
    }

    public List<String> getReferenceList() {
        return referenceList;
    }
}
