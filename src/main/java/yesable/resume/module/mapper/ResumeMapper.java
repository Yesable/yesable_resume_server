package yesable.resume.module.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import yesable.resume.global.config.EntityMapper;
import yesable.resume.module.model.Resume;
import yesable.resume.module.service.ResumeData;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResumeMapper extends EntityMapper<ResumeData, Resume> {
    ResumeMapper INSTANCE = Mappers.getMapper(ResumeMapper.class);
}


