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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.data.datastore.SavedCity
import com.example.weatherapp.ui.composable.SearchAutoComplete


@Composable
fun SearchCityScreen(
    viewModel: SearchCityViewModel = hiltViewModel(),
    navigateToPagerScreen: (Int) -> Unit
) {
    val autoCompletionResult = viewModel.autoCompletionResult
    Column(modifier = Modifier.padding(16.dp)) {
        Text(modifier = Modifier.padding(vertical = 16.dp), text = "Gérer les villes", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        SearchAutoComplete(cities = autoCompletionResult) { city ->
            viewModel.addCityToUserFavoriteCities(city)
        }
        Spacer(Modifier.height(16.dp))
        if(viewModel.savedCities.isNotEmpty()) {
            UserCitiesList(
                savedCities = viewModel.savedCities,
                onItemSelected = { index -> navigateToPagerScreen(index) },
                onLongClick = { savedCity -> viewModel.deleteCityFromUserFavoriteCities(savedCity = savedCity) })
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserCitiesList(savedCities: List<SavedCity>, onItemSelected:(Int) -> Unit, onLongClick:(SavedCity) ->Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        itemsIndexed(items = savedCities) { index, savedCity ->
            Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(color = Color.Blue.copy(alpha = 0.4f)).padding(horizontal = 12.dp, vertical = 16.dp).combinedClickable(onLongClick = { onLongClick(savedCity) } ) { onItemSelected(index) }, verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(1f), text = savedCity.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(modifier = Modifier.weight(0.15f), text = "${savedCity.temperature}°", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

