package org.com.gateway.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * Inicia o servidor TCP do H2 para permitir conexões externas (ex.: DBeaver)
 * à base em memória usada pela aplicação.
 * Acessar via: jdbc:h2:tcp://localhost:9092/mem:gatewaydb
 */
@Configuration
public class H2ServerConfig {

    /**
     * Inicia o servidor TCP do H2 apenas para localhost (sem -tcpAllowOthers).
     * Porta padrão: 9092.
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TcpServer() throws SQLException {
        return Server.createTcpServer(
                "-tcp",
                "-tcpPort", "9092",
                "-ifNotExists"
        );
    }
}
