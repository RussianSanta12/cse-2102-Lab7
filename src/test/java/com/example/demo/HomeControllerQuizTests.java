package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.mock.web.MockHttpServletResponse;
import com.example.demo.controller.HomeController;

public class HomeControllerQuizTests {

        @Test
        public void independentSessionsDoNotInterfere() throws Exception {
                HomeController controller = new HomeController();

                MockHttpSession session1 = new MockHttpSession();
                MockHttpSession session2 = new MockHttpSession();

                ConcurrentModel model1 = new ConcurrentModel();
                String view1 = controller.questionForm(model1, session1);
                org.junit.jupiter.api.Assertions.assertEquals("question", view1);
                org.junit.jupiter.api.Assertions.assertEquals(0, model1.getAttribute("currentIndex"));

                ConcurrentModel model2 = new ConcurrentModel();
                String view2 = controller.questionForm(model2, session2);
                org.junit.jupiter.api.Assertions.assertEquals("question", view2);
                org.junit.jupiter.api.Assertions.assertEquals(0, model2.getAttribute("currentIndex"));

                // session1 answers 'true'
                MockHttpServletResponse response1 = new MockHttpServletResponse();
                String postResult = controller.submitAnswer(true, new ConcurrentModel(), session1, response1);
                // For redirects controller sets 303 and returns null
                org.junit.jupiter.api.Assertions.assertNull(postResult);
                org.junit.jupiter.api.Assertions.assertEquals(303, response1.getStatus());

                // session1 should now have currentIndex 1
                ConcurrentModel model1b = new ConcurrentModel();
                controller.questionForm(model1b, session1);
                org.junit.jupiter.api.Assertions.assertEquals(1, model1b.getAttribute("currentIndex"));

                // session2 should still be at index 0
                ConcurrentModel model2b = new ConcurrentModel();
                controller.questionForm(model2b, session2);
                org.junit.jupiter.api.Assertions.assertEquals(0, model2b.getAttribute("currentIndex"));
        }

}
