
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.LocalAppBarColor
import com.example.weatherapp.ui.theme.LocalBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTopAppBar(
    text: String,
    actionIcon: ImageVector,
    actionIconContentDescription: String? = null,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = LocalAppBarColor.current.appBarColor),
    onActionClick: () -> Unit = {},
    onNavigationClick:() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(text = text, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold) },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        },
        colors = colors,
        modifier = modifier,
    )
}