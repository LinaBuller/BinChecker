package com.buller.binchecker.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.buller.binchecker.R

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    val items = listOf(BottomNavItem.HomeItem, BottomNavItem.HistoryItem)
    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.label)
                    )
                },
                label = {
                    Text(text = stringResource(item.label))
                },
                selected = selectedItem == item,
                onClick = {
                    onItemSelected(item)
                },
                alwaysShowLabel = true
            )
        }
    }


}

sealed class BottomNavItem(
    val icon: ImageVector,
    val label: Int,
    val route: String
) {
    data object HomeItem :
        BottomNavItem(icon = Icons.Filled.Home, label = R.string.home, route = "home")

    data object HistoryItem :
        BottomNavItem(icon = Icons.Filled.Star, label = R.string.history, route = "history")

}