package yesable.resume.module.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

/**
 * 경력사항 테이블
 */
@Data
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentHistoryVo {

    // 근무지명
    private String corporateName;

    // 근무시작일
    private LocalDate startDate;

    // 근무종료일
    private LocalDate endDate;

    // 근무상태 (true : 재직, false: 퇴사)
    private boolean isEmployed;

    // 직무
    private String duty;

    // 근무 내용 관련 설명
    private String jobDescription;
}
