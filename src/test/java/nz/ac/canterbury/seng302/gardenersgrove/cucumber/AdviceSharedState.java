package nz.ac.canterbury.seng302.gardenersgrove.cucumber;

import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class AdviceSharedState {
    private Long gardenId;

    public Long getGardenId() {
        return gardenId;
    }

    public void setGardenId(Long gardenId) {
        this.gardenId = gardenId;
    }
}
