package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {
    public boolean existsByName(String name);
    public Tag findByName(String name);

    /**
     * Find autocomplete suggestions by finding all the tags that contain the input text.
     * Order higher if the result starts with the input text.
     * For example: for result: "cool", the input "co" is a closer match than "ol".
     *
     * @param query User tag input
     * @return List of tags containing user input.
     */
    @Query("SELECT tag FROM Tag tag WHERE tag.name LIKE %?1% ORDER BY CASE WHEN tag.name LIKE ?1% THEN 0 ELSE 1 END")
    List<Tag> findByNameContains(String query);
}
