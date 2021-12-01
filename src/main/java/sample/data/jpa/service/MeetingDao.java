package sample.data.jpa.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sample.data.jpa.domain.Meeting;

@Transactional
public interface MeetingDao extends JpaRepository<Meeting,Long> {
}
