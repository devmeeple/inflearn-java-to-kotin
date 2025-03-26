package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @DisplayName("책을 등록한다")
    @Test
    fun saveBook() {
        val request = BookRequest("강철의 연금술사 1")

        bookService.saveBook(request)

        val results = bookRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("강철의 연금술사 1")
    }

    @DisplayName("책을 대출한다")
    @Test
    fun loanBook() {
        val savedUser = userRepository.save(User("에드워드 엘릭", 15))
        bookRepository.save(Book("강철의 연금술사 1"))
        val request = BookLoanRequest(
            "에드워드 엘릭",
            "강철의 연금술사 1"
        )

        bookService.loanBook(request)

        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("강철의 연금술사 1")
        assertThat(results[0].user.id).isEqualTo(savedUser.id)
        assertThat(results[0].isReturn).isFalse()
    }

    @DisplayName("[예외] 이미 대출된 책은 대출할 수 없다")
    @Test
    fun loanBookThrowsException() {
        val savedUser = userRepository.save(User("에드워드 엘릭", 15))
        bookRepository.save(Book("강철의 연금술사 1"))
        val request = BookLoanRequest(
            "에드워드 엘릭",
            "강철의 연금술사 1"
        )
        userLoanHistoryRepository.save(
            UserLoanHistory(savedUser, "강철의 연금술사 1", false)
        )

        assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.apply {
            assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
        }
    }

    @DisplayName("책을 반납한다")
    @Test
    fun returnBook() {
        val savedUser = userRepository.save(User("에드워드 엘릭", 15))
        userLoanHistoryRepository.save(
            UserLoanHistory(savedUser, "강철의 연금술사 1", false)
        )
        val request = BookReturnRequest("에드워드 엘릭", "강철의 연금술사 1")

        bookService.returnBook(request)

        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].isReturn).isTrue()
    }
}