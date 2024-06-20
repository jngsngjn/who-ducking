package hello.repository;

import hello.dto.admin.RequestDetailDTO;
import hello.dto.admin.RequestListDTO;
import hello.dto.user.MyRequestDTO;
import hello.entity.request.Request;
import hello.entity.request.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT new hello.dto.user.MyRequestDTO(r.id, r.title, r.content, r.writeDate, r.status, r.responseDate, r.response)" +
            "FROM Request r")
    Page<MyRequestDTO> findMyRequest(Pageable pageable);

    @Query("SELECT new hello.dto.admin.RequestListDTO(r.id, r.title, u.nickname, r.writeDate) " +
            "FROM Request r JOIN r.user u " +
            "WHERE r.status = :status " +
            "ORDER BY r.writeDate ASC")
    Page<RequestListDTO> findByStatus(@Param("status") RequestStatus status, Pageable pageable);

    @Query("SELECT new hello.dto.admin.RequestDetailDTO(r.id, r.title, r.content, u.nickname, r.writeDate) " +
            "FROM Request r JOIN r.user u " +
            "WHERE r.id = :id")
    RequestDetailDTO findRequestDetailById(@Param("id") Long id);
}