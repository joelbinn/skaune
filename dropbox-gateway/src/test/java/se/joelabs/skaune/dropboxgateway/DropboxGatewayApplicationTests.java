package se.joelabs.skaune.dropboxgateway;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import se.joelabs.skaune.dropboxgateway.web.DropBoxController;

@SpringBootTest
class DropboxGatewayApplicationTests {

  @Autowired
  ApplicationContext applicationContext;

  @Test
  void contextLoads() {
    Assertions.assertThat(applicationContext.getBean(DropBoxController.class)).isNotNull();
  }

}
