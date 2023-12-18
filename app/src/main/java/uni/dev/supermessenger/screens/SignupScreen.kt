package uni.dev.supermessenger.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uni.dev.supermessenger.R
import uni.dev.supermessenger.model.User
import uni.dev.supermessenger.navigation.Screen
import uni.dev.supermessenger.ui.theme.Black20
import uni.dev.supermessenger.ui.theme.Green
import uni.dev.supermessenger.ui.theme.Primary
import uni.dev.supermessenger.ui.theme.Secondary
import uni.dev.supermessenger.ui.theme.Tertiary
import uni.dev.supermessenger.ui.theme.Text
import uni.dev.supermessenger.ui.theme.Text2
import uni.dev.supermessenger.util.Helper

@Preview
@Composable
fun SignupPrev() {
    SignupScreen(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    val context = LocalContext.current
    val passwordVisibility = remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val errorText1 = "Username can not be empty"
    val errorText2 = "Username can contain only letters and numbers"
    val errorText3 = "Username is already taken"

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
        modifier = Modifier
            .fillMaxSize()
            .background(color = Primary)
            .padding(horizontal = 36.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(42.dp))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Super app logo",
            Modifier.height(100.dp)
        )
        Spacer(modifier = Modifier.height(42.dp))
        TextField(value = firstName.value,
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
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
                    imageVector = Icons.Rounded.Person, contentDescription = "", tint = Black20
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
            value = lastName.value,
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            onValueChange = { lastName.value = it.trim() },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text(text = "LastName (Optional)", fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Person, contentDescription = "", tint = Black20
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
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
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
                        if (usernameExists) {
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
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            onValueChange = {
                password.value = it.trim()
                val invalidPassword = password.value.length < 8
                passwordError.value = invalidPassword
                if (!invalidPassword){
                    retypePasswordError.value = retypePassword.value != password.value
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(text = "Password", fontSize = 14.sp)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Lock, contentDescription = "", tint = Black20
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
                val image = if (passwordVisibility.value) R.drawable.password_toggle_hide
                else R.drawable.password_toggle

                val description = if (passwordVisibility.value) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(painter = painterResource(id = image), description, tint = Black20)
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
        TextField(value = retypePassword.value,
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
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
                    imageVector = Icons.Rounded.Lock, contentDescription = "", tint = Black20
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
        Spacer(modifier = Modifier.height(42.dp))

        Button(
            enabled = firstName.value.isNotEmpty() && retypePassword.value.isNotEmpty() && password.value.isNotEmpty() && username.value.isNotEmpty() && !firstNameError.value && !usernameError.value && !passwordError.value && !retypePasswordError.value,
            onClick = {
                focusManager.clearFocus(true)
                val user = User(username.value, password.value, firstName.value, lastName.value, "","")
                Helper.register(user, context) { success ->
                    if (success) {
                        Toast.makeText(context, "Successfully signed up", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                }

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
                text = "Sign up",
                color = Text,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { navController.popBackStack() },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Tertiary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 62.dp)
        ) {
            Text(
                text = "Log in",
                color = Text2,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

        }


    }
}