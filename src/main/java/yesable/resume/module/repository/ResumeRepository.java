package yesable.resume.module.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import yesable.resume.module.model.Resume;

@Repository
public interface ResumeRepository extends MongoRepository<Resume, Long> {
}
