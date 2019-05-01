package com.joinesty;

public class Client {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class);
    }

    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            String str = restTemplate.getForObject(
                    "https://api.joinesty.com/healthcheck", String.class);
            log.info(str);
        };
    }
}
