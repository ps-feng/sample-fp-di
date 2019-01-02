package com.sample.todos.domain

import reactor.core.publisher.Mono

typealias TodoFetcherFn = (userId: Int) -> Mono<List<Todo>>

fun getCompletedTodos(fetchTodos: TodoFetcherFn, userId: Int): Mono<List<Todo>> {
    return fetchTodos(userId).map { todos -> todos.filter { it.isCompleted } }
}