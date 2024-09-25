package yesable.resume.module.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import yesable.resume.global.config.EntityMapper;
import yesable.resume.module.model.Onboarding;
import yesable.resume.module.service.OnboardingData;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OnboardingMapper extends EntityMapper<OnboardingData, Onboarding> {
    OnboardingMapper INSTANCE = Mappers.getMapper(OnboardingMapper.class);
}
