package tikhonov.dev.user;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Хранит пользователей в памяти и выдает им идентификаторы.
 */
@Service
public class UserService {

    private final Map<Integer, User> usersById = new LinkedHashMap<>();
    private final Map<String, Integer> userIdsByLogin = new LinkedHashMap<>();
    private int nextUserId;

    /**
     * Создает пользователя без счетов. Первый счет открывает команда создания пользователя.
     *
     * @param login логин, уникальный в системе
     * @return созданный пользователь
     */
    public User createUser(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("login must not be empty");
        }
        String normalizedLogin = login.trim();
        if (userIdsByLogin.containsKey(normalizedLogin)) {
            throw new IllegalArgumentException(
                    "user with login '%s' already exists".formatted(normalizedLogin));
        }
        if (nextUserId == Integer.MAX_VALUE) {
            throw new IllegalStateException("user id space is exhausted");
        }
        int id = ++nextUserId;
        User user = new User(id, normalizedLogin, new ArrayList<>());
        usersById.put(id, user);
        userIdsByLogin.put(normalizedLogin, id);
        return user;
    }

    /**
     * @param id идентификатор пользователя
     * @return пользователь, если он есть
     */
    public Optional<User> findById(int id) {
        return Optional.ofNullable(usersById.get(id));
    }

    /**
     * @return пользователи в порядке создания
     */
    public List<User> findAll() {
        return List.copyOf(usersById.values());
    }
}
