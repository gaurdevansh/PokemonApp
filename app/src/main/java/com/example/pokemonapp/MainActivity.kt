package com.example.pokemonapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pokemonapp.data.PokemonBaseResponse
import com.example.pokemonapp.databinding.ActivityMainBinding
import com.example.pokemonapp.remote.RetrofitInstance
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //private lateinit var pokemonList: List<PokemonBaseResponse>
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        fetchPokemonData()
    }

    private fun fetchPokemonData() {
        try {
            lifecycleScope.launch {
                val response =  RetrofitInstance.api.getPokemonList(0, 20)
                binding.data.text = response.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}