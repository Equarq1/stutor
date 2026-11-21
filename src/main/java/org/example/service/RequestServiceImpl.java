package org.example.service;

import org.example.dto.CreateRequestDto;
import org.example.models.*;
import org.example.models.enums.RequestStatus;
import org.example.repository.RequestRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public void createRequest(CreateRequestDto createRequestDto) throws SQLException {

        if (requestRepository.existsByOfferAndSubjectAndStudent(
                createRequestDto.getOfferId(),
                createRequestDto.getSubjectId(),
                createRequestDto.getStudentId())) {
            throw new IllegalArgumentException("Вы уже отправляли заявку на этот предмет по данному объявлению");
        }

        Offer offer = Offer.builder().id(createRequestDto.getOfferId()).build();
        Subject subject = Subject.builder().id(createRequestDto.getSubjectId()).build();
        User student = User.builder().id(createRequestDto.getStudentId()).build();
        FileInfo attachment = FileInfo.builder().id(createRequestDto.getAttachmentId()).build();
        Request request = Request.builder()
                .offer(offer)
                .subject(subject)
                .message(createRequestDto.getMessage())
                .student(student)
                .attachment(attachment)
                .build();
        requestRepository.save(request);
    }

    @Override
    public List<Request> getIncomingRequests(Long userId) {
        return requestRepository.findByOfferAuthorId(userId);
    }

    @Override
    public List<Request> getOutgoingRequests(Long userId) {
        return requestRepository.findByStudentId(userId);
    }

    @Override
    public void changeStatus(Long requestId, RequestStatus status) {
        requestRepository.updateStatus(requestId, status);
    }

    @Override
    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }


}
