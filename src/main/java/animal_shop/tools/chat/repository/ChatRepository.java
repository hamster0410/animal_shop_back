package animal_shop.tools.chat.repository;

import animal_shop.tools.chat.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByChatRoomId(String chatRoomId);
    Optional<Chat> findTopByChatRoomIdOrderByDateDesc(String chatRoomId);

}

