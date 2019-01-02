package com.sample.todos.data

import com.sample.todos.domain.Todo

data class DataTodo(
    val id: Int,
    val title: String,
    val userId: Int,
    val completed: Boolean
)

