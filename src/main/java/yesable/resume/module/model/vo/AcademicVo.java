package yesable.resume.module.model.vo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import yesable.resume.module.enums.AcademicType;
import yesable.resume.module.enums.EnrollmentType;

import java.time.LocalDate;

/**
 * 학력사항
 */
@Data
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class AcademicVo {

    // 재학 상태 (재학, 휴학, 졸업)
    @Enumerated(value = EnumType.STRING)
    private EnrollmentType enrollmentType;

    // 학력 상태 (학사, 석사, 박사)
    @Enumerated(value = EnumType.STRING)
    private AcademicType academicType;

    // 학교명
    private String schoolName;

    // 잔공
    private String major;

    // 입학년도
    private LocalDate entranceDate;

    // 졸업년도
    private LocalDate graduateDate;



}
