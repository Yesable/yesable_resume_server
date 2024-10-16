package yesable.resume.module.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 이력서 질문
 */
@Data
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVo {
    private String question;
    private String answer;
}
