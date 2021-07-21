package br.com.brasilprevteste.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("brasilprevtesteconfig")
public class BrasilprevtesteProperty {

    private final Seguranca seguranca = new Seguranca();
    private final Mail mail = new Mail();
    private final Disco disco = new Disco();
    private final S3 s3 = new S3();
    private final Oauth2 oauth2 = new Oauth2();

    @Data
    public static class Disco {
        private String raiz;
        private String diretorioFotos;
    }

    @Data
    public static class Mail {
        private String host;
        private Integer port;
        private String username;
        private String password;
    }

    @Data
    public static class S3 {
        private String accessKeyId;
        private String secretAccessKey;
        private String bucket;
    }

    @Data
    public static class Seguranca {
        private String originPermitida;
        private boolean enableHttps;
    }

    @Data
    public static class Oauth2 {
        private String clientId;
        private String clientSecretEncript;
        private String clientSecretDecript;
        private int tokenTimeout;
        private int refreshTokenTimeout;
        private String privateKey;
        private String publicKey;
        private String accessTokenUri;
    }
}
