package yesable.resume.global.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import yesable.resume.global.config.GptClientConfig;
import yesable.resume.global.model.GptRequest;

@FeignClient(name = "gptClient", url = "${openai.api.url}", configuration = GptClientConfig.class)
public interface GptClient {

    @PostMapping("")
    String generateResume(@RequestBody GptRequest request);
}

