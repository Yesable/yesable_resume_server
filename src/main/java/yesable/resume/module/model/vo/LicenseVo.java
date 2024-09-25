package yesable.resume.module.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 유저 온보딩 테이블
 */
@Data
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class LicenseVo {

    // 자격증명
    private String licenseName;

    // 자격증 발행기관
    private String licenseInstitution;

    // 취득년도
    private Integer acquisitionYear;

}
