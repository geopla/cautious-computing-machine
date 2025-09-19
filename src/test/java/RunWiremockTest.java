import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
public class RunWiremockTest {

    @Test
    @DisplayName("Should simply run Wiremock having some stubs from 'test/resources/mappings'")
    void shouldRunWiremock(WireMockRuntimeInfo wireMockRuntimeInfo) {
        WireMock wireMock = wireMockRuntimeInfo.getWireMock();

        assertThat(wireMock.allStubMappings().getMappings()).isNotEmpty();
    }
}
