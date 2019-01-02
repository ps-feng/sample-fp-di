package com.sample.todos

import com.sample.todos.data.UserTodosRepository
import com.sample.todos.domain.Todo
import com.sample.todos.domain.getCompletedTodos
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Component
class TodosHandler @Autowired constructor(
    private val repository: UserTodosRepository
) {
    fun getTodos(request: ServerRequest): Mono<ServerResponse> {
        val userId = request.pathVariable("id").toIntOrNull()

        return if (userId != null) {
            val todosResponseMono =
                getCompletedTodos(repository::getUserTodos, userId)
                    .map { it.toResponse(userId) }
                    .onErrorResume { e -> internalServerError(e) }

            ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(todosResponseMono, GetTodosResponse::class.java)
        } else {
            ServerResponse.badRequest().syncBody("id is not an integer")
        }
    }
}

private fun internalServerError(e: Throwable): Mono<GetTodosResponse> {
    return Mono.error(
        ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Some internal service is failing",
            e
        )
    )
}

private fun List<Todo>.toResponse(userId: Int) = GetTodosResponse(
    GetTodosResponse.Data(userId, this)
)

data class GetTodosResponse(
    val data: Data
) {
    data class Data(
        val userId: Int,
        val todos: List<Todo>
    )
}
