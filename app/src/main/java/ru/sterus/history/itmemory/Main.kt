package ru.sterus.history.itmemory

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.sterus.history.itmemory.databinding.ActivityMainBinding
import ru.sterus.history.itmemory.fragments.NewsFragment
import ru.sterus.history.itmemory.fragments.QuizFragment
import ru.sterus.history.itmemory.fragments.ServiceFragment
import ru.sterus.history.itmemory.model.MapsMarker
import ru.sterus.history.itmemory.model.Parser

class Main : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var vb : ActivityMainBinding
    private var f1 = true
    private var f2 = false
    private var f3 = false
    private val markers = ArrayList<MapsMarker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
        supportActionBar?.hide()

        val newsFragment = NewsFragment()
        //val quizFragment = QuizFragment()
        val mapFragment = SupportMapFragment.newInstance()
        val serviceFragment = ServiceFragment()
        mapFragment.getMapAsync(this)
        setCurrentFragment(mapFragment)

        vb.mainMapButtonContainer.setOnClickListener {
            mapFragment.getMapAsync(this)
            setCurrentFragment(mapFragment)
            doAllBlack()
            vb.mainMapButton.setColorFilter(resources.getColor(R.color.white))
            if(f3){
                val animation = AnimationUtils.loadAnimation(this, R.anim.anim3_1)
                animation.fillAfter = true
                vb.switcherView.startAnimation(animation)
            }
            if (f2){
                val animation = AnimationUtils.loadAnimation(this, R.anim.anim2_1)
                animation.fillAfter = true
                vb.switcherView.startAnimation(animation)
            }
            buttonClick(1)

        }
        vb.newsPageButtonContainer.setOnClickListener {
            setCurrentFragment(newsFragment)
            doAllBlack()
            vb.newsPageButton.setColorFilter(resources.getColor(R.color.white))
            if(f1){
                val animation = AnimationUtils.loadAnimation(this, R.anim.anim1_2)
                animation.fillAfter = true
                vb.switcherView.startAnimation(animation)
            }
            if(f3){
                val animation = AnimationUtils.loadAnimation(this, R.anim.anim3_2)
                animation.fillAfter = true
                vb.switcherView.startAnimation(animation)
            }

            buttonClick(2)

        }
        vb.personButtonContainer.setOnClickListener {
            setCurrentFragment(serviceFragment)
            doAllBlack()
            vb.personButton.setColorFilter(resources.getColor(R.color.white))

            if(f1) {
                val animation = AnimationUtils.loadAnimation(this, R.anim.anim1_3)
                animation.fillAfter = true
                vb.switcherView.startAnimation(animation)
            }
            if (f2){
                val animation = AnimationUtils.loadAnimation(this, R.anim.anim2_3)
                animation.fillAfter = true
                vb.switcherView.startAnimation(animation)
            }
            buttonClick(3)
        }

    }


    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.f1Fragment, fragment).commit();
    }
    private fun doAllBlack(){
        vb.newsPageButton.setColorFilter(resources.getColor(R.color.black))
        vb.personButton.setColorFilter(resources.getColor(R.color.black))
        vb.mainMapButton.setColorFilter(resources.getColor(R.color.black))
    }



    private fun buttonClick(a: Int) {
        if (a == 1) {
            f1 =  true
            f2 = false
            f3 = false
        } else if (a == 2){
            f1 = false
            f2 = true
            f3 = false
        } else if (a == 3){
            f1 = false
            f2 = false
            f3 = true
        }
    }
    //Да да да, не хорошо, знаю, прямо даже сказать, ужасно, но я делал это очень быстро, как нибудь исправлю)
    override fun onMapReady(p0: GoogleMap) {
        val db = Firebase.firestore
        db
            .collection("maps_markers")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    markers.add(Parser.parseMarker(document.data))
                    println(Parser.parseMarker(document.data).toString())
                    for (marker in markers) {
                        if (marker.image != "") {
                            Glide.with(applicationContext).asBitmap().load(marker.image)
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        val bitmap =
                                            Bitmap.createScaledBitmap(resource, 350, 350, false)
                                        p0.addMarker(
                                            MarkerOptions()
                                                .position(
                                                    LatLng(
                                                        marker.latLng[0] as Double,
                                                        marker.latLng[1] as Double
                                                    )
                                                )
                                                .title(marker.name)
                                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                        )
                                    }
                                    override fun onLoadCleared(placeholder: Drawable?) {
                                    }
                                })
                        } else {
                            p0.addMarker(
                                MarkerOptions()
                                    .position(
                                        LatLng(
                                            marker.latLng[0] as Double,
                                            marker.latLng[1] as Double
                                        )
                                    )
                                    .title(marker.name)
                                    .icon(BitMapHelper.vectorToBitmap(this, R.drawable.ic_baseline_fmd_good_24, 110, 110))
                            )
                        }
                    }
                }
            }
            .addOnFailureListener{
                print(it.stackTrace)
            }
        val saintPetersburg = LatLng(59.9375, 30.308611)
        p0.setMinZoomPreference(12.5f)
        p0.mapType = GoogleMap.MAP_TYPE_NORMAL
        p0.moveCamera(CameraUpdateFactory.zoomTo(12.0f))
        p0.moveCamera(CameraUpdateFactory.newLatLng(saintPetersburg))
    }
}
