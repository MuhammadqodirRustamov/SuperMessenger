package uni.dev.supermessenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import uni.dev.supermessenger.navigation.NavGraph
import uni.dev.supermessenger.ui.theme.Primary
import uni.dev.supermessenger.ui.theme.SuperTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Primary
                ) {
                    NavGraph()
                }
            }
        }
    }
}


