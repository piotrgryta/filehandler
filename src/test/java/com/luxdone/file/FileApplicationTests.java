package com.luxdone.file;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FileApplicationTests extends IntegrationTest {

  @Autowired
  FileApplication application;

  @Test
  void context_loads() {
    assertThat(application).isNotNull();
  }

}
