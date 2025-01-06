package com.example.weatherapp.ui.search_city

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.data.datastore.SavedCity
import com.example.weatherapp.ui.composable.LoadingScreen
import com.example.weatherapp.ui.composable.SearchAutoComplete
import com.example.weatherapp.ui.theme.LocalBackgroundColor
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCityScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchCityViewModel = hiltViewModel(),
    navigateToPagerScreen: (Int) -> Unit
) {
    val autoCompletionResult = viewModel.autoCompletionResult
    val inSelectionMode by remember { derivedStateOf { viewModel.selectedCitiesToRemove.isNotEmpty() } }
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val state = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val scope = rememberCoroutineScope()
    LaunchedEffect(inSelectionMode) {
        if(inSelectionMode) { state.bottomSheetState.expand() } }
    LaunchedEffect(!inSelectionMode) {
        if(!inSelectionMode) { state.bottomSheetState.hide() } }
    BottomSheetScaffold(sheetDragHandle = {}, scaffoldState = state, sheetPeekHeight = 0.dp,sheetShape = RectangleShape, sheetContent = {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Outlined.Delete, null, Modifier.size(35.dp).clickable {
            scope.launch {
                viewModel.deleteCitiesFromUserCities()
                state.bottomSheetState.hide()
            }
       })
        Text(text = "Supprimer")
    } }) {
        if (viewModel.isLoading) { LoadingScreen()
        } else {
            Column(modifier = modifier.padding(16.dp)) {
                Text(
                    modifier = Modifier.padding(vertical = 16.dp),
                    text = "Gérer les villes",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                SearchAutoComplete(cities = autoCompletionResult) { city ->
                    viewModel.addCityToUserFavoriteCities(city)
                }
                Spacer(Modifier.height(16.dp))
                if (viewModel.savedCities.isNotEmpty()) {
                    UserCitiesList(
                        viewModel = viewModel,
                        savedCities = viewModel.savedCities,
                        inSelectionMode = inSelectionMode,
                        onItemSelected = { index -> navigateToPagerScreen(index) })
                }
            }
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserCitiesList(
    viewModel: SearchCityViewModel,
    savedCities: List<SavedCity>,
    onItemSelected: (Int) -> Unit,
    inSelectionMode:Boolean
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        itemsIndexed(items = savedCities) { index, savedCity ->
            val selected by remember { derivedStateOf { savedCity in viewModel.selectedCitiesToRemove } }
            UserCityItem(selected = selected, inSelectionMode = inSelectionMode, savedCity = savedCity,
                modifier = if(inSelectionMode) { Modifier.clickable { if(selected) viewModel.selectCityToRemove(savedCity) else viewModel.unSelectCityToRemove(savedCity)} } else {Modifier.combinedClickable(
                onLongClick = { viewModel.unSelectCityToRemove(savedCity) },
                onClick = { onItemSelected(index) }
            ) } )
        }
    }
}


@Composable
fun UserCityItem(
    selected: Boolean,
    inSelectionMode: Boolean,
    savedCity: SavedCity,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        LocalBackgroundColor.current.backgroundColor.copy(0.6f),
                        LocalBackgroundColor.current.backgroundColor.copy(0.4f)
                    )
                )
            )
            .padding(horizontal = 8.dp, vertical = 12.dp)
    )
    {
        Text(
            modifier = Modifier.weight(1f),
            text = savedCity.name,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            modifier = Modifier.weight(0.2f),
            text = "${savedCity.temperature}°",
            fontWeight = FontWeight.SemiBold
        )
        if (inSelectionMode) {
            if (selected) {
                Icon(Icons.Filled.CheckCircle, null, tint = Color.White)
            } else {
                Icon(Icons.Filled.RadioButtonUnchecked, null, tint = Color.White)
            }
        }

    }
}

