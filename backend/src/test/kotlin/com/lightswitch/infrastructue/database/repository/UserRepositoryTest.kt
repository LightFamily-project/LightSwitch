package com.lightswitch.infrastructue.database.repository

import com.lightswitch.infrastructure.database.entity.User
import com.lightswitch.infrastructure.database.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryTest : BaseRepositoryTest() {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `findAll returns entities`() {
        userRepository.save(
            User(
                username = "jason",
                passwordHash = "1234!@#$",
            ),
        )

        assertThat(userRepository.findAll())
            .hasSize(1)
            .extracting("username", "passwordHash")
            .contains(Tuple.tuple("jason", "1234!@#$"))
    }
}
