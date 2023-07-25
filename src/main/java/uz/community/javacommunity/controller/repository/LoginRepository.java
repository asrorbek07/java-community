package uz.community.javacommunity.controller.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import uz.community.javacommunity.controller.domain.Login;

@Repository
public interface LoginRepository extends CassandraRepository<Login, String> {
}
