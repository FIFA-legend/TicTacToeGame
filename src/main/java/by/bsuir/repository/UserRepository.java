package by.bsuir.repository;

import by.bsuir.entity.User;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends Repository<User, Long> {

    User save(User user);

    User findById(Long id);

    User findByUsername(String username);

}