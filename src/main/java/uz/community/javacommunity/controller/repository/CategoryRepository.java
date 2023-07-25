package uz.community.javacommunity.controller.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import uz.community.javacommunity.controller.domain.Category;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends CassandraRepository<Category, UUID> {
    @Query(allowFiltering = true)
    List<Category> findAllByName(String name);
    @Query(allowFiltering = true)
    List<Category> findAllByNameAndParentId(String name, UUID parentId);
    @Query(allowFiltering = true)
    List<Category> findAllByParentId(UUID id);
    @Query("SELECT * FROM category WHERE parent_id = 'null' ALLOW FILTERING")
    List<Category> findAllByParentIdNull();
}
