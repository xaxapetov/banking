package ru.iql.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.iql.banking.configs.ContainersEnvironment;
import ru.iql.banking.services.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BankingApplicationTests extends ContainersEnvironment {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJVU0VSX0lEIjoiMSJ9.15wp5NTWAdO3qW2nn7g32PebaAbkYtu5zsvjyZgrpp8";

    @Test
    public void getUser_Expect_List_Size_Is_Two() throws Exception {
        System.out.println("null");
        MvcResult mvcResult = this.mockMvc.perform(get("/banking/users")
                        .param("phone", "79781234567").header("Authorization", BEARER_TOKEN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content.size()").value(1))
                .andReturn();
        mvcResult.getRequest();
    }


}
