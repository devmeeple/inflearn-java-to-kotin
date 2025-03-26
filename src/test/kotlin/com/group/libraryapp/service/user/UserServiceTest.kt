package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository
) {

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @DisplayName("사용자를 등록한다")
    @Test
    fun saveUserTest() {
        val request = UserCreateRequest("Martin Fowler", null)

        userService.saveUser(request)

        val results = userRepository.findAll()
        assertThat(results.size).isEqualTo(1)
        assertThat(results[0].name).isEqualTo("Martin Fowler")
        assertThat(results[0].age).isNull()
    }

    @DisplayName("등록한 사용자를 전체 조회한다")
    @Test
    fun getUserTest() {
        userRepository.saveAll(
            listOf(
                User("김동영", 29),
                User("최용준", null),
            )
        )

        val results = userService.getUsers()

        assertThat(results).hasSize(2)
        assertThat(results).extracting("name").containsExactlyInAnyOrder("김동영", "최용준")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(29, null)
    }

    @DisplayName("사용자 이름을 수정한다")
    @Test
    fun updateUserNameTest() {
        val savedUser = userRepository.save(User("문지수", 30))
        val request = UserUpdateRequest(savedUser.id, "박창현")

        userService.updateUserName(request)

        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("박창현")
    }

    @DisplayName("사용자를 삭제한다")
    @Test
    fun deleteUserTest() {
        userRepository.save(User("양인규", 29))

        userService.deleteUser("양인규")

        assertThat(userRepository.findAll()).isEmpty()


    }
}