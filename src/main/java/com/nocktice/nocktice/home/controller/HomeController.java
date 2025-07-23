package com.nocktice.nocktice.home.controller;

import com.nocktice.nocktice.home.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final TestService testService;
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public HomeController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/health")
    public String health(Model model) {
        model.addAttribute("test", testService.getDataHealth()); // JSP에 전달할 데이터
        logger.info("INFO 로그 테스트");
        logger.error("ERROR 로그 테스트");
        return "health"; // → /WEB-INF/views/health.jsp 렌더링
    }
}
