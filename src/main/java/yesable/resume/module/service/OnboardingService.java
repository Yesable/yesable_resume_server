package yesable.resume.module.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import yesable.resume.module.mapper.OnboardingMapper;
import yesable.resume.module.model.Onboarding;
import yesable.resume.module.repository.OnboardingRepository;

/**
 * 장애인 사용자 온보딩 처리
 */
@GrpcService
@RequiredArgsConstructor
public class OnboardingService extends OnboardingServiceGrpc.OnboardingServiceImplBase {

    private final OnboardingMapper onboardingMapper;
    private final OnboardingRepository onboardingRepository;

    /**
     * 온보딩 생성
     */
    @Override
    public void createOnboarding(OnboardingRequest onboardingRequest, StreamObserver<OnboardingResponse> responseObserver) {

        // 온보딩 데이터 Entity로 변환 후 저장
        onboardingRepository.save(onboardingMapper.toEntity(onboardingRequest.getOnboarding()));

        // response 값 생성
        OnboardingResponse response = OnboardingResponse.newBuilder()
                .setOnboarding(onboardingRequest.getOnboarding())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * 온보딩 저장
     */
    @Override
    public void updateOnboarding(OnboardingRequest onboardingRequest, StreamObserver<OnboardingResponse> responseObserver) {
        // TODO: Exception 에러 처리
        // userId 로 온보딩 데이터 있는지 확인 후 없으면 에러 처리
        Onboarding onboarding = onboardingRepository.findById(onboardingRequest.getOnboarding().getUserId())
                .orElseThrow(() -> new RuntimeException());

        // 온보딩 데이터 Entity로 변환 후 저장
        onboardingRepository.save(onboardingMapper.toEntity(onboardingRequest.getOnboarding()));

        // response 값 생성
        OnboardingResponse response = OnboardingResponse.newBuilder()
                .setOnboarding(onboardingRequest.getOnboarding())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
