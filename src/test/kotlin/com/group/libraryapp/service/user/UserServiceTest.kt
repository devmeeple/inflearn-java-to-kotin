package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository
) {

    @Test
    fun saveUserTest() {
        val request = UserCreateRequest("Martin Fowler", null)

        userService.saveUser(request)

        val results = userRepository.findAll()
        assertThat(results.size).isEqualTo(1)
        assertThat(results[0].name).isEqualTo("Martin Fowler")
        assertThat(results[0].age).isNull()
    }
}