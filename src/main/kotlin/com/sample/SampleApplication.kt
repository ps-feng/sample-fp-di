package com.sample

import arrow.syntax.function.partially1
import com.sample.ExternalServices.Companion.TODOS_BASE_URL
import com.sample.todos.data.getUserTodos
import com.sample.todos.domain.Todo
import com.sample.todos.domain.getCompletedTodos
import com.sample.todos.getTodos
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@SpringBootApplication
class SampleApplication

fun main(args: Array<String>) {
    runApplication<SampleApplication>(*args)
}

@Configuration
class Router @Autowired constructor(
    @Qualifier(TODOS_BASE_URL) private val todosBaseUrl: String
) {

    @Bean
    fun routes(): RouterFunction<ServerResponse> = router {
        accept(MediaType.APPLICATION_JSON).nest {
            GET("/user/{id}/completedtodos") {
                getTodos(it, getCompletedTodosInteractorFn())
            }
        }
    }

    private fun getCompletedTodosInteractorFn(): (userId: Int) -> Mono<List<Todo>> {
        val getUserTodosFn = ::getUserTodos.partially1(todosBaseUrl)
        return ::getCompletedTodos.partially1(getUserTodosFn)
    }
}

@Configuration
class ExternalServices {

    @Bean
    @Qualifier(TODOS_BASE_URL)
    fun todosBaseUrl(): String = "https://jsonplaceholder.typicode.com"

    companion object {
        const val TODOS_BASE_URL = "TODOS_BASE_URL"
    }
}