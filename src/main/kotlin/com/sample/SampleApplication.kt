package com.sample

import com.sample.todos.TodosHandler
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

@SpringBootApplication
class SampleApplication

fun main(args: Array<String>) {
    runApplication<SampleApplication>(*args)
}

@Configuration
class Router @Autowired constructor(
    private val todosHandler: TodosHandler
) {

    @Bean
    fun routes(): RouterFunction<ServerResponse> = router {
        accept(MediaType.APPLICATION_JSON).nest {
            GET("/user/{id}/completedtodos", todosHandler::getTodos)
        }
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