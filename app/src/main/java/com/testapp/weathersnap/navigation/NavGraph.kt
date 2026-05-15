package com.testapp.weathersnap.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.testapp.weathersnap.screens.CameraScreen
import com.testapp.weathersnap.screens.CreateReportScreen
import com.testapp.weathersnap.screens.WeatherScreen
import com.testapp.weathersnap.weather.getWeatherDescription

@Composable
fun WeatherNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Routes.WEATHER,
        modifier = Modifier.fillMaxSize()
    ) {


        composable(
            route = Routes.WEATHER,
            exitTransition = {

                slideOutHorizontally(animationSpec = tween(350)) { -it }
            },
            popEnterTransition = {

                slideInHorizontally(animationSpec = tween(350)) { -it }
            }
        ) {

            WeatherScreen(
                contentPadding = innerPadding,
                onNavigateToCreateReport = { city, weather ->
                    val conditionDesc = getWeatherDescription(weather.weatherCode?: 0)
                    navController.navigate(
                        "${Routes.CREATE_REPORT}/${city.name}/${weather.temperature.toFloat()}/${weather.humidity}/${weather.windSpeed.toFloat()}/${weather.pressure.toFloat()}?condition=$conditionDesc"
                    )
                },
                onNavigateToReports = {
                    navController.navigate(Routes.SAVED_REPORTS)
                }
            )
        }


        composable(
            route = "${Routes.CREATE_REPORT}/{cityName}/{temp}/{humidity}/{wind}/{pressure}?condition={condition}",
            arguments = listOf(
                navArgument("cityName") { type = NavType.StringType },
                navArgument("temp") { type = NavType.FloatType },
                navArgument("humidity") { type = NavType.IntType },
                navArgument("wind") { type = NavType.FloatType },
                navArgument("pressure") { type = NavType.FloatType }
            ),
            enterTransition = {

                slideInHorizontally(animationSpec = tween(350)) { it }
            },
            popExitTransition = {

                slideOutHorizontally(animationSpec = tween(350)) { it }
            }
        ) {
            CreateReportScreen(
                contentPadding = innerPadding,
                navController = navController,
                onNavigateToCamera = { navController.navigate(Routes.CAMERA) },
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.navigate(Routes.SAVED_REPORTS) }
            )
        }


        composable(
            route = Routes.CAMERA,
            enterTransition = {
                slideInHorizontally(animationSpec = tween(350)) { it }
            },
            popExitTransition = {
                slideOutHorizontally(animationSpec = tween(350)) { it }
            }
        ) {
            CameraScreen(navController = navController)
        }


        composable(
            route = Routes.SAVED_REPORTS,
            enterTransition = {
                slideInHorizontally(animationSpec = tween(350)) { it }
            },
            popExitTransition = {
                slideOutHorizontally(animationSpec = tween(350)) { it }
            }
        ) {
            com.testapp.weathersnap.screens.SavedReportsScreen(
                contentPadding = innerPadding,
                onBackClick = { navController.popBackStack(Routes.WEATHER, inclusive = false)}
            )
        }
    }
}


