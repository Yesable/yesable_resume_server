package yesable.resume.module.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.mongodb.core.mapping.Document;
import yesable.resume.module.enums.DisabilityType;
import yesable.resume.module.enums.LocationType;
import yesable.resume.module.model.vo.AcademicVo;
import yesable.resume.module.model.vo.EmploymentHistoryVo;
import yesable.resume.module.model.vo.LicenseVo;

import java.util.List;

/**
 * 유저 온보딩 테이블
 */
@Data
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ONBOARDING")
public class Onboarding {

    @Id
    private long userId;

    // 장애 유형
    @Enumerated(value = EnumType.STRING)
    private DisabilityType disabilityType;

    // 희망 근무지 유형
    @Enumerated(value = EnumType.STRING)
    private LocationType locationType;

    // 대중교통 이용 가능 여부
    private boolean isTransit;

    // 학력사항
    private List<AcademicVo> academicList;

    // 경력사항
    private List<EmploymentHistoryVo> employmentHistoryList;

    // 자격증
    private List<LicenseVo> licenseList;

}
