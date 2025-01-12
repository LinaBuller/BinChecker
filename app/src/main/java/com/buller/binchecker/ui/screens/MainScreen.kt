package com.buller.binchecker.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.buller.binchecker.ui.screens.history.HistoryRoute
import com.buller.binchecker.ui.screens.home.HomeRoute

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.HomeItem) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(selectedItem = selectedItem) { item ->
                if (navController.currentBackStackEntry?.destination?.route != item.route) {
                    selectedItem = item
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }

    ) { innerPadding ->
        NavHostContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = navController
        )
    }
}

@Composable
fun NavHostContainer(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavItem.HomeItem.route) {
        composable(BottomNavItem.HomeItem.route) {
            HomeRoute(modifier = modifier)
        }
        composable(BottomNavItem.HistoryItem.route) {
            HistoryRoute(modifier = modifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}