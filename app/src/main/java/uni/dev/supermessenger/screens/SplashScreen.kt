package uni.dev.supermessenger.screens

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import kotlinx.coroutines.delay
import uni.dev.supermessenger.R
import uni.dev.supermessenger.navigation.Screen
import uni.dev.supermessenger.ui.theme.Primary
import uni.dev.supermessenger.util.SharedHelper

@Preview
@Composable
fun PreviewSplash() {
    SplashScreen(navController = rememberNavController())
}

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        delay(3000)
        if (SharedHelper.getInstance(context).getKey() == "")
            navController.navigate(Screen.Login.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        else {
            navController.navigate(Screen.Home.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    }


    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(R.drawable.logogif, imageLoader),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
    )

}