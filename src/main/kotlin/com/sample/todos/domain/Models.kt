package com.sample.todos.domain

import com.sample.todos.data.DataTodo

data class Todo(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)

fun DataTodo.toDomain() = Todo(
    id = id,
    title = title,
    isCompleted = completed
)