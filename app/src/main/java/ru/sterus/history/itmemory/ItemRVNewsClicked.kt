package ru.sterus.history.itmemory

import ru.sterus.history.itmemory.model.Article

interface ItemRVNewsClicked {
    fun onItemClicked(article: Article)
}