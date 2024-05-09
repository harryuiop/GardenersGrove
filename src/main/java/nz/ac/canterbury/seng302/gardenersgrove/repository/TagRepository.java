package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {
    public boolean existsByName(String name);
    public Tag findByName(String name);

    /**
     * Find autocomplete suggestions by finding all tags starting with parameter
     *
     * @param prefix User input
     * @return List of tags starting with user inputted prefix.
     */
    @Query("SELECT tag FROM Tag tag WHERE tag.name LIKE ?1%")
    List<Tag> findByNameStartingWith(String prefix);
}
