package uni.dev.supermessenger.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object Chat : Screen("chat" + "/{key}")
    object Search : Screen("search" + "/{focused}")
    object Profile : Screen("profile")
}