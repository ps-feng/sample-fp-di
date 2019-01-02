package com.sample.todos.data

import com.sample.todos.domain.Todo
import com.sample.todos.domain.toDomain
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

fun getUserTodos(
    baseUrl: String,
    userId: Int
): Mono<List<Todo>> {
    return WebClient.create(baseUrl).get()
        .uri { uriBuilder ->
            uriBuilder.path("/users/$userId/todos")
                .build()
        }
        .retrieve()
        .bodyToMono(object : ParameterizedTypeReference<List<DataTodo>>() {})
        .map { dataTodos -> dataTodos.map { it.toDomain() } }
}