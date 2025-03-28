
package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.AdviceRanges;
import nz.ac.canterbury.seng302.gardenersgrove.repository.AdviceRangesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdviceRangesService {
    private final AdviceRangesRepository adviceRangesRepository;

    @Autowired
    public AdviceRangesService(AdviceRangesRepository adviceRangesRepository) {
        this.adviceRangesRepository = adviceRangesRepository;
    }

    /**
     * Saves an AdviceRanges object to the database
     *
     * @param adviceRanges The advice ranges entity to be saved.
     */
    public void saveAdviceRanges(AdviceRanges adviceRanges) {
        this.adviceRangesRepository.save(adviceRanges);
    }
}
