package org.example.repository;

import org.example.models.Request;
import org.example.models.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends CrudRepository<Request> {
    List<Request> findByStudentId(Long studentId); // Исходящие заявки (для ученика)
    List<Request> findByOfferAuthorId(Long authorId); // Входящие заявки (для репетитора)
    Optional<Request> findById(Long id);
    void updateStatus(Long requestId, RequestStatus status);
    boolean existsByOfferAndSubjectAndStudent(Long offerId, Integer subjectId, Long studentId);

}
