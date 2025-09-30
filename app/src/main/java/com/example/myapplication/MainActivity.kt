package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil3.compose.AsyncImage
import com.example.myapplication.api.AnimalService
import com.example.myapplication.model.Animal
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.view.MainIntent
import com.example.myapplication.view.MainState
import com.example.myapplication.view.MainViewModel
import com.example.myapplication.view.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainViewModel =
            ViewModelProvider(this, ViewModelFactory(AnimalService.api))[MainViewModel::class.java]

        val onButtonClick: () -> Unit = {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.FetchAnimals)
            }
        }


        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        MainScreen(vm = mainViewModel, onButtonClick = onButtonClick)
                    }
                }
            }
        }
    }

    @Composable
    fun MainScreen(vm: MainViewModel, onButtonClick: () -> Unit) {
        val state = vm.state.value
        when (state) {
            is MainState.Idle -> IdleScreen(onButtonClick)
            is MainState.Loading -> LoadingScreen()
            is MainState.Animals -> AnimalsListScreen(state.animals)
            is MainState.Error -> {
                IdleScreen(onButtonClick)
                Toast.makeText(LocalContext.current, state.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun IdleScreen(onButtonClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TextButton(onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue
            )) {
            Text(text = "Fetch Animals",
                color = Color.White)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun AnimalsListScreen(animals: List<Animal>) {
    LazyColumn {
        items(animals) {
            AnimalItem(animal = it)
            Divider(
                color = Color.LightGray,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
    }
}

@Composable
fun AnimalItem(animal: Animal) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp)
    ) {
        val url = AnimalService.BASE_URL + animal.image
        AsyncImage(
            model = url,
            contentDescription = animal.name,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.FillHeight
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(text = animal.name,
                fontWeight = FontWeight.Bold)
            Text(text = animal.location)
        }
    }
}