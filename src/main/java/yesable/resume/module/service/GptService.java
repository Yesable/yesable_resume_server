package yesable.resume.module.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yesable.resume.global.client.GptClient;
import yesable.resume.global.model.GptRequest;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GptService {

    private final GptClient gptClient;

    /**
     * 공통 GPT 요청 처리
     */
    private String callGptApi(String key, String value) {
        // 프롬프트 생성
        String prompt = createPrompt(key, value, null);
        GptRequest request = new GptRequest(
                List.of(Map.of("question", key, "answer", prompt))
        );

        return gptClient.generateResume(request);
    }

    /**
     * 직무 경험
     */
    public String generateExperience(String workExperience) {
        return callGptApi("Work Experience", workExperience);
    }

    /**
     * 문항과 답변
     */
    public String refineAnswer(String question, String answer) {
        return callGptApi(question, answer);
    }

    /**
     * 프롬프트 생성
     */
    private String createPrompt(String key, String value, String additionalContext) {
        return String.format(
                """
                        다음의 정보를 바탕으로 더 나은 설명을 제공하세요.\s
                        1. **키**: %s\s
                        2. **현재 설명**: %s\s
                        3. **배경 정보**: %s\s
                           - 이 정보를 활용하여 더 구체적이고 유용한 설명을 제공해 주세요.\s
                        4. **기대하는 형식**: 결과는 한 문단으로 요약하고, 가능하다면 불릿 포인트 형식으로도 제공해 주세요.""",
                key,
                value,
                additionalContext != null ? additionalContext : "해당 정보는 없다."
        );
    }
}


