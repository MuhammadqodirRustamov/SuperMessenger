package uni.dev.supermessenger.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uni.dev.supermessenger.R
import uni.dev.supermessenger.model.Message
import uni.dev.supermessenger.model.User
import uni.dev.supermessenger.ui.theme.Back1
import uni.dev.supermessenger.ui.theme.Back2
import uni.dev.supermessenger.ui.theme.Blue
import uni.dev.supermessenger.ui.theme.Primary
import uni.dev.supermessenger.ui.theme.Secondary
import uni.dev.supermessenger.ui.theme.Text
import uni.dev.supermessenger.ui.theme.Text2
import uni.dev.supermessenger.ui.theme.Text3
import uni.dev.supermessenger.util.Alert
import uni.dev.supermessenger.util.Helper
import uni.dev.supermessenger.util.SharedHelper

@Preview
@Composable
fun ChatPreview() {
    ChatScreen(rememberNavController(), "admin")
}


@Composable
fun ChatScreen(navController: NavController, key: String) {
    LocalContext.current


    val deleteDialogOpen = remember { mutableStateOf(false) }
    val deleteIndex = remember { mutableStateOf(-1) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val name = remember { mutableStateOf("") }
    val gotUser = remember { mutableStateOf(false) }
    val user = remember { mutableStateOf(User("", "", "", "", "", "")) }

    Helper.getUser(key) {
        user.value = it
        name.value = user.value.firstName + " " + user.value.lastName
        gotUser.value = true
    }
    val messages = remember { mutableStateListOf<Message>() }
    Helper.getMessages(LocalContext.current, key) { m ->
        messages.clear()
        messages.addAll(m)
        coroutineScope.launch {
            delay(300)
            if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
        }
    }
    BackHandler {
        navController.popBackStack("home", inclusive = false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
    ) {


        ChatTopBar(user, name)


        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(messages.size) { index ->
                MessageItem(messages[index], deleteDialogOpen, deleteIndex, index)
            }
        }
        if (gotUser.value) EnterMessage(user.value.key!!)

    }
    if (deleteDialogOpen.value) Alert(
        isDialogOpen = deleteDialogOpen,
        text = "Do you want to delete this message?",
        confirmButtonColor = Red
    ) {
        if (deleteIndex.value > -1) {
            Helper.deleteMessage(messages[deleteIndex.value])
            deleteIndex.value = -1
        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageItem(
    message: Message,
    deleteDialogOpen: MutableState<Boolean>,
    deleteIndex: MutableState<Int>,
    index: Int
) {
    val currentUserKey = SharedHelper.getInstance(LocalContext.current).getKey()
    val fromMe = message.from == currentUserKey
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        if (fromMe) Spacer(modifier = Modifier.width(100.dp))
        Card(
            modifier = Modifier
                .padding(12.dp)
                .combinedClickable(
                    onClick = {

                    },
                    onLongClick = {
                        deleteDialogOpen.value = true
                        deleteIndex.value = index
                    }
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(if (fromMe) Back2 else Back1)
        ) {
            Text(
                text = message.text!!,
                color = Text2,
                modifier = Modifier.padding(12.dp),
                textAlign = if (fromMe) TextAlign.End else TextAlign.Start
            )
        }
        if (!fromMe) Spacer(modifier = Modifier.width(100.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterMessage(
    userKey: String
) {
    val context = LocalContext.current
    val message = remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        value = message.value,
        onValueChange = { message.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(24.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            Text2,
            containerColor = Secondary,
            focusedBorderColor = Blue,
            unfocusedBorderColor = Color.Transparent
        ),
        trailingIcon = {
            IconButton(
                onClick = {
                    Helper.writeMessage(message.value.trim(), context, userKey)
                    message.value = ""
                    focusManager.clearFocus()
                },
                enabled = message.value.isNotEmpty(),
            ) {
                Icon(
                    Icons.Rounded.Send,
                    contentDescription = "",
                    tint = if (message.value.isNotEmpty()) Blue else Primary
                )
            }
        },
        maxLines = 3,
        placeholder = {
            Text(
                text = "Write a message", color = Text3
            )
        }

    )
}


@Composable
fun ChatTopBar(user: MutableState<User>, name: MutableState<String>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = R.drawable.user),
            contentDescription = "",
            Modifier
                .clip(
                    CircleShape
                )
                .clickable { /*TODO*/ })
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                name.value,
                fontSize = 16.sp,
                color = Text,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Text(
                "@${user.value.username}",
                color = Text3,
                fontWeight = FontWeight.Light,
                maxLines = 1
            )
        }
        Box(modifier = Modifier.padding(6.dp)) {
//            Icon(
//                painter = painterResource(id = R.drawable.search),
//                contentDescription = "Search button",
//                tint = Text2,
//                modifier = Modifier
//                    .border(0.5.dp, Text2, RoundedCornerShape(50))
//                    .clickable { /* TODO */ }
//                    .padding(6.dp)
//                    .clip(CircleShape),
//            )
        }
    }
}
