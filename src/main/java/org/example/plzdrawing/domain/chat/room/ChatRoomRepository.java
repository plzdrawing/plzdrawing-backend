package org.example.plzdrawing.domain.chat.room;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    List<ChatRoom> findBySellerIdOrBuyerId(Long sellerId, Long buyerId);

    Optional<ChatRoom> findBySellerIdAndBuyerId(Long sellerId, Long buyerId);
}
