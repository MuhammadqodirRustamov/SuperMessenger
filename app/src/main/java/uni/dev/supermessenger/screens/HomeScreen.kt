package uni.dev.supermessenger.screens

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import uni.dev.supermessenger.R
import uni.dev.supermessenger.model.Message
import uni.dev.supermessenger.model.User
import uni.dev.supermessenger.ui.theme.Blue
import uni.dev.supermessenger.ui.theme.Primary
import uni.dev.supermessenger.ui.theme.Secondary
import uni.dev.supermessenger.ui.theme.Text
import uni.dev.supermessenger.ui.theme.Text2
import uni.dev.supermessenger.util.Alert
import uni.dev.supermessenger.util.Helper
import uni.dev.supermessenger.util.SharedHelper
import java.text.SimpleDateFormat
import java.util.Date

@Preview
@Composable
fun PreviewHome() {
    HomeScreen(navController = rememberNavController())
}

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val currentUserKey = SharedHelper.getInstance(context).getKey()!!
    val contacts = remember { mutableStateListOf<User>() }
    val lastMessages = remember { mutableStateListOf<Message>() }
    val explore = remember { mutableStateOf(false) }
    val dataFetched = remember { mutableStateOf(false) }


    Helper.getChats(currentUserKey) { c, lm ->
        contacts.clear()
        contacts.addAll(c)
        lastMessages.clear()
        lastMessages.addAll(lm)
        explore.value = c.isEmpty()
        dataFetched.value = true
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Primary)
            .padding(8.dp)
    ) {
        HomeTopBar(navController)
        Box(Modifier.fillMaxSize()) {
            ChatsColumn(navController, Secondary, contacts, lastMessages, "", true)
            if (explore.value) ExploreButton(navController = navController)
        }
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
                .height(40.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun ChatsColumn(
    navController: NavController,
    backColor: Color,
    contacts: SnapshotStateList<User>,
    lastMessages: SnapshotStateList<Message>?,
    popUpTo: String,
    deleteFunction: Boolean
) {
    val context = LocalContext.current
    val deleteDialogOpen = remember {
        mutableStateOf(false)
    }
    val deleteIndex = remember {
        mutableStateOf(-1)
    }
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            backColor
        ), modifier = Modifier.fillMaxSize()
    ) {

        if (contacts.isNotEmpty()) LazyColumn(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .padding(4.dp, 0.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(contacts.size) { index ->
                contacts[index]
                var lastMessageText: String? = null
                var lastMessageDate = ""
                if (lastMessages != null) {
                    val lastMessage = lastMessages[index]
                    lastMessageText = lastMessage.text!!


                    var lastDate = lastMessage.date!!
                    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                    val currentDate = sdf.format(Date())
                    val d = Date().date - 1
                    val y = if (d < 10) "0$d" else d.toString()
                    val yesterday = y + currentDate.substring(2, currentDate.length)

                    lastDate = if (currentDate.substring(
                            0,
                            currentDate.length - 8
                        ) == lastMessage.date.substring(
                            0,
                            lastMessage.date.length - 8
                        )
                    ) lastDate.substring(lastDate.length - 8, lastDate.length - 3)
                    else if (yesterday.substring(
                            0,
                            currentDate.length - 8
                        ) == lastMessage.date.substring(
                            0,
                            lastMessage.date.length - 8
                        )
                    ) "Yesterday"
                    else {
                        lastMessage.date.substring(0, 10)
                    }
                    lastMessageDate = lastDate

                }
                val user = contacts[index]
//                val unreadCount = 1
                ChatItem(
                    navController,
                    user,
                    lastMessageText,
                    lastMessageDate,
                    popUpTo,
                    deleteFunction,
                    deleteDialogOpen,
                    deleteIndex,
                    index
                )
            }
        }


    }
    if (deleteFunction && deleteDialogOpen.value) Alert(
        isDialogOpen = deleteDialogOpen,
        text = "Dou you want to delete this chat?",
        confirmButtonColor = Red
    ) {
        if (deleteIndex.value > -1) {
            val key1 = SharedHelper.getInstance(context).getKey()
            val key2 = contacts[deleteIndex.value].key!!
            Helper.deleteChat(key1, key2)
            deleteIndex.value = -1
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatItem(
    navController: NavController,
    user: User,
    lastMessageText: String?,
    lastMessageDate: String,
    popUpTo: String,
    deleteFunction: Boolean,
    deleteDialogOpen: MutableState<Boolean>,
    deleteIndex: MutableState<Int>,
    index: Int
) {
    val secondText = lastMessageText ?: user.username!!
    Card(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = {
                    if (deleteFunction) {
                        deleteIndex.value = index
                        deleteDialogOpen.value = true
                    }
                },
                onClick = {
                    navController.navigate("chat/${user.key}") {
                        if (popUpTo != "") {
                            popUpTo(popUpTo)
                        }
                    }
                }
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Row(Modifier.padding(8.dp, 8.dp)) {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "",
                modifier = Modifier.clip(
                    CircleShape
                )
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                var name = user.firstName!!
                if (user.lastName!!.isNotEmpty()) name += " " + user.lastName!!

                Text(
                    text = name,
                    color = Text,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
                Text(
                    text = secondText,
                    color = Text2,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
            }
            Column(
                Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = lastMessageDate, color = Text2, fontWeight = FontWeight.Light)
//                            if (unreadCount > 0)
//                                Text(
//                                    text = unreadCount.toString(),
//                                    color = Color.White,
//                                    modifier = Modifier
//                                        .size(20.dp)
//                                        .background(color = Blue, shape = RoundedCornerShape(50)),
//                                    textAlign = TextAlign.Center,
//                                    fontWeight = FontWeight.Bold
//                                )

            }
        }
    }
}


@Composable
fun ExploreButton(navController: NavController) {
    val focus = false
    Box(modifier = Modifier.fillMaxSize()) {
        TextButton(
            onClick = { navController.navigate("search/$focus") },
            modifier = Modifier.align(Alignment.Center),
            colors = ButtonDefaults.buttonColors(
                Blue
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text("Explore people", Modifier.padding(horizontal = 8.dp))
        }
    }
}

@Composable
fun HomeTopBar(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = "",
            Modifier
                .clip(
                    CircleShape
                )
                .clickable { navController.navigate("profile") }
        )
        val focus = true
        Image(painter = painterResource(id = R.drawable.super_name), contentDescription = "")
        Box(modifier = Modifier.padding(2.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search button",
                tint = Text2,
                modifier = Modifier
                    .border(0.5.dp, Text2, RoundedCornerShape(50))
                    .clickable { navController.navigate("search/$focus") }
                    .padding(6.dp)
                    .clip(CircleShape),
            )
        }
    }
}
