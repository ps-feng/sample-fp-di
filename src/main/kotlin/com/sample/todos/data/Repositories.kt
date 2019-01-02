package com.sample.todos.data

import com.sample.ExternalServices
import com.sample.todos.domain.Todo
import com.sample.todos.domain.toDomain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class UserTodosRepository @Autowired constructor(
    @Qualifier(ExternalServices.TODOS_BASE_URL) private val baseUrl: String
) {
    fun getUserTodos(userId: Int): Mono<List<Todo>> {
        return WebClient.create(baseUrl).get()
            .uri { uriBuilder ->
                uriBuilder.path("/users/$userId/todos")
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<DataTodo>>() {})
            .map { dataTodos -> dataTodos.map { it.toDomain() } }
    }
}
