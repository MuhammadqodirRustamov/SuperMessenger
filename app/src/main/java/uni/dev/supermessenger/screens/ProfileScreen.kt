package uni.dev.supermessenger.screens

import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import uni.dev.supermessenger.R
import uni.dev.supermessenger.model.User
import uni.dev.supermessenger.ui.theme.Black20
import uni.dev.supermessenger.ui.theme.Green
import uni.dev.supermessenger.ui.theme.Primary
import uni.dev.supermessenger.ui.theme.Red
import uni.dev.supermessenger.ui.theme.Secondary
import uni.dev.supermessenger.ui.theme.Text
import uni.dev.supermessenger.ui.theme.Text2
import uni.dev.supermessenger.util.Alert
import uni.dev.supermessenger.util.Helper
import uni.dev.supermessenger.util.SharedHelper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val isLogoutDialogOpen = remember {
        mutableStateOf(false)
    }
    val isUpdateDialogOpen = remember {
        mutableStateOf(false)
    }
    val dataFetched = remember {
        mutableStateOf(false)
    }
    val user = remember {
        mutableStateOf(User("", "", "", "", "", ""))
    }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val context = LocalContext.current
    val username = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val retypePassword = remember {
        mutableStateOf("")
    }
    val firstName = remember {
        mutableStateOf("")
    }
    val lastName = remember {
        mutableStateOf("")
    }
    val curUserKey = SharedHelper.getInstance(context).getKey()

    Helper.getUser(curUserKey) {
        if (!dataFetched.value) {
            user.value = it
            username.value = it.username!!
            firstName.value = it.firstName!!
            lastName.value = it.lastName!!
            dataFetched.value = true
        }
    }

    val passwordVisibility = remember { mutableStateOf(false) }
    val errorText1 = "Username can not be empty"
    val errorText2 = "Username can contain only letters and numbers"
    val errorText3 = "Username is already taken"


    val firstNameError = remember {
        mutableStateOf(false)
    }
    val usernameError = remember {
        mutableStateOf(false)
    }
    val usernameErrorText = remember {
        mutableStateOf(errorText1)
    }
    val passwordError = remember {
        mutableStateOf(false)
    }
    val retypePasswordError = remember {
        mutableStateOf(false)
    }

    Column(
        Modifier
            .background(Primary)
            .fillMaxSize()
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Super app logo",
                Modifier.height(100.dp)
            )
            Spacer(modifier = Modifier.height(42.dp))

            Box {
                Column (horizontalAlignment = Alignment.CenterHorizontally) {

                    TextField(value = firstName.value,
                        enabled = dataFetched.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        onValueChange = {
                            firstName.value = it.trim()
                            firstNameError.value = firstName.value.isEmpty()
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = { Text(text = "Firstname", fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = "",
                                tint = Black20
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Text2,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Text2,
                            containerColor = Secondary
                        ),
                        textStyle = TextStyle(fontSize = 16.sp),
                        isError = firstNameError.value,
                        supportingText = {
                            if (firstNameError.value) Text(
                                text = "Firstname can not be empty",
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.error
                            )
                        })
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        enabled = dataFetched.value,
                        value = lastName.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        onValueChange = {
                            lastName.value = it.trim()
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = { Text(text = "LastName (Optional)", fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = "",
                                tint = Black20
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Text2,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Text2,
                            containerColor = Secondary
                        ),
                        textStyle = TextStyle(fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(value = username.value,
                        enabled = dataFetched.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        onValueChange = { it ->
                            username.value = it.trim()
                            if (username.value.isEmpty()) {
                                usernameError.value = true
                                usernameErrorText.value = errorText1
                            } else if (!username.value.all { it.isLetterOrDigit() }) {
                                usernameError.value = true
                                usernameErrorText.value = errorText2
                            } else {
                                usernameError.value = false
                                Helper.checkUsername(username.value) { usernameExists ->
                                    if (usernameExists && user.value.username != username.value) {
                                        usernameError.value = true
                                        usernameErrorText.value = errorText3
                                    }
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = {
                            Text(text = "Username", fontSize = 14.sp)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.AccountCircle,
                                contentDescription = "",
                                tint = Black20
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Text2,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Text2,
                            containerColor = Secondary
                        ),
                        textStyle = TextStyle(fontSize = 16.sp),
                        isError = usernameError.value,
                        supportingText = {
                            if (usernameError.value) Text(
                                text = usernameErrorText.value,
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.error
                            )
                        })
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(value = password.value,
                        enabled = dataFetched.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        onValueChange = {
                            password.value = it.trim()
                            val invalidPassword = password.value.length in 1..7
                            passwordError.value = invalidPassword
                            retypePasswordError.value = !invalidPassword
                            if (password.value.isEmpty()) {
                                retypePassword.value = ""
                                retypePasswordError.value = false
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = {
                            Text(text = "New password", fontSize = 14.sp)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Lock,
                                contentDescription = "",
                                tint = Black20
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Text2,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Text2,
                            containerColor = Secondary
                        ),
                        textStyle = TextStyle(fontSize = 16.sp),
                        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image =
                                if (passwordVisibility.value) R.drawable.password_toggle_hide
                                else R.drawable.password_toggle

                            val description =
                                if (passwordVisibility.value) "Hide password" else "Show password"

                            IconButton(onClick = {
                                passwordVisibility.value = !passwordVisibility.value
                            }) {
                                Icon(
                                    painter = painterResource(id = image),
                                    description,
                                    tint = Black20
                                )
                            }
                        },
                        isError = passwordError.value,
                        supportingText = {
                            if (passwordError.value) Text(
                                text = "Password must have at least 8 characters",
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.error
                            )
                        })
                    Spacer(modifier = Modifier.height(16.dp))
                    if (dataFetched.value && password.value.isNotEmpty()) TextField(value = retypePassword.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        onValueChange = {
                            retypePassword.value = it.trim()
                            retypePasswordError.value =
                                password.value.length > 7 && retypePassword.value != password.value
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = { Text(text = "Retype password", fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Lock,
                                contentDescription = "",
                                tint = Black20
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Text2,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Text2,
                            containerColor = Secondary
                        ),
                        textStyle = TextStyle(fontSize = 16.sp),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = retypePasswordError.value,
                        supportingText = {
                            if (retypePasswordError.value) Text(
                                text = "Passwords must match",
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.error
                            )
                        })

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
                        .fillMaxSize().align(Alignment.Center)
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

            Spacer(modifier = Modifier.height(42.dp))
            Log.d("TAG", user.value.username.toString() + " " + username.value)
            ElevatedButton(
                enabled = dataFetched.value && (user.value.username!! != username.value || user.value.firstName!! != firstName.value || user.value.lastName!! != lastName.value || (password.value.isNotEmpty() && user.value.password!! != password.value)) && !firstNameError.value && !usernameError.value && !passwordError.value && !retypePasswordError.value,
                onClick = {
                    focusManager.clearFocus(true)
                    isUpdateDialogOpen.value = true
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green, disabledContainerColor = Secondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 62.dp)
            ) {
                Text(
                    text = "Save",
                    color = Text,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

            }
            Spacer(modifier = Modifier.height(8.dp))
            ElevatedButton(
                onClick = {
                    isLogoutDialogOpen.value = true
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 62.dp)
            ) {
                Text(
                    text = "Log out",
                    color = Text,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

            }
        }


    }

    if (isLogoutDialogOpen.value) Alert(
        isDialogOpen = isLogoutDialogOpen,
        text = "Do you really want to log out?",
        confirmButtonColor = Red
    ) {
        SharedHelper.getInstance(context).logOut()
        navController.navigate("login") {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }


    }
    if (isUpdateDialogOpen.value) Alert(
        isDialogOpen = isUpdateDialogOpen,
        text = "Do you confirm changes?",
        confirmButtonColor = Green
    ) {
        val key = SharedHelper.getInstance(context).getKey()
        user.value.username = username.value
        user.value.lastName = lastName.value
        user.value.firstName = firstName.value
        if (password.value.isNotEmpty()) user.value.password = password.value
        Helper.updateUser(key, user.value, context)
        password.value = ""
    }


}

