package `in`.gopalpoddar.kubuddy.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import `in`.gopalpoddar.kubuddy.data.auth.AuthRepository
import `in`.gopalpoddar.kubuddy.data.local.LocalDataSource
import `in`.gopalpoddar.kubuddy.data.local.dataStore
import `in`.gopalpoddar.kubuddy.data.remote.RemoteDataSource
import `in`.gopalpoddar.kubuddy.data.user.UserRepository
import `in`.gopalpoddar.kubuddy.db.AppDatabase
import `in`.gopalpoddar.kubuddy.screen.auth.login.LoginScreen
import `in`.gopalpoddar.kubuddy.screen.auth.signup.SignupScreen
import `in`.gopalpoddar.kubuddy.screen.auth.signup.SignupViewModel
import `in`.gopalpoddar.kubuddy.screen.auth.signup.SignupViewModelFactory
import `in`.gopalpoddar.kubuddy.screen.home.HomeScreen
import `in`.gopalpoddar.kubuddy.screen.home.HomeViewModel
import `in`.gopalpoddar.kubuddy.screen.home.HomeViewModelFactory
import `in`.gopalpoddar.kubuddy.screen.list.ListRepository
import `in`.gopalpoddar.kubuddy.screen.list.ListScreen
import `in`.gopalpoddar.kubuddy.screen.list.ListViewModel
import `in`.gopalpoddar.kubuddy.screen.list.ListViewModelFactory
import `in`.gopalpoddar.kubuddy.screen.pdf.PDFScreen
import `in`.gopalpoddar.kubuddy.screen.settings.SettingScreen
import `in`.gopalpoddar.kubuddy.screen.settings.SettingViewModel
import `in`.gopalpoddar.kubuddy.screen.settings.SettingViewModelFactory
import `in`.gopalpoddar.kubuddy.screen.splash.SplashScreen
import java.net.URLDecoder


@Composable
fun App(){

    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseDatabase.getInstance()
    val context = LocalContext.current
    val local = LocalDataSource(context.dataStore)
    val remote = RemoteDataSource(auth,db)


    val appDB = remember {
        AppDatabase.getDatabase(context)
    }
    val dao = remember {
        appDB.studyMaterialDao()
    }

    NavHost(navController = navController, startDestination = Routes.SPLASH){

        composable(route = Routes.SPLASH){
            SplashScreen(
                goToHome = {
                    navController.navigate(Routes.HOME){
                        popUpTo(Routes.SPLASH){inclusive = true}
                    }
                },
                goToLogin = {
                    navController.navigate(Routes.LOGIN){
                        popUpTo(Routes.SPLASH){inclusive = true}
                    }
                }
            )
        }
        composable(route = Routes.LOGIN){
            LoginScreen({
                navController.navigate(Routes.SIGNUP)
            },{
                navController.navigate(Routes.HOME){
                    popUpTo(Routes.LOGIN){inclusive = true}
                }
            })
        }
        composable(route= Routes.SIGNUP) {
            val userRepository = UserRepository(local,remote,dao)
            val authRepository = AuthRepository()
            val viewModel: SignupViewModel=viewModel(
                factory = SignupViewModelFactory(authRepository,userRepository)
            )
            SignupScreen({
                navController.navigate(Routes.HOME){
                    popUpTo(Routes.SIGNUP){inclusive = true}
                }
            },{
                navController.navigate(Routes.LOGIN)
            },{

            },
                viewModel
                )
        }
        composable(route = Routes.HOME) {
            val userRepository = UserRepository(local,remote,dao)
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(userRepository)
            )
            HomeScreen(
                 {
                    materialType, semester ->
                    navController.navigate("list/$materialType/$semester")
                },
                viewModel,{
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        composable (route = Routes.LIST_WITH_ARGS, arguments = listOf(
            navArgument("materialType"){type = NavType.StringType},
            navArgument("semester"){type = NavType.StringType}
        )){
            val materialType = it.arguments?.getString("materialType")
            val semester = it.arguments?.getString("semester")

            val listRepository = ListRepository(remote,materialType!!,"sem$semester",dao,local)

            val viewModel: ListViewModel = viewModel(
                factory = ListViewModelFactory(listRepository)
            )
            ListScreen(
                materialType,
                semester,
                viewModel,{pdfkey, pdfName->
                    navController.navigate("pdf/{$pdfkey}/{$pdfName}")
                },{
                    navController.navigate(Routes.HOME){
                        popUpTo(Routes.HOME){inclusive = true}
                    }
                }
            )
        }

        composable(route = Routes.PDF_WITH_ARGS, arguments = listOf(
            navArgument("pdfkey"){type = NavType.StringType},
            navArgument("pdfName"){type = NavType.StringType}
        )){
            val pdfkey = it.arguments?.getString("pdfkey")
            val pdfName = it.arguments?.getString("pdfName")

            val url = URLDecoder.decode(
                it.arguments?.getString("pdfkey")?:"","UTF-8"
            )
            PDFScreen(url,pdfName?:"")
        }

        composable (route = Routes.SETTINGS){
            val userRepository = UserRepository(local,remote,dao)
            val authRepository = AuthRepository()

            val viewModel: SettingViewModel = viewModel(
                factory = SettingViewModelFactory(userRepository,authRepository)
            )
            SettingScreen(
                viewModel,{
                    navController.navigate(Routes.HOME){
                        popUpTo(Routes.HOME){inclusive = true}
                    }
                },{
                    navController.navigate(Routes.LOGIN){
                        popUpTo(Routes.HOME){inclusive=true}
                    }
                }
            )
        }


    }

}