package uni.dev.supermessenger.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uni.dev.supermessenger.screens.ChatScreen
import uni.dev.supermessenger.screens.HomeScreen
import uni.dev.supermessenger.screens.LoginScreen
import uni.dev.supermessenger.screens.ProfileScreen
import uni.dev.supermessenger.screens.SearchScreen
import uni.dev.supermessenger.screens.SignupScreen
import uni.dev.supermessenger.screens.SplashScreen

@Composable
fun NavGraph() {
    val timeNorm = 600
    val timeFast = 300

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(route = Screen.Splash.route,
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(timeNorm)
                )
            }) {
            SplashScreen(navController = navController)

        }
        composable(
            route = Screen.Login.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(timeNorm)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(timeNorm)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(timeNorm)
                )
            },
        ) {
            LoginScreen(navController = navController)
        }
        composable(
            route = Screen.Signup.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(timeNorm)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(timeNorm)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(timeNorm)
                )
            }
        ) {
            SignupScreen(navController = navController)

        }
        composable(
            route = Screen.Home.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(timeNorm)
                )
            }, popEnterTransition = { null }
        ) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Chat.route, arguments = listOf(
            navArgument("key") { type = NavType.StringType }
        ),
            enterTransition = {
                scaleIn(
                    initialScale = 1.2f,
                    animationSpec = tween(300)
                )
//                fadeIn(
//                    tween(timeFast)
//                )
            },
            exitTransition = {
                scaleOut(
                    targetScale = 1.2f,
                    animationSpec = tween(timeFast)
                )
//                fadeOut(tween(timeFast))
            }) { entry ->
            ChatScreen(navController = navController, entry.arguments?.getString("key")!!)
        }
        composable(route = Screen.Search.route, arguments = listOf(
            navArgument("focused") { type = NavType.BoolType }
        ), enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(timeNorm)
            )
        }, exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(timeNorm)
            )
        }) { entry ->
            SearchScreen(navController = navController, entry.arguments?.getBoolean("focused")!!)
        }
        composable(route = Screen.Profile.route, enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(timeNorm)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(timeNorm)
            )
        }) {
            ProfileScreen(navController = navController)
        }
    }
}


