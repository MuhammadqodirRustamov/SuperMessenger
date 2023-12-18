package uni.dev.supermessenger.screens

import android.content.Context
import android.os.Build
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import uni.dev.supermessenger.R
import uni.dev.supermessenger.model.User
import uni.dev.supermessenger.ui.theme.Primary
import uni.dev.supermessenger.ui.theme.Secondary
import uni.dev.supermessenger.ui.theme.Text
import uni.dev.supermessenger.ui.theme.Text2
import uni.dev.supermessenger.ui.theme.Text3
import uni.dev.supermessenger.util.Helper


@Composable
fun SearchScreen(navController: NavController, focus: Boolean) {
    val keyWord = remember { mutableStateOf("") }
    val dataFetched = remember { mutableStateOf(false) }
    val contacts = remember { mutableStateListOf<User>() }
    val context = LocalContext.current

    searchUpdate(keyWord, contacts, context, dataFetched)

    Column(
        Modifier
            .fillMaxSize()
            .background(Primary)
    ) {
        SearchField(focus, navController, keyWord, contacts, context, dataFetched)
        ChatsColumn(
            navController = navController,
            backColor = Color.Transparent,
            contacts = contacts,
            lastMessages = null,
            popUpTo = "home", false
        )
    }

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    if (!dataFetched.value) Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = rememberAsyncImagePainter(R.drawable.progress, imageLoader),
            contentDescription = null,
            modifier = Modifier
                .height(40.dp).align(Alignment.Center)
        )
    }
}

fun searchUpdate(
    keyWord: MutableState<String>,
    contacts: SnapshotStateList<User>,
    context: Context,
    dataFetched: MutableState<Boolean>,

    ) {
    Helper.search(keyWord.value, context) { users ->
        contacts.clear()
        contacts.addAll(users)
        dataFetched.value = true
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    focus: Boolean,
    navController: NavController,
    keyWord: MutableState<String>,
    contacts: SnapshotStateList<User>,
    context: Context,
    dataFetched: MutableState<Boolean>
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
//    if (focus) focusRequester.requestFocus()
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    OutlinedTextField(
        value = keyWord.value,
        onValueChange = {
            keyWord.value = it
            searchUpdate(keyWord, contacts, context, dataFetched)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(24.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            Text2,
            containerColor = Secondary,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        trailingIcon = {
            Icon(
                painterResource(id = R.drawable.search),
                contentDescription = "",
                tint = Text
            )
        },
        placeholder = {
            Text(
                text = "Search", color = Text3
            )
        },
        singleLine = true,
        maxLines = 1,
        leadingIcon = {
            IconButton(onClick = {
                onBackPressedDispatcher?.onBackPressed()
            }) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = "",
                    tint = Text,
                )
            }
        }

    )
}
