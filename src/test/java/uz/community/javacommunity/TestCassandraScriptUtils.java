package uz.community.javacommunity;

import com.datastax.oss.driver.api.core.CqlSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.cassandra.core.cql.session.init.ScriptUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("functionalTest")
@Slf4j
public class TestCassandraScriptUtils {
    private final CqlSession cqlSession;

    public void executeClasspathScript(String path) {
        log.info("Executing classpath script {}", path);
        try {
            ScriptUtils.executeCqlScript(cqlSession, new ClassPathResource(path));
        } catch (Exception ex) {
            log.error("Error while executing script {}", path, ex);
        }
        log.info("Executing classpath script {} done", path);
    }

    public void execute(String cql) {
        cqlSession.execute(cql);
    }

}

