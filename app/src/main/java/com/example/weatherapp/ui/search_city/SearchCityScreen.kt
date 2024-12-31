package com.example.weatherapp.ui.search_city

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.weatherapp.ui.composable.SearchAutoComplete
import com.example.weatherapp.ui.theme.LocalBackgroundColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCityScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchCityViewModel = hiltViewModel(),
    navigateToPagerScreen: (Int) -> Unit
) {
    val autoCompletionResult = viewModel.autoCompletionResult
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = "Gérer les villes",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        SearchAutoComplete(cities = autoCompletionResult) { city ->
            viewModel.addCityToUserFavoriteCities(city)
        }
        Spacer(Modifier.height(16.dp))
        if (viewModel.savedCities.isNotEmpty()) {
            UserCitiesList(
                viewModel = viewModel,
                savedCities = viewModel.savedCities,
                onItemSelected = { index -> navigateToPagerScreen(index) })
          }
   }
    if(viewModel.selectedCities.isNotEmpty()) {
        ModalBottomSheet(modifier = Modifier.height(120.dp), onDismissRequest = {}, shape = RectangleShape, dragHandle = {}) {
            Column(modifier = Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Outlined.Delete, null, Modifier.size(35.dp).clickable { viewModel.deleteCitiesFromUserCities() })
                Text(text = "Supprimer")
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
) {

    val inSelectionMode by remember { derivedStateOf { viewModel.selectedCities.isNotEmpty() } }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        itemsIndexed(items = savedCities) { index, savedCity ->
            val selected by remember { derivedStateOf { savedCity in viewModel.selectedCities } }
            UserCityItem(selected = selected, inSelectionMode = inSelectionMode, savedCity = savedCity,
                modifier = if(inSelectionMode) Modifier.clickable { if(selected) viewModel.removeCity(savedCity) else viewModel.addCity(savedCity)}  else Modifier.combinedClickable(
                onLongClick = { viewModel.addCity(savedCity) },
                onClick = { onItemSelected(index) }
            ))
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
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            modifier = Modifier.weight(0.2f),
            text = "${savedCity.temperature}°",
            color = Color.White,
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

