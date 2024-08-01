package com.example.pokemonapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pokemonapp.data.Pokemon
import com.example.pokemonapp.databinding.ActivityMainBinding
import com.example.pokemonapp.remote.RetrofitInstance
import com.example.pokemonapp.utils.NetworkUtil
import com.example.pokemonapp.utils.PokemonFetchCallback
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var pokemonList: MutableList<Pokemon> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }

    override fun onStart() {
        super.onStart()
        if (NetworkUtil.isInternetAvailable(this)) {
            fetchPokemonData()
        } else {
            Toast.makeText(this, "Connect to Internet", Toast.LENGTH_SHORT).show()
            binding.data.text = "No Internet Connection!"
        }
    }

    private val pokeFetchInterface = object: PokemonFetchCallback {
        override fun onPokemonFetched(pokemon: Pokemon) {
            pokemonList.add(pokemon)
        }

        override fun onError(error: String) {
            println(error)
        }

    }

    private fun fetchPokemonData() {
        var res = ""
        try {
            lifecycleScope.launch {
                val response =  RetrofitInstance.api.getPokemonList(0, 20)
                response.results.map {
                    val idParts = it.url.split("/")
                    val id = idParts.lastOrNull { it.isNotEmpty() }?.toIntOrNull()
                    Log.d("***", "*** id : $id")
                    fetchPokemon(id!!)
                }
                binding.data.text = pokemonList.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun fetchPokemon(id: Int) {
        lifecycleScope.launch {
            RetrofitInstance.api.getPokemon(id!!).enqueue(object: retrofit2.Callback<Pokemon> {
                override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res != null) {
                            Log.d("***", "*** : ${res.toString()}")
                            pokeFetchInterface.onPokemonFetched(
                                Pokemon(id = res.id, name = res.name,
                                    weight = res.weight, height = res.height)
                            )
                        } else {
                            pokeFetchInterface.onError("No able to fetch pokemon")
                        }
                    }
                }

                override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                    println("${t.message}")
                }

            })
        }
    }
}