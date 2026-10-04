package com.arsenalsimulator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.arsenalsimulator.data.model.Category
import com.arsenalsimulator.data.model.Weapon
import com.arsenalsimulator.data.repository.WeaponRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ArsenalViewModel(private val repo: WeaponRepository) : ViewModel() {
    private val _query = MutableStateFlow("")
    private val _category = MutableStateFlow<Category?>(null)
    private val _favoritesOnly = MutableStateFlow(false)

    val weapons: StateFlow<List<Weapon>> = combine(_query, _category, _favoritesOnly) { q, c, fav ->
        Triple(q, c, fav)
    }.flatMapLatest { (q, c, fav) ->
        when {
            fav -> repo.favorites()
            q.isNotBlank() -> repo.search(q)
            c != null -> repo.byCategory(c)
            else -> repo.all()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setQuery(q: String) { _query.value = q }
    fun setCategory(c: Category?) { _category.value = c }
    fun toggleFavorites() { _favoritesOnly.value = !_favoritesOnly.value }
    fun toggleFavorite(w: Weapon) = viewModelScope.launch { repo.toggleFavorite(w) }

    companion object {
        fun factory(repo: WeaponRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ArsenalViewModel(repo) as T
            }
        }
    }
}
