package yesable.resume.module.service;

import com.google.protobuf.ByteString;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import yesable.resume.module.mapper.ResumeMapper;
import yesable.resume.module.model.Resume;
import yesable.resume.module.model.vo.QuestionVo;
import yesable.resume.module.repository.ResumeRepository;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 장애인 사용자 자기소개서 작성 처리
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ResumeService extends ResumeServiceGrpc.ResumeServiceImplBase {

    private final ResumeRepository resumeRepository;
    private final TemplateEngine templateEngine;

    private final GptService gptService;
    private final ResumeMapper resumeMapper;

    @Override
    public void createResume(ResumeCreateRequest resumeRequest, StreamObserver<ResumeResponse> responseObserver) {
        try {
            // Request에서 데이터 추출
            ResumeData resumeData = resumeRequest.getResume();
            List<QuestionVo> questionData = resumeData.getQuestionDatasList().stream()
                    .map(q -> new QuestionVo(q.getQuestion(), q.getAnswer()))
                    .toList();

            // GPT API 호출하여 자기소개서(직무 경험) 생성
            String generatedWorkExperience = gptService.generateExperience(resumeData.getWorkExperience());

            // 질문, 답변을 이용하여 QuestionVo 리스트 생성
            List<QuestionVo> updatedQuestionData = questionData.stream()
                    .map(question -> {
                        // GPT API로 질문과 답변을 전달하여 GPT가 다듬어준 답변 받기
                        String gptAnswer = gptService.refineAnswer(question.getQuestion(), question.getAnswer());
                        return QuestionVo.builder()
                                .question(question.getQuestion())
                                .answer(gptAnswer)
                                .build();
                    })
                    .collect(Collectors.toList());

            // Resume 엔티티 생성 및 저장
            Resume resume = Resume.builder()
                    .userId(resumeData.getUserId())
                    .resumeName(resumeData.getResumeName())
                    .experienced(resumeData.getExperienced())
                    .workExperience(generatedWorkExperience)
                    .questionDatas(updatedQuestionData)
                    .build();

            Resume savedResume = resumeRepository.save(resume);

            // 응답 처리
            ResumeResponse response = ResumeResponse.newBuilder()
                    .setResumeId(savedResume.getResumeId())
                    .setResume(resumeMapper.toDto(savedResume))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error creating resume", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void updateResume(ResumeUpdateRequest resumeRequest, StreamObserver<ResumeResponse> responseObserver) {
        try {
            // 수정할 Resume 데이터 조회
            Resume existingResume = resumeRepository.findById(resumeRequest.getResumeId())
                    .orElseThrow(() -> new RuntimeException("Resume not found"));

            ResumeData resumeData = resumeRequest.getResume();

            // 질문과 답변 데이터 준비
            List<QuestionVo> questionData = resumeData.getQuestionDatasList().stream()
                    .map(q -> new QuestionVo(q.getQuestion(), q.getAnswer()))
                    .toList();

            // 각 질문과 답변에 대해 GPT API 호출하여 업데이트된 답변 생성
            List<QuestionVo> updatedQuestionData = questionData.stream()
                    .map(questionVo -> new QuestionVo(questionVo.getQuestion(), gptService.refineAnswer(questionVo.getQuestion(), questionVo.getAnswer())))
                    .collect(Collectors.toList());

            // 직무 경험 데이터 업데이트
            String updatedResumeExperience = gptService.generateExperience(existingResume.getWorkExperience());

            // 기존 자기소개서 업데이트
            existingResume.setResumeName(resumeData.getResumeName());
            existingResume.setExperienced(resumeData.getExperienced());
            existingResume.setWorkExperience(updatedResumeExperience);
            existingResume.setQuestionDatas(updatedQuestionData);

            // 저장
            Resume savedResume = resumeRepository.save(existingResume);

            // 응답 처리
            ResumeResponse response = ResumeResponse.newBuilder()
                    .setResumeId(savedResume.getResumeId())
                    .setResume(resumeMapper.toDto(savedResume))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error updating resume", e);
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to update resume").asRuntimeException());
        }
    }


    /**
     * 자기소개서 PDF 다운로드
     */
    @Override
    public void downloadResumeRdf(ResumeDownloadRequest resumeDownloadRequest, StreamObserver<ResumeDownloadResponse> responseObserver) {
        try {
            // 이력서 데이터 가져오기
            Resume resumeData = resumeRepository.findById(resumeDownloadRequest.getResumeId()).orElse(null);
            if (resumeData == null) {
                responseObserver.onError(new Throwable("Resume not found"));
                return;
            }

            // 이력서를 Thymeleaf 템플릿으로 변환
            String htmlContent = generateHtmlFromTemplate(resumeData);

            // HTML을 PDF로 변환
            byte[] pdfData = generatePdfFromHtml(htmlContent);

            // gRPC 응답 생성
            ResumeDownloadResponse response = ResumeDownloadResponse.newBuilder()
                    .setPdfData(ByteString.copyFrom(pdfData))
                    .build();

            // 응답 전송
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Failed to download resume PDF", e);
            responseObserver.onError(e);
        }
    }

    // HTML을 Thymeleaf 템플릿을 이용해 생성
    private String generateHtmlFromTemplate(Resume resumeData) {
        Context context = new Context();
        context.setVariable("resume", resumeData);

        // Thymeleaf 템플릿에서 HTML 생성
        return templateEngine.process("resumeTemplate", context);
    }

    // HTML을 PDF로 변환
    private byte[] generatePdfFromHtml(String htmlContent) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);

        return outputStream.toByteArray();
    }
}
