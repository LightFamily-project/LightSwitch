package com.lightswitch.controller

import com.lightswitch.application.service.AuthService
import com.lightswitch.infrastructure.security.JwtToken
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Mock
    lateinit var authService: AuthService

    @InjectMocks
    lateinit var userController: UserController

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build()
        val authentication: Authentication = UsernamePasswordAuthenticationToken("1", "password", emptyList())
        val securityContext: SecurityContext = SecurityContextHolder.createEmptyContext()
        securityContext.authentication = authentication
        SecurityContextHolder.setContext(securityContext)
    }

    @Test
    fun `should login user successfully`() {
        val jwtToken = JwtToken("accessToken", "refreshToken")

        `when`(authService.login("testUser", "password")).thenReturn(jwtToken)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"username": "testUser", "password": "password"}""")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.accessToken").value("accessToken"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.refreshToken").value("refreshToken"))
    }


    @Test
    fun `should refresh user token successfully`() {
        val refreshToken = "Bearer refreshToken"
        val jwtToken = JwtToken("newAccessToken", "newRefreshToken")

        `when`(authService.reissue("refreshToken", 1L)).thenReturn(jwtToken)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/v1/users/auth/refresh")
                .header("Authorization", refreshToken)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.accessToken").value("newAccessToken"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.refreshToken").value("newRefreshToken"))
    }

    @Test
    fun `should logout user successfully`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/users/logout")
                .header("Authorization", "Bearer someAccessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"username": "testUser", "password": "password"}""")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("testUser"))

    }
}
