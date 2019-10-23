package com.kdotz.theinfurmant

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.kdotz.theinfurmant.model.DogImageResponse
import com.kdotz.theinfurmant.web.GetData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var myCompositeDisposable: CompositeDisposable? = null
    private var myDogBreedList: ArrayList<DogImageResponse>? = ArrayList()
    private val BASE_URL = "https://api.thedogapi.com/v1/"
    private lateinit var imageView: ImageView
    private lateinit var breed: TextView
    private lateinit var temperament: TextView
    private lateinit var height: TextView
    private lateinit var weight: TextView
    private lateinit var lifeSpan: TextView
    private lateinit var bredFor: TextView
    private lateinit var onNext: Button
    private lateinit var onPrev: Button
    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myCompositeDisposable = CompositeDisposable()
        loadData()

        imageView = findViewById(R.id.imageView)
        breed = findViewById(R.id.breed)
        temperament = findViewById(R.id.temperament)
        height = findViewById(R.id.height)
        weight = findViewById(R.id.weight)
        lifeSpan = findViewById(R.id.lifeSpan)
        bredFor = findViewById(R.id.bredFor)
        onNext = findViewById(R.id.next)
        onPrev = findViewById(R.id.back)
    }

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private fun loadData() {
        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(GetData::class.java)

        println("REQUEST: " + requestInterface.getImage("100"))

        var int = 0
        do {
            myCompositeDisposable?.add(
                requestInterface.getImage(int.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse)
            )
            int++
        } while (int <= 3)
    }

    @SuppressLint("SetTextI18n")
    private fun handleResponse(allDogBreedList: List<DogImageResponse>) {
        myDogBreedList?.addAll(ArrayList(allDogBreedList.filter { it.breeds.isNotEmpty() }.distinctBy { it.breeds[0].name }))
        println(myDogBreedList)
        showBreed()
    }

    private fun showBreed() {
        Glide.with(this).load(myDogBreedList?.get(count)?.url).into(imageView)
        breed.text = myDogBreedList?.get(count)?.breeds?.get(0)?.name
        temperament.text = myDogBreedList?.get(count)?.breeds?.get(0)?.temperament
        height.text = myDogBreedList?.get(count)?.breeds?.get(0)?.height?.imperial.plus(" in.")
        weight.text = myDogBreedList?.get(count)?.breeds?.get(0)?.weight?.imperial.plus(" lbs.")
        lifeSpan.text = myDogBreedList?.get(count)?.breeds?.get(0)?.life_span
        bredFor.text = myDogBreedList?.get(count)?.breeds?.get(0)?.bred_for
    }

    fun nextBreed(view: View) {
        if (count != myDogBreedList?.size?.minus(1)) {
            count++
            showBreed()
        } else {
            Toast.makeText(this, "No more breeds to display.", Toast.LENGTH_LONG).show()
        }
    }

    fun prevBreed(view: View) {
        if (count != 0) {
            count--
            showBreed()
        } else {
            Toast.makeText(this, "You've reached the start of the breeds list", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myCompositeDisposable?.clear()
    }
}
