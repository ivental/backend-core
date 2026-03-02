package ru.mentee.power.crm.spring.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class FieldInjectionProblemTest {

  @Test
  void fieldInjectionCausesNullPointerWithoutSpring() {
    DemoController controller = new DemoController(null);
    assertThrows(
        NullPointerException.class,
        () -> controller.getFieldRepository().findAll(),
        "Field Injection вызывает NPE без Spring контекста");
  }
}
