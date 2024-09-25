package yesable.resume.module.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import yesable.resume.module.model.Onboarding;

@Repository
public interface OnboardingRepository extends MongoRepository<Onboarding, Long> {
}
