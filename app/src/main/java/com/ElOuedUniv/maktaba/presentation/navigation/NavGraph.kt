package com.ElOuedUniv.maktaba.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ElOuedUniv.maktaba.data.local.PreferenceManager
import com.ElOuedUniv.maktaba.presentation.book.BookListView
import com.ElOuedUniv.maktaba.presentation.book.add.AddBookView
import com.ElOuedUniv.maktaba.presentation.book.detail.BookDetailView
import com.ElOuedUniv.maktaba.presentation.category.CategoryListView
import com.ElOuedUniv.maktaba.presentation.onboarding.OnboardingView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    val isOnboardingCompleted: Flow<Boolean> = preferenceManager.isOnboardingCompleted
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: NavViewModel = hiltViewModel()
) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsState(initial = null)

    if (isOnboardingCompleted == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(
        navController = navController,
        startDestination = if (isOnboardingCompleted == true) Screen.BookList.route else Screen.Onboarding.route
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingView(
                onNavigateToLibrary = {
                    navController.navigate(Screen.BookList.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.BookList.route) {
            BookListView(
                onCategoriesClick = { navController.navigate(Screen.CategoryList.route) },
                onAddBookClick = { navController.navigate(Screen.AddBook.route) },
                onBookClick = { isbn ->
                    navController.navigate(Screen.BookDetail.createRoute(isbn))
                }
            )
        }

        composable(Screen.BookDetail.route) {
            BookDetailView(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.CategoryList.route) {
            CategoryListView(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.AddBook.route) {
            AddBookView(onBackClick = { navController.popBackStack() })
        }
    }
}
