package yesable.resume.module.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.mongodb.core.mapping.Document;
import yesable.resume.module.model.vo.QuestionVo;

import java.util.List;

/**
 * 유저 이력서 테이블
 */
@Data
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "RESUME")
public class Resume {
    @Id
    private long resumeId;

    private long userId;

    private String resumeName;

    private Boolean experienced;

    private String workExperience;

    private List<QuestionVo> questionDatas;
}
