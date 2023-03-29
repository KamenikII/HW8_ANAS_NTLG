package ru.netology.coroutines.dto

data class PostWithCommentsAndAuthors(
    val post: PostWithAuthor,
    val comments: List<Comment>,
)
